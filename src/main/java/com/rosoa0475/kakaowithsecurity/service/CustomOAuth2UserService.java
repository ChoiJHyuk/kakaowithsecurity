package com.rosoa0475.kakaowithsecurity.service;

import com.rosoa0475.kakaowithsecurity.domain.UserEntity;
import com.rosoa0475.kakaowithsecurity.dto.CustomOAuth2User;
import com.rosoa0475.kakaowithsecurity.dto.KakaoResponse;
import com.rosoa0475.kakaowithsecurity.dto.OAuth2Response;
import com.rosoa0475.kakaowithsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//                          UserDetailsService와 유사
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    //                              OAuth2UserRequest에는 access token과 유저 정보가 포함되어 있다.
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //                          user 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //                            getAttributes()에는 리소스 서버에서 보낸 유저 정보가 있다.
        System.out.println(oAuth2User.getAttributes());

        OAuth2Response oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        String role = "ROLE_USER";

        UserEntity userEntity = new UserEntity();
        userEntity.setNickname(oAuth2Response.getnickname());
        userEntity.setRole(role);

        userRepository.save(userEntity);

        //UserDetailsService와 다른 점은 이 때 리턴한 것은 그저 SecurityContextHolder에 저장하기 위함
        return new CustomOAuth2User(oAuth2Response, role);
    }
}
