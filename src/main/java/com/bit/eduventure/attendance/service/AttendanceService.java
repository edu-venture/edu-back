
package com.bit.eduventure.attendance.service;

import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Repository.UserRepository;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
import com.bit.eduventure.ES3_Course.Service.CourseService;
import com.bit.eduventure.attendance.dto.AttendDTO;
import com.bit.eduventure.attendance.entity.Attend;
import com.bit.eduventure.attendance.repository.AttendRepository;

import com.bit.eduventure.timetable.entity.TimeTable;
import com.bit.eduventure.timetable.repository.TimeTableRepository;
import com.bit.eduventure.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendRepository attendRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final TimeTableRepository timeTableRepository;


    public class DayOfWeekMapping {

        private static final Map<DayOfWeek, String> DAY_OF_WEEK_KOREAN_MAP = new HashMap<>();

        static {
            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.MONDAY, "월요일");
            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.TUESDAY, "화요일");
            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.WEDNESDAY, "수요일");
            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.THURSDAY, "목요일");
            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.FRIDAY, "금요일");
            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.SATURDAY, "토요일");
            DAY_OF_WEEK_KOREAN_MAP.put(DayOfWeek.SUNDAY, "일요일");
        }

        public static String toKorean(DayOfWeek dayOfWeek) {
            return DAY_OF_WEEK_KOREAN_MAP.get(dayOfWeek);
        }
    }
    // 교시별 시작 시간 매핑
    private static final Map<String, LocalTime> COURSE_START_TIMES = Map.of(
            "1교시", LocalTime.of(14, 0),
            "2교시", LocalTime.of(15, 0),
            "3교시", LocalTime.of(16, 0),
            "4교시", LocalTime.of(17, 0),
            "5교시", LocalTime.of(18, 0),
            "6교시", LocalTime.of(20, 0),
            "7교시", LocalTime.of(21, 0),
            "8교시", LocalTime.of(22, 0)
    );

    public AttendDTO registerAttendance(int userId, LocalDateTime attendTime) {
        Attend record = new Attend();
        record.setUserNo(userId);
        record.setAttStart(attendTime);

        User user = userService.findById(userId);


        if (user.getCourse() == null) {
            throw new IllegalArgumentException("User is not registered for any course.");
        }

        List<TimeTable> timeTableList = timeTableRepository.findAllByCouNo(user.getCourse().getCouNo());

        List<String> returnTimeWeekList = new ArrayList<>();


        String currentDayOfWeek = DayOfWeekMapping.toKorean(attendTime.getDayOfWeek());
        System.out.println("currentDayOfWeek: " + currentDayOfWeek);

        for(TimeTable test : timeTableList) {
            returnTimeWeekList.add(test.getTimeWeek());
            System.out.println("course: " + test.getTimeWeek());
        }

        if (returnTimeWeekList.contains(currentDayOfWeek)) {
            String timeClass = timeTableList.get(0).getTimeClass(); //첫번째 시간을 뽑아내겠다.
            LocalTime courseStart = COURSE_START_TIMES.get(timeClass);
            LocalTime courseEnd = courseStart.plusMinutes(50); // 강의는 50분 간격이라고 했으므로
            System.out.println(courseStart);

            // 입실 시간 제한 조건
            if(attendTime.isBefore(LocalDateTime.of(attendTime.toLocalDate(), courseStart))
                    || attendTime.isAfter(LocalDateTime.of(attendTime.toLocalDate(), courseEnd))) {
                throw new IllegalArgumentException("입실은 수업 시간 내에서만 가능합니다.");
            }

            if (courseStart == null) {
                throw new IllegalArgumentException("Invalid course time provided.");
            }

            // 출석 시간 비교
            if (attendTime.isBefore(LocalDateTime.of(attendTime.toLocalDate(), courseStart))) {
                record.setAttContent("출석중");
            } else if (attendTime.isBefore(LocalDateTime.of(attendTime.toLocalDate(), courseStart).plusMinutes(10))) {
                record.setAttContent("1");
            } else {
                record.setAttContent("2");
            }
        }

        AttendDTO attendDTO = attendRepository.save(record).EntityToDTO();

        return attendDTO;
    }

    public AttendDTO registerExitTime(int userId, LocalDateTime exitTime) {
        User user = userService.findById(userId);
        // 1. User와 연결된 Course를 찾는다.
        Course course = user.getCourse();

        if (course == null) {
            throw new IllegalArgumentException("User is not registered for any course.");
        }

        List<TimeTable> timeTableList = timeTableRepository.findAllByCouNo(user.getCourse().getCouNo());

        String currentDayOfWeek = DayOfWeekMapping.toKorean(exitTime.getDayOfWeek());
        LocalDate currentDate = exitTime.toLocalDate();


        List<String> returnTimeWeekList = new ArrayList<>();

        System.out.println("currentDayOfWeek: " + currentDayOfWeek);

        for(TimeTable test : timeTableList) {
            returnTimeWeekList.add(test.getTimeWeek());
        }

        LocalTime courseEndTime = null;
        System.out.println("timeTableList.toString(): " + timeTableList.toString());
        if (returnTimeWeekList.contains(currentDayOfWeek)) {
            String timeClass = timeTableList.get(timeTableList.size() - 1).getTimeClass(); //첫번째 시간을 뽑아내겠다.
            LocalTime courseStart = COURSE_START_TIMES.get(timeClass);
            courseEndTime = courseStart.plusMinutes(50); // 강의는 50분 간격이라고 했으므로
            System.out.println(courseEndTime);
        }
//        System.out.println("timeClass: " + timeClass);
        System.out.println(currentDayOfWeek);
        System.out.println(courseEndTime);

        // 퇴실 시간 제한 조건
//        if(exitTime.isBefore(LocalDateTime.of(exitTime.toLocalDate(), courseEndTime.minusMinutes(30)))
//                || exitTime.isAfter(LocalDateTime.of(exitTime.toLocalDate(), courseEndTime))) {
//            throw new IllegalArgumentException("퇴실은 수업 종료 30분 전부터 종료 시간까지만 가능합니다.");
//        }


        if (courseEndTime == null) {
            throw new IllegalArgumentException("Invalid course time or day for the user.");
        }

        LocalDateTime startOfDay = exitTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        List<Attend> records = attendRepository.findByUserNoAndAttStartBetween(user.getId(), startOfDay, endOfDay);


        if (records.isEmpty()) {
            throw new IllegalArgumentException("금일 입실 기록이 없습니다.");
        }

        //첫번째 기록 가져오기
        Attend record = records.get(0);

        if (record == null) {
            throw new IllegalArgumentException("이 날짜에 해당하는 입실 기록이 없습니다.");
        }

        record.setAttFinish(exitTime);
        record.setAttDate(currentDate);

        // 출석 상태 결정
        if (exitTime.isBefore(LocalDateTime.of(exitTime.toLocalDate(), courseEndTime))) {
            record.setAttContent("0");
        } else {
            record.setAttContent("2");
        }

        AttendDTO attendDTO = attendRepository.save(record).EntityToDTO();
        return attendDTO;
    }



    //특정 사용자의 출석 기록 조회
    public List<AttendDTO> getAttendanceRecordsByUser(User user) {
        List<Attend> records = attendRepository.findAllByUserNo(user.getId());
        return records.stream().map(AttendDTO::new).collect(Collectors.toList());
    }

    //특정 사용자 및 특정 날짜의 출석 기록 조회
    public List<AttendDTO> getAttendanceRecordsByUserAndDate(User user, LocalDate date) {
        List<Attend> attendances = attendRepository.findByUserNoAndAttDate(user.getId(), date);
        return attendances.stream()
                .map(Attend::EntityToDTO)
                .collect(Collectors.toList());
    }

    //특정 사용자 및 특정 달의 출석 기록 조회
    public List<AttendDTO> getAttendanceRecordsByUserAndMonth(User user, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Attend> attendances = attendRepository.findByUserNoAndAttDateBetween(user.getId(), startDate, endDate);
        return attendances.stream()
                .map(Attend::EntityToDTO)
                .collect(Collectors.toList());
    }



    public boolean checkIfClassExistsForToday(User user) {
        String currentDayOfWeek = DayOfWeekMapping.toKorean(LocalDateTime.now().getDayOfWeek());

        // 1. User와 연결된 Course를 찾는다.
        Course course = user.getCourse();

        // 사용자에게 할당된 수업 목록을 가져옵니다.
        List<String> timeTableList = courseService.getTimeWeeksByCouNo(course.getCouNo());

        System.out.println("currentDayOfWeek: " + currentDayOfWeek);

        // 사용자의 수업 중 현재 요일과 일치하는 수업이 있는지 확인
        if (timeTableList.contains(currentDayOfWeek)) {
            return true; // 일치하는 수업이 있으면 true 반환
        }

        return false; // 일치하는 수업이 없으면 false 반환
    }
}
