package com.bit.eduventure.vodBoard.entity;

import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.vodBoard.dto.VodBoardCommentDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "T_VOD_BOARD_CMT")
public class VodBoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VOD_CMT_NO")
    private Integer id; // 댓글의 고유한 인덱스(ID)

    @Column(name="VOD_CMT_CONTENT")
    private String vodCmtContent; // 댓글 내용

    @Column(name="VOD_CMT_REGDATE")
    private LocalDateTime vodCmtRegdate; // 댓글 작성일자와 시간

    @Column(name="VOD_CMT_PAR_NO")
    private int vodCmtParentNo; //대댓글을 위한 부모 댓글의 인덱스

    @Column(name="VOD_NO")
    private int vodNo; // 댓글이 속하는 VOD 게시글의 인덱스(ID)

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;  // 유저 정보를 findBy 안쓰고 편하게 쓰기 위해서 작성함.

    public VodBoardCommentDTO EntityTODTO() {
        return VodBoardCommentDTO.builder()
                .id(this.id)
                .vodCmtContent(this.vodCmtContent)
                .vodCmtRegdate(this.vodCmtRegdate)
                .vodCmtParentNo(this.vodCmtParentNo)
                .vodNo(this.vodNo)
                .userDTO(this.user.EntityToDTO())
                .build();
    }
}
