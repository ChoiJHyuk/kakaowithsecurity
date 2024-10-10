package com.rosoa0475.kakaowithsecurity.controller;

import com.rosoa0475.kakaowithsecurity.oauth2.custom.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String mainMethod(@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        return oauth2User.getId();
    }
}
