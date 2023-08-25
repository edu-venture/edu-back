package com.bit.eduventure.lecture.entity;

import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.lecture.dto.LectureDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
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

    @ManyToOne
    @JoinColumn(name = "COU_NO")
    private Course course;

    public LectureDTO EntityTODTO() {
        LectureDTO dto = LectureDTO.builder()
                .id(this.id)
                .title(this.title)
                .liveStationId(this.liveStationId)
                .courseDTO(this.course.EntityToDTO())
                .build();

        return dto;
    }
}
