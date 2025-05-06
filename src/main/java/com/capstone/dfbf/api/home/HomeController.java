package com.capstone.dfbf.api.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String homeV1(@CookieValue(name = "accessToken", required = false) String accessToken) {
        System.out.println("Access Token: " + accessToken);
        return "Welcome to GGZZ";
    }
}
