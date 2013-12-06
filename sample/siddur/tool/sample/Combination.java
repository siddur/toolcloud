package siddur.tool.sample;

public abstract class Combination {
	private boolean repeat;
	private int total = 0;
	
	public void combine(int data[]){
		combine(data, data.length, false);
	}
	
	public void combine(int data[], int count){
		combine(data, count, false);
	}
	
	public void combine(int data[], int count, boolean repeat){
		this.repeat = repeat;
		int times = data.length;
		int size = (int)Math.pow(times, count);
		for (int i = 0; i < size; i++) {
			int[] result = toArray(data, i, count);
			if(result != null){
				handle(result);
				total ++;
			}
		}
	}
	
	
	private int[] toArray(int data[], int i, int count){
		int [] indices = new int[count];
		int times = data.length;
		for (int j = 0; j < count; j++) {
			int temp = 0;
			if(i > 0){
				temp = i%times;
				i = (i - temp)/times;
			}
			indices[j] = temp;
		}
		
		
		if(!repeat){
			//remove repetition
			for (int x = 0; x < count; x++) {
				for(int y = 0; y < count; y++){
					if(x != y){
						if(indices[x] == indices[y])
							return null;
					}
				}
			}
		}
		
		int [] result = new int[count];
		for (int x = 0; x < count; x++) {
			int selected = data[indices[x]];
			result[x] = selected;
		}
		
		return result;
	}
	
	public int getTotal() {
		return total;
	}
	protected abstract void handle(int[] result);
}
