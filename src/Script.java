import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;


public class Script extends Thread{
	InputStream in;
	OutputStream out;
	
	private Script(InputStream in, OutputStream out) {
		super();
		this.in = in;
		this.out = out;
	}
	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		while(true){
			String cmd = s.nextLine();
			System.out.println("---->" + cmd);
			if(cmd.equals("exit")){
				return;
			}
			Process p = Runtime.getRuntime().exec(cmd);
			copyInThread(p.getInputStream(), System.out);
			copyInThread(p.getErrorStream(), System.err);
		}
	}
	private static void copyInThread(final InputStream in, final OutputStream out) {
	    new Script(in, out).start();
	}
	
	public void run() {
        try {
            while (true) {
                int x = in.read();
                if (x < 0) {
                    return;
                }
                if (out != null) {
                    out.write(x);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
