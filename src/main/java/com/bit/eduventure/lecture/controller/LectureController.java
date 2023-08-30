package com.bit.eduventure.lecture.controller;


import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Service.CourseService;
import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.entity.Lecture;
import com.bit.eduventure.lecture.service.LectureService;
import com.bit.eduventure.livestation.service.LiveStationService;
import com.bit.eduventure.vodBoard.dto.VodBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequestMapping("/lecture")
@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;
    private final LiveStationService liveStationService;
    private final UserService userService;
    private final CourseService courseService;
//    private final VodBoardService vodBoardService;

    //강사가 강의 개설
    @PostMapping("/lecture")
    public ResponseEntity<?> createLecture(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody LectureDTO lectureDTO) {
        ResponseDTO<LectureDTO> responseDTO = new ResponseDTO<>();

        try {

            int userNo = customUserDetails.getUser().getId();
            User user = userService.findById(userNo);

            if (!user.getUserType().equals("teacher")) {
                throw new RuntimeException("선생님이 아닙니다.");
            }

            String title = lectureDTO.getTitle();

            String channelId = liveStationService.createChannel(title);

            lectureDTO.setLiveStationId(channelId);

            lectureDTO = lectureService.createLecture(lectureDTO).EntityTODTO();

            responseDTO.setItem(lectureDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {

            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }


    }

    @GetMapping("/lecture-list")
    public ResponseEntity<?> getAllLectures() {
        ResponseDTO<LectureDTO> response = new ResponseDTO<>();

        try {

            List<LectureDTO> res = lectureService.getAllLecture();

            response.setItems(res);
            response.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }

    }

    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<?> getLecture(@PathVariable int lectureId) {
        ResponseDTO<LectureDTO> response = new ResponseDTO<>();
        LectureDTO lectureDTO = lectureService.getLecture(lectureId).EntityTODTO();

        response.setItem(lectureDTO);
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/lecture/{lectureId}")
    public ResponseEntity<?> removeLecture(@PathVariable int lectureId) {
        ResponseDTO<String> response = new ResponseDTO<>();

        lectureService.deleteLecture(lectureId);

        response.setItem("삭제되었습니다.");
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/student/lecture")
    public ResponseEntity<?> getLecture(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        ResponseDTO<LectureDTO> response = new ResponseDTO<>();
        int userId = customUserDetails.getUser().getId();
        User user = userService.findById(userId);
        if (user.getCourse() != null) {
            int couNo = user.getCourse().getCouNo();
            LectureDTO lectureDTO = lectureService.getCouLecture(couNo).EntityTODTO();
            response.setItem(lectureDTO);
            response.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(response);
        } else {
            throw new NoSuchElementException();
        }
    }

//    @GetMapping("/lecture/{channelId}")
//    public ResponseEntity<?> getAllLectures() {
//        ResponseDTO<LectureDTO> response = new ResponseDTO<>();
//
//        try {
//            List<LectureDTO> res = lectureService.getAllLecture();
//            response.setItems(res);
//            response.setStatusCode(HttpStatus.OK.value());
//
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            response.setErrorMessage(e.getMessage());
//
//            return ResponseEntity.badRequest().body(response);
//        }
//
//    }


//    @DeleteMapping("/delete/{channelID}")
//    public ResponseEntity<?> deleteLecture(@PathVariable String channelID) {
//        ResponseDTO<LectureDTO> responseDTO = new ResponseDTO<>();
//        try {
//            //게시글 자동 등록
//            RecordVodDTO recordVodDTO = liveStationService.getRecord(channelID);
//            System.out.println("recordVodDTO: " + recordVodDTO);
//            /* 보드서비스에 게시글등록하게 */
//            VodBoardDTO vodBoardDTO = VodBoardDTO.builder()
//                    .title(recordVodDTO.getFileName())
//                    .content("자동저장된 내용입니다.")
//                    .path(recordVodDTO.getFileName())
//                    .build();
//
//            vodBoardService.insertBoard(vodBoardDTO.DTOTOEntity(), null);
//
//            liveStationService.deleteChannel(channelID);
//
//            responseDTO.setStatusCode(HttpStatus.OK.value());
//
//            return ResponseEntity.ok().body(responseDTO);
//        } catch (Exception e) {
//            responseDTO.setErrorMessage(e.getMessage());
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//
//    }


}
