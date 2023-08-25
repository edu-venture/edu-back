package com.bit.eduventure.vodBoard.service;

import com.bit.eduventure.vodBoard.dto.VodBoardLikeDTO;
import com.bit.eduventure.vodBoard.entity.VodBoardLikeEntity;
import com.bit.eduventure.vodBoard.repository.VodBoardLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VodBoardLikeService {

    private final VodBoardLikeRepository vodBoardLikeRepository;

    @Transactional
    public VodBoardLikeDTO likeVodBoard(int vb_idx, int m_idx) {
        VodBoardLikeEntity likeEntity = vodBoardLikeRepository.findByVBidxAndMIdx(vb_idx, m_idx);
        if (likeEntity == null) {
            likeEntity = VodBoardLikeEntity.builder()
                    .VBidx(vb_idx)
                    .MIdx(m_idx)
                    .likestatus(1) // 좋아요 상태로 설정
                    .build();
        } else {
            likeEntity.setLikestatus(1); // 이미 좋아요한 경우 상태 업데이트
        }
        vodBoardLikeRepository.save(likeEntity);
        return VodBoardLikeDTO.vodBoardLikeDTO(likeEntity);
    }

    @Transactional
    public void unlikeVodBoard(int vb_idx, int m_idx) {
        VodBoardLikeEntity likeEntity = vodBoardLikeRepository.findByVBidxAndMIdx(vb_idx, m_idx);
        if (likeEntity != null) {
            likeEntity.setLikestatus(0); // 좋아요 취소 상태로 설정
            vodBoardLikeRepository.save(likeEntity);
        }
    }
}
