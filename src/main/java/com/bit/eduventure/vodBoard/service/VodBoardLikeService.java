package com.bit.eduventure.vodBoard.service;

import com.bit.eduventure.vodBoard.dto.VodBoardLikeDTO;
import com.bit.eduventure.vodBoard.entity.VodBoardLike;
import com.bit.eduventure.vodBoard.repository.VodBoardLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VodBoardLikeService {

    private final VodBoardLikeRepository vodBoardLikeRepository;

    @Transactional
    public void likeVodBoard(int vodNo, int userNo) {
        VodBoardLike vodBoardLike = VodBoardLike.builder()
                .vodNo(vodNo)
                .userNo(userNo)
                .likeStatus(1)
                .build();
        vodBoardLikeRepository.save(vodBoardLike);
    }

    public int getLikeStatue(int vodNo, int userNo) {
        if (vodBoardLikeRepository.findByVodNoAndUserNo(vodNo, userNo) != null) {
            return 1;
        } else {
            return 0;
        }
    }

    //좋아요 개수 구하기
    public int getLikeCount(int vodNo) {
        return vodBoardLikeRepository.countAllByVodNo(vodNo);
    }

    @Transactional
    public void unlikeVodBoard(int likeNo) {
        vodBoardLikeRepository.deleteById(likeNo);
    }
}
