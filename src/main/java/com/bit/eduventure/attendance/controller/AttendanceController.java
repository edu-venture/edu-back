//package com.bit.eduventure.attendance.controller;
//
//import com.bit.eduventure.ES1_User.Entity.User;
//import com.bit.eduventure.attendance.entity.Attend;
//import com.bit.eduventure.attendance.service.AttendanceService;
//import com.bit.eduventure.attendance.dto.AttendDTO;
//import com.bit.eduventure.dto.ResponseDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@Controller
//@RequestMapping("/attendance")
//public class AttendanceController {
//
//    @Autowired
//    private AttendanceService attendanceService;
//
//    //학생의 수업일 확인
//    @GetMapping("/check")
//    public ResponseEntity<?> checkClassForToday(User user) {
//        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
//        try {
//            boolean hasClass = attendanceService.checkIfClassExistsForToday(user);
//
//            Map<String, String> returnMap = new HashMap<>();
//
//            System.out.println(hasClass);
//            System.out.println(user.getUserName());
//            if (hasClass) {
//                returnMap.put("name", user.getUserName());
//                returnMap.put("bool", "T");
//            } else {
//                returnMap.put("name", user.getUserName());
//                returnMap.put("bool", "F");
//            }
//
//            responseDTO.setItem(returnMap);
//            System.out.println(returnMap);
//            return ResponseEntity.ok().body(responseDTO);
//
//        } catch (Exception e) {
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDTO.setErrorMessage(e.getMessage());
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }
//
//    @GetMapping("/main")
//    public ModelAndView attendMainPage(Attend attend) {
//
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("user", attend.getUser());
//        mv.addObject("attStart", attend.getAttStart());
//        mv.addObject("attFinish", attend.getAttFinish());
//        mv.addObject("attContent", attend.getAttContent());
//
//        mv.setViewName("attendance/main.html");
//
//        return mv; // Thymeleaf는 자동으로 resources/templates 디렉토리를 참조합니다.
//    }
//
//    @GetMapping("/studentName")
//    public ResponseEntity<?> getStudentName(@RequestBody User user) {
//        ResponseDTO<AttendDTO> responseDTO = new ResponseDTO<>();
//        // 로직 추가하여 학생 이름 가져오기
//        String studentName = user.getUserName();
//
//        // 데이터를 DTO로 감싸서 반환
//        String response = studentName;
//        return ResponseEntity.ok(response);
//    }
//
//    // 입실 처리
//    @PostMapping("/enter")
//    public ResponseEntity<?> registerEnterTime(@RequestBody User user) {
//        ResponseDTO<AttendDTO> responseDTO = new ResponseDTO<>();
//        System.out.println(user);
//
//        try {
//            LocalDateTime attendTime = LocalDateTime.now(); // 현재 시간으로 입실 시간 설정
//
//            AttendDTO response = attendanceService.registerAttendance(user, attendTime);
//
//
//            responseDTO.setItem(response);
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDTO.setErrorMessage(e.getMessage());
//
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }
//
//    // 퇴실 처리
//    @PostMapping("/exit")
//    public ResponseEntity<?> registerExitTime(@RequestBody User user, Attend attend) {
//        ResponseDTO<AttendDTO> responseDTO = new ResponseDTO<>();
//
//        try {
//            LocalDateTime exitTime = LocalDateTime.now(); // 현재 시간으로 입실 시간 설정
//
//            AttendDTO response = attendanceService.registerExitTime(user, exitTime);
//
//            responseDTO.setItem(response);
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            responseDTO.setErrorMessage(e.getMessage());
//
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }
//
//    // 특정 사용자의 출석 기록 조회
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<AttendDTO>> getAttendanceRecordsByUser(@PathVariable Integer userId) {
//        User user = new User();
//        user.setId(userId);
//        List<AttendDTO> records = attendanceService.getAttendanceRecordsByUser(user);
//        return ResponseEntity.ok(records);
//    }
//
//    // 특정 사용자의 특정 날짜의 출석 기록 수정 (PUT 매핑 예시)
//    @PutMapping("/{userId}/{date}")
//    public ResponseEntity<String> updateAttendanceRecord(@PathVariable Integer userId, @PathVariable String date, @RequestBody AttendDTO updatedRecord) {
//        // Implement this with service. This is just a placeholder.
//        // You may need to parse the date and find the corresponding record, then update with the provided DTO.
//
//        return ResponseEntity.ok("Updated successfully");
//    }
//
//    // 특정 사용자의 특정 날짜의 출석 기록 삭제 (DELETE 매핑 예시)
//    @DeleteMapping("/{userId}/{date}")
//    public ResponseEntity<String> deleteAttendanceRecord(@PathVariable Integer userId, @PathVariable String date) {
//        // Implement this with service. This is just a placeholder.
//        // You may need to parse the date and find the corresponding record, then delete it.
//
//        return ResponseEntity.ok("Deleted successfully");
//    }
//}
//
