package siddur.common.security;

import java.util.ArrayList;
import java.util.List;


public class PermissionManager {
	private static PermissionGroup permGroup = new PermissionGroup();
	
	static{
		for(Permission p : Permission.values()){
			permGroup.addPerm(p);
		}

	}
	
	public static PermissionGroup admin(){
		return new PermissionGroup("1 1111 11111 1111");
	}
	
	//add/delete/editor/update tools
	public static PermissionGroup editor(){
		return new PermissionGroup("0 0000 00000 1111");
	}
	
	//run tools
	public static PermissionGroup client(){
		return new PermissionGroup("0 0000 00000 0001");
	}
	
	public static PermissionGroup createPermissionGroup(Integer value){
		return new PermissionGroup(value);
	}
	
	public static List<Permission> getAllPermissions(){
		return permGroup.perms;
	}
	
	public static class PermissionGroup{
		private List<Permission> perms = new ArrayList<Permission>();
		
		private PermissionGroup() {
		}
		
		public PermissionGroup(Integer value) {
			for (int i = 0; i < permGroup.perms.size(); i++) {
				if(((value >> i) & 1) == 1){
					addPerm(i);
				}
			}
		}
		
		public PermissionGroup(String value){
			this(Integer.parseInt(value.replace(" ", ""), 2));
		}
		
		
		public void addPerm(Permission p){
			perms.add(p);
		}
		
		public void addPerm(int index){
			addPerm(getPerm(index));
		}
		
		public void delePerm(Permission p){
			perms.remove(p);
		}
		
		public void delePerm(int index){
			delePerm(getPerm(index));
		}
		
		public boolean hasPerm(Permission p){
			if(p == null)
				return true;
			return perms.indexOf(p) > -1;
		}
		
		public boolean hasPerm(int index){
			return perms.indexOf(getPerm(index)) > -1;
		}
		
		public Permission getPerm(int index){
			return permGroup.perms.get(index);
		}
		
		public Integer toInteger(){
			int sum = 0;
			for (int i = 0; i < perms.size(); i++) {
				int item = 1 << permGroup.perms.indexOf(perms.get(i));
				sum += item;
			}
			return sum;
		}
		
		@Override
		public String toString() {
			return Integer.toBinaryString(toInteger());
		}
		
	}
	
}
