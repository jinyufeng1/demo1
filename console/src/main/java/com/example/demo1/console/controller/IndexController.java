package com.example.demo1.console.controller;

import com.example.demo1.console.domain.IndexVo;
import com.example.demo1.module.common.Response;
import com.example.demo1.module.service.index.BannerService;
import com.example.demo1.module.service.index.ChannelService;
import com.example.demo1.module.service.index.EventService;
import com.example.demo1.module.service.index.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ItemService itemService;

    /**
     * 模拟多数据源接口
     * @return
     */
    @RequestMapping("getData")
    public Response<IndexVo> getData() {
        // 固定线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(4);

        // 模拟多线程去不同的service对应的数据源取数据
        AtomicReference<String> bannerData = new AtomicReference<>();
        executorService.execute(() -> {
            bannerData.set(bannerService.getData());
            countDownLatch.countDown();
        });

        AtomicReference<String> channelData = new AtomicReference<>();
        executorService.execute(() -> {
            channelData.set(channelService.getData());
            countDownLatch.countDown();
        });

        AtomicReference<String> eventData = new AtomicReference<>();
        executorService.execute(() -> {
            eventData.set(eventService.getData());
            countDownLatch.countDown();
        });

        AtomicReference<String> itemData = new AtomicReference<>();
        executorService.execute(() -> {
            itemData.set(itemService.getData());
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Response<>(3001);
        }

        IndexVo ret = new IndexVo();
        ret.setBannerData(bannerData.get());
        ret.setChannelData(channelData.get());
        ret.setEventData(eventData.get());
        ret.setItemData(itemData.get());
        return new Response<>(1001, ret);
    }
}
