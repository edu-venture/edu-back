package com.bit.eduventure.timetable.controller;

import com.bit.eduventure.payment.dto.ResponseDTO;
import com.bit.eduventure.timetable.dto.TimeTableGetResponseDTO;
import com.bit.eduventure.timetable.dto.TimeTableRegistResquestDTO;
import com.bit.eduventure.timetable.dto.TimeTableUpdateRequestDTO;
import com.bit.eduventure.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/timetable")
@RequiredArgsConstructor
public class TimeTableController {

    private final TimeTableService courseService;

    /* 시간표 등록 */
    @PostMapping("/regist")
    //클라이언트로부터 받은 HTTP POST 요청의 body 부분을 PaymentCreateRequestDTO 타입의 객체로 변환하고, 이를 requestDTO라는 매개변수로 전달
    public ResponseEntity<?> registTimeTable(@RequestBody TimeTableRegistResquestDTO requestDTO) {

        // 클라이언트에게 전달할 최종 응답 객체 생성
        ResponseDTO<TimeTableRegistResquestDTO> response = new ResponseDTO<>();

        try {
            TimeTableRegistResquestDTO res = courseService.registerTimetable(requestDTO);  // 서비스 메서드 호출
            response.setItem(res); // 응답 DTO 설정
            response.setStatusCode(HttpStatus.CREATED.value()); // 상태 코드 설정
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 성공적인 응답 반환
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage()); // 에러 메시지 설정
            response.setStatusCode(HttpStatus.BAD_REQUEST.value()); // 상태 코드 설정
            return ResponseEntity.badRequest().body(response); // 에러 발생시 응답 반환
        }
    }

    /* 시간표 조회 */
    @GetMapping("/{couNo}/getTimeTable")
    public ResponseEntity<?> getTimeTable(@PathVariable int couNo) {

        System.out.println(couNo);

        ResponseDTO<TimeTableGetResponseDTO> response = new ResponseDTO<>();

        try {
            TimeTableGetResponseDTO res = courseService.getTimetable(couNo);
            response.setItem(res);
            response.setStatusCode(HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /* 시간표 목록 조회 */
    @GetMapping("/getTimeTable-list")
    public ResponseEntity<?> getAllTimetables() {

        ResponseDTO<List<TimeTableGetResponseDTO>> response = new ResponseDTO<>();

        try {
            List<TimeTableGetResponseDTO> res = courseService.getAllTimetables();
            response.setItem(res);
            response.setStatusCode(HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /* 시간표 수정 */
    @PutMapping("/{couNo}/updateTimeTable")
    public ResponseEntity<?> updateTimeTable(@PathVariable int couNo, @RequestBody TimeTableUpdateRequestDTO requestDTO) {

        System.out.println("couNo: " + couNo);

        // 클라이언트에게 전달할 최종 응답 객체 생성
        ResponseDTO<TimeTableGetResponseDTO> response = new ResponseDTO<>();

        if (couNo != requestDTO.getCouNo()) {
            response.setErrorMessage("ID mismatch");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            TimeTableGetResponseDTO res = courseService.updateTimeTable(requestDTO);
            response.setItem(res);
            response.setStatusCode(HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /* 시간표 삭제 */
    @DeleteMapping("/deleteTimeTable")
    public ResponseEntity<?> deleteTimeTable(@RequestBody Map<String, String> request) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<Map<String, String>>();

        String claName = request.get("claName");
        String couWeek = request.get("couWeek");

        try {
            courseService.deleteTimetable(claName, couWeek);
            Map<String, String> returnMap = new HashMap<String, String>();

            returnMap.put("msg", "정상적으로 삭제되었습니다.");

            responseDTO.setItem(returnMap);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


}