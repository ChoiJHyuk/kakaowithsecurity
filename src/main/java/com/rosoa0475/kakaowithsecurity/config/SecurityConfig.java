package com.rosoa0475.kakaowithsecurity.config;

import com.rosoa0475.kakaowithsecurity.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable);
        http
                .formLogin(AbstractHttpConfigurer::disable);
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        http    //oauth2Login()은 oauth2 관련 필터 사용하게 하는 역할
                .oauth2Login((oauth2)->oauth2
                        //리소스 서버에서 정보를 가져왔을 때 실행할 UserService 설정
                        .userInfoEndpoint((userInfoEndpoint)->userInfoEndpoint
                                .userService(customOAuth2UserService)));
        http
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/","/oauth2/**","/login/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
