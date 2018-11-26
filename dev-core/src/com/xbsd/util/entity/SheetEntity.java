package com.xbsd.util.entity;

import java.util.List;
import java.util.Map;

public class SheetEntity {
private String sheetName;
	
	private String[] titles;
	
	private String[] fields;
	
	private List<Map<String,String>> datalist;

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public List<Map<String, String>> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<Map<String, String>> list) {
		this.datalist = list;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}
	
	
}
