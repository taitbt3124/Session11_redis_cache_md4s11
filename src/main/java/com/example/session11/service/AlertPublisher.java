package com.example.session11.service;

import com.example.session11.dto.AlertMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertPublisher {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public AlertPublisher(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(String channel, String type, String message) {
        try {
            String json = objectMapper.writeValueAsString(new AlertMessage(type, message));
            stringRedisTemplate.convertAndSend(channel, json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Không thể chuyển message sang JSON", e);
        }
    }
}
