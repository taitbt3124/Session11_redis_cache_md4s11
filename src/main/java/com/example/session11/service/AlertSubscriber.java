package com.example.session11.service;

import com.example.session11.dto.AlertMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class AlertSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;

    public AlertSubscriber(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            AlertMessage alert = objectMapper.readValue(body, AlertMessage.class);
            System.out.println("=== THÔNG BÁO DASHBOARD QUẢN LÝ ===");
            System.out.println("Type : " + alert.getType());
            System.out.println("Nội dung: " + alert.getMessage());
            System.out.println("===================================");
        } catch (Exception e) {
            System.out.println("Không đọc được message: " + body);
        }
    }
}
