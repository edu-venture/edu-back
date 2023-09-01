package com.bit.eduventure.lecture.controller;

import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.jwt.JwtTokenProvider;
import com.bit.eduventure.lecture.entity.ChatMessage;
import com.bit.eduventure.lecture.entity.LecUser;
import com.bit.eduventure.lecture.service.LecUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RequestMapping("/liveChat")
@Controller
//@RestController
public class LectureChatController {

    private final SimpMessagingTemplate template;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final LecUserService lecUserService;


    @GetMapping("/abc")
    public String chat() {
        return "/chat/chat";
    }

    @MessageMapping("/sendMsg/{lectureId}")
    @SendTo("/topic/lecture/{lectureId}")
    public String sendMessage(@Header("Authorization") String token,
                              @Payload String chatMessage,
                              @DestinationVariable String lectureId) {
        Gson gson = new Gson();
        try {
            Map<String, Object> chatMsgMap = gson.fromJson(chatMessage, Map.class);

            String content = (String) chatMsgMap.get("content");

            token = token.substring(7);
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            String userName = userService.findByUserId(userId).getUserName();

            ChatMessage returnMsg = ChatMessage.builder()
                    .content(content)
                    .sender(userName)
                    .build();

            return gson.toJson(returnMsg);

        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    @MessageMapping("/sendMsg/{lectureId}/addUser")
    @SendTo("/topic/lecture/{lectureId}") //보내는 곳은 똑같이
    public String addUser(@Header("Authorization") String token,
                          @DestinationVariable String lectureId) {
        Gson gson = new Gson();
        try {
            token = token.substring(7);
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            String userName = userService.findByUserId(userId).getUserName();

            ChatMessage returnMsg = ChatMessage.builder()
                    .content(userName + "님이 입장하였습니다.")
                    .build();

            //DB에 강의에 들어온 유저 저장
            lecUserService.enterLecUser(lectureId, userName);

            List<LecUser> lecUserList = lecUserService.lecUserList(lectureId);

            if (!lecUserList.isEmpty()) {
                List<String> userList = lecUserList.stream()
                        .map(LecUser::getUserName)
                        .collect(Collectors.toList());
                returnMsg.setUserList(userList);
            }

            return gson.toJson(returnMsg);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @MessageMapping("/sendMsg/{lectureId}/leave")
    @SendTo("/topic/lecture/{lectureId}") //보내는 곳은 똑같이
    public String leaveUser(@Header("Authorization") String token,
                            @DestinationVariable String lectureId) {
        Gson gson = new Gson();
        try {
            token = token.substring(7);
            String userId = jwtTokenProvider.validateAndGetUsername(token);
            String userName = userService.findByUserId(userId).getUserName();

            ChatMessage returnMsg = ChatMessage.builder()
                    .content(userName + "님이 나가셨습니다.")
                    .build();

            //DB에 강의에 나간 유저 삭제
            lecUserService.leaveLecUser(lectureId, userName);

            List<LecUser> lecUserList = lecUserService.lecUserList(lectureId);

            if (!lecUserList.isEmpty()) {
                List<String> userList = lecUserList.stream()
                        .map(LecUser::getUserName)
                        .collect(Collectors.toList());
                returnMsg.setUserList(userList);
            }

            return gson.toJson(returnMsg);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

//    @PostMapping("/sendMsg")
//    public void sendMessage(@RequestBody ChatMessage chatMessage,
//                            @RequestHeader("Authorization") String token) {
//        // 토큰에서 사용자 이름을 가져옵니다. (실제 로직은 JWT 라이브러리나 인증 로직에 따라 다를 수 있습니다.)
//        String sender = jwtTokenProvider.validateAndGetUsername(token);
//        chatMessage.setSender(sender);
//
//        // 메시지를 모든 구독자에게 전송
//        template.convertAndSend("/topic/lecture/123", chatMessage);
//    }



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
