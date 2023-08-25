package com.bit.eduventure.postIt.dto;

import lombok.Data;

@Data
public class PostItDTO {
    private Integer senderId; // 보내는 사람의 ID
    private Integer receiverId; // 받는 사람의 ID
    private String message; // 포스트잇 메시지
}
