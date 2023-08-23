package com.bit.eduventure.ES3_Course.Service;


import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl  implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }


    @Override
    public List<Course> getCourseList() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> findById(int noticeNo) {
        return courseRepository.findById(noticeNo);
    }

    @Override
    public Optional<Course> findByCouNo(Integer couNo) {
        return courseRepository.findByCouNo(couNo);
    }
}
