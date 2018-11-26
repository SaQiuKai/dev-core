package com.xbsd.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 用于图片数据格式转换  base64加密解密工具类
 * @ClassName:  Base64Utils
 * @Description: base64加密解密
 * @author: szk
 * @date:   2017年3月14日 下午7:32:35
 *
 */
public class Base64Utils {
	
	/**
	 * base64字符串转换为byte[]数组（解码）
	 * @Title: getBase64ToBytes   
	 * @Description: 
	 * @param: @param base64Str
	 * @param: @return      
	 * @return: byte[]      
	 * @throws
	 */
	public static byte[] getBase64ToBytes(String base64Str){
		if (base64Str == null||"".equals(base64Str)){ // 图像数据为空
            return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		 // Base64解码
        byte[] bytes = null;
		try {
			bytes = decoder.decodeBuffer(base64Str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bytes!=null){
	        for (int i = 0; i < bytes.length; ++i) {
	            if (bytes[i] < 0) {// 调整异常数据
	                bytes[i] += 256;
	            }
	        }
		}
        return bytes;
	}
	
	/**
	 * base64为byte[]数组转换字符串（编码）
	 * @Title: getBase64ToString   
	 * @Description: 
	 * @param: @param b
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String getBase64ToString(byte[] b){
		if (b == null||b.length==0){ // 图像数据为空
            return "";
		}
		BASE64Encoder encoder = new BASE64Encoder(); 
		 // Base64解码
       String re = "";
		try {
			re = encoder.encode(b);;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
        return re;
	}
}
