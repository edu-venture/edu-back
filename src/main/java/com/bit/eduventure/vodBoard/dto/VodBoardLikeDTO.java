package com.bit.eduventure.vodBoard.dto;

import com.bit.eduventure.vodBoard.entity.VodBoardLike;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class VodBoardLikeDTO {
    private int id; // 내이름은VodBoard 좋아요 인덱스 자동생성이죠
    private int userNo; // 사용자 인덱스 (누가 좋아요를 눌렀는지 식별
    private int vodNo; // VodBoard 인덱스
    private int likeStatus; // 좋아요 상태

    public VodBoardLike DTOTOEntity() {
        return VodBoardLike.builder()
                .id(this.id)
                .userNo(this.userNo)
                .vodNo(this.vodNo)
                .likeStatus(this.likeStatus)
                .build();
    }
}
