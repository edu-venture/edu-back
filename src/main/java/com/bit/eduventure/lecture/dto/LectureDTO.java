package com.bit.eduventure.lecture.dto;

import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.lecture.entity.Lecture;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LectureDTO {

    private Integer id;
    private String title;
    private String write;
    private String liveStationId;

    private Course course;



    public Lecture DTOTOEntity() {
        Lecture lecture = Lecture.builder()
                .id(this.id)
                .title(this.title)
                .write(this.write)
                .liveStationId(this.liveStationId)
                .course(this.course)
                .build();

        return lecture;
    }
}
