package com.umpay.proxyservice.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: tyf
 * Date: 14-2-24
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    public static String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

	public static String format(Date date, String rex) {
		DateFormat dateFormat = new SimpleDateFormat(rex);
		try {
			return dateFormat.format(date);
		} catch (Exception e) {
			return dateFormat.format(new Date());
		}
	}
	
    public static String getDate8(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try{
            return dateFormat.format(date);
        }catch (Exception e) {
            return dateFormat.format(new Date());
        }
    }

    public static String getDate10(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            return dateFormat.format(date);
        }catch (Exception e) {
            return dateFormat.format(new Date());
        }
    }

    public static String getTime14(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try{
            return dateFormat.format(date);
        }catch (Exception e) {
            return dateFormat.format(new Date());
        }
    }
    
    public static String getTime6(Date date){
        DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        try{
            return dateFormat.format(date);
        }catch (Exception e) {
            return dateFormat.format(new Date());
        }
    }

    public static String getTime14(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

    public static Date string2Date(String s){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    
    public static String DateTostring10(Date d){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        try {
            return dateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    
    public static String DateTostring14(Date d){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return dateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    
    public static String DateTostring19(Date d){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    
    public static String DateTostring19Time(Date d){
        DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
        try {
            return dateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Date string10ToDate(String s){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Date string14ToDate(String s){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Date string8ToDate(String s){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static String getTime(Date date){
        if(date == null){
            return " 00:00:00";
        }else{
        	return DateTostring19Time(date);
        }
    }

    public static String getTime(int m){
        if(m>=1440 || m<=0){
            return " 00:00:00";
        }

        if(m%60 == 0){
            return new StringBuffer(" ").append(completion(m/60)).append(":00:00").toString();
        }else {
            return new StringBuffer(" ").append(completion(m/60)).append(":").append(completion(m%60)).append(":00").toString();
        }
    }

    private static String completion(int i){
        if(i<10){
            return "0"+i;
        }else{
            return String.valueOf(i);
        }
    }
    
    public static Date getNextDateHour(Date d,int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.HOUR, num);
        return calendar.getTime();
    }
    
    public static Date getNextDateDay(Date d,int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, num);
        return calendar.getTime();
    }
    
    public static Date getNextDateWeek(Date d,int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.WEEK_OF_YEAR, num);
        return calendar.getTime();
    }
    
    public static Date getNextDateMonth(Date d,int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    public static String getNextHour(int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR,num);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        return dateFormat.format(date);
    }

    public static String getNextHour(Date d,int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.HOUR,num);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        return dateFormat.format(date);
    }

    public static String getNextDay(int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,num);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getNextDay(Date d,int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, num);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static Date getNextDay(Date d){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static String getNextMonth(int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,num);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    
    
    public static String getBeforeMonth(int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,(-1)*num);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        return dateFormat.format(date);
    }

    public static String getNextMonth(Date d,int num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MONTH,num);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getNextWeek(int num){
        return getNextDay(num*7);
    }

    public static String getNextWeek(Date d,int num){
        return getNextDay(d,num*7);
    }

    public static String getNextQuarter(int num){
        return getNextMonth(num*3);
    }

    public static String getNextYear(int num){
        return getNextMonth(num * 12);
    }
    
    
    public static String beforeYear(int num){
        return getBeforeMonth(num * 12);
    }

    public static String dateFormat(String datetime,int m) {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
        Date date = null;  
        try {  
            date = sdf.parse(datetime);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        Calendar cl = Calendar.getInstance();  
        cl.setTime(date);  
        cl.add(Calendar.MONTH, m*(-1));  
        date = cl.getTime();  
        return sdf.format(date);  
    }  

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
    
    public static void main(String args[]){
    	
    	compare_date("2017-05-09 12:28:32", "2017-04-09 12:28:31");
       /* System.out.println("=="+getNextDateHour(new Date(),2));
        System.out.println(":ab:cd:ef::".split(":").length);
        System.out.println(":ab:cd:ef::::".split(":",2).length);*/
    	
    	/*Date dataTime = DateUtil.string10ToDate("2015031315");
    	dataTime = DateUtil.getNextDateHour(dataTime, 1);
    	String dt;
    	dt = DateUtil.DateTostring10(dataTime);
    	System.out.println("dt="+dt);
    			*/
    	/*Date dataTime =new Date();
    	System.out.println(dataTime.toLocaleString());
    	dataTime = DateUtil.getNextDateHour(dataTime, 1);
    	System.out.println(dataTime.toLocaleString());
    	dataTime = DateUtil.getNextDateHour(dataTime, -24);
    	System.out.println(dataTime.toLocaleString());*/
    }
}
