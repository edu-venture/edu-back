package com.bit.eduventure.lecture.entity;

import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.lecture.dto.LectureDTO;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "T_LECTURE")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LEC_ID")
    private Integer id;
    @Column(name = "LEC_TITLE")
    private String title;

    @Column(name = "LEC_LIVE_ID")
    private String liveStationId;

    @Column(name = "COU_NO")
    private int couNo;

    public LectureDTO EntityTODTO() {
        LectureDTO dto = LectureDTO.builder()
                .id(this.id)
                .title(this.title)
                .liveStationId(this.liveStationId)
                .couNo(this.couNo)
                .build();

        return dto;
    }
}
