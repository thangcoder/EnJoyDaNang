package node.com.enjoydanang.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author: Tavv
 * Created on 27/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DateUtils {

    /**
     * 获取yyyyMMdd格式日期
     *
     * @param time
     * @return
     */
    public static Date getDate(String time, String s_format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(s_format);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 根扰日期字符串获得星期
     */

    public static String getWeekOfStr(String time, String format) {

        Date date = getDate(time, format);
        if (date != null) {
            return getWeekOfDate(date);
        }
        return "";
    }

    /**
     *
     * @param date1
     * @param date2
     * @param dtFormat
     * @return
     */
    public static int compareDate(String date1, String date2, String dtFormat) {
        DateFormat df = new SimpleDateFormat(dtFormat);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                // System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    public static String getCurrentTime() {
        // return getCurrentTime("yyyy-MM-dd HH:mm:ss");
        return getCurrentTime("yyyyMMddHHmmss");
    }

    public static String getDateStr(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        }
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    // 获取当前日期的前一天时间
    public static Date getYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    // 判断给定的日期是否为当前日期的前一天
    public static boolean dayIsYesterday(String dayString) {
        Date date = new Date(System.currentTimeMillis());
        Date yesterday = getYesterday(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String yesterdayString = simpleDateFormat.format(yesterday);
        return dayString.substring(0, 8).equals(yesterdayString.substring(0, 8));
    }

    // 判断给定日期是否为本月的某一天
    public static boolean dayIsMouthaDay(String dayString) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String todayString = simpleDateFormat.format(date);
        return dayString.substring(0, 6).equals(todayString.substring(0, 6));
    }

    /**
     * 日期格式化.
     *
     * @param date
     *            日期原字符串
     * @param oldFormat
     *            原日期格式
     * @param newFormat
     *            新日期格式
     * @return 新字符串
     * @throws ParseException
     */
    public static String dateFormat(String date, String oldFormat, String newFormat) throws ParseException {
        if (date == null || "".equals(date))
            return "";

        SimpleDateFormat oldSDF = new SimpleDateFormat(oldFormat);
        SimpleDateFormat newSDF = new SimpleDateFormat(newFormat);
        return newSDF.format(oldSDF.parse(date));

    }
}
