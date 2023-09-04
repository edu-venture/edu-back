package com.bit.eduventure.board.Service;


import com.bit.eduventure.board.Entity.Board;
import com.bit.eduventure.board.Entity.BoardFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    Board getBoard(int boardNo);

    List<Board> getBoardList();

    void insertBoard(Board board, List<BoardFile> uploadFileList);

    void updateBoard(Board board, List<BoardFile> uFileList);

    void deleteBoard(int boardNo);

    List<BoardFile> getBoardFileList(int boardNo);

    Page<Board> getBoardList(Pageable pageable, String searchCondition, String searchKeyword);
}
