package com.nwu.util;

/**
 * @author Rex Joush
 * @time 2021.03.29
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 关于时间的格式化信息
 */
public class TimeUtils {

    // 格式化时间
    public static String strDateFormat = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);


    /**
     * 获取 20 分钟前的时间值
     * @return 格式为 2021.02.10 20:20:20
     */
    public static String getTwentyMinuteAgo(){
        return sdf.format(new Date().getTime() - 1000 * 60 * 20);
    }


}
