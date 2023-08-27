package com.bit.eduventure.ES3_Course.Service;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {


    List<Course> getCourseList();

    Course getCourse(int id);

    List<String> getTimeWeeksByCouNo(int couNo);

    Course findByClaName(String claName);

    Course findByTeacherId(int teacherId);

    void createCourse(CourseDTO courseDTO);
}
