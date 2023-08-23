package com.bit.eduventure.ES8_Quiz;

import com.bit.eduventure.ES1_User.DTO.ResponseDTO;
import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES7_Board.DTO.BoardDTO;
import com.bit.eduventure.ES7_Board.DTO.BoardFileDTO;
import com.bit.eduventure.ES7_Board.Entity.Board;
import com.bit.eduventure.ES7_Board.Entity.BoardFile;
import com.bit.eduventure.ES7_Board.Repository.BoardRepository;
import com.bit.eduventure.ES7_Board.Service.BoardService;
import com.bit.eduventure.common.FileUtil;
import com.bit.eduventure.common.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/quiz")
public class ControllerQuizBoard {

    private QuizBoardService quizBoardService;
    @Autowired
    private RepositoryQuizBoard repositoryQuizBoard;
    @Value("${file.path}")
    String attachPath;

    @Autowired
    public ControllerQuizBoard(QuizBoardService quizBoardService) {
        this.quizBoardService = quizBoardService;
    }

    @GetMapping("/board-list")
    public ResponseEntity<?> getBoardList(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @RequestParam(value = "searchCondition", required = false) String searchCondition,
                                          @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        ResponseDTO<QuizBoardDTO> responseDTO = new ResponseDTO<>();

        try {
            searchCondition = searchCondition == null ? "all" : searchCondition;
            searchKeyword = searchKeyword == null ? "" : searchKeyword;

            Page<QuizBoard> pageBoard = quizBoardService.getBoardList(pageable, searchCondition, searchKeyword);

            Page<QuizBoardDTO> pageBoardDTO = pageBoard.map(board ->
                    QuizBoardDTO.builder()
                            .boardNo(board.getBoardNo())
                            .boardTitle(board.getBoardTitle())
                            .boardWriter(board.getBoardWriter())
                            .boardContent(board.getBoardContent())
                            .boardRegdate(board.getBoardRegdate().toString())
                            .boardCnt(board.getBoardCnt())
                            .build()
            );

//            List<Board> boardList = boardService.getBoardList();
//
//            List<BoardDTO> boardDTOList = new ArrayList<>();
//
//            for(Board board : boardList) {
//                boardDTOList.add(board.EntityToDTO());
//            }

            responseDTO.setPageItems(pageBoardDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);

        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }





    //multipart form 데이터 형식을 받기 위해 consumes 속성 지정
    @PostMapping(value = "/board", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertBoard(QuizBoardDTO quizBoardDTO,
                                         MultipartHttpServletRequest mphsRequest) {
        ResponseDTO<Map<String, String>> responseDTO =
                new ResponseDTO<Map<String, String>>();
//        String attachPath =
//                request.getSession().getServletContext().getRealPath("/")
//                + "/upload/";

        File directory = new File(attachPath);

        if(!directory.exists()) {
            directory.mkdir();
        }

        List<QuizBoardFile> uploadFileList = new ArrayList<QuizBoardFile>();

        try {
            //BoardEntity에 지정한 boardRegdate의 기본값은
            //기본생성자 호출할 때만 기본값으로 지정되는데
            //builder()는 모든 매개변수를 갖는 생성자를 호출하기 때문에
            //boardRegdate의 값이 null값으로 들어간다.
            QuizBoard quizBoard = QuizBoard.builder()
                    .boardTitle(quizBoardDTO.getBoardTitle()).option1(quizBoardDTO.getOption1()).option2(quizBoardDTO.getOption2()).option3(quizBoardDTO.getOption3()).option4(quizBoardDTO.getOption4()).answer(quizBoardDTO.getAnswer())
                    .boardContent(quizBoardDTO.getBoardContent()).claName(quizBoardDTO.getClaName())
                    .boardWriter(quizBoardDTO.getBoardWriter())
                    .boardRegdate(LocalDateTime.now())
                    .build();
            System.out.println("========================"+quizBoard.getBoardRegdate());

            Iterator<String> iterator = mphsRequest.getFileNames();

            while(iterator.hasNext()) {
                List<MultipartFile> fileList = mphsRequest.getFiles(iterator.next());

                for(MultipartFile multipartFile : fileList) {
                    if(!multipartFile.isEmpty()) {
                        QuizBoardFile quizBoardFile = new QuizBoardFile();

                        quizBoardFile = FileUtil.parseFileInfo(multipartFile, attachPath);

                        quizBoardFile.setQuizBoard(quizBoard);

                        uploadFileList.add(quizBoardFile);
                    }
                }
            }

            quizBoardService.insertBoard(quizBoard, uploadFileList);

            Map<String, String> returnMap =
                    new HashMap<String, String>();

            returnMap.put("msg", "정상적으로 저장되었습니다.");

            responseDTO.setItem(returnMap);

            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

//    수정할때 boardDTO를 quizBoardDTO로 바꿨다
    @PutMapping(value = "/board")
    public ResponseEntity<?> updateBoard(@RequestPart(value = "quizBoardDTO") QuizBoardDTO quizBoardDTO,
                                         @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
                                         @RequestPart(value = "changeFileList", required = false) MultipartFile[] changeFileList,
                                         @RequestPart(value = "originFileList", required = false) String originFileList)
            throws Exception {
        System.out.println(quizBoardDTO);
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        List<QuizBoardFileDTO> originFiles = null;

        if(originFileList != null) {
            originFiles = new ObjectMapper().readValue(originFileList,
                    new TypeReference<List<QuizBoardFileDTO>>() {
                    });
        }


        //DB에서 수정, 삭제, 추가 될 파일 정보를 담는 리스트
        List<QuizBoardFile> uFileList = new ArrayList<QuizBoardFile>();

        try {
            QuizBoard quizBoard = quizBoardDTO.DTOToEntity();
            if(originFiles != null) {
                //파일 처리
                for (int i = 0; i < originFiles.size(); i++) {
                    //수정되는 파일 처리
                    if (originFiles.get(i).getBoardFileStatus().equals("U")) {
                        for (int j = 0; j < changeFileList.length; j++) {
                            if (originFiles.get(i).getNewFileName().equals(
                                    changeFileList[j].getOriginalFilename())) {
                                QuizBoardFile quizBoardFile = new QuizBoardFile();

                                MultipartFile file = changeFileList[j];

                                quizBoardFile = FileUtil.parseFileInfo(file, attachPath);

                                quizBoardFile.setQuizBoard(quizBoard);
                                quizBoardFile.setBoardFileNo(originFiles.get(i).getBoardFileNo());
                                quizBoardFile.setBoardFileStatus("U");

                                uFileList.add(quizBoardFile);
                            }
                        }
                        //삭제되는 파일 처리
                    } else if (originFiles.get(i).getBoardFileStatus().equals("D")) {
                        QuizBoardFile boardFile = new QuizBoardFile();

                        boardFile.setQuizBoard(quizBoard);
                        boardFile.setBoardFileNo(originFiles.get(i).getBoardFileNo());
                        boardFile.setBoardFileStatus("D");

                        //실제 파일 삭제
                        File dFile = new File(attachPath + originFiles.get(i).getBoardFileName());
                        dFile.delete();

                        uFileList.add(boardFile);
                    }
                }
            }
            //추가된 파일 처리
            if(uploadFiles != null && uploadFiles.length > 0) {
                for(int i = 0; i < uploadFiles.length; i++) {
                    MultipartFile file = uploadFiles[i];

                    if(file.getOriginalFilename() != null &&
                            !file.getOriginalFilename().equals("")) {
                        QuizBoardFile quizBoardFile = new QuizBoardFile();

                        quizBoardFile = FileUtil.parseFileInfo(file, attachPath);

                        quizBoardFile.setQuizBoard(quizBoard);
                        quizBoardFile.setBoardFileStatus("I");

                        uFileList.add(quizBoardFile);
                    }
                }
            }

            quizBoardService.updateBoard(quizBoard, uFileList);

            Map<String, Object> returnMap = new HashMap<>();

            QuizBoard updateBoard = quizBoardService.getBoard(quizBoard.getBoardNo());
            List<QuizBoardFile> updateBoardFileList =
                    quizBoardService.getBoardFileList(quizBoard.getBoardNo());

            QuizBoardDTO returnBoardDTO = updateBoard.EntityToDTO();

            List<QuizBoardFileDTO> boardFileDTOList = new ArrayList<>();

            for(QuizBoardFile boardFile : updateBoardFileList) {
                QuizBoardFileDTO boardFileDTO = boardFile.EntityToDTO();
                boardFileDTOList.add(boardFileDTO);
            }

            returnMap.put("board", returnBoardDTO);
            returnMap.put("boardFileList", boardFileDTOList);

            responseDTO.setItem(returnMap);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/board/{boardNo}")
    public ResponseEntity<?> deleteBoard(@PathVariable int boardNo) {
        ResponseDTO<Map<String, String>> responseDTO =
                new ResponseDTO<Map<String, String>>();
        try {
            quizBoardService.deleteBoard(boardNo);
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

    @GetMapping("/board/{boardNo}")
    public ResponseEntity<?> getBoard(@PathVariable int boardNo) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            QuizBoard quizBoard = quizBoardService.getBoard(boardNo);

            QuizBoardDTO returnBoardDTO = quizBoard.EntityToDTO();

            List<QuizBoardFile> boardFileList = quizBoardService.getBoardFileList(boardNo);

            List<QuizBoardFileDTO> boardFileDTOList = new ArrayList<>();

            for (QuizBoardFile boardFile : boardFileList) {
                QuizBoardFileDTO boardFileDTO = boardFile.EntityToDTO();
                boardFileDTOList.add(boardFileDTO);
            }

            Map<String, Object> returnMap = new HashMap<>();

            returnMap.put("board", returnBoardDTO);
            returnMap.put("boardFileList", boardFileDTOList);

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



}
