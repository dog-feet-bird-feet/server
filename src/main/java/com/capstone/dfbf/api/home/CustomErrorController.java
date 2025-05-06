package com.capstone.dfbf.api.home;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController{

    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleError(HttpServletRequest request) {
        return Map.of(
                "error", "Not Found",
                "message", "요청한 URL은 존재하지 않습니다.",
                "status", 404
        );
    }
}