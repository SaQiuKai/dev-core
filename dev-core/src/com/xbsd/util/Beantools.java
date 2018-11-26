package com.xbsd.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.formula.functions.T;


/**
 * Bean 转换工具类
 * @ClassName:  Beantools
 * @Description:
 * @author: szk
 * @date:   2017年3月14日 下午9:06:20
 *
 */
public class Beantools {
	
	/**
	 * Map 转 实体Bean ，Map 为数据查询出来的数据,字段都为大写 如 : ID,NAME,USER_ID
	 */
	public static <T> T tableMapToBean(Map<String,Object> map,Class<T> clazz){
		if(map == null){
			return null;
		}
		Field[] fields = clazz.getDeclaredFields();
		T bean = null;
		try {
			bean = (T) clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String key = SUtils.getHumpToUpperCase(fields[i].getName());
			Object value = map.get(key);
			if(value == null){
				value = map.get(key.toLowerCase());
			}
			setBean(bean,fields[i],value);
		}
		return bean;
	}
	//bean转Map map的key均为大写
	public static Map<String, Object> beanToMap(Object bean) {
		Class<? extends Object> clazz = bean.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			try {
				map.put(field.getName().toUpperCase(), field.get(bean));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return map;
		
	}
	//list转beans
	public static List<Map<String, Object>> beansToMaps(List list){
		List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
		for (Object object : list) {
			list2.add(beanToMap(object));
		}
		return list2;
	}
	
	/**
	 * List<Map<String,object>>转实体BEAN集合
	 * @param maps
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> mapsToBean(List<Map<String, Object>> maps,Class<T> clazz){
		List<T> list=new ArrayList<T>();
		for (Map<String, Object> map : maps) {
			list.add(tableMapToBean(map, clazz));
		}
		return list;
	}
 
 /**
	 * 给实体属性赋值  如 TestBean 有id ,赋予 1001值
	 */
	public static void setBean(Object classzs,Field field,Object value){
		try {
			if (String.class == field.getType()) {
				field.set(classzs,value==null?null:value.toString());
			}else if (Date.class ==field.getType()) {
				field.set(classzs, value==null?null:(Date)value);
			}else if (Integer.class == field.getType()){
				if(value instanceof Boolean){
					field.set(classzs, (Boolean)value ? 1 : 0 );
				}else{
					field.set(classzs, value==null?null:Integer.parseInt(value.toString()));
				}
			}else if(Short.class==field.getType()){
				field.set(classzs, value==null?null:Short.parseShort(value.toString()));
			}else if (Long.class == field.getType()){
				field.set(classzs, value==null?null:Long.parseLong(value.toString()));
			}else if (Float.class == field.getType()){
				field.set(classzs, value==null?null:Float.parseFloat(value.toString()));
			}else if (Double.class == field.getType()){
				field.set(classzs, value==null?null:Double.parseDouble(value.toString()));
			}else if(BigDecimal.class==field.getType()){
				field.set(classzs, value==null?null:new BigDecimal(value.toString()));
			}else if(Timestamp.class == field.getType() ){
				field.set(classzs, value==null?null:(Timestamp)value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 给实体属性赋值  如 TestBean 有id ,赋予 1001值
	 */
	public static void setBean(Object classzs, String attrName, Object attrValue)throws Exception {
		Field[] fields = classzs.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].getName().equals(attrName)){
				fields[i].setAccessible(true);
				setBean(classzs,fields[i],attrValue);
			}
		}
	}
	public static void setBean(Object classzs,Map<String,Object> map)throws Exception {
		Field[] fields = classzs.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				setBean(classzs,fields[i],map.get(fields[i].getName()));
		}
	}
	/**
	 * ajax序列化表单转Map
	 * @param param
	 * @return
	 */
	public static Map<String,Object> formToMap(String param){
		Map<String, Object> result=new HashMap<String,Object>();
		String replaceAll = param.replaceAll("=&","= &");
		String[] split = replaceAll.split("&");
		for(int i=0;i<split.length;i++){
			if(i==split.length-1){
				split[i]+=" ";
			}
			String[] keyvalue=split[i].split("=");
			if(keyvalue[1]==null){
				result.put(keyvalue[0],"");
			}else{
			result.put(keyvalue[0],keyvalue[1].trim());
			}
		}
		return result;
	}
	
	/*----------request转bean----------*/
	public static <T> T requestToBean(HttpServletRequest request, Class<T> beanClass) {  
        try {  
            // 创建封装数据的bean  
            T bean = beanClass.newInstance();  
            Map map = request.getParameterMap();  
            BeanUtils.populate(bean, map);  
            return bean;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
	/*----------request转bean----------*/
	

	
}
