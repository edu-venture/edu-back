package com.bit.eduventure.lecture.controller;

import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.jwt.JwtTokenProvider;
import com.bit.eduventure.lecture.entity.ChatMessage;
import com.bit.eduventure.lecture.entity.LecUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("/liveChat")
@Controller
//@RestController
public class LectureChatController {

    private final SimpMessagingTemplate template;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    @GetMapping("/abc")
    public String chat() {
        return "/chat/chat";
    }
    @MessageMapping("/sendMsg/{lectureId}")
    @SendTo("/topic/lecture/{lectureId}")
    public String sendMessage(@Header("Authorization") String token,
                              @Payload String chatMessage,
                              @DestinationVariable String lectureId) {
        // JSON 파서를 사용하기 위한 ObjectMapper 객체
        ObjectMapper mapper = new ObjectMapper();

        try {
            // chatMessage를 JSON 객체로 변환
            Map<String, Object> chatMessageMap = mapper.readValue(chatMessage, Map.class);

            // content 값을 추출
            String content = (String) chatMessageMap.get("content");

            // token에서 사용자 이름 추출
            token = token.substring(7);
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            String userName = userService.findByUserId(userId).getUserName();

            // 새로운 JSON 객체 생성
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("content", content);
            responseMap.put("sender", userName);

            String returnStr = mapper.writeValueAsString(responseMap);
            // JSON 객체를 문자열로 변환하여 반환
            System.out.println("returnStr: " + returnStr);
            return returnStr;

        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // 에러가 발생하면 빈 JSON 문자열 반환
        }
    }

//    @MessageMapping("/sendMsg/{lectureId}/addUser")
//    @SendTo("/topic/lecture/{lectureId}") //보내는 곳은 똑같이
//    public String addUser(@Payload LecUser lecUser,
//                          @DestinationVariable String lectureId) {
//        return null;
//
//    }

    @PostMapping("/sendMsg")
    public void sendMessage(@RequestBody ChatMessage chatMessage,
                            @RequestHeader("Authorization") String token) {
        // 토큰에서 사용자 이름을 가져옵니다. (실제 로직은 JWT 라이브러리나 인증 로직에 따라 다를 수 있습니다.)
        String sender = jwtTokenProvider.validateAndGetUsername(token);
        chatMessage.setSender(sender);

        // 메시지를 모든 구독자에게 전송
        template.convertAndSend("/topic/lecture/123", chatMessage);
    }






//    @MessageMapping("/newUser")
//    @SendTo("/topic/public")
//    public ChatMessage newUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("username", chatMessage.sender);
//        return chatMessage;
//    }

//    @MessageMapping("/sendMessage/{lecId}")
//    @SendTo("/topic/public/{lecId}")
//    public String sendMessage(@Payload String chatMessage,
//                              @DestinationVariable String lecId) {
//        return chatBotService.processMessage(chatMessage);
//    }


}
