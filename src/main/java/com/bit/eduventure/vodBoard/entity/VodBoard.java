package com.bit.eduventure.vodBoard.entity;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.vodBoard.dto.VodBoardDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_VOD_BOARD")
public class VodBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VOD_NO")
    private Integer id;

    @Column(name = "VOD_TITLE")
    private String title;

    @Column(name = "VOD_CONTENT")
    private String content;

    @Column(name = "VOD_WRITER")
    private String writer;

    @Column(name = "VOD_REG")
    private LocalDateTime regDate;

    @Column(name = "VOD_MOD")
    private LocalDateTime modDate;

    @Column(name = "VOD_HITS", nullable = false) //조회수
    private int hits = 0;

    @Column(name = "VOD_SAVE_PATH") //실제로 저장된 파일명과 원본 파일명으로 나눠서 저장
    private String savePath;

    @Column(name = "VOD_ORIGIN_PATH")
    private String originPath;

    @Column(name = "VOD_SAVE_THUMB")
    private String saveThumb;

    @Column(name = "VOD_ORIGIN_THUMB")
    private String originThumb;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;  // 유저 정보를 findBy 안쓰고 편하게 쓰기 위해서 작성함.

    public VodBoardDTO EntityToDTO() {
        VodBoardDTO vodBoardDTO = VodBoardDTO.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .writer(this.writer)
                .regDate(this.regDate)
                .modDate(this.modDate)
                .originThumb(this.originThumb)
                .saveThumb(this.saveThumb)
                .hits(this.hits)
                .originPath(this.originPath)
                .savePath(this.savePath)
                .userDTO(this.user.EntityToDTO())
                .build();
        return vodBoardDTO;
    }
}
