package com.example.demo1.message.controller;

import com.example.demo1.module.entity.MessageTask;
import com.example.demo1.module.service.MessageTaskService;
import com.example.demo1.module.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/async")
public class AsyncMessageController {
    @Autowired
    private MessageTaskService messageTaskService;

    /**
     * 异步发送一个验证码
     * @param phone
     */
    @RequestMapping("/send/code/single")
    public Response<Boolean> asynSendCodeSingle(@RequestParam("phone") String phone) {
        MessageTask messageTask = new MessageTask();
        messageTask.setPhone(phone);
        messageTask.setStatus(0);
        messageTaskService.insert(messageTask);
        return new Response<>(1001, true);
    }

    /**
     * 同步发送多个验证码
     * @param phones
     */
    @RequestMapping("/send/code/multi")
    public Response<Boolean> sendCodeMulti(@RequestParam("phones") List<String> phones) {
        // 使用多线程
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(phones.size());

        AtomicBoolean ret = new AtomicBoolean(true);
        for (String phone : phones) {
            executorService.execute(() -> {
                try {
                    MessageTask messageTask = new MessageTask();
                    messageTask.setPhone(phone);
                    messageTask.setStatus(0);
                    messageTaskService.insert(messageTask);
                }
                catch (Exception e) {
                    ret.set(false);
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        return new Response<>(1001, ret.get());
    }
}
