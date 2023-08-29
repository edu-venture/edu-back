package com.bit.eduventure.vodBoard.service;

import com.bit.eduventure.ES1_User.Repository.UserRepository;
import com.bit.eduventure.vodBoard.dto.VodBoardCommentDTO;
import com.bit.eduventure.vodBoard.entity.VodBoardComment;
import com.bit.eduventure.vodBoard.repository.VodBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VodBoardCommentService {

    private final VodBoardCommentRepository vodBoardCommentRepository;

    //게시물에 해당하는 모든 댓글 리스트 가져오는 메서드
    @Transactional
    public List<VodBoardCommentDTO> getAllCommentList(int vodNo) {
        List<VodBoardComment> list = vodBoardCommentRepository.findAllByVodNo(vodNo);
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
                        if (parentDTO.getVodSonCmtList() == null) {
                            parentDTO.setVodSonCmtList(new ArrayList<>()); // Initialize the list if null
                        }
                        parentDTO.getVodSonCmtList().add(commentDTO);
                    }
                });

        return returnList;
    }

    //댓글 작성 메소드
    @Transactional
    public VodBoardCommentDTO addComment(VodBoardCommentDTO commentDTO) {
        commentDTO.setVodCmtRegdate(LocalDateTime.now());
        VodBoardComment comment = commentDTO.DTOTOEntity();
        VodBoardComment savedComment = vodBoardCommentRepository.save(comment);
        return savedComment.EntityTODTO();
    }

    @Transactional
    //댓글 삭제 메소드
    public void deleteComment(int commentNo) {
        vodBoardCommentRepository.deleteById(commentNo);
    }

    @Transactional
    public VodBoardComment getComment(int commentNo) {
        return vodBoardCommentRepository.findById(commentNo).orElseThrow();
    }

    @Transactional
    public void deleteCommentVodNo(int vodNo) {
        vodBoardCommentRepository.deleteAllByVodNo(vodNo);
    }
}
