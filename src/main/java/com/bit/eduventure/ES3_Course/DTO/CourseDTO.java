package com.bit.eduventure.ES3_Course.DTO;


import com.bit.eduventure.ES1_User.DTO.UserDTO;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES3_Course.Entity.Course;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    private Integer couNo;
    private String claName;
    private UserDTO userDTO;

    public Course DTOToEntity(){
        Course course = Course.builder()
                .couNo(this.couNo)
                .claName(this.claName)
                .user(this.userDTO.DTOToEntity())
                .build();
        return course;
    }



}
