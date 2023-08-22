package com.bit.eduventure.timetable.service;

import com.bit.eduventure.timetable.dto.TimeTableGetResponseDTO;
import com.bit.eduventure.timetable.dto.TimeTableRegistResquestDTO;
import com.bit.eduventure.timetable.dto.TimeTableUpdateRequestDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    /* 시간표 등록 */
    public TimeTableRegistResquestDTO  registerTimetable(TimeTableRegistResquestDTO requestDTO) {

        // 선생님 이름으로 사용자 엔터티 조회
        User teacher = userRepository.findByUserName(requestDTO.getTeacherName())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        // Course 엔터티 변환
        Course course = requestDTO.toEntity();
        course.setCouTeacher(teacher);

        // 데이터베이스에 저장
        courseRepository.save(course);

        return requestDTO; // 저장 성공한 시간표 정보 반환
    }

    /* 시간표 조회 - 특정 couNo 별 */
    public TimeTableGetResponseDTO getTimetable(int couNo) {
        Course course = courseRepository.findByCouNo(couNo);

        return new TimeTableGetResponseDTO(course);
    }

    /* 시간표 전체 조회 */
    public List<TimeTableGetResponseDTO> getAllTimetables() {
        List<Course> courses = courseRepository.findAll(); // 모든 시간표 가져오기

        // Course 엔터티를 TimeTableGetResponseDTO로 변환
        return courses.stream()
                .map(TimeTableGetResponseDTO::new) // 생성자를 이용한 변환
                .collect(Collectors.toList());
    }

    /* 시간표 수정 */
    public TimeTableGetResponseDTO updateTimeTable(TimeTableUpdateRequestDTO requestDTO) {
        Course course = courseRepository.findById(requestDTO.getCouNo())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        User teacher = userRepository.findByUserName(requestDTO.getTeacherName())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        // 수정하고자 하는 필드 업데이트
        course.setClaName(requestDTO.getClaName());
        course.setCouWeek(requestDTO.getCouWeek());
        course.setCouTime(requestDTO.getCouTime());
        course.setCouTeacher(teacher);
        course.setCouColor(requestDTO.getCouColor());
        // 기타 필요한 수정 로직

        courseRepository.save(course);

        return new TimeTableGetResponseDTO(course);
    }

    /* 시간표 삭제 */
    public void deleteTimetable(String claName, String couWeek) {
        List<Course> courses = courseRepository.findByClaNameAndCouWeek(claName, couWeek);
        if (!courses.isEmpty()) {
            for (Course course : courses) {
                courseRepository.delete(course);
            }
        } else {
            throw new IllegalArgumentException("Course not found");
        }
    }

}