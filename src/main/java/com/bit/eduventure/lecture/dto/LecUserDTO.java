package com.bit.eduventure.lecture.dto;

import com.bit.eduventure.lecture.entity.LecUser;
import lombok.Builder;

@Builder
public class LecUserDTO {
    int id;
    String liveStationId;
    String userName;

    public LecUser DTOTOEntity() {
        return LecUser.builder()
                .id(this.id)
                .liveStationId(this.liveStationId)
                .userName(this.userName)
                .build();
    }
}
