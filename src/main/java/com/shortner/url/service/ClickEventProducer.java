package com.shortner.url.service;

import com.shortner.url.DTO.UrlClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClickEventProducer {

    @Autowired
    private KafkaTemplate<String, UrlClickEvent> kafkaTemplate;

    private static final String TOPIC = "url-clicks";

    public void sendClickEvent(String shortCode) {

        UrlClickEvent event = new UrlClickEvent(shortCode, System.currentTimeMillis());

        kafkaTemplate.send(TOPIC, event);
    }
}