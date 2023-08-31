package com.bit.eduventure.lecture.controller;


import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Service.CourseService;
import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.entity.Lecture;
import com.bit.eduventure.lecture.service.LectureService;
import com.bit.eduventure.livestation.dto.LiveStationInfoDTO;
import com.bit.eduventure.livestation.dto.LiveStationUrlDTO;
import com.bit.eduventure.livestation.dto.RecordVodDTO;
import com.bit.eduventure.livestation.service.LiveStationService;
import com.bit.eduventure.objectStorage.service.ObjectStorageService;
import com.bit.eduventure.validate.ValidateService;
import com.bit.eduventure.vodBoard.entity.VodBoard;
import com.bit.eduventure.vodBoard.service.VodBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/lecture")
@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;
    private final LiveStationService liveStationService;
    private final UserService userService;
    private final CourseService courseService;
    private final VodBoardService vodBoardService;
    private final ObjectStorageService objectStorageService;
    private final ValidateService validateService;

    //강사가 강의 개설
    @PostMapping("/lecture")
    public ResponseEntity<?> createLecture(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody LectureDTO lectureDTO) {
        ResponseDTO<LiveStationInfoDTO> responseDTO = new ResponseDTO<>();

        System.out.println("강의 생성 시 couNo 들어오는 지 확인 lectureDTO.getCouNo(): " + lectureDTO.getCouNo());
        //권한 확인
        int userNo = customUserDetails.getUser().getId();
        User user = userService.findById(userNo);
        validateService.validateTeacherAndAdmin(user);

        String title = lectureDTO.getTitle();

        String channelId = liveStationService.createChannel(title);

        lectureDTO.setLiveStationId(channelId);

        lectureDTO = lectureService.createLecture(lectureDTO).EntityTODTO();

        LiveStationInfoDTO liveStationInfoDTO = liveStationService.getChannelInfo(channelId);
        liveStationInfoDTO.setLectureId(lectureDTO.getId());

        responseDTO.setItem(liveStationInfoDTO);
        responseDTO.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok().body(responseDTO);

    }

    @GetMapping("/lecture/{liveStationId}")
    public ResponseEntity<?> getLiveStation(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @PathVariable String liveStationId) {
        ResponseDTO<LiveStationInfoDTO> response = new ResponseDTO<>();

        Lecture lecture = lectureService.getLectureLiveStationId(liveStationId);

        LiveStationInfoDTO liveStationInfoDTO = liveStationService.getChannelInfo(liveStationId);
        liveStationInfoDTO.setLectureId(lecture.getId());

        response.setItem(liveStationInfoDTO);
        response.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/lecture/{liveStationId}")
    public ResponseEntity<?> deleteLiveStation(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                               @PathVariable String liveStationId) {
        ResponseDTO<String> response = new ResponseDTO<>();

        //권한 확인
        int userNo = customUserDetails.getUser().getId();
        User user = userService.findById(userNo);
        validateService.validateTeacherAndAdmin(user);

        Lecture lecture = lectureService.getLectureLiveStationId(liveStationId);
        int lectureId = lecture.getId();

        RecordVodDTO recordVodDTO = liveStationService.getRecord(liveStationId);

        //녹화된 파일이 있다면 게시글 작성
        if (recordVodDTO != null) {
            Course course = courseService.getCourse(lecture.getCouNo());

            String vodName = recordVodDTO.getFileName();    //녹화된 파일명
            String thumb = "edu-venture.png";               //기본 썸네일

            //삭제 전 녹화파일 게시글 작성
            VodBoard vodBoard = VodBoard.builder()
                    .title(lecture.getTitle())
                    .content(lecture.getTitle() + " 으로 자동 생성된 게시글 입니다.")
                    .writer(course.getUser().getUserName())
                    .savePath(objectStorageService.getObjectSrc(vodName)) //영상 주소
                    .originPath(vodName)
                    .objectPath(vodName) //오브젝트에 저장된 영상 파일 명
                    .saveThumb(objectStorageService.getObjectSrc(thumb))
                    .objectThumb(thumb)
                    .user(course.getUser())
                    .build();
            vodBoardService.insertBoard(vodBoard, null);
        }

        liveStationService.deleteChannel(liveStationId);
        lectureService.deleteLecture(lectureId);

        if (recordVodDTO != null) {
            response.setItem("녹화된 강의가 게시되었습니다.");
        } else if (recordVodDTO == null) {
            response.setItem("녹화된 강의가 없어 게시글 없이 삭제되었습니다.");
        }
        response.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/student/lecture")
    public ResponseEntity<?> getLecture(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        ResponseDTO<LiveStationUrlDTO> response = new ResponseDTO<>();

        int userId = customUserDetails.getUser().getId();
        User user = userService.findById(userId);

        if (user.getCourse() != null) {
            int couNo = user.getCourse().getCouNo();
            LectureDTO lectureDTO = lectureService.getCouLecture(couNo).EntityTODTO();
            String channelID = lectureDTO.getLiveStationId();
            LiveStationInfoDTO dto = liveStationService.getChannelInfo(channelID);

            if (!dto.getChannelStatus().equals("PUBLISH")) {
                response.setErrorMessage("진행 중인 강의가 없습니다.");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.ok().body(response);
            }

            List<LiveStationUrlDTO> urlList = liveStationService.getServiceURL(channelID);

            response.setItems(urlList);
            response.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(response);

        } else {
            throw new NoSuchElementException();
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
}
