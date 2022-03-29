package com.maxlength.aggregate.service;

import com.maxlength.aggregate.repository.ClientDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

@Slf4j
@Service
@EnableCaching
public class CustomClientDetailsService implements ClientDetailsService {


    private final ClientDetailsRepository clientDetailsRepository;

    public CustomClientDetailsService(ClientDetailsRepository clientDetailsRepository) {
        this.clientDetailsRepository = clientDetailsRepository;
    }

    @Cacheable("oauthClientDetailsCache")
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return new BaseClientDetails(clientDetailsRepository.findByClientId(clientId));


    }

}
