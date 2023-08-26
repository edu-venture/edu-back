package com.bit.eduventure.lecture.controller;


import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.service.LectureService;
import com.bit.eduventure.livestation.service.LiveStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/lecture")
@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;
    private final LiveStationService liveStationService;
    private final UserService userService;
//    private final VodBoardService vodBoardService;

    //강사가 강의 개설
    @PostMapping("/lecture")
    public ResponseEntity<?> createLecture(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody LectureDTO lectureDTO) {
        ResponseDTO<LectureDTO> responseDTO = new ResponseDTO<>();
        try {
            System.out.println(lectureDTO);
            int userNo = Integer.parseInt(customUserDetails.getUsername());
            User user = userService.findById(userNo);

            lectureDTO.setCourseDTO(user.EntityToDTO().getCourseDTO());
            String title = lectureDTO.getTitle();

            Course course = user.getCourse();

            lectureDTO.setCourseDTO(course.EntityToDTO());
            System.out.println("2 user.getCourse().EntityToDTO(): " + user.getCourse().EntityToDTO());
            String channelId = liveStationService.createChannel(title);
            System.out.println("3 channelId: " + channelId);
            lectureDTO.setLiveStationId(channelId);
            lectureDTO = lectureService.createLecture(lectureDTO);

            responseDTO.setItem(lectureDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

}
