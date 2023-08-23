package com.bit.eduventure.ES8_Quiz;

import com.bit.eduventure.ES7_Board.Entity.Board;
import com.bit.eduventure.ES7_Board.Entity.BoardFile;
import com.bit.eduventure.ES7_Board.Entity.BoardFileId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryQuizBoard extends JpaRepository<QuizBoard, Integer> {


    Optional<QuizBoard> findByBoardNo(int boardNo);

    Page<QuizBoard> findByBoardTitleContainingOrBoardContentContainingOrBoardWriterContaining(String searchKeyword, String searchKeyword1, String searchKeyword2, Pageable pageable);

    Page<QuizBoard> findByBoardTitleContaining(String searchKeyword, Pageable pageable);

    Page<QuizBoard> findByBoardContentContaining(String searchKeyword, Pageable pageable);

    Page<QuizBoard> findByBoardWriterContaining(String searchKeyword, Pageable pageable);





}
