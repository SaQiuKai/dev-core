package com.xbsd.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 通用转换器工具类（包括根据身份证号判断相关信息、字符串格式转换）
 * @ClassName:  Converter
 * @Description:
 * @author: szk
 * @date:   2017年3月14日 下午8:01:39
 *
 */
public class Converter {
	
	/**
	 * 对象转换为json串
	 * @param obj
	 * @return
	 */
	public static String converToJsonStr(Object obj){
		String result = "{result:false}";
		ObjectMapper mapper = new ObjectMapper();
		if(obj!=null){
			try {
				result = mapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 身份证号计算出生日期
	 * @param sfzh
	 * @return
	 */
	public static String converSfzhToCsrq(String sfzh){
		String csrq = "";
		if(sfzh.length()==15){
			
		}else if(sfzh.length()==18){
			csrq = sfzh.substring(6,10)+"-"+sfzh.substring(10,12)+"-"+sfzh.substring(12,14);
		}
		return csrq;
	}
	/**
	 * 身份证号计算性别
	 * @param sfzh
	 * @return
	 */
	public static String sfzhToXb(String sfzh){
		if(StringUtils.isNotBlank(sfzh)){
			String xbCode =  sfzh.substring(sfzh.length()-2,sfzh.length()-1);
			if(StringUtils.isNotBlank(xbCode)){
				int xb = Integer.parseInt(xbCode);
				return xb%2==0?"女":"男";
			}
		}
		return "";
	}
	/**
	 * 新疆维吾尔自治区替换为新疆
	 * @param docList
	 * @return
	 */
	public static List<Map<String,Object>> getXinjiangStr(List<Map<String,Object>> docList){
		 if(null != docList && docList.size()>0){
			 for(Map<String,Object> doc : docList){
				 String hjqhmc = (String) doc.get("HJQHMC");
				 if(StringUtils.isNotBlank(hjqhmc)){
					 String str = hjqhmc.replace("新疆维吾尔自治区", "新疆");
					 doc.put("HJQHMC",str );
				 }
			 }
		 }
		return docList;
	}
	
	/**
	* 空 或者'' 显示 str2
	* @param str
	* @return date
	 * @throws ParseException 
	*/
	public static String StrToStr2(String str,String str2)  {
		if(StringUtils.isNotBlank(str)&&!("null".equals(str) || "NULL".equals(str))){
			return str;
		}
		return str2;
	}
	
	/**
	 * 指定精度转换百分比
	 * @param num
	 * @param precision
	 * @return
	 */
	public static String numberToPercent(double num,int precision){
		 NumberFormat nt = NumberFormat.getPercentInstance();
		 nt.setMinimumFractionDigits(precision);
		 return nt.format(num);
		
	}
	/**
	 * 指定小数精度
	 * @param num
	 * @param precision
	 * @return
	 */
	public static String numberToDecimal(double num,String precision){
		 DecimalFormat nt = new DecimalFormat(precision);
		 return nt.format(num);
	}
	
	/**
	 * 
	 * 修补15位居民身份证号码为18位
	 * @param personIDCode
	 * @return
	 */
	public static String fixPersonIDCode(String personIDCode)
	{
		String retIDCode = "";
		if (personIDCode == null || personIDCode.trim().length() != 15) {
			return personIDCode;
		}
        try{
			String id17 = personIDCode.substring(0, 6) + "19"
					+ personIDCode.substring(6, 15); // 15为身份证补\'19\'
			// char[] code =
			// {\'1\',\'0\',\'X\',\'9\',\'8\',\'7\',\'6\',\'5\',\'4\',\'3\',\'2\'};
			// //11个
			char[] code = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' }; // 11个
			int[] factor = { 0, 2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7 }; // 18个;
			int[] idcd = new int[18];
			int i;
			int j;
			int sum;
			int remainder;
			for (i = 1; i < 18; i++)
			{
				j = 17 - i;
				idcd[i] = Integer.parseInt(id17.substring(j, j + 1));
			}
			sum = 0;
			for (i = 1; i < 18; i++)
			{
				sum = sum + idcd[i] * factor[i];
			}
			remainder = sum % 11;
			String lastCheckBit = String.valueOf(code[remainder]);
			retIDCode = id17 + lastCheckBit;
        }catch(Exception e){
        	retIDCode = personIDCode;
        }
        return retIDCode;
	}
}
