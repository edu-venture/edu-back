//package com.bit.eduventure.attendance.service;
//
//import com.bit.eduventure.attendance.dto.AttendDTO;
//import com.bit.eduventure.attendance.entity.Attend;
//import com.bit.eduventure.timetable.repository.CourseRepository;
//import com.bit.eduventure.user.entity.User;
//import com.bit.eduventure.attendance.repository.AttendRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AttendanceOldService {
//
//    @Autowired
//    private AttendRepository attendRepository;
//
//    @Autowired
//    private CourseRepository courseRepository;
//
//
//    public AttendDTO registerEnterTime(User user, LocalDateTime enterTime) {
//        Attend record = new Attend();
//        record.setUser(user);
//        record.setAttStart(enterTime);
//
//        if(enterTime.isAfter(LocalDateTime.of(enterTime.toLocalDate(), LocalTime.of(9, 10)))) {
//            record.setAttContent("지각");
//        } else if(enterTime.isAfter(LocalDateTime.of(enterTime.toLocalDate(), LocalTime.of(14, 0)))) {
//            record.setAttContent("결석");
//        } else {
//            record.setAttContent("출석중"); // 퇴실 전 상태
//        }
//
//        AttendDTO attendDTO = attendRepository.save(record).EntityToDTO();
//        return attendDTO;
//    }
//
//    public AttendDTO registerExitTime(User user, LocalDateTime exitTime) {
//        List<Attend> attendRecords = attendRepository.findByUserAndAttFinishIsNullOrderByAttStartDesc(user);
//        if(attendRecords.isEmpty()) {
//            // 적절한 예외 처리 (예: "출석 기록이 없거나 모든 기록에 퇴실시간이 이미 기록되어 있습니다.")
//            throw new RuntimeException("출석 기록이 없습니다.");
//        }
//        Attend record = attendRecords.get(0);
//        record.setAttFinish(exitTime);
//
//        if(exitTime.isBefore(LocalDateTime.of(exitTime.toLocalDate(), LocalTime.of(18, 0)))) {
//            record.setAttContent("조퇴");
//        } else {
//            record.setAttContent("출석");
//        }
//
//        AttendDTO attendDTO = attendRepository.save(record).EntityToDTO();
//        return attendDTO;
//    }
//
//
//    public List<AttendDTO> getAttendanceRecordsByUser(User user) {
//        List<Attend> records = attendRepository.findByUser(user);
//        // Convert AttendanceRecord to AttendanceRecordDTO if needed
//        return records.stream().map(AttendDTO::new).collect(Collectors.toList());
//    }
//}
//
