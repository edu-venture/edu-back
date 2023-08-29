package com.bit.eduventure.ES3_Course.Service;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
import com.bit.eduventure.timetable.entity.TimeTable;
import com.bit.eduventure.timetable.service.TimeTableService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl  implements CourseService {

    private final CourseRepository courseRepository;
    private final TimeTableService timeTableService;
    private final UserService userService;


    @Override
    public List<Course> getCourseList() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourse(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
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
        Course course = courseDTO.DTOToEntity();
        courseRepository.save(course);
    }

    @Override
    public void deleteCourseList(List<Integer> couNoList) {
        couNoList.stream()
                .forEach(couNo -> {
                    deleteCourseAndAdjustUsers(couNo);
                });
    }
    @Transactional
    public void deleteCourseAndAdjustUsers(int couNo) {
        // t_course 레코드 삭제
        Course course = courseRepository.findById(couNo)
                .orElseThrow(() -> new RuntimeException("반 삭제 도중 반 번호가 디비에 없다."));
        if (course != null) {
            // t_user 레코드들의 외래 키 값을 변경
            List<User> userList = userService.getUserListForCouNo(couNo);
            for (User user : userList) {
                user.setCourse(null);
                userService.update(user);
            }
            timeTableService.deleteAllCourse(couNo);
            // t_course 레코드 삭제
            courseRepository.delete(course);
        }
    }


    @Override
    public List<Integer> jsonToIntList(String couNoList) {
        try {
            JsonElement jsonElement = JsonParser.parseString(couNoList);
            String jsonArrayString = jsonElement.getAsJsonObject().get("couNoList").getAsString();
            JsonArray jsonArray = JsonParser.parseString(jsonArrayString).getAsJsonArray();

            List<Integer> result = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                int value = element.getAsInt();
                result.add(value);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("JSON 처리 오류");
        }
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
