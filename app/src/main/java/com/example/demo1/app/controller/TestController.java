package com.example.demo1.app.controller;

import com.example.demo1.module.common.Constant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {
    @RequestMapping("jointBuffer1")
    public Integer jointBuffer1() {
        int count = 0;
        // 然后循环列表，在多线程下拼接“1”
        while (0 != Constant.queue.size()) {
            Constant.stringBuffer.append(Constant.queue.remove());
            count++;
            try {
                Thread.sleep(10);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    @RequestMapping("jointBuffer2")
    public Integer jointBuffer2() {
        int count = 0;
        // 然后循环列表，在多线程下拼接“1”
        while (0 != Constant.queue.size()) {
            Constant.stringBuffer.append(Constant.queue.remove());
            count++;
            try {
                Thread.sleep(15);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    @RequestMapping("getBufferLength")
    public Integer getBufferLength() {
        return Constant.stringBuffer.length();
    }

    @RequestMapping("jointBuilder1")
    public Integer jointBuilder1() {
        int count = 0;
        // 然后循环列表，在多线程下拼接“1”
        while (0 != Constant.queue2.size()) {
            Constant.stringBuilder.append(Constant.queue2.remove());
            count++;
            try {
                Thread.sleep(10);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    @RequestMapping("jointBuilder2")
    public Integer jointBuilder2() {
        int count = 0;
        // 然后循环列表，在多线程下拼接“1”
        while (0 != Constant.queue2.size()) {
            Constant.stringBuilder.append(Constant.queue2.remove());
            count++;
            try {
                Thread.sleep(15);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    @RequestMapping("getBuilderLength")
    public Integer getBuilderLength() {
        return Constant.stringBuilder.length();
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 创建 StringBuffer 和 StringBuilder
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder stringBuilder = new StringBuilder();

        // 创建任务
        Runnable task1 = () -> {
            for (int i = 0; i < 10000; i++) {
                stringBuffer.append("a");
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 10000; i++) {
                stringBuilder.append("a");
            }
        };

        // 提交任务
        executor.submit(task1);
        executor.submit(task2);

        // 关闭线程池
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // 输出结果
        System.out.println("StringBuffer length: " + stringBuffer.length());
        System.out.println("StringBuilder length: " + stringBuilder.length());
    }

}
