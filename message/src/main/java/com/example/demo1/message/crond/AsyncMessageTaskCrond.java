package com.example.demo1.message.crond;

import com.example.demo1.module.entity.MessageTask;
import com.example.demo1.message.service.MessageService;
import com.example.demo1.module.service.MessageTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AsyncMessageTaskCrond {
    @Autowired
    private MessageTaskService messageTaskService;

    @Autowired
    private MessageService messageService;

    private void doTask(MessageTask task) {
        Integer code = 0;
        try {
            code = messageService.sendCode(task.getPhone());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // 发送成功
        if (200 == code) {
            task.setStatus(1);
        }
        // 发送失败
        else {
            task.setStatus(2);
        }
        messageTaskService.update(task);
    }

    // 每10秒执行一次
    @Scheduled(fixedDelay = 10000)
    public void fixedDelayJob() {
        List<MessageTask> tasks = messageTaskService.getByStatus(0);
        int size = tasks.size();
        if (0 == size) {
            return;
        }

        // 大于等于5个的时候使用多线程
        if (5 <= size) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (MessageTask task : tasks) {
                executorService.execute(() -> doTask(task));
            }
        }
        else {
            for (MessageTask task : tasks) {
                doTask(task);
            }
        }

    }

}
