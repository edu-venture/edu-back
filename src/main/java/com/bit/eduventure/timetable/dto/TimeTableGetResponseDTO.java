package com.bit.eduventure.timetable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableGetResponseDTO {

    private int couNo;
    private String claName; // 반이름
    private String couWeek; //요일
    private String couTime; // 시간
    private String couClass; // 강의실
    private String couColor;
    private String teacherName; // 선생님 이름 (userName)
    private String timeTitle;

}
