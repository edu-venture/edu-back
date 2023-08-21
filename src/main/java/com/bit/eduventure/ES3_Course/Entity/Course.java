package com.bit.eduventure.ES3_Course.Entity;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table: 테이블 이름등을 지정
@Table(name="T_COURSE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couNo;

    private String claName;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User couTeacher;

    private String couWeek; /* 요일 */
    private String couTime; /* n교시 */
    private String couClass; /* 강의실 */
    private String couColor;

    public CourseDTO EntityToDTO(){
        CourseDTO courseDTO = CourseDTO.builder().id(this.couTeacher.getId()).couNo(this.couNo).claName(this.claName).couWeek(this.couWeek).couTime(this.couTime).couClass(this.couClass).couColor(this.couColor).build();
        return courseDTO;

    }




}