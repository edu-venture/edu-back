package com.bit.eduventure.timetable.dto;

import com.bit.eduventure.timetable.entity.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTableDTO {
    private Integer timeNo;
    private String timeTitle;
    private String timePlace;
    private String timeClass;
    private String timeColor;
    private String timeWeek;
    private String timeTeacher;
    private String claName;

    public TimeTable DTOTOEntity() {
        TimeTable timeTable = TimeTable.builder()
                .timeNo(this.timeNo)
                .timeTitle(this.timeTitle)
                .timePlace(this.timePlace)
                .timeColor(this.timeColor)
                .timeWeek(this.timeWeek)
                .timeClass(this.timeClass)
                .timeTeacher(this.timeTeacher)
                .claName(this.claName)
                .build();
        return timeTable;
    }
}
