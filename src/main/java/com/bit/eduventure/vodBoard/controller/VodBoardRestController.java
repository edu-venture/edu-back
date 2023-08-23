package com.bit.eduventure.vodBoard.controller;


import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.objectStorage.service.ObjectStorageService;
import com.bit.eduventure.vodBoard.dto.VodBoardDTO;
import com.bit.eduventure.vodBoard.dto.VodBoardFileDTO;
import com.bit.eduventure.vodBoard.entity.VodBoard;
import com.bit.eduventure.vodBoard.entity.VodBoardFile;
import com.bit.eduventure.vodBoard.service.VodBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vod")
@RequiredArgsConstructor
public class VodBoardRestController {
    private final VodBoardService vodBoardService;
    private final ObjectStorageService objectStorageService;

    //등록
    //value 주소 / cosumes 공식
    @PostMapping(value = "/board")
    public ResponseEntity<?> insertBoard(@RequestPart(value = "boardDTO", required = false) VodBoardDTO boardDTO,
                                         @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
                                         @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                         @RequestPart(value = "fileList", required = false) MultipartFile[] fileList
    ) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        System.out.println(boardDTO);
        System.out.println(videoFile);
        System.out.println(thumbnail);
        System.out.println(fileList);
        List<VodBoardFile> uploadFileList = new ArrayList<>();

        try {

                for (int i = 0; i < fileList.length; i++) {
                    MultipartFile file = fileList[i];
                    if (!file.isEmpty()) {
                        System.out.println("getOriginalFilename: " + file.getOriginalFilename());
                        VodBoardFile boardFile = new VodBoardFile();
                        String saveName = objectStorageService.uploadFile(file);
                        boardFile.setVodOriginName(file.getOriginalFilename());
                        boardFile.setVodSaveName(saveName);
                        uploadFileList.add(boardFile);
                    }
                }

            System.out.println(uploadFileList);

            //메인 비디오 저장
            boardDTO.setOriginPath(videoFile.getOriginalFilename());
            boardDTO.setSavePath(objectStorageService.uploadFile(videoFile));

            //섬네일 저장
            //메인 비디오 저장
            boardDTO.setOriginThumb(thumbnail.getOriginalFilename());
            boardDTO.setSaveThumb(objectStorageService.uploadFile(thumbnail));

            VodBoard board = boardDTO.DTOTOEntity();

            vodBoardService.insertBoard(board, uploadFileList);

            Map<String, String> returnMap =
                    new HashMap<String, String>();

            returnMap.put("msg", "정상적으로 저장되었습니다.");

            responseDTO.setItem(returnMap);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //강의 목록(제목, 강사, 영상 다 포함됨)
    @GetMapping("/list")
    public ResponseEntity<?> getList() { // ResponseEntity<?> 스프링에서 제공하는 응답 객체
        //ResponseEntity 바디에 담아줄 객체(ResponseDTO) 선언
        //선언하면서 제네릭에 VodBoardFile 형태로 선언
        //했기때문에 items에는 List<VodBoardFile> 를 저장할 수 있다 items = List<VodBoardFile>
        //그리고 item에는 VodBoardFile 을 저장할 수 있다.
        ResponseDTO<VodBoardDTO> responseDTO = new ResponseDTO<>(); //응답 바디와 <데이터 형태>

        try {
            List<VodBoard> vodBoardList = vodBoardService.getVodBoardList();

            List<VodBoardDTO> vodBoardDTOList = new ArrayList<>();

            for(VodBoard vodBoard : vodBoardList) {
                VodBoardDTO vodBoardDTO = vodBoard.EntityToDTO();
                vodBoardDTOList.add(vodBoardDTO);
            }

            responseDTO.setItems(vodBoardDTOList);
            //데이터, 통신 오류나 상태 코드 등을 담기 위해서 responseDTO를 선언하고 사용한다.
            responseDTO.setStatusCode(200);


            return ResponseEntity.ok().body(responseDTO); // 파일 목록을 보여줄 뷰 페이지로 이동
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //상세 페이지 보여주기
    @GetMapping("/board/{boardNo}")
    public ResponseEntity<?> getBoard(@PathVariable int boardNo) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        try {
            VodBoard board = vodBoardService.getBoard(boardNo);

            VodBoardDTO returnBoardDTO = board.EntityToDTO();

            List<VodBoardFile> boardFileList = vodBoardService.getBoardFileList(boardNo); //첨부파일 첨가

            List<VodBoardFileDTO> boardFileDTOList = new ArrayList<>(); //DTO형태의 list 선언해주기

            for (VodBoardFile boardFile : boardFileList) { //하나씩 꺼내서 엔티티형태를 DTO형태의 list에 넣어주기
                VodBoardFileDTO boardFileDTO = boardFile.EntityToDTO();
                boardFileDTOList.add(boardFileDTO);
            }

            Map<String, Object> returnMap = new HashMap<>();

            returnMap.put("board", returnBoardDTO); //키 밸류값 게시글dto
            returnMap.put("boardFileList", boardFileDTOList); //첨부파일dto

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //조회수 증가해주는 요청을 하나 만든다.
    //화면에서는 상세조회 화면으로 들어올때
    //조회수 증가시켜주는 요청을 보내준다.
    //조회수가 증가되면 여기서 조회수 증가시키고 상세조회시키는곳으로 리다이랙트해준다

//    @PostMapping("/create") //강의 제목, 강사, 파일명 등 DB에 저장하기
//    //jwt data토큰을 받아올때 spring security context에다가 customuserdetais 객체를 등록해놨기 때문에 쓴다
//    public ResponseEntity<?> createVodBoard(@RequestBody VodBoardDTO vodBoardDTO,
//                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
//        ResponseDTO<VodBoardDTO> responseDTO = new ResponseDTO<>();
//
//        System.out.println("create들어옴");
//        System.out.println(vodBoardDTO);
//        try {
//            VodBoard vodBoard = vodBoardDTO.DTOTOEntity();
//            VodBoard savedVodBoard = vodBoardService.createVodBoard(vodBoard);
//            VodBoardDTO returnVodBoardDTO = savedVodBoard.EntityToDTO();
//            responseDTO.setItem(returnVodBoardDTO);
//
//            //파일 업로드 로직 추가
//
//
//            return ResponseEntity.ok().body(responseDTO);
//
//
//        } catch (Exception e) {
//            responseDTO.setErrorMessage(e.getMessage());
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }

//    @PutMapping("/update/{boardNo}") //수정하는거
//    public ResponseEntity<?> updateVodBoard(@PathVariable int boardNo,
//                                            @RequestBody VodBoardDTO vodBoardDTO, MultipartHttpServletRequest mhslr) {
//        ResponseDTO<VodBoardDTO> responseDTO = new ResponseDTO<>();
//
//        try {
//            VodBoard vodBoard = vodBoardDTO.DTOTOEntity();
//
//            //멀티파트 파일로 받아와서 오브젝트스토리지에 올리고
//            //파일 유틸즈를 써서 정보를 다시 빼와서
//            //VodBoard에다가 파일정보들을 세팅해서 저장을 해야겠죠?
//
//
//            VodBoard updatedVodBoard = vodBoardService.updateVodBoard(boardNo, vodBoard);
//            VodBoardDTO returnVodBoardDTO = updatedVodBoard.EntityToDTO();
//            responseDTO.setItem(returnVodBoardDTO);
//
//
//
//            // 수정 성공 메시지 등을 설정
//            responseDTO.setStatusCode(200);
//
//            return ResponseEntity.ok().body(responseDTO);
//        } catch (Exception e) {
//            responseDTO.setErrorMessage(e.getMessage());
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }

    @PutMapping("/update/{boardNo}") // 수정 기능
    public ResponseEntity<?> updateVodBoard(@PathVariable int boardNo,
                                            @RequestPart(value = "boardDTO", required = false) VodBoardDTO updatedBoardDTO,
                                            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
                                            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                            @RequestPart(value = "fileList", required = false) MultipartFile[] fileList) {
        ResponseDTO<VodBoardDTO> responseDTO = new ResponseDTO<>();

        try {
            VodBoard vodBoard = new VodBoard();

            if (updatedBoardDTO != null) {
                vodBoard = updatedBoardDTO.DTOTOEntity();
            } else {
                // If updatedBoardDTO is null, fetch existing VodBoard to modify
                vodBoard = vodBoardService.getBoard(boardNo);
            }

            // 기존에 등록된 첨부파일 정보 가져오기
            List<VodBoardFile> existingFileList = vodBoardService.getBoardFileList(boardNo);

            // 새로 업로드한 파일 저장
            List<VodBoardFile> uploadFileList = new ArrayList<>();
            if (fileList != null) {
                for (MultipartFile file : fileList) {
                    if (!file.isEmpty()) {
                        VodBoardFile boardFile = new VodBoardFile();
                        String saveName = objectStorageService.uploadFile(file);
                        boardFile.setVodOriginName(file.getOriginalFilename());
                        boardFile.setVodSaveName(saveName);
                        uploadFileList.add(boardFile);
                    }
                }
            }

            // 기존에 등록된 파일 삭제
            for (VodBoardFile existingFile : existingFileList) {
                objectStorageService.deleteObject(existingFile.getVodSaveName());
                vodBoardService.deleteFile(existingFile);
            }

            // 새로 업로드한 파일 등록
            if (!uploadFileList.isEmpty()) {
                vodBoardService.insertBoardFiles(vodBoard, uploadFileList);
            }

            // 새로 업로드한 비디오 및 섬네일 파일 저장
            if (videoFile != null && !videoFile.isEmpty()) {
                vodBoard.setOriginPath(videoFile.getOriginalFilename());
                vodBoard.setSavePath(objectStorageService.uploadFile(videoFile));
            }
            if (thumbnail != null && !thumbnail.isEmpty()) {
                vodBoard.setOriginThumb(thumbnail.getOriginalFilename());
                vodBoard.setSaveThumb(objectStorageService.uploadFile(thumbnail));
            }

            VodBoard updatedVodBoard = vodBoardService.updateVodBoard(boardNo, vodBoard);
            VodBoardDTO returnVodBoardDTO = updatedVodBoard.EntityToDTO();
            responseDTO.setItem(returnVodBoardDTO);

            // 수정 성공 메시지 등을 설정
            responseDTO.setStatusCode(200);

            return ResponseEntity.ok().body(responseDTO);
        } catch (NullPointerException npe) {
            responseDTO.setErrorMessage("요청에 필요한 정보가 부족합니다.");
            return ResponseEntity.badRequest().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/delete/{boardNo}")
    public ResponseEntity<?> deleteVodBoard(@PathVariable int boardNo) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();

        try {
            List<VodBoardFile> boardFileList = vodBoardService.getBoardFileList(boardNo); //첨부파일 첨가


            for(VodBoardFile vodBoardFile : boardFileList) {
                //오브젝트스토리지
                objectStorageService.deleteObject(vodBoardFile.getVodSaveName());
                //첨부파일디비삭제
                vodBoardService.deleteFile(vodBoardFile);
            }

            //게시물db삭제 (작은것 부터 삭제하는게 좋을것 같다. 첨부파일(오브젝트스토리지, 디비) -> 게시판
            vodBoardService.deleteVodBoard(boardNo);

            // 삭제 성공 메시지 등을 설정
            responseDTO.setItem("정상적으로 모두 삭제되었습니다");
            responseDTO.setStatusCode(200);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
