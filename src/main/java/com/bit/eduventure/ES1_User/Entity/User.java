package com.bit.eduventure.ES1_User.Entity;


import com.bit.eduventure.ES1_User.DTO.UserDTO;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
@Entity
@Table(name = "T_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "USER_NO") //
    private Integer id;
    @Column(unique = true)
    private String userId;
    @Column(name = "USER_PWD")
    private String userPw;
    @Column(name = "USER_NAME")
    private String userName;
//    @Column(name = "USER_EMAIL")
//    private String userEmail;
    @Column(name = "USER_TEL")
    private String userTel;
    @Column(name = "USER_BIRTH")
    private String userBirth;
    @Column(name = "USER_SCHOOL")
    private String userSchool;
    @Column(name = "USER_ADDRESS")
    private String userAddress;
    @Column(name = "USER_ADDRESSDETAIL")
    private String userAddressDetail;
    @Column(name = "USER_BUS")
    private Integer userBus;
    @Column(name = "USER_JOIN_ID") //
    private Integer userJoinId;

    @Column(name = "USER_SCORE")
    @ColumnDefault("0")

    private Integer userScore;




    @Column(name = "USER_TYPE")
    private String userType;
    @Column(name = "USER_CONSULTCONTENT")
    private String userConsultContent;
    @Column(name = "USER_SPECIALNOTE")
    private String userSpecialNote;
    @Builder.Default
    private LocalDateTime userRegdate = LocalDateTime.now();

    @Column(name = "USER_APPROVAL")
    private String approval="x";


    @ManyToOne
    //2 Fk로 가져올 컬럼 지정
    @JoinColumn(name="COU_NO")
    //1 boardno를 가져올 객체를 맴버변수로 선언
    private Course course;


    @Column
    @ColumnDefault("'ROLE_USER'")
    private String role;
    public UserDTO EntityToDTO() {
        CourseDTO courseDTO = null;
        if (this.course != null) {
            courseDTO = CourseDTO.builder()
                    .couNo(this.course.getCouNo())
                    .claName(this.course.getClaName())
                    .build();
        }

        UserDTO userDTO = UserDTO.builder()
                .id(this.id)
                .courseDTO(courseDTO)
                .userId(this.userId)

                .userId(this.userId).approval(this.approval).userScore(this.userScore)

                .userName(this.userName).userAddressDetail(this.userAddressDetail)
//                .userEmail(this.userEmail)
                .userTel(this.userTel).userBus(this.userBus).userSpecialNote(this.userSpecialNote).userConsultContent(this.userConsultContent)
                .userBirth(this.userBirth).userType(this.userType) .userSchool(this.userSchool).userAddress(this.userAddress).userJoinId(this.userJoinId)    .userRegdate(this.userRegdate)
                .role(this.role)
                .build();

        return userDTO;
    }
}
