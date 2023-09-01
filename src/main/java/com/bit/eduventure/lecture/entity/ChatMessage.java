package com.bit.eduventure.lecture.entity;


import lombok.Data;

@Data
public class ChatMessage {
    private String content;
    private String sender;

    // 기본 생성자, 게터, 세터, toString 등의 메서드들 ...
}