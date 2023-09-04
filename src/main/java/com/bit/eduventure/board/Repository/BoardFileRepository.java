package com.bit.eduventure.board.Repository;


import com.bit.eduventure.board.Entity.BoardFile;
import com.bit.eduventure.board.Entity.BoardFileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardFileRepository  extends JpaRepository<BoardFile, BoardFileId> {

    //아 하 꺽쇠 오른쪽에 잇는게 피케이냐?

    //@Query : repository에 원한 쿼리를 작성하게 해주는 어노테이션
//nativeQuery : JPA에서 지정한 규칙을 모두 무시할 수 있는 속성.



    @Query(value = "SELECT IFNULL(MAX(F.BOARD_FILE_NO),0)+1 FROM T_BOARD_FILE F WHERE F.BOARD_NO= :boardNo", nativeQuery = true)
    public int findMaxFileNo(int boardNo);


    List<BoardFile> findByBoardBoardNo(int boardNo);


//    List<BoardFile> findByBoardNo(int boardNo);



}
