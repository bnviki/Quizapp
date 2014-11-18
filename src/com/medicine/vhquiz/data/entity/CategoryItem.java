package com.medicine.vhquiz.data.entity;

import org.json.JSONArray;
import org.json.JSONObject;

public class CategoryItem {
	public static int MAIN_CATEGORY = 0, SUB_CATEGORY = 1;
	public String id;
	public String name;
	public String parent;
	public String desc;
	public int type;
	
	public CategoryItem(String id, String name, String parent, String desc, int type){
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.desc = desc;
		this.type = type;
	}
	
	public CategoryItem(int type, JSONObject obj){
		try{
			if(type == MAIN_CATEGORY){
				id = obj.getString("subjectid");
				name = obj.getString("subject_name");
				parent = obj.getString("subject_parent_subjectid");
				desc = obj.getString("subject_description");
				type = MAIN_CATEGORY;
			} else {
				id = obj.getString("testid");
				name = obj.getString("test_name");
				parent = obj.getString("subjectid");
				desc = "";
				type = SUB_CATEGORY;
			}
		} catch(Exception e){
			
		}
	}
}
