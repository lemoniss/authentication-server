package com.maxlength.aggregate.service;

import com.maxlength.aggregate.entity.AccountEntity;
import com.maxlength.aggregate.entity.AccountRoleEntity;
import com.maxlength.aggregate.repository.AccountRepository;
import com.maxlength.spec.common.BaseException;
import com.maxlength.spec.common.JwtUtil;
import com.maxlength.spec.enums.Yesno;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String publicKey;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getParameter("refresh_token");

        if (StringUtils.isBlank(token)) {
            token = request.getHeader("Authorization");
        }

        JwtUtil jwtUtil = new JwtUtil();
        Map<String, Object> jwtMap = new HashMap<>();
        try {
            jwtMap = jwtUtil.decode(token.replaceAll("Bearer ", ""), publicKey);
        } catch (Exception e) {
            throw new BaseException("token decode fail");
        }

        AccountEntity accountEntity = accountRepository.findByIdAndDelYn(Long.valueOf((Integer) jwtMap.get("accountId")), Yesno.N);

        if(accountEntity == null)
            throw new UsernameNotFoundException("Username Not Found");

        return userDetails(accountEntity);
    }

    public CustomUserDetails customLoadUserByUsername(String username, String service, String provider) throws UsernameNotFoundException {

        AccountEntity accountEntity = accountRepository.findByUsernameAdnDelYnAndProviderAndServiceName(username, Yesno.N.name(), provider, service);

        if(accountEntity == null)
            throw new UsernameNotFoundException("Username Not Found");

        return userDetails(accountEntity);
    }

    public CustomUserDetails userDetails(AccountEntity accountEntity) {

        CustomUserDetails customUserDetails = new CustomUserDetails();
        List<GrantedAuthority> authorityList = new ArrayList<>();

        for(AccountRoleEntity accountRoleEntity: accountEntity.getAccountRoleEntityList()) {
            authorityList.add(new SimpleGrantedAuthority(accountRoleEntity.getRoleEntity().getName()));
        }

        customUserDetails.setUsername(accountEntity.getUsername());
        customUserDetails.setPassword(accountEntity.getPassword());
        customUserDetails.setAuthorities(authorityList);

        return customUserDetails;
    }

}
