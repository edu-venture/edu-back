package com.bit.eduventure.timetable.dto;

import com.bit.eduventure.ES3_Course.Entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 시간표 등록할 때 보내는 요청 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableRegistResquestDTO {

    private int couNo;
    private String claName; // 반이름
    private String couWeek; //요일
    private String couTime; // 시간
    private String couClass; // 강의실
    private String teacherName; // 선생님 이름 (userName)
    private String couColor;

    public Course toEntity() {
        return Course.builder()
                .couNo(this.couNo)
                .claName(this.claName)
                .couWeek(this.couWeek)
                .couTime(this.couTime)
                .couClass(this.couClass)
                .couColor(this.couColor)
                .build();
    }

}
