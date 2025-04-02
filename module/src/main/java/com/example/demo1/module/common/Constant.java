package com.example.demo1.module.common;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Constant {
    public static String PIC_SPLIT = "\\$";  //表示单纯字符，而非正则表达式
    public static int PAGE_SIZE = 10;

    public static Queue<Integer> queue = new ConcurrentLinkedQueue<>(); //非线程安全的队列
    public static Queue<Integer> queue2 = new ConcurrentLinkedQueue<>(); //非线程安全的队列
    public static StringBuilder stringBuilder = new StringBuilder();
    public static StringBuffer stringBuffer = new StringBuffer();

}
