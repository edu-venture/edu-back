package com.bit.eduventure.lecture.entity;

import com.bit.eduventure.lecture.dto.LecUserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_LECTURE_USER")
public class LecUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String liveStationId;
    String userName;

    public LecUserDTO EntityTODTO() {
        return LecUserDTO.builder()
                .id(this.id)
                .liveStationId(this.liveStationId)
                .userName(this.userName)
                .build();
    }
}
