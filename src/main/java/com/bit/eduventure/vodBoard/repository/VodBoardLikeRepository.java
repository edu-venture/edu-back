package com.bit.eduventure.vodBoard.repository;

import com.bit.eduventure.vodBoard.entity.VodBoardLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VodBoardLikeRepository extends JpaRepository<VodBoardLikeEntity, Integer> {
    VodBoardLikeEntity findByVBidxAndMIdx(int vb_idx, int m_idx);
}
