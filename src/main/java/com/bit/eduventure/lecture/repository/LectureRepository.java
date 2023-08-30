package com.bit.eduventure.lecture.repository;

import com.bit.eduventure.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    List<Lecture> findAllByCouNo(Integer couNo);


    Optional<Lecture> findById(Integer lecId);


}
