package com.example.demo1.app.test.stringbuilderandbuffer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
@RestController
public class TestController {
    @RequestMapping("jointBuffer")
    public Integer jointBuffer() {
        //先生成一个10000个1的列表
        int clientTotal = 10000;
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < clientTotal; i++) {
            queue.add(1);
        }
        StringBuffer stringBuffer = new StringBuffer();
        ExecutorService executorService = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                try {
//                    stringBuffer.append(queue.remove());
                    stringBuffer.append(1);
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        return stringBuffer.length();
    }

    @RequestMapping("jointBuilder")
    public Integer jointBuilder() {
        //先生成一个10000个1的列表
        int clientTotal = 10000;
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < clientTotal; i++) {
            queue.add(1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        ExecutorService executorService = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                try {
//                    stringBuilder.append(queue.remove());
                    stringBuilder.append(1);
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }

        return stringBuilder.length();
    }
}
