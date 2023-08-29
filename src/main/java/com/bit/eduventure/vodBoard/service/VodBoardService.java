package com.bit.eduventure.vodBoard.service;


import com.bit.eduventure.vodBoard.entity.VodBoard;
import com.bit.eduventure.vodBoard.entity.VodBoardFile;
import com.bit.eduventure.vodBoard.repository.VodBoardFileRepository;
import com.bit.eduventure.vodBoard.repository.VodBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VodBoardService {
    private final VodBoardRepository vodBoardRepository;
    private final VodBoardFileRepository vodBoardFileRepository;
    private final VodBoardCommentService vodBoardCommentService;

    //디비에 저장되어있는 파일 삭제
    public void deleteFile(VodBoardFile boardFile) {
        vodBoardFileRepository.delete(boardFile);
    }

    public VodBoard getBoard(int boardNo) {
        VodBoard returnBoard = vodBoardRepository.findById(boardNo)
                .orElseThrow(() -> new NoSuchElementException());
        returnBoard.setHits(returnBoard.getHits()+1); //조회수 증가
        vodBoardRepository.save(returnBoard);

        return returnBoard;
    }

    //리스트
    public List<VodBoard> getVodBoardList() {
        return vodBoardRepository.findAll();
    }

    //생성
    public VodBoard createVodBoard(VodBoard vodBoard) {
        return vodBoardRepository.save(vodBoard);
    }

    //수정 기능
    public VodBoard updateVodBoard(int boardNo, VodBoard updatedVodBoard) {
        vodBoardRepository.findById(boardNo)
                .orElseThrow(() -> new NoSuchElementException());
        return vodBoardRepository.save(updatedVodBoard);
    }

    //삭제 기능
    public void deleteVodBoard(int boardNo) {
        vodBoardCommentService.deleteCommentVodNo(boardNo);
        vodBoardRepository.deleteById(boardNo);
    }

    //첨부파일 리스트 불러오기
    public List<VodBoardFile> getBoardFileList(int boardNo) {
        return vodBoardFileRepository.findAllByVodBoardNo(boardNo); // boardNo 해당하는 첨부 file 가져오기
    }

    //게시글 등록하기
    public void insertBoard(VodBoard board, List<VodBoardFile> fileList) {
        board.setRegDate(LocalDateTime.now());
        board.setHits(0);

        vodBoardRepository.save(board);
        vodBoardRepository.flush();
        if (fileList != null) {
            fileList.stream().forEach(boardFile -> {
                boardFile.setVodBoardNo(board.getId());
                vodBoardFileRepository.save(boardFile);
            });
        }

    }

    //게시글 수정하기
    public void insertBoardFiles(VodBoard vodBoard, List<VodBoardFile> uploadFileList) {
    }

//    public List<VodBoardFile> getBoardFileList(int boardNo) {
//        return vodBoardFileRepository.findAllByVodBoardNo(boardNo);
//    }

//    public void insertBoard(VodBoard board, List<VodBoardFile> fileList) {
//        vodBoardRepository.save(board);
//        vodBoardRepository.flush();
//
//        for (VodBoardFile boardFile : fileList) {
//            boardFile.setVodBoardNo(board.getId());
//
//            int boardFilNo = vodBoardFileRepository.save(boardFile).getVodFileNo();
//            boardFile.setVodFileNo(boardFilNo);
//
//            vodBoardFileRepository.save(boardFile);
//        }
//    }
}