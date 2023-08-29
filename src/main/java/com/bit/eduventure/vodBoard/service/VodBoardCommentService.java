package com.bit.eduventure.vodBoard.service;

import com.bit.eduventure.ES1_User.Repository.UserRepository;
import com.bit.eduventure.vodBoard.dto.VodBoardCommentDTO;
import com.bit.eduventure.vodBoard.entity.VodBoard;
import com.bit.eduventure.vodBoard.entity.VodBoardComment;
import com.bit.eduventure.vodBoard.repository.VodBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VodBoardCommentService {

    private final VodBoardCommentRepository vodBoardCommentRepository;
    private final UserRepository userRepository;

    //게시물에 해당하는 모든 댓글 리스트 가져오는 메서드
    public List<VodBoardCommentDTO> getAllCommentList(int vodNo) {
        List<VodBoardComment> list = vodBoardCommentRepository.findAllByVodNoId(vodNo);

        List<VodBoardCommentDTO> dtoList = list.stream()
                .map(VodBoardComment::EntityTODTO)
                .collect(Collectors.toList());

        List<VodBoardCommentDTO> returnList = new ArrayList<>();
        Map<Integer, VodBoardCommentDTO> dtoMap = new HashMap<>(); // Create a map to store DTOs by their ID

        for (VodBoardCommentDTO commentDTO : dtoList) {
            if (commentDTO.getVodCmtParentNo() == 0) {
                returnList.add(commentDTO);
            }

            dtoMap.put(commentDTO.getId(), commentDTO); // Add DTOs to the map using their ID
        }

        dtoList.stream()
                .filter(commentDTO -> commentDTO.getVodCmtParentNo() != 0)
                .forEach(commentDTO -> {
                    VodBoardCommentDTO parentDTO = dtoMap.get(commentDTO.getVodCmtParentNo()); // Retrieve parent DTO from the map
                    if (parentDTO != null) {
                        if (parentDTO.getVodCmtList() == null) {
                            parentDTO.setVodCmtList(new ArrayList<>()); // Initialize the list if null
                        }
                        parentDTO.getVodCmtList().add(commentDTO);
                    }
                });

        return returnList;
    }

    //댓글 작성 메소드
    public VodBoardCommentDTO addComment(VodBoardCommentDTO commentDTO) {
        commentDTO.setVodCmtRegdate(LocalDateTime.now());
        VodBoardComment comment = commentDTO.DTOTOEntity();
        VodBoardComment savedComment = vodBoardCommentRepository.save(comment);
        return savedComment.EntityTODTO();
    }

//    //부모 댓글 삭제 메소드
//    public void deleteParentComment(int commentNo) {
//        List<VodBoardComment> childComments = vodBoardCommentRepository.findAllByVodCmtParentNo(commentNo);
//
//        for (VodBoardComment childComment : childComments) {
//            deleteChildComment(childComment.getId()); // 자식 댓글 삭제
//        }
//
//        vodBoardCommentRepository.deleteById(commentNo); // 부모 댓글 삭제
//    }
//
//    //자식 댓글 삭제 메소드
//    public void deleteChildComment(int commentNo) {
//        vodBoardCommentRepository.deleteById(commentNo);
//    }


    // 게시물 삭제 시 해당 게시물의 댓글 전체 삭제
    public void deleteAllCommentsAndRepliesByVodNo(int boardNo) {
        vodBoardCommentRepository.deleteAllByVodNoId(boardNo);
    }

    // 댓글 삭제 시 댓글인지 대댓글인지 구분 후 삭제
    public void deleteCommentAndReplies(int commentId) {
        VodBoardComment comment = vodBoardCommentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            // 댓글이 없는 경우 처리
            return;
        }

        if (comment.getVodCmtParentNo() == 0) {
            // 댓글일 경우: 댓글과 그에 딸린 대댓글 모두 삭제
            List<VodBoardComment> childComments = vodBoardCommentRepository.findAllByVodCmtParentNo(commentId);

            childComments.forEach(childComment -> deleteCommentAndReplies(childComment.getId())); // 재귀적으로 자식 댓글 삭제
        }

        // 댓글 또는 대댓글 삭제
        vodBoardCommentRepository.deleteById(commentId);
    }

    public VodBoardComment getComment(int commentNo) {
        return vodBoardCommentRepository.findById(commentNo).orElseThrow();
    }



}
