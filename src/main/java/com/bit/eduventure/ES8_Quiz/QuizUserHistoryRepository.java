package com.bit.eduventure.ES8_Quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface QuizUserHistoryRepository extends JpaRepository<QuizUserHistory, Integer> {


    Optional<QuizUserHistory> findByIdAndBoardNo(Integer id, Integer boardNo);



}
