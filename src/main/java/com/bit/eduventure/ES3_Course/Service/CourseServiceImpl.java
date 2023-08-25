package com.bit.eduventure.ES3_Course.Service;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
import com.bit.eduventure.timetable.entity.TimeTable;
import com.bit.eduventure.timetable.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl  implements CourseService {

    private final CourseRepository courseRepository;

    private final TimeTableRepository timeTableRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             TimeTableRepository timeTableRepository){
        this.courseRepository = courseRepository;
        this.timeTableRepository = timeTableRepository;
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

    @Override
    public Course findByClaName(String claName) {
        return courseRepository.findByClaName(claName);
    }

    //선생님 이름으로 반 정보 찾기
    @Override
    public Course findByTeacherId(int id) {
        return courseRepository.findByUserId(id);
    }

    @Override
    public void createCourse(User user) {
        Course course = Course.builder()
                .user(user)
                .claName("강호현반")
                .build();
        courseRepository.save(course);
    }

    @Override
    public List<String> getTimeWeeksByCouNo(Integer couNo) {
        // First, find the Course by couNo
        Course course = courseRepository.findById(couNo).orElse(null);

        if (course == null) {
            // Handle the error, e.g., throw an exception or return an empty list
            return Collections.emptyList();
        }

        // Using the claName from the found Course, find all matching timetables
        List<TimeTable> timetables = timeTableRepository.findAllByClaName(course.getClaName());

        // Extract timeWeek from each timetable and collect to a list
        return timetables.stream()
                .map(TimeTable::getTimeWeek)
                .collect(Collectors.toList());
    }



}
