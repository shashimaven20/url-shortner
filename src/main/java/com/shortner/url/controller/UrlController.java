package com.shortner.url.controller;

import com.shortner.url.DTO.ShortenUrlResponse;
import com.shortner.url.entity.UrlMapping;
import com.shortner.url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    @ResponseBody
    public ShortenUrlResponse shortenUrl(@RequestBody Map<String,String> body) {
        String originalUrl = body.get("originalUrl");

        UrlMapping result = urlService.checkOriginalUrlExists(originalUrl);

        return new ShortenUrlResponse(result.getShortCode());
    }

    @GetMapping("/{shortCode}")
    public String redirect(@PathVariable String shortCode) {

        String originalUrl = urlService.getOriginalUrl(shortCode);

        return "redirect:" + originalUrl;
    }
}