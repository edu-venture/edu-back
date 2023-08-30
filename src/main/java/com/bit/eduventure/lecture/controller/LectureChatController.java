package com.bit.eduventure.lecture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/liveChat")
@RestController
public class LectureChatController {
    @MessageMapping("/sendMessage/{liveStationId}")
    @SendTo("/topic/public/{liveStationId}")
    public String sendMessage(@Payload String chatMessage,
                              @DestinationVariable String liveStationId) {
        System.out.println(chatMessage);
        return chatMessage;
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
