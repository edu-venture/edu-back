package com.bit.eduventure.ES6_SMS.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SmsRequestDTO {

    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<MessageDTO> messages;



}
