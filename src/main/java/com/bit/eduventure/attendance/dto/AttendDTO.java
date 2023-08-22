package com.bit.eduventure.attendance.dto;

import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.attendance.entity.Attend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendDTO {


    private int id;
    private User user;
    private LocalDateTime attStart;
    private LocalDateTime attFinish;
    private String attContent;


    public AttendDTO(Attend attend) {
        this.id = attend.getId();
        this.user = attend.getUser();
        this.attStart = attend.getAttStart();
        this.attFinish = attend.getAttFinish();
        this.attContent = attend.getAttContent();

    }

    public Attend DTOToEntity() {
        Attend attend = Attend.builder()
                .id(this.id)
                .user(this.user)
                .attStart(this.attStart)
                .attFinish(this.attFinish)
                .attContent(this.attContent)
                .build();
        return attend;
    }

}
