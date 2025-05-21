package com.capstone.dfbf.api.personality.controller;

import com.capstone.dfbf.api.personality.dto.PersonalityRequest;
import com.capstone.dfbf.api.personality.dto.PersonalityResponse;
import com.capstone.dfbf.api.personality.service.PersonalityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/personality")
public class PersonalityController {

    private final PersonalityService personalityService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestBody PersonalityRequest request) {
        PersonalityResponse response = personalityService.evaluate(request);
        return ResponseEntity.ok().body(response);
    }
}
