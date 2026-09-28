package com.example.session11.controller;

import com.example.session11.dto.AlertRequest;
import com.example.session11.service.AlertPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertPublisher alertPublisher;

    public AlertController(AlertPublisher alertPublisher) {
        this.alertPublisher = alertPublisher;
    }

    @PostMapping
    public ResponseEntity<String> publish(@RequestBody AlertRequest request) {
        alertPublisher.publish(request.getTopicListen(), request.getType(), request.getMessage());
        return ResponseEntity.ok("Đã gửi thông báo đến kênh : " + request.getTopicListen());
    }
}
