
package com.bit.eduventure.attendance.controller;

import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.ES1_User.Service.UserServiceImpl;
import com.bit.eduventure.attendance.entity.Attend;
import com.bit.eduventure.attendance.service.AttendanceService;
import com.bit.eduventure.attendance.dto.AttendDTO;
import com.bit.eduventure.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import retrofit2.http.Path;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {


    private final AttendanceService attendanceService;

    private final UserService userService;

    // 입실 처리
    @PostMapping("/enter")
    public ResponseEntity<?> registerEnterTime(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<AttendDTO> responseDTO = new ResponseDTO<>();
        int userId = Integer.parseInt(customUserDetails.getUsername());
        System.out.println(userId);


        try {
            LocalDateTime attendTime = LocalDateTime.now(); // 현재 시간으로 입실 시간 설정


            AttendDTO response = attendanceService.registerAttendance(userId, attendTime);

            responseDTO.setItem(response);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 퇴실 처리
    @PostMapping("/exit")
    public ResponseEntity<?> registerExitTime(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<AttendDTO> responseDTO = new ResponseDTO<>();
        int userId = Integer.parseInt(customUserDetails.getUsername());
        System.out.println(userId);

        try {
            LocalDateTime exitTime = LocalDateTime.now(); // 현재 시간으로 퇴실 시간 설정


            AttendDTO response = attendanceService.registerExitTime(userId, exitTime);

            responseDTO.setItem(response);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 특정 사용자의 출석 기록 조회
    @GetMapping("/attend")
    public ResponseEntity<?> getAttendanceRecordsByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<List<AttendDTO>> responseDTO = new ResponseDTO<>();
        int userId = Integer.parseInt(customUserDetails.getUsername());

        try {

            User user = userService.findById(userId);
            List<AttendDTO> records = attendanceService.getAttendanceRecordsByUser(user);

            responseDTO.setItem(records);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    // 특정 날짜의 특정 사용자 출석 기록 조회
    @GetMapping("/attend/date/{date}")
    public ResponseEntity<?> getAttendanceRecordsByUserAndDate(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                             @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ResponseDTO<List<AttendDTO>> responseDTO = new ResponseDTO<>();
        int userId = Integer.parseInt(customUserDetails.getUsername());

        try {

            User user = userService.findById(userId);
            List<AttendDTO> records = attendanceService.getAttendanceRecordsByUserAndDate(user, date);

            responseDTO.setItem(records);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 특정 달에 해당하는 특정 사용자 출석 기록 조회
    @GetMapping("/attend/month/{yearMonth}")
    public ResponseEntity<?> getAttendanceRecordsByUserAndMonth(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                              @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        ResponseDTO<List<AttendDTO>> responseDTO = new ResponseDTO<>();
        int userId = Integer.parseInt(customUserDetails.getUsername());

        try {

            User user = userService.findById(userId);
            List<AttendDTO> records = attendanceService.getAttendanceRecordsByUserAndMonth(user, yearMonth);

            responseDTO.setItem(records);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    // 출석 기록 수정
    @PutMapping("/admin/attend")
    public ResponseEntity<?> updateAttendRecord(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                @RequestBody AttendDTO attendDTO) {
        ResponseDTO<AttendDTO> responseDTO = new ResponseDTO<>();
        System.out.println(attendDTO);
        System.out.println("update 시작");

        try {
            Attend attend = attendDTO.DTOToEntity();
            Attend updatedAttend = attendanceService.updateAttendRecord(attend);
            AttendDTO updateAttendDTO = updatedAttend.EntityToDTO();

            responseDTO.setItem(updateAttendDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }


    @DeleteMapping("/admin/attend")
    public ResponseEntity<?> deleteAttendanceRecord(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                    @RequestBody String attList) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try{
            attendanceService.deleteAttendList(attList);


            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("msg", "Deleted successfully");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage("Error deleting the record");
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}

