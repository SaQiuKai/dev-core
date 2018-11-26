package com.xbsd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hdgf.streams.Stream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xbsd.util.entity.ExcelEntity;
import com.xbsd.util.entity.SheetEntity;

/**
 * Excel工具类
 * @author szk
 * 
 */
public class ExcelUtil {
	/**
	 * 创建excel对象
	 * @return
	 * @throws Exception
	 */
	public static HSSFWorkbook createExcel(ExcelEntity excelEntity) throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		List<SheetEntity> list = excelEntity.getSheetList();
		if(excelEntity.getSheetList() != null && excelEntity.getSheetList().size() > 0){
			for(SheetEntity sheetEntity : list)
			addSheet(workbook, sheetEntity);
		}
		
		return workbook;
	}
	/**
	 * 创建excel对象,考核排行中比较特殊
	 * @return
	 * @throws Exception
	 */
	public static HSSFWorkbook createExcelJF(ExcelEntity excelEntity) throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		List<SheetEntity> list = excelEntity.getSheetList();
		if(excelEntity.getSheetList() != null && excelEntity.getSheetList().size() > 0){
			for(SheetEntity sheetEntity : list)
			addSheetJF(workbook, sheetEntity);
		}
		
		return workbook;
	}
	
	/**
	 * 获取excel中的数据(后缀名为.xls)
	 * 默认第一行不进行获取，因为一般作为说明行
	 */
	public static List<Map<String, Object>> ReadExcel(File file) throws Exception{
		
		List<Map<String,Object>> list = new LinkedList<>();
		Map map = null;
        //获取文件流
        POIFSFileSystem pois = new POIFSFileSystem(new FileInputStream(file));
        //新建WorkBook
        HSSFWorkbook wb = new HSSFWorkbook(pois);
        //获取Sheet（工作薄）总个数
        int sheetNumber = wb.getNumberOfSheets();
         
        for (int i = 0; i < sheetNumber; i++) {
            //获取Sheet（工作薄）
            HSSFSheet sheet = wb.getSheetAt(i);
            //开始行数
            int firstRow = sheet.getFirstRowNum()+1;
            //结束行数
            int lastRow = sheet.getLastRowNum();
            //判断该Sheet（工作薄)是否为空
            boolean isEmpty = false;
            if(firstRow == 0 && lastRow ==0){
                isEmpty = true;
            }
             
            if(!isEmpty){
                for (int j = firstRow; j <= lastRow; j++) {
                    //获取一行
                    HSSFRow row = sheet.getRow(j);
                    //开始列数
                    int firstCell = row.getFirstCellNum();
                    //结束列数
                    int lastCell = row.getLastCellNum();
                    //判断该行是否为空
                    if(firstCell != lastCell){
                         map = new HashMap<String,Object>();
                        for (int k = firstCell; k < lastCell; k++) {
                            //获取一个单元格
                            HSSFCell cell = row.getCell(k);
                            Object value = null;
                            //  类型说明
                            //  CELL_TYPE_NUMERIC 数值型 0
                            //  CELL_TYPE_STRING 字符串型 1
                            //  CELL_TYPE_FORMULA 公式型 2
                            //  CELL_TYPE_BLANK 空值 3
                            //  CELL_TYPE_BOOLEAN 布尔型 4
                            //  CELL_TYPE_ERROR 错误 5
                             
                            //获取单元格，值的类型
                            int cellType = cell.getCellType();
                             
                            if(cellType == 0){
                                value = cell.getNumericCellValue();
                            }else if(cellType == 1){
                                value = cell.getStringCellValue();
                            }else if(cellType == 2){
                            }else if(cellType == 4){
                                value = cell.getBooleanCellValue();
                            }
                            map.put(k, value+"");
                             
                        }
                         
                    }
                    list.add(map); 
                }
                 
            }
             
        }
		
		return list;
		
	}
	
	/**
	 * @description: 读取后缀名为.xlsx的Excel
	 * @author: Sa
	 * @since: 2018年3月13日 上午8:46:45
	 */
	public static List<Map<String, Object>> ReadXlsxExcel(File file) throws Exception{
		
		List<Map<String,Object>> list = new LinkedList<>();
		Map map = null;
        //获取文件流
        //POIFSFileSystem pois = new POIFSFileSystem(new FileInputStream(file));
        //新建WorkBook
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
        //获取Sheet（工作薄）总个数
        int sheetNumber = wb.getNumberOfSheets();
         
        for (int i = 0; i < sheetNumber; i++) {
            //获取Sheet（工作薄）
            XSSFSheet sheet = wb.getSheetAt(i);
            //开始行数
            int firstRow = sheet.getFirstRowNum()+1;
            //结束行数
            int lastRow = sheet.getLastRowNum();
            //判断该Sheet（工作薄)是否为空
            boolean isEmpty = false;
            if(firstRow == 0 && lastRow ==0){
                isEmpty = true;
            }
             
            if(!isEmpty){
                for (int j = firstRow; j <= lastRow; j++) {
                    //获取一行
                    XSSFRow row = sheet.getRow(j);
                    //开始列数
                    int firstCell = row.getFirstCellNum();
                    //结束列数
                    int lastCell = row.getLastCellNum();
                    //判断该行是否为空
                    if(firstCell != lastCell){
                         map = new HashMap<String,Object>();
                        for (int k = firstCell; k < lastCell; k++) {
                            //获取一个单元格
                            XSSFCell cell = row.getCell(k);
                            Object value = null;
                            //  类型说明
                            //  CELL_TYPE_NUMERIC 数值型 0
                            //  CELL_TYPE_STRING 字符串型 1
                            //  CELL_TYPE_FORMULA 公式型 2
                            //  CELL_TYPE_BLANK 空值 3
                            //  CELL_TYPE_BOOLEAN 布尔型 4
                            //  CELL_TYPE_ERROR 错误 5
                             
                            //获取单元格，值的类型
                            int cellType = cell.getCellType();
                             
                            if(cellType == 0){
                                value = cell.getNumericCellValue();
                            }else if(cellType == 1){
                                value = cell.getStringCellValue();
                            }else if(cellType == 2){
                            }else if(cellType == 4){
                                value = cell.getBooleanCellValue();
                            }
                            map.put(k, value+"");
                             
                        }
                         
                    }
                    list.add(map); 
                }
                 
            }
             
        }
		
		return list;
		
	}
	
	public static HSSFWorkbook addSheet(HSSFWorkbook workbook, SheetEntity sheetEntity) throws Exception{
		//创建sheet标签页
		HSSFSheet sheet = workbook.createSheet(sheetEntity.getSheetName());
		int rowsize =  sheetEntity.getDatalist().size();
		List<Map<String,String>> datalist = sheetEntity.getDatalist();
		String[] fileds = sheetEntity.getFields();
		String[] titlenames = sheetEntity.getTitles();
		Row row = sheet.createRow(0);
		int i = 0;
		for(String filed : fileds){
			Cell cell = row.createCell(i);
			cell.setCellValue(filed);
			i++;
		}
		for(int j = 1;j <= rowsize;j++){
			row = sheet.createRow(j);
			
			Map<String,String> m = datalist.get(j-1);
			int k = 0;
			for(String title : titlenames){
				Cell cell = row.createCell(k);
				try{
					Object obj = m.get(title);
					if(obj != null){
						if(NumberUtils.isNumber(obj.toString())){
							String str = obj.toString();
							if(str.length()>10){
								cell.setCellValue(obj.toString());
							}else{
								cell.setCellValue(Integer.valueOf(obj.toString()));
							}
						}else{
							cell.setCellValue(obj.toString());
						}
					}else{
						cell.setCellValue("");
					}
				}catch(Exception e){
					Object obj = m.get(title);
					System.out.println("title----------------------------------"+title);
					System.out.println("m.get(title)----------------------------------"+m.get(title).toString());
				}
				k++;
			}
		}
		
		return workbook;	
	}
	
	/**
	 * 考核排行中excel比较特殊，
	 * @param workbook
	 * @param sheetEntity
	 * @return
	 * @throws Exception
	 */
	public static HSSFWorkbook addSheetJF(HSSFWorkbook workbook, SheetEntity sheetEntity) throws Exception{
		//创建sheet标签页
		HSSFSheet sheet = workbook.createSheet(sheetEntity.getSheetName());
		int rowsize =  sheetEntity.getDatalist().size();
		List<Map<String,String>> datalist = sheetEntity.getDatalist();
		String[] fileds = sheetEntity.getFields();
		String[] titlenames = sheetEntity.getTitles();
		Row row = sheet.createRow(0);
		int i = 0;
		for(String filed : fileds){
			Cell cell = row.createCell(i);
			cell.setCellValue(filed);
			i++;
		}
		for(int j = 1;j <= rowsize;j++){
			row = sheet.createRow(j);
			
			Map<String,String> m = datalist.get(j-1);
			int k = 0;
			for(String title : titlenames){
				Cell cell = row.createCell(k);
				try{
					Object obj = m.get(title);
					if(obj != null){
						if(NumberUtils.isNumber(obj.toString())){
							String str = obj.toString();
//							if(str.length()>10){
								cell.setCellValue(obj.toString());
//							}else{
//								cell.setCellValue(Integer.valueOf(obj.toString()));
//							}
						}else{
							cell.setCellValue(obj.toString());
						}
					}else{
						cell.setCellValue("");
					}
				}catch(Exception e){
					Object obj = m.get(title);
					System.out.println("title----------------------------------"+title);
					System.out.println("m.get(title)----------------------------------"+m.get(title).toString());
				}
				k++;
			}
		}
		
		return workbook;	
	}
	

	
	//读取两种格式的Excel内容
	/**
	 * @description: 读取两种格式的Excel
	 * @param stream 输入流
	 * @param extString excel后缀
	 * @param columns 列名称
	 * @author: baipengfei
	 * @since: 2018年3月21日 下午12:01:44
	 */
	public static List<Map<String, String>> readExcelByStream(InputStream stream, String extString, String[] columns) throws Exception {
		Workbook wb = getExcelWb(stream, extString);
		List<Map<String, String>> list = null;
		String cellData = null;
		if(wb != null){
			//用来存放表中数据
			list = new ArrayList<Map<String,String>>();
			//获取第一个sheet
			Sheet sheet = wb.getSheetAt(0);
			//获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			//获取第一行
			Row row = sheet.getRow(0);
			//获取最大列数
			int column = row.getPhysicalNumberOfCells();
			for(int i = 1; i < rownum; i++){
				Map<String, String> map = new LinkedHashMap<String, String>();
				row = sheet.getRow(i);
				if(row != null){
					for(int j = 0; j < column; j++){
						cellData = String.valueOf(getCellFormatValue(row.getCell(j)));
						map.put(columns[j], cellData);
					}
				}else {
					break;
				}
				list.add(map);
			}
			
		}
		return list;
	}
	
	/**
	 * @description: 获取Workbook对象
	 * @author: baipengfei
	 * @since: 2018年3月23日 上午9:59:35
	 */
	public static Workbook getExcelWb(InputStream stream, String extString) throws Exception {
		Workbook wb = null;
		if(stream == null){
			return null;
		}
		
		if(".xls".equals(extString)){
			return wb = new HSSFWorkbook(stream);
		}else if(".xlsx".equals(extString)){
			return wb = new XSSFWorkbook(stream);
		}else{
			return wb = null;
		}

	}
	
	
	/**
	 * @description: 获取单元格值
	 * @author: baipengfei
	 * @since: 2018年3月23日 上午9:59:48
	 */
	public static Object getCellFormatValue(Cell cell) {
		Object cellValue = null;
		if(cell != null){
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:{
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			}
			
			case Cell.CELL_TYPE_FORMULA:{
				if(DateUtil.isCellDateFormatted(cell)){
					cellValue = cell.getDateCellValue();
				}else {
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			
			case Cell.CELL_TYPE_STRING:{
				cellValue = cell.getRichStringCellValue();
				break;
			}
			
			default:
				cellValue = "";
				
			}
		}else {
			cellValue = "";
		}
		return cellValue;
	}
	public static void main(String[] args) throws Exception{
/*		String[] arr = {"13212","adad","w234","02"};
		for (String str : arr) {
			if(NumberUtils.isNumber(str)){
				System.out.println(Integer.valueOf(str));
			}
		}*/
		
		InputStream stream = new FileInputStream(new File("C:/Users/Administrator/Desktop/插入人员测试/test1.xlsx"));
		String[] columns = {"ry_xm","ry_sfzh","ry_hjdz","ry_byqk","ry_lxdh","ry_xzz","ry_jtmc","ry_jysm"};
		List<Map<String, String>> list = readExcelByStream(stream, ".xlsx", columns);
		System.out.println(list);
	}
}
