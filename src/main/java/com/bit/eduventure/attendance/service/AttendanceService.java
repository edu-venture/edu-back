//package com.bit.eduventure.attendance.service;
//
//import com.bit.eduventure.ES1_User.Entity.User;
//import com.bit.eduventure.ES3_Course.Entity.Course;
//import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
//import com.bit.eduventure.attendance.dto.AttendDTO;
//import com.bit.eduventure.attendance.entity.Attend;
//import com.bit.eduventure.attendance.repository.AttendRepository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.DayOfWeek;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AttendanceService {
//
//    private final AttendRepository attendRepository;
//
//    private final CourseRepository courseRepository;
//
//
//    public class DayOfWeekMapping {
//
//        private static final Map<DayOfWeek, String> DAY_OF_WEEK_KOREAN_MAP = new HashMap<>();
//
//        static {
//            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.MONDAY, "월요일");
//            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.TUESDAY, "화요일");
//            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.WEDNESDAY, "수요일");
//            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.THURSDAY, "목요일");
//            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.FRIDAY, "금요일");
//            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.SATURDAY, "토요일");
//            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.SUNDAY, "일요일");
//        }
//
//        public static String toKorean(DayOfWeek dayOfWeek) {
//            return DAY_OF_WEEK_KOREAN_MAP.get(dayOfWeek);
//        }
//    }
//    // 교시별 시작 시간 매핑
//    private static final Map<String, LocalTime> COURSE_START_TIMES = Map.of(
//            "1교시", LocalTime.of(10, 0),
//            "2교시", LocalTime.of(11, 0),
//            "3교시", LocalTime.of(13, 0),
//            "4교시", LocalTime.of(14, 0),
//            "5교시", LocalTime.of(15, 0),
//            "6교시", LocalTime.of(16, 0),
//            "7교시", LocalTime.of(17, 0),
//            "8교시", LocalTime.of(18, 0)
//    );
//
//    public AttendDTO registerAttendance(User user, LocalDateTime attendTime) {
//        Attend record = new Attend();
//        record.setUser(user);
//        record.setAttStart(attendTime);
//
//        List<Course> userCourses = courseRepository.findByCouTeacher(user);
//        if (userCourses == null || userCourses.isEmpty()) {
//            throw new IllegalArgumentException("User is not registered for any course.");
//        }
//
//        String currentDayOfWeek = DayOfWeekMapping.toKorean(attendTime.getDayOfWeek());
//
////        String currentDayOfWeek = attendTime.getDayOfWeek().name();
//        for (Course course : userCourses) {
//            if (course.getCouWeek().equals(currentDayOfWeek)) {
//                String couTime = course.getCouTime();
//                LocalTime courseStart = COURSE_START_TIMES.get(couTime);
//                System.out.println(courseStart);
//
//                if (courseStart == null) {
//                    throw new IllegalArgumentException("Invalid course time provided.");
//                }
//
//                // 출석 시간 비교
//                if (attendTime.isBefore(LocalDateTime.of(attendTime.toLocalDate(), courseStart))) {
//                    record.setAttContent("출석중");
//                } else if (attendTime.isBefore(LocalDateTime.of(attendTime.toLocalDate(), courseStart).plusMinutes(10))) {
//                    record.setAttContent("지각");
//                } else {
//                    record.setAttContent("결석");
//                }
//            }
//        }
//
//        AttendDTO attendDTO = attendRepository.save(record).EntityToDTO();
//        return attendDTO;
//    }
//
//    public AttendDTO registerExitTime(User user, LocalDateTime exitTime) {
//
//        // 1. User와 연결된 Course를 찾는다.
//        List<Course> userCourses = courseRepository.findByCouTeacher(user);
//
//        if (userCourses == null || userCourses.isEmpty()) {
//            throw new IllegalArgumentException("User is not registered for any course.");
//        }
//
//        String currentDayOfWeek = DayOfWeekMapping.toKorean(exitTime.getDayOfWeek());
//
//        LocalTime courseEndTime = null;
//
//        for (Course course : userCourses) {
//            if (course.getCouWeek().equals(currentDayOfWeek)) {
//                String couTime = course.getCouTime();
//                LocalTime courseStart = COURSE_START_TIMES.get(couTime);
//                courseEndTime = courseStart.plusMinutes(50); // 강의는 50분 간격이라고 했으므로
//            }
//        }
//
//        System.out.println(currentDayOfWeek);
//        System.out.println(courseEndTime);
//
//        if (courseEndTime == null) {
//            throw new IllegalArgumentException("Invalid course time or day for the user.");
//        }
//
//        LocalDateTime startOfDay = exitTime.toLocalDate().atStartOfDay();
//        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
//
//        List<Attend> records = attendRepository.findByUserAndAttStartBetween(user, startOfDay, endOfDay);
//
//
//        if (records.isEmpty()) {
//            throw new IllegalArgumentException("금일 입실 기록이 없습니다.");
//        }
//
//        //첫번째 기록 가져오기
//        Attend record = records.get(0);
//
//        if (record == null) {
//            throw new IllegalArgumentException("이 날짜에 해당하는 입실 기록이 없습니다.");
//        }
//
//        record.setAttFinish(exitTime);
//
//        // 출석 상태 결정
//        if (exitTime.isBefore(LocalDateTime.of(exitTime.toLocalDate(), courseEndTime))) {
//            record.setAttContent("출석");
//        } else {
//            record.setAttContent("결석");
//        }
//
//        AttendDTO attendDTO = attendRepository.save(record).EntityToDTO();
//        return attendDTO;
//    }
//
//
//    public List<AttendDTO> getAttendanceRecordsByUser(User user) {
//        List<Attend> records = attendRepository.findByUser(user);
//        return records.stream().map(AttendDTO::new).collect(Collectors.toList());
//    }
//
//    public boolean checkIfClassExistsForToday(User user) {
//        String currentDayOfWeek = DayOfWeekMapping.toKorean(LocalDateTime.now().getDayOfWeek());
//
//        // 사용자에게 할당된 수업 목록을 가져옵니다.
//        List<Course> userCourses = courseRepository.findByCouTeacher(user);
//
//
//        if (userCourses == null || userCourses.isEmpty()) {
//            return false; // 사용자가 등록된 수업이 없는 경우 false 반환
//        }
//
//        // 사용자의 수업 중 현재 요일과 일치하는 수업이 있는지 확인
//        for (Course course : userCourses) {
//            if (course.getCouWeek().equals(currentDayOfWeek)) {
//                return true; // 일치하는 수업이 있으면 true 반환
//            }
//        }
//
//        return false; // 일치하는 수업이 없으면 false 반환
//    }
//}
