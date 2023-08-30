package com.bit.eduventure.ES3_Course.Repository;

import com.bit.eduventure.ES3_Course.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findByClaName(String claName);

    List<Course> findByUserId(int userId);

}