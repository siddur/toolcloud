package siddur.tool.core;

import java.util.Map;

public class MapVisitor<K,V> {

	protected Map<K,V> map;
	
	public void visit(Map<K,V> map){
		this.map = map;
	}
	
}
