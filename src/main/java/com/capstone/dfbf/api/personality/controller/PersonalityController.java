package com.capstone.dfbf.api.personality.controller;

import com.capstone.dfbf.api.personality.dto.PersonalityImageRes;
import com.capstone.dfbf.api.personality.dto.PersonalityRequest;
import com.capstone.dfbf.api.personality.dto.PersonalityResponse;
import com.capstone.dfbf.api.personality.service.PersonalityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/personality")
public class PersonalityController {

    private final PersonalityService personalityService;

    @PostMapping("/upload")
    public PersonalityImageRes uploadPersonality(@RequestPart("personality-file") MultipartFile file) throws IOException {
        return personalityService.uploadImage(file);
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestBody PersonalityRequest request) {
        PersonalityResponse response = personalityService.evaluate(request);
        return ResponseEntity.ok().body(response);
    }
}
