package com.maxlength.spec.vo;

import com.maxlength.spec.enums.Provider;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@ApiModel(description = "인증 VO")
public class Auth {

    @Getter
    public static class LoginRequest {

        @NotBlank(message = "로그인아이디없음")  // 로그인ID가 없습니다.
        @ApiModelProperty(notes = "로그인ID", required = true, example = "test")
        private String username;

        @NotBlank(message = "비밀번호없음")
        @ApiModelProperty(notes = "비밀번호", required = true, example = "1234")
        private String password;

        @NotBlank(message = "서비스가 없습니다.")
        @ApiModelProperty(notes = "서비스", required = true, example = "I, V 중 하나")
        private String service;

        @NotNull(message = "계정제공자가 없습니다.")
        @ApiModelProperty(notes = "계정제공자", example = "VIMUSE, INSTANTENEGY 중 하나")
        @Enumerated(EnumType.STRING)
        private Provider provider;
    }

    @Getter
    public static class SnsLoginRequest {

        @NotBlank(message = "로그인아이디없음")  // 로그인ID가 없습니다.
        @ApiModelProperty(notes = "로그인ID", required = true, example = "test")
        private String username;

        @NotBlank(message = "서비스가 없습니다.")
        @ApiModelProperty(notes = "서비스", required = true, example = "I, V 중 하나")
        private String service;

        @NotNull(message = "계정제공자가 없습니다.")
        @ApiModelProperty(notes = "계정제공자", example = "GOOGLE, NAVER, KAKAO, APPLE 이중 하나")
        @Enumerated(EnumType.STRING)
        private Provider provider;
    }

    @Getter
    public static class LoginResponse {

        @ApiModelProperty(notes = "액세스토큰", example = "인증서버가 발행한 access_token")
        private String accessToken;

        @ApiModelProperty(notes = "리프레시토큰", example = "인증서버가 발행한 refresh_token")
        private String refreshToken;

        public LoginResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Getter
    public static class RefreshTokenRequest {

        @NotBlank(message = "리프레시토큰이 없습니다.")
        @ApiModelProperty(notes = "리프레시토큰", required = true, example = "최초인증시 발행한 리프레시토큰")
        private String refreshToken;
    }

    @Getter
    public static class ManagerLoginRequest {

        @NotBlank(message = "로그인아이디없음")
        @ApiModelProperty(notes = "로그인ID", required = true, example = "test")
        private String username;

        @NotNull(message = "관리자식별자없음")
        @ApiModelProperty(notes = "관리자식별자", required = true, example = "1")
        private Long managerId;

        @NotNull(message = "역할이 없습니다.")
        @ApiModelProperty(notes = "역할", required = true, example = "ROLE_CS")
        private String[] roles;

        @NotBlank(message = "서비스가 없습니다.")
        @ApiModelProperty(notes = "서비스", required = true, example = "I, V 중 하나")
        private String service;

        @NotNull(message = "계정제공자가 없습니다.")
        @ApiModelProperty(notes = "계정제공자", example = "VIMUSE, INSTANTENEGY 중 하나")
        @Enumerated(EnumType.STRING)
        private Provider provider;
    }
}
