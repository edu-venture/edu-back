package com.bit.eduventure.lecture.controller;


import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/lecUser")
@RestController
public class LecUserController {
    @GetMapping("/getCurrentUserName")
    public Map<String, String> getCurrentUserName(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Map<String, String> response = new HashMap<>();
        response.put("userName", customUserDetails.getUser().getUserName());
        return response;
    }
}
