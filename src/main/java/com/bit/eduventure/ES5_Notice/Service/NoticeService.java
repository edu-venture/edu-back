package com.bit.eduventure.ES5_Notice.Service;


import com.bit.eduventure.ES5_Notice.Entity.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeService {

    Notice create(Notice notice);

    List<Notice> getNoticeList();

    void deleteNotice(int id);

    Notice getNotice(Integer noticeNo);

    Notice update(Notice notice);

    List<Notice> getCourseNoticeList(String claName);
}
