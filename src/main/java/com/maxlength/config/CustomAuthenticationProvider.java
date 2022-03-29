package com.maxlength.config;

import com.maxlength.aggregate.entity.ServiceEntity;
import com.maxlength.aggregate.repository.ServiceRepository;
import com.maxlength.aggregate.service.CustomUserDetailsService;
import com.maxlength.spec.enums.Yesno;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final CustomUserDetailsService customUserDetailsService;

    private final ServiceRepository serviceRepository;

    @Override
    public Authentication authenticate(Authentication authentication) {

        Map<String, Object> detailsMap = (Map<String, Object>) authentication.getDetails();

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        ServiceEntity serviceEntity = serviceRepository.findByNameAndDelYn(String.valueOf(detailsMap.get("service")), Yesno.N);

        if(serviceEntity == null)
            throw new BadCredentialsException("service is not match");

        UserDetails userDetails = customUserDetailsService.customLoadUserByUsername(username, String.valueOf(detailsMap.get("service")), String.valueOf(detailsMap.get("provider")));
        if(userDetails == null)
            throw new BadCredentialsException("user not found");

        if(StringUtils.isEmpty(password)) {    // SNS Login
            return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
        } else {    // normal Login
            if (!passwordEncoder.matches(password, userDetails.getPassword()))
                throw new BadCredentialsException("password is not valid");

            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
            UsernamePasswordAuthenticationToken.class);
    }
}