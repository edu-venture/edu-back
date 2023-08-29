package com.bit.eduventure.vodBoard.repository;


import com.bit.eduventure.vodBoard.entity.VodBoard;
import com.bit.eduventure.vodBoard.entity.VodBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VodBoardCommentRepository extends JpaRepository<VodBoardComment, Integer> {
//    //VBidx와 VBcref 값으로 댓글을 검색하고, 작성일자(VBcwriteday)를 기준으로 내림차순으로 정렬하여 반환합니다.
//    List<VodBoardComment> findAllByVBidxAndVBcrefEqualsOrderByVBcwritedayDesc(int VBidx, int VBcref);
    // vb_idx (댓글이 속하는 VOD 게시글의 인덱스) 값으로 대댓글을 검색하고,
    // 작성일자(VBcwriteday)를 기준으로 내림차순으로 정렬하여 반환합니다.
//    List<VodBoardComment> findAllByVBcrefOrderByVBcwritedayDesc(int qb_idx);
//    //해당 vb_idx (댓글이 속하는 VOD 게시글의 인덱스) 값에 대한 댓글의 개수를 반환합니다.
//    int countAllByVBidx(int qb_idx); //vb_idx VBidx
//    //해당 vbc_idx (댓글의 고유한 인덱스) 값에 대한 대댓글의 개수를 반환합니다.
//    int countAllByVBcref(int qbc_idx); //vbc_idx VBcref
//    //해당 vbc_idx (댓글의 고유한 인덱스) 값에 대한 대댓글을 모두 삭제합니다.
//    void deleteAllByVBcref(int qbc_idx); //vbc_idx VBcref
    //게시물 번호에 해당하는 모든 댓글 불러오기
    List<VodBoardComment> findAllByVodNoId(int vodNo);

    List<VodBoardComment> findAllByVodCmtParentNo(int commentNo);

    List<VodBoardComment> findAllByVodNoAndVodCmtParentNo(VodBoard vodNo, int parentNo);

    void deleteAllByVodNoId(int id);
}
