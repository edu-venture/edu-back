package com.bit.eduventure.Notice.Service;


import com.bit.eduventure.Notice.Entity.Notice;

import java.util.List;

public interface NoticeService {

    Notice create(Notice notice);

    List<Notice> getNoticeList();

    void deleteNotice(int id);

    Notice getNotice(Integer noticeNo);

    Notice update(Notice notice);

    List<Notice> getCourseNoticeList(String claName);
}
