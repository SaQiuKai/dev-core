package com.xbsd.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 所有和日期相关的转化类、年龄相关的转化类
 * @ClassName:  DateProcess
 * @Description:
 * @date:   2017年3月14日 下午7:55:57
 *
 */
public class DateProcess {

    public static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat dateTimeFormat2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat dateTimeOnlyNumFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final DateFormat dateTimeOnlyNumFormatYM = new SimpleDateFormat("yyyyMM");

    /**
     * 返回当前日期
     * @return
     */
    public static Date getCurrentDate(){
   	 return new Date();
   }
    public static int  getYear(){
    	Calendar cl=Calendar.getInstance();
    	return cl.get(Calendar.YEAR);
    }
    public static String getCurrentDateByStr(String parttern){
    	return new SimpleDateFormat(parttern).format(new Date());
    }
    /**
     * 返回当前系统时间的标准形式
     * @return  当前时间的字符串
     */
    public static String getPresentTime() {
        return dateTimeFormat.format(new Date());
    }
    public static String getPresentTime2() {
        return dateTimeFormat2.format(new Date());
    }
    /**
     * 返回当前系统时间的标准形式
     * @return  当前时间的字符串
     */
    public static String getPresentTimeOnlyNum() {
        return dateTimeOnlyNumFormat.format(new Date());
    }
    /**
     * 返回当前系统时间的标准形式(只有年月)
     * @return  当前时间的字符串
     */
    public static String getPresentTimeOnlyNumYM() {
    	return dateTimeOnlyNumFormatYM.format(new Date());
    }
    public static String getTimeCode(){
    	String a = getPresentTime().replace(":", "");
    	a = a.replace("-", "");
    	a=a.replace(" ","");
    	return a;
    }
    /**
     * 返回当前日期字符
     * @return
     */
    public static String getPresentDate(){
    	 return dateFormat.format(new Date());
    }
    /**
     * 返回出生日期
     * @param age 年龄
     * @return 出现异常时 默认返回1970-01-01
     * */
    public static String getBirth(int age){
    	try {   
    		
            Calendar cd = Calendar.getInstance();   
            cd.setTime(dateFormat.parse(dateFormat.format(new Date())));   
            cd.add(Calendar.YEAR, - age);
            return dateFormat.format(cd.getTime());   
        } catch (Exception e) {  
        	e.printStackTrace();
            return "1970-01-01";   
        }   
  
    }
    /**
     * 返回年龄
     * @param age
     * @return
     */
    public static int getAge(String date){
    	int year = Integer.parseInt(date.substring(0,4));
    	Calendar cd = Calendar.getInstance();   
    	return cd.get(Calendar.YEAR)-year;
    }
    /**
     * 字符串转为日期
     * @param str
     * @return
     */
    public static Date getDate(String str){
    	if(StringUtils.isNotBlank(str)){
    		try {
				return dateTimeFormat.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	return null;
    }
    /**
     * String转Date
     * @param dateString
     * @param parttern
     * @return
     * @throws ParseException
     */
    public static Date parseDateByString(String dateString,String parttern) throws ParseException{
		Date newDate = null;
		if(StringUtils.isNotBlank(dateString)){
			newDate = new SimpleDateFormat(parttern).parse(dateString);
		}
		return newDate;
	}
    /**
     * date转String
     * @param date
     * @param parttern
     * @return
     * @throws ParseException
     */
	public static String parseStringByDate(Date date,String parttern) throws ParseException{
		String newDate = null;
		if(date != null){
			newDate = new SimpleDateFormat(parttern).format(date);
		}
		return newDate;
	}
	/**
	* 日期转换成字符串
	* @param date 
	* @return str
     * @throws ParseException 
	*/
	public static String DateToStr(Date date) throws ParseException {
	   return DateProcess.parseStringByDate(date,"yyyy-MM-dd HH:mm:ss");
	}
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	 * @throws ParseException 
	*/
	public static Date StrToDate(String str) throws ParseException {
	   return DateProcess.parseDateByString(str,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 获取几天后的时间
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d,int day){
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE)+day);
		return now.getTime();
	}
	/**
	 * 获取几天前的时间
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d,int day){
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE)-day);
		return now.getTime();
	}
	/**
	 * 获取几小时前的日期
	 * @param hour
	 * @return
	 * @throws ParseException
	 */
	public static String getDateBeforeHour(int hour) throws ParseException{
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.set(Calendar.HOUR, Calendar.HOUR-hour);
		return DateProcess.parseStringByDate(now.getTime(), "yyyy-MM-dd");
	}
	/**
	 * 获取月的第一天
	 * @param month 0:本月，-1：上月，1：下月
	 * @return
	 * @throws ParseException
	 */
	public static String getMonthOfFistDay(int month,String parttern) {
		Calendar now = Calendar.getInstance();
    	now.add(Calendar.MONTH, month);//0:本月，-1：上月，1：下月
    	now.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat(parttern).format(now.getTime());
	}
	/**
	 * 获取月的最后一天
	 * @param month 0:本月，-1：上月，1：下月
	 * @param parttern
	 * @return
	 */
	public static String getMonthOfLastDay(int month,String parttern) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, month);//0:本月，-1：上月，1：下月
		now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new SimpleDateFormat(parttern).format(now.getTime());
	}
}
