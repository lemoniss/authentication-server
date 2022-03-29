package com.maxlength.config;

import com.maxlength.aggregate.entity.AccountEntity;
import com.maxlength.aggregate.repository.AccountRepository;
import com.maxlength.aggregate.service.CustomUserDetails;
import com.maxlength.spec.common.JwtUtil;
import com.maxlength.spec.enums.Yesno;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String publicKey;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        if(!StringUtils.isEmpty(authentication.getOAuth2Request().getGrantType()) && authentication.getOAuth2Request().getGrantType().equals("client_credentials"))
            return accessToken;

        final Map<String, Object> additionalInfo = new HashMap<>();
        Map<String, String> map = (Map<String, String>) authentication.getUserAuthentication().getDetails();

        String username = authentication.getUserAuthentication().getName();
        if(StringUtils.isEmpty(username)) {
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getUserAuthentication().getPrincipal();
            username = customUserDetails.getUsername();
        }

        String service;
        AccountEntity accountEntity;
        if (null != map) {
            service = map.get("service");
            additionalInfo.put("service", service);
            accountEntity = accountRepository.findByUsernameAdnDelYnAndProviderAndServiceName(username, Yesno.N.name(), map.get("provider"), service);
        } else {
            JwtUtil jwtUtil = new JwtUtil();
            OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
            String jwt = refreshToken.getValue();
            Map<String, Object> jwtMap = jwtUtil.decode(jwt, publicKey);
            service = String.valueOf(jwtMap.get("service"));
            additionalInfo.put("service", service);
            accountEntity = accountRepository.findByUsernameAdnDelYnAndProviderAndServiceName(username, Yesno.N.name(), String.valueOf(jwtMap.get("provider")), service);
        }

        if(accountEntity == null)
            new Exception("account not found");

        additionalInfo.put("accountId", accountEntity.getId());
        additionalInfo.put("provider", accountEntity.getProvider());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
