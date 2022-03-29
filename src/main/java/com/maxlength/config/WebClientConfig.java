package com.maxlength.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${server.protocal}")
    private String protocal;

    @Value("${server.hostname}")
    private String hostname;

    @Value("${server.port}")
    private String port;

    @Bean
    @Qualifier("oauthWebCLient")
    public WebClient oauthWebClient() {

        String baseUrl = String.format("%s://%s:%s/oauth/token", protocal, hostname, port);

        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }
}
