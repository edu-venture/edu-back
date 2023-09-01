package com.bit.eduventure.lecture.service;

import com.bit.eduventure.lecture.entity.LecUser;
import com.bit.eduventure.lecture.repository.LecUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecUserService {
    private final LecUserRepository lecUserRepository;

    @Transactional
    public void enterLecUser(String liveStationId, String userName) {
        LecUser lecUser = LecUser.builder()
                .liveStationId(liveStationId)
                .userName(userName)
                .build();
        lecUserRepository.save(lecUser);
        lecUserRepository.flush();
    }

    @Transactional
    public void leaveLecUser(String liveStationId, String userName) {
        lecUserRepository.deleteAllByLiveStationIdAndUserName(liveStationId, userName);
        lecUserRepository.flush();
    }

    public List<LecUser> lecUserList(String liveStationId) {
        return lecUserRepository.findAllByLiveStationId(liveStationId);
    }

    public void deleteLecture(String liveStationId) {
        lecUserRepository.deleteAllByLiveStationId(liveStationId);
    }
}
