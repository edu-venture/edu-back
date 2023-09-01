package com.bit.eduventure.ES5_Notice.Service;


import com.bit.eduventure.ES5_Notice.Entity.Notice;
import com.bit.eduventure.ES5_Notice.Repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NoticeServiceImpl implements NoticeService {

    private NoticeRepository noticeRepository;

    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository){
        this.noticeRepository = noticeRepository;


    }

    @Override
    public Notice create(Notice notice) {

        System.out.println("서비스까지 왔음");
        return noticeRepository.save(notice);
    }

    @Override
    public List<Notice> getNoticeList() {
        return noticeRepository.findAll();
    }

    @Override
    public void deleteNotice(int id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public Notice getNotice (Integer noticeNo) {
        return noticeRepository.findById(noticeNo)
                .orElseThrow(() -> new NoSuchElementException());
    }

    @Override
    @Transactional
    public Notice update(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public List<Notice> getCourseNoticeList(String claName) {
        return noticeRepository.findAllByClaAndAdmin(claName);
    }


}
