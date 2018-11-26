package com.xbsd.util;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class WordReplaceUtil {
	 public static Map<String, String> params = new HashMap<String, String>();
	    
	    //获取文件类型
	    public static String getFileSufix(String fileName){
	        int index = fileName.lastIndexOf(".");
	        return fileName.substring(index + 1);
	    }
	    
	    public static void replaceFooterAndHeader(XWPFDocument doc){
	        List<XWPFParagraph> footers = doc.getHeaderFooterPolicy().getDefaultFooter().getParagraphs();
	        List<XWPFParagraph> headers = doc.getHeaderFooterPolicy().getDefaultHeader().getParagraphs();
	        //处理页脚
	        for (XWPFParagraph paragraph : footers) {
	            List<XWPFRun> runs = paragraph.getRuns();
	            for (XWPFRun run : runs) {
	                String text = run.getText(0);
	                if(StringUtils.isNotEmpty(text)){
	                    for(Entry<String, String> entry : params.entrySet()){
	                        String key = entry.getKey();
	                        if(text.indexOf(key) != -1){
	                            Object value = entry.getValue();
	                            if(value instanceof String){
	                                text = text.replace(key, value.toString());
	                                run.setText(text,0);
	                            }
	                        }
	                    }
	                }
	            }
	        }
	        //处理页眉
	        for (XWPFParagraph paragraph : headers) {
	            List<XWPFRun> runs = paragraph.getRuns();
	            for (XWPFRun run : runs) {
	                String text = run.getText(0);
	                if(StringUtils.isNotEmpty(text)){
	                    for(Entry<String, String> entry : params.entrySet()){
	                        String key = entry.getKey();
	                        if(text.indexOf(key) != -1){
	                            Object value = entry.getValue();
	                            if(value instanceof String){
	                                text = text.replace(key, value.toString());
	                                run.setText(text,0);
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
	    public static XWPFDocument generateWord(Map<String, String> param, InputStream inputStream) {
	        XWPFDocument doc = null;
	        try {
	            doc = new XWPFDocument(inputStream);
	            if (param != null && param.size() > 0) {
	                //处理段落
	                List<XWPFParagraph> paragraphList = doc.getParagraphs();
	                processParagraphs(paragraphList, param, doc);
	                //处理表格
	                Iterator<XWPFTable> it = doc.getTablesIterator();
	                while (it.hasNext()) {
	                    XWPFTable table = it.next();
	                    List<XWPFTableRow> rows = table.getRows();
	                    for (XWPFTableRow row : rows) {
	                        List<XWPFTableCell> cells = row.getTableCells();
	                        for (XWPFTableCell cell : cells) {
	                            List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
	                            processParagraphs(paragraphListTable, param, doc);
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return doc;
	    }
	    
	    public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, String> param,XWPFDocument doc){
	        if(paragraphList != null && paragraphList.size() > 0){
	            for(XWPFParagraph paragraph:paragraphList){
	                List<XWPFRun> runs = paragraph.getRuns();
	                for (XWPFRun run : runs) {
	                    String text = run.getText(0);
	                    if(text != null){
	                        for (Entry<String, String> entry : param.entrySet()) {
	                            String key = entry.getKey();
	                            if(text.indexOf(key) != -1){
	                                Object value = entry.getValue();
	                                if (value instanceof String) {//文本替换
	                                    text = text.replace(key, value.toString());
	                                    run.setText(text,0);
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
}
