package com.bit.eduventure.ES5_Notice.Repository;

import com.bit.eduventure.ES5_Notice.Entity.Notice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    @Query("SELECT n FROM Notice n WHERE n.user.course.couNo = :couNo OR n.user.userType = 'admin'")
    List<Notice> findAllByCourseIdAndAdmin(@Param("couNo") int couNo);
}
