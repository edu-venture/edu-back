package com.bit.eduventure.lecture.controller;

import com.bit.eduventure.lecture.entity.LecUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/liveChat")
@Controller
//@RestController
public class LectureChatController {
    @GetMapping("/abc")
    public String chat() {
        return "/chat/chat";
    }
    @MessageMapping("/sendMsg/{lectureId}")
    @SendTo("/topic/lecture/{lectureId}")
    public String sendMessage(@Payload String chatMessage,
                              @DestinationVariable String lectureId) {
        System.out.println(chatMessage);
        System.out.println();
        return chatMessage;
    }

    @MessageMapping("/sendMsg/{lectureId}/addUser")
    @SendTo("/topic/lecture/{lectureId}") //보내는 곳은 똑같이
    public String addUser(@Payload LecUser lecUser,
                          @DestinationVariable String lectureId) {
        return null;

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
