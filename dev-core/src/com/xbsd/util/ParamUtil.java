package com.xbsd.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamUtil {
	private static Logger logger = LoggerFactory.getLogger(ParamUtil.class);
	//参数文件名
	private static final String CONF_FILE_NAME = "params.properties";
	/**
	 * 获取发布项目的根目录
	 */
	public String getAbsolutePathByClass() {
		String classUrl = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String path = classUrl.substring(0, classUrl.indexOf("WEB-INF")) + "WEB-INF/";
		String realPath = "";
		try {
			realPath = URLDecoder.decode(path,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return realPath;
	}
	/**
	 * 获取参数值
	 * @param key 参数建
	 * @return String 参数值
	 */
	public String getParam(String key){
		Properties props = new Properties(); 
        InputStream in = null;
        String filePath = new ParamUtil().getAbsolutePathByClass()+"config/"+CONF_FILE_NAME;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);  
		} catch (Exception e) {
			logger.error("获取配置文件失败{}",e);
		}  
        String value = props.getProperty(key);  
        return value;  
	}
	public static void main(String[] args) {
			System.out.println(new ParamUtil().getParam("ftp.ip"));
	}
}
