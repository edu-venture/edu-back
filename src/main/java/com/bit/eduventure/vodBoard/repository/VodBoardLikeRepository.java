package com.bit.eduventure.vodBoard.repository;

import com.bit.eduventure.vodBoard.entity.VodBoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VodBoardLikeRepository extends JpaRepository<VodBoardLike, Integer> {
    VodBoardLike findByVodNoAndUserNo(int vodNo, int userNo);
    int countAllByVodNo(int vodNo);
}
