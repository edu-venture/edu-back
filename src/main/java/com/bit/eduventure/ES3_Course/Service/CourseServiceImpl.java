package com.bit.eduventure.ES3_Course.Service;


import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
import com.bit.eduventure.timetable.entity.TimeTable;
import com.bit.eduventure.timetable.repository.TimeTableRepository;
import com.bit.eduventure.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl  implements CourseService {

    private final CourseRepository courseRepository;

    private final TimeTableService timeTableService;


    @Override
    public List<Course> getCourseList() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourse(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("찾을 수 없는 반 번호입니다."));
    }

    @Override
    public Course findByClaName(String claName) {
        return courseRepository.findByClaName(claName);
    }

    //선생님 이름으로 반 정보 찾기
    @Override
    public Course findByTeacherId(int teacherId) {
        return courseRepository.findByUserId(teacherId);
    }

    @Override
    public void createCourse(CourseDTO courseDTO) {
        Course course = Course.builder()
                .user(courseDTO.getUserDTO().DTOToEntity())
                .claName(courseDTO.getClaName())
                .build();
        courseRepository.save(course);
    }

    @Override
    public List<String> getTimeWeeksByCouNo(int couNo) {
        // First, find the Course by couNo
        Course course = courseRepository.findById(couNo).orElse(null);

        if (course == null) {
            // Handle the error, e.g., throw an exception or return an empty list
            return Collections.emptyList();
        }

        // Using the claName from the found Course, find all matching timetables
        List<TimeTable> timetables = timeTableService.getTimeTableListForClaName(course.getClaName());

        // Extract timeWeek from each timetable and collect to a list
        return timetables.stream()
                .map(TimeTable::getTimeWeek)
                .collect(Collectors.toList());
    }



}
