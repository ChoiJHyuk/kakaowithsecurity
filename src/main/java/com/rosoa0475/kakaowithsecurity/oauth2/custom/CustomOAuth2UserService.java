package com.rosoa0475.kakaowithsecurity.oauth2.custom;

import com.rosoa0475.kakaowithsecurity.domain.UserEntity;
import com.rosoa0475.kakaowithsecurity.oauth2.response.KakaoResponse;
import com.rosoa0475.kakaowithsecurity.oauth2.response.OAuth2Response;
import com.rosoa0475.kakaowithsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
//                          UserDetailsService와 유사
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    //                              OAuth2UserRequest에는 access token과 설정 정보가 포함되어 있다.
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //                          user 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //                            getAttributes()에는 리소스 서버에서 보낸 유저 정보가 있다.
        System.out.println(oAuth2User.getAttributes());

//        userRequest.getClientRegistration().getRegistrationId()로
//        서비스명 받아온 뒤에 서비스에 맞는 Response 생성
        OAuth2Response oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        String role = "ROLE_USER";

        Optional<UserEntity> byRegistrationId = userRepository
                .findByRegistrationId(oAuth2Response.getId());

        if (byRegistrationId.isPresent()) {
            UserEntity user = byRegistrationId.get();
            user.setNickname(oAuth2Response.getnickname());
        } else {
            UserEntity user = new UserEntity();
            user.setNickname(oAuth2Response.getnickname());
            user.setRegistrationId(oAuth2Response.getId());
            user.setRole(role);
            userRepository.save(user);
        }
        //UserDetailsService와 다른 점은 이 때 리턴한 것은 그저 SecurityContextHolder에 저장하기 위함
        return new CustomOAuth2User(oAuth2Response, role);
    }
}
