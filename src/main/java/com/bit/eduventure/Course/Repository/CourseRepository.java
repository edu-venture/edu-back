package com.bit.eduventure.Course.Repository;

import com.bit.eduventure.Course.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByClaName(String claName);

    List<Course> findByUserId(int userId);

}