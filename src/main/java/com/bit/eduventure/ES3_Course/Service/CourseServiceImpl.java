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

    //선생님PK로 반 정보 찾기
    @Override
    public List<Course> findByTeacherId(int teacherId) {
        return courseRepository.findByUserId(teacherId);
    }

    @Override
    @Transactional
    public void createCourse(CourseDTO courseDTO) {
        Course course = courseDTO.DTOToEntity();
        if (course.getCouMemo() == null) {
            course.setCouMemo("");
        }
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourseList(List<Integer> couNoList) {
        couNoList.stream()
                .forEach(couNo -> {
                    deleteCourseAndAdjustUsers(couNo);
                });
    }

    @Override
    @Transactional
    public void deleteCourseAndAdjustUsers(int couNo) {
        // t_course 레코드 삭제
        Course course = courseRepository.findById(couNo)
                .orElseThrow(() -> new RuntimeException("반 삭제 도중 반 번호가 디비에 없다."));
        if (course != null) {
            // t_user 레코드들의 외래 키 값을 변경
            List<User> userList = userService.getUserListForCouNo(couNo);
            userList.forEach(user -> {
                user.setCourse(null);
                userService.createUser(user);
            });
            //연동된 시간표 삭제
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

}
