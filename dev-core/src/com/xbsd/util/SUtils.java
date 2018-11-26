package com.xbsd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

/**
 * 用于BeanToos中方法名、类名、属性名的格式转化工具类
 * @ClassName:  SUtils
 * @Description:
 * @date:   2017年3月14日 下午8:52:00
 *
 */
public class SUtils {
	/**
	 * 驼峰命名 转 大写+"_" 如 wtjTestTable
	 * @param hump
	 * @return
	 */
	public static String getHumpToUpperCase(String hump){
		StringBuffer sb = new StringBuffer();
		char[] chArr = hump.toCharArray();
		for(int i=0;i<chArr.length;i++){
			char c = chArr[i];
			if(c<='Z'){
				if(i!=0){
					sb.append('_');
				}
			}
				sb.append(c);
		}
		return sb.toString().toUpperCase();
	}
	
	/**
	 * 字符串 转 驼峰
	 * @param str
	 * @return
	 */
	public static String getStrToHump(String str){
		String[] cs = StringUtils.split(str.toLowerCase(), "_");
		String field2 = "";
		for(int k=0; k<cs.length; k++){
			if(k==0){
				field2 = cs[0];
			}else{
				field2 += toUpperCaseFirstOne(cs[k]);
			}
		}
		return field2;
	}
	
	/**
	 * 把输入字符串的首字母改成大写
	 * @param value
	 * @return
	 */
	public static String toUpperCaseFirstOne(String value) {
		char[] ch = value.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}
	
	public static void main(String[] arg){
		System.out.println(getHumpToUpperCase("wtjTestTable"));
		System.out.println(getStrToHump("wtj_Test_Table"));
	}
	
	public  static boolean isEmpty(String s){
		
		if(s == null || "".equals(s) ){
			return true;
		}
		return false;
	}
	
	
	/**
	 * @param str
	 * @return 去除标签后的字符串
	 */
	public static String removeLabel(String str){
		if(str!=null&&str.length()>0){
			str=str.replaceAll("<br/?>", "\r\n");
			str=str.replaceAll("</?[a-zA-Z]+[^><]*>", "");
			return str.replaceAll("&nbsp;", " "); 	
		}else{
			return "";
		}
	}
	
	public static String nullToEmpty(String str){
		if(str==null){
			str = "";
		}
		return str;
	}
	
	 //clob转String。
	public static String ClobToString(Clob clob) {
		String a = "";
		try {
			Reader is = clob.getCharacterStream();
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while(null!=s) {
				sb.append(s);
				s = br.readLine();
			}
			a = sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}
}
