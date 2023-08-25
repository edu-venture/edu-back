package com.bit.eduventure.ES1_User.DTO;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    private Integer id;
    private String userId;
    private String userPw;
    private String userName;
//    private String userEmail;
    private String userTel;
    private LocalDateTime userRegdate;
    private String role;
    private String curUserPw;
    private String token;
    private String userType;
    private String userBirth;
    private String userSchool;
    private String userAddress;
    private Integer userJoinId;
    private String userAddressDetail;
    private String userConsultContent;

    private CourseDTO courseDTO;



private String approval;

    private String userSpecialNote;

    private Integer userBus;

    public User DTOToEntity() {
        Course course = Course.builder().couNo(this.courseDTO.getCouNo()).build();


        User user = User.builder()
                .id(this.id).course(course)
                .userId(this.userId).approval(this.approval)
                .userPw(this.userPw).userBus(this.userBus)
//                .userEmail(this.userEmail)
                .userType(this.userType).userSpecialNote(this.userSpecialNote).userConsultContent(this.userConsultContent)
                .userName(this.userName)
                .userTel(this.userTel).userAddressDetail(this.userAddressDetail)
                .userRegdate(LocalDateTime.now())
                .role(this.role).userBirth(this.userBirth).userSchool(this.userSchool).userAddress(this.userAddress).userJoinId(this.userJoinId)
                .build();

        return user;
    }
}
