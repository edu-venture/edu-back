package com.bit.eduventure.SMS.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class MessageDTO {

    String to;
    String content;
}
