package com.bit.eduventure.ES5_Notice.Controller;


import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES5_Notice.DTO.NoticeDTO;
import com.bit.eduventure.ES5_Notice.Entity.Notice;
import com.bit.eduventure.ES5_Notice.Service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;


    @GetMapping("/notice-list")
    public ResponseEntity<?> getBoardList(
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails
                                         ) {
        ResponseDTO<NoticeDTO> responseDTO = new ResponseDTO<>();
        System.out.println("노티스리스트 도착완료 트라이 직전");
        try {
            List<Notice> noticeList = noticeService.getNoticeList();
            List<NoticeDTO> noticeDTOList = new ArrayList<>();
            for(Notice notice : noticeList) {
                noticeDTOList.add(notice.EntityToDTO());
            }
            responseDTO.setItems(noticeDTOList);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }




    @PutMapping("/noticeupdate")
    public ResponseEntity<?> update(@RequestBody NoticeDTO noticeDTO) {
        ResponseDTO<NoticeDTO> responseDTO = new ResponseDTO<>();
        System.out.println(noticeDTO);
        System.out.println("notice dto 업데이트에 들어왔음");
        try {
            Notice notice = noticeDTO.DTOToEntity();
            System.out.println(notice);
//            user.setUserPw(
//                    passwordEncoder.encode(userDTO.getUserPw())
//            );

            Notice noticetogo = noticeService.update(notice);
            System.out.println(noticetogo);


            NoticeDTO noticeDTOtogo = noticetogo.EntityToDTO();
            responseDTO.setItem(noticeDTOtogo);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }






    @GetMapping("/getnotice/{noticeNo}")
    public ResponseEntity<?> getNotice(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @PathVariable int noticeNo) {
        ResponseDTO<NoticeDTO> responseDTO = new ResponseDTO<>();

        Notice notice = noticeService.findById(noticeNo)
                .orElseThrow(() -> new NoSuchElementException("Notice not found"));
        NoticeDTO noticeDTOtosend = notice.EntityToDTO();

        responseDTO.setItem(noticeDTOtosend);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(responseDTO);
    }








    @PostMapping("/createnotice")
    public ResponseEntity<?> join(@RequestBody NoticeDTO noticeDTO) {
        ResponseDTO<NoticeDTO> responseDTO = new ResponseDTO<>();
        System.out.println(noticeDTO);
        try {

            Notice notice = noticeDTO.DTOToEntity();
            System.out.println(notice);
            System.out.println("이게 그냥 노티스");
            Notice resultNotice = noticeService.create(notice);
            System.out.println("트라이로는 들어왔음");
            NoticeDTO resultNoticeDTO = resultNotice.EntityToDTO();
            responseDTO.setItem(resultNoticeDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println("캐치로 넘어왔다.");
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable int id) {
        ResponseDTO<Map<String, String>> responseDTO =
                new ResponseDTO<Map<String, String>>();
        try {
            noticeService.deleteNotice(id);
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

    @GetMapping("/course")
    public ResponseEntity<?> getCourseNotice(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ResponseDTO<NoticeDTO> responseDTO = new ResponseDTO<>();

        int userNo = userDetails.getUser().getId();
        User user = userService.findById(userNo);

        List<Notice> noticeList = noticeService.getCourseNoticeList(user.getCourse().getCouNo());

        List<NoticeDTO> noticeDTOList = noticeList.stream()
                .map(Notice::EntityToDTO)
                .collect(Collectors.toList());

        responseDTO.setItems(noticeDTOList);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(responseDTO);
    }



}
