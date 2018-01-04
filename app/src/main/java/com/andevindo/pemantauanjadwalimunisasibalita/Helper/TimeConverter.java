package com.andevindo.pemantauanjadwalimunisasibalita.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by heendher on 6/25/2016.
 */
public class TimeConverter {

    private static final String sAPIFormat = "h:m a";
    private static final String sCalendarFormat = "";
    private static final String sCalendarDateFormat = "dd-MM-yyyy";
    private static final String sCalendarTimeFormat = "k:m";
    private static final String sLocalDateFormat = "dd MMM yyyy";
    private static final String sLocalTimeFormat = "kk:mm";
    private static final String sLocalFormat = "dd MMM yyyy kk:mm";
    private static final String sServerDateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static Date getVaccineDate(String babyBirthDate, int vaccineTime, String vaccineFormat){
        Date baby = fromLocalStringToDate(babyBirthDate);
        if (vaccineFormat.equals("jam")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(baby);
            calendar.add(Calendar.HOUR, vaccineTime);
            return calendar.getTime();
            /*long longTime = vaccineTime;
            TimeUnit.HOURS.toMillis(longTime);*/
        }else if (vaccineFormat.equals("bulan")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(baby);
            calendar.add(Calendar.MONTH, vaccineTime);
            return calendar.getTime();
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(baby);
            calendar.add(Calendar.YEAR, vaccineTime);
            return calendar.getTime();
        }
    }

    public static String formatDateFromNewDate(Date date) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat(sLocalFormat);
        return localDateFormat.format(date).toString();
    }

    public static String fromCalendarDateToLocal(String srcDate) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat(sCalendarDateFormat);
        SimpleDateFormat localDateFormat = new SimpleDateFormat(sLocalDateFormat);
        try {
            String date = localDateFormat.format(calendarDateFormat.parse(srcDate));
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date fromLocalStringToDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sLocalFormat);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date fromLocalDateStringToDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sLocalDateFormat);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fromCalendarTimeToLocal(String srcDate) {
        SimpleDateFormat calendarTimeFormat = new SimpleDateFormat(sCalendarTimeFormat);
        SimpleDateFormat localTimeFormat = new SimpleDateFormat(sLocalTimeFormat);
        try {
            String time = localTimeFormat.format(calendarTimeFormat.parse(srcDate));
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String fromServerToLocal(String srcDate) {
        SimpleDateFormat serverDateFormat = new SimpleDateFormat(sServerDateTimeFormat);
        SimpleDateFormat localDateFormat = new SimpleDateFormat(sLocalFormat);
        try {
            String date = localDateFormat.format(serverDateFormat.parse(srcDate));
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String fromLocalToServer(String srcDate) {
        SimpleDateFormat serverDateFormat = new SimpleDateFormat(sServerDateTimeFormat);
        SimpleDateFormat localDateFormat = new SimpleDateFormat(sLocalFormat);
        try {
            String date = serverDateFormat.format(localDateFormat.parse(srcDate));
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateFromLocal(String srcDate) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat(sLocalDateFormat);
        SimpleDateFormat localFormat = new SimpleDateFormat(sLocalFormat);
        try {
            String date = localDateFormat.format(localFormat.parse(srcDate));
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTimeFromLocal(String srcDate) {
        SimpleDateFormat localTimeFormat = new SimpleDateFormat(sLocalTimeFormat);
        SimpleDateFormat localFormat = new SimpleDateFormat(sLocalFormat);
        try {
            String date = localTimeFormat.format(localFormat.parse(srcDate));
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isDifferentDay(String todayDate, String yesterdayDate) {
        Long todayD;
        Long yesterdayD;
        SimpleDateFormat localDateFormat = new SimpleDateFormat(sLocalDateFormat);
        try {
            todayD = localDateFormat.parse(todayDate).getTime();
            yesterdayD = localDateFormat.parse(yesterdayDate).getTime();
            return (todayD > yesterdayD);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}