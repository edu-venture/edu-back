package com.bit.eduventure.ES3_Course.DTO;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES3_Course.Entity.Course;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    private Integer couNo;
    private String claName;
    private Integer id;
    private String couWeek; /* 요일 */
    private String couTime; /* n교시 */
    private String couClass; /* 강의실 */
    private String couColor;
    public Course DTOToEntity(){
        User user = User.builder().id(this.id).build();
        Course course = Course.builder().couTeacher(user).couNo(this.couNo).claName(this.claName).couWeek(this.couWeek).couTime(this.couClass).couColor(this.couColor).build();
        return course;


    }



}
