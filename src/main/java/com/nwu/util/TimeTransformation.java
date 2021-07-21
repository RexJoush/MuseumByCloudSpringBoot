package com.nwu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转化
 */
public class TimeTransformation {
    public static String FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * 世界时间转化为北京时间
     * @param UTCStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static String UTCToCST(String UTCStr, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(UTCStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return sdf.format(calendar.getTime());
    }

    /**
     * local时间转换成UTC时间
     * @param localTime
     * @param format
     * @return
     */
    public static String localToUTC(String localTime, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date localDate = sdf.parse(localTime);
        /** long时间转换成Calendar */
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(localDate);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        return sdf.format(calendar.getTimeInMillis());
    }
}
