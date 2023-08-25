package com.bit.eduventure.lecture.controller;


import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.service.LectureService;
import com.bit.eduventure.livestation.service.LiveStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/lecture")
@RequiredArgsConstructor
@RestController
public class LectureController {

    private final LectureService lectureService;
    private final LiveStationService liveStationService;
//    private final VodBoardService vodBoardService;

    //강사가 강의 개설
    @PostMapping("/create")
    public ResponseEntity<?> createLecture(LectureDTO lectureDTO) {
        ResponseDTO<LectureDTO> responseDTO = new ResponseDTO<>();
        try {
            String title = lectureDTO.getTitle();
            String channelId = liveStationService.createChannel(title);
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
