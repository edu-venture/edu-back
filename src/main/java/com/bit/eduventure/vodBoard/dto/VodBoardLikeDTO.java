package com.bit.eduventure.vodBoard.dto;

import com.bit.eduventure.vodBoard.entity.VodBoardLikeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class VodBoardLikeDTO {
    private int vb_like_idx; // VodBoard 좋아요 인덱스
    private int m_idx; // 사용자 인덱스를 저장함
    private int vb_idx; // VodBoard 인덱스
    private int likestatus; // 좋아요 상태 (예: 0 - 좋아요 취소, 1 - 좋아요)

    public static VodBoardLikeDTO vodBoardLikeDTO(VodBoardLikeEntity entity) {

        return VodBoardLikeDTO.builder()
                .vb_like_idx(entity.getVBlikeidx())
                .m_idx(entity.getMIdx())
                .vb_idx(entity.getVBidx())
                .likestatus(entity.getLikestatus())
                .build();
    }
}
