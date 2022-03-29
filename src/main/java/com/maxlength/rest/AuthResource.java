package com.maxlength.rest;

import com.maxlength.spec.common.BaseException;
import com.maxlength.spec.common.BaseResponse;
import com.maxlength.spec.vo.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Slf4j
@RestController
@RequestMapping("/auth")
@EnableSwagger2
@EnableAsync
@Api(tags= {"[인증] - 인증 API"}, protocols="http", produces="application/json", consumes="application/json")
public class AuthResource {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    public AuthResource(@Qualifier("oauthWebCLient") WebClient webClient) {
        this.webClient = webClient;
    }


    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "계정/비번 로그인입니다.")
    public ResponseEntity<Mono<Auth.LoginResponse>> login(@Valid @RequestBody Auth.LoginRequest request) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", request.getUsername());
        params.add("password", request.getPassword());
        params.add("service", request.getService());
        params.add("provider", request.getProvider().name());

        return BaseResponse.ok(getToken(params));
    }

    @PostMapping("/snsLogin")
    @ApiOperation(value = "SNS로그인", notes = "SNS로그인")
    public ResponseEntity<Mono<Auth.LoginResponse>> snsLogin(@Valid @RequestBody Auth.SnsLoginRequest request) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", request.getUsername());
        params.add("provider", request.getProvider().name());
        params.add("service", request.getService());

        return BaseResponse.ok(getToken(params));
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "리프레시토큰 요청", notes = "리프레시토큰으로 액세스토큰을 새로 발행합니다.")
    public ResponseEntity<Mono<Auth.LoginResponse>> refreshToken(@Valid @RequestBody Auth.RefreshTokenRequest request) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", request.getRefreshToken());

        return BaseResponse.ok(getToken(params));
    }

    @PostMapping("/moduleLogin")
    @ApiOperation(value = "모듈로그인", notes = "모듈간 인증입니다.")
    public ResponseEntity<Mono<Auth.LoginResponse>> moduleLogin() {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");

        return BaseResponse.ok(getToken(params));
    }

    private Mono<Auth.LoginResponse> getToken(MultiValueMap<String, String> params) {
        return webClient
            .post()
            .headers(httpHeaders -> httpHeaders.setBasicAuth(clientId, clientSecret))
            .body(BodyInserters.fromFormData(params))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BaseException("인증실패")))
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new BaseException("서버오류")))
            .bodyToMono(Map.class)
            .map(responseMap -> new Auth.LoginResponse((String) responseMap.get("access_token"), (String) responseMap.get("refresh_token")));
    }

}




