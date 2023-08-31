package com.bit.eduventure.attendance.service;


import com.bit.eduventure.attendance.entity.Attend;
import com.bit.eduventure.attendance.repository.AttendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final AttendRepository attendRepository;

    //자정마다 퇴실이 없으면 결석(2)으로 변경 코드
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAttContentForNullAttFinish() {
        List<Attend> recordsWithNullAttFinish = attendRepository.findByAttFinishIsNull();

        for (Attend record : recordsWithNullAttFinish) {
            record.setAttContent("2");
            attendRepository.save(record);
        }
    }
}
