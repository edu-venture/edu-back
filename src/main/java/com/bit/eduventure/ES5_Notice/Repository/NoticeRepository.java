package com.bit.eduventure.ES5_Notice.Repository;

import com.bit.eduventure.ES5_Notice.Entity.Notice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

//update나 Delete가 발생했을 때 곧장 커밋 롤백 처리


@Transactional
public interface NoticeRepository extends JpaRepository<Notice,Integer> {








}
