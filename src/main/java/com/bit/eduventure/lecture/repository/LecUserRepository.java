package com.bit.eduventure.lecture.repository;

import com.bit.eduventure.lecture.entity.LecUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecUserRepository extends JpaRepository<LecUser, Integer> {
}
