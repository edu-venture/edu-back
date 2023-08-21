package com.bit.eduventure.ES5_Notice.Entity;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES5_Notice.DTO.NoticeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="T_NOTICE")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeNo;


    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;
    private String claName;
    private String noticeTitle;
    private String date;
    @Column(length = 1500)
    private String noticeContent;
    @Builder.Default
    private LocalDateTime noticeRegdate = LocalDateTime.now();

    public NoticeDTO EntityToDTO(){
        NoticeDTO noticeDTO = NoticeDTO.builder().id(this.user.getId()).noticeNo(this.noticeNo).claName(this.claName).noticeTitle(this.noticeTitle).date(this.date).noticeContent(this.noticeContent).noticeRegdate(this.noticeRegdate.toString()).build();
        return noticeDTO;

    }

}
