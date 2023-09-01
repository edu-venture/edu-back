package com.bit.eduventure.ES5_Notice.DTO;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES5_Notice.Entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDTO {

    private Integer noticeNo;
    private String claName;
    private String noticeTitle;
    private String date;
    private String noticeContent;
    private String noticeRegdate;

    private Integer id;


    public Notice DTOToEntity(){
        User user = User.builder().id(this.id).build();

        LocalDateTime noticeRegdate = this.noticeRegdate != null ?
                LocalDateTime.parse(this.noticeRegdate) : LocalDateTime.now(); // 기본값 설정

        Notice notice = Notice.builder().user(user).noticeNo(this.noticeNo).claName(this.claName).noticeTitle(this.noticeTitle).date(this.date).noticeContent(this.noticeContent).noticeRegdate(  noticeRegdate).build();
        return notice;


    }


}
