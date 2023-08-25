package com.bit.eduventure.lecture.dto;

import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.lecture.entity.Lecture;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LectureDTO {

    private Integer id;
    private String title;
    private String liveStationId;

    private CourseDTO courseDTO;



    public Lecture DTOTOEntity() {
        Lecture lecture = Lecture.builder()
                .id(this.id)
                .title(this.title)
                .liveStationId(this.liveStationId)
                .course(this.courseDTO.DTOToEntity())
                .build();

        return lecture;
    }
}
