package com.ms.bap.services.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SenderService {

    private static final Logger logger = LoggerFactory.getLogger(SenderService.class);
    private final WebClient webClient;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    public SenderService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .build();
    }

    public String send(String url, HttpHeaders headers, String json) {

        log.info("Sending Input Request {}", url);
        String responseData="";
        try {
            Mono<String> response = this.webClient.post()
                    .uri(url)
                    .headers(h -> {
                        h.addAll(headers);
                    })
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(json), String.class)
                    .retrieve()
                    .bodyToMono(String.class);
            responseData = response.block();
            log.info("responseData from bpp post call: {}", responseData);

        } catch (Exception e) {
            log.error("Exception while calling the post at url {}", url);
            log.error("Exception is {}", e.getMessage());
        }
        return responseData;
    }

    public String sendWithTemplate(String url, HttpHeaders headers, String json) {

        log.info("Calling BPP {}", url);
        try {
            ResponseEntity<String> response= restTemplate.postForEntity(url,json, String.class);
            log.info("response from post call: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("Exception while calling the post at url {}", url);
            log.error("Exception is {}", e.getMessage());
        }
        return null;
    }

}
