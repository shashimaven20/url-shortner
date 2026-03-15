package com.shortner.url.service;

import com.shortner.url.DTO.UrlClickEvent;
import com.shortner.url.repo.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClickEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ClickEventConsumer.class);

    @Autowired
    private UrlRepository urlRepository;

    @KafkaListener(topics = "url-clicks")
    public void consume(UrlClickEvent event) {
        System.out.println("Event consumed: "+ event);
        log.info("event: {}", event);
        urlRepository.incrementClick(event.getShortCode());
    }
}