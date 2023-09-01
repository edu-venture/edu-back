package com.bit.eduventure.lecture.repository;

import com.bit.eduventure.lecture.entity.LecUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecUserRepository extends JpaRepository<LecUser, Integer> {
    List<LecUser> findAllByLiveStationId(String liveStationId);
    void deleteAllByLiveStationIdAndUserName(String liveStationId, String userName);
    void deleteAllByLiveStationId(String liveStationId);
}
