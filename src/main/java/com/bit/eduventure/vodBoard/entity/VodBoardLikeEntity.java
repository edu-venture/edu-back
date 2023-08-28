package com.bit.eduventure.vodBoard.entity;

import com.bit.eduventure.vodBoard.dto.VodBoardLikeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "vboard_like")
@Builder
public class VodBoardLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vb_like_idx")
    private int VBlikeidx; // 내이름은VodBoard 좋아요 인덱스 자동생성이죠

    @Column(name = "m_idx")
    private int MIdx; // 사용자 인덱스 (누가 좋아요를 눌렀는지 식별

    @Column(name = "vb_idx")
    private int VBidx; // VodBoard 인덱스

    @Column(name = "likestatus")
    private int likestatus; // 좋아요 상태

    public  VodBoardLikeEntity(int MIdx, int QBidx) {
        this.MIdx = MIdx;
        this.VBidx = QBidx;
        this.likestatus = likestatus;
    }

    public static VodBoardLikeEntity toVodboardLikelikeEntity(VodBoardLikeDTO dto){

        return VodBoardLikeEntity.builder()
                .VBlikeidx(dto.getVb_like_idx())
                .MIdx(dto.getM_idx())
                .VBidx(dto.getVb_idx())
                .likestatus(dto.getLikestatus())
                .build();
    }
}
