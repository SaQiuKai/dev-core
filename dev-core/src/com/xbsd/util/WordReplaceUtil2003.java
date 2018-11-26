package com.xbsd.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

public class WordReplaceUtil2003{
	 public static Map<String, String> params = new HashMap<String, String>();
	 
	  
	    public static HWPFDocument generateWord(Map<String, String> param, InputStream inputStream) {
	        HWPFDocument document = null;
	        
	        try {
				document = new HWPFDocument(inputStream);
				
				Range range = document.getRange();
				
				for(String key : param.keySet()){
					range.replaceText(key, param.get(key));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
			return document;
	    }
			
	   
}
