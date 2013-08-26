package siddur.tool.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import siddur.common.miscellaneous.Paging;
import siddur.tool.core.data.ToolDescriptor;



public class MemoryVisitor extends MapVisitor<String, IToolWrapper>{

	public IToolWrapper findById(String id){
		return map.get(id);
	}
	
	public List<IToolWrapper> findAll(List<String> ids){
		List<IToolWrapper> list = new ArrayList<IToolWrapper>(ids.size());
		for (String id : ids) {
			IToolWrapper t = findById(id);
			if(t != null)
				list.add(findById(id));
		}
		return list;
	}
	
	public List<IToolWrapper> findLatestOnes(int num){
		List<IToolWrapper> list = new ArrayList<IToolWrapper>(map.size());
		for (Entry<String, IToolWrapper> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		Collections.sort(list);
		int size = list.size();
		return list.subList(0, num > size ? size : num);
	}
	
	public List<IToolWrapper> findByAuthor(String userId){
		List<IToolWrapper> list = new ArrayList<IToolWrapper>();
		for(Entry<String, IToolWrapper> entry : map.entrySet()){
			IToolWrapper tpu = entry.getValue();
			if(tpu.getDescriptor().getAuthorId().equals(userId)){
				list.add(tpu);
			}
		}
		return list;
	}
	
	public List<IToolWrapper> findByKeyword(String key){
		List<IToolWrapper> list = new ArrayList<IToolWrapper>();
		for(Entry<String, IToolWrapper> entry : map.entrySet()){
			IToolWrapper tpu = entry.getValue();
			ToolDescriptor pd = tpu.getDescriptor();
			if(key == null || key.equals("")){
				list.add(tpu);
			}
			else{
				key = key.trim().toLowerCase();
				if(pd.getPluginName().toLowerCase().contains(key)){
					list.add(tpu);
				}
				else if(pd.getDescription().toLowerCase().contains(key)){
					list.add(tpu);
				}
			}
		}
		
		return list;
	}
	
	public Paging<IToolWrapper> findMine(String userId, int pageSize, int pageIndex){
		return getCurrentPage(findByAuthor(userId), pageSize, pageIndex);
		
	}
	
	
	public Paging<IToolWrapper> findAll(String key, int pageSize, int pageIndex){
		return getCurrentPage(findByKeyword(key), pageSize, pageIndex);
	}
	
	private Paging<IToolWrapper> getCurrentPage(List<IToolWrapper> list, int pageSize, int pageIndex){
		Paging<IToolWrapper> p = new Paging<IToolWrapper>();
		p.setPageIndex(pageIndex);
		p.setPageSize(pageSize);
		p.setTotal(list.size());
		Collections.sort(list);
		int size = list.size();
		int end = pageIndex * pageSize;
		if(end > size){
			end = size;
		}
		p.setData(list.subList((pageIndex - 1) * pageSize, end));
		return p;
	}
}