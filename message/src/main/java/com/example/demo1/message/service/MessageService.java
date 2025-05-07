package com.example.demo1.message.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.example.demo1.module.entity.MessageRecord;
import com.example.demo1.module.service.MessageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private Client client;

    @Autowired
    private MessageRecordService messageRecordService;

    public Integer sendCode(String phone) throws Exception {
        String content = "{\"code\":\"1234\"}";
        SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName("阿里云短信测试").setTemplateCode("SMS_154950909")
                .setTemplateParam(content);
        SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
        Integer statusCode = sendSmsResponse.getStatusCode();
        if (200 == statusCode) {
            // 记录到数据表
            MessageRecord messageRecord = new MessageRecord();
            messageRecord.setPhone(phone);
            messageRecord.setContent(content);
            messageRecord.setCode(statusCode);
            messageRecord.setReason("这个不知道怎么拿到");
            messageRecordService.insert(messageRecord);
        }
        return statusCode;
    }
}
