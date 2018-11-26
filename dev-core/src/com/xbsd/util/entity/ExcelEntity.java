package com.xbsd.util.entity;

import java.util.List;

public class ExcelEntity {
	//文件名
	private String excelName;
	
	private List<SheetEntity> sheetList;

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public List<SheetEntity> getSheetList() {
		return sheetList;
	}

	public void setSheetList(List<SheetEntity> sheetList) {
		this.sheetList = sheetList;
	}
	
}

