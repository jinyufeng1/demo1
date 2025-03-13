package com.example.demo1.module.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {
    public static String PIC_SPLIT = "\\$";  //表示单纯字符，而非正则表达式
    public static int pageSize = 10;

    public static String transformTimestamp(int timestamp) {
        Date date = new Date(timestamp * 1000L); //1000和1000L int类型不能准确转换时间戳
        // 定义日期格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 转换为字符串
        return formatter.format(date);
    }
}
