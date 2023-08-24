//package com.bit.eduventure.payment.repository;
//
//import com.bit.eduventure.payment.entity.Payment;
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Transactional
//public interface PaymentRepository extends JpaRepository<Payment, Integer> {
//
//    public List<Payment> findByPayNo(int payNo);
//
//    // 특정 유저의 특정 달에 해당하는 납부서 조회
//    @Query(value="SELECT * FROM t_payment p WHERE p.user_no = :userNo AND year(p.iss_date) = year(:issDate) AND month(p.iss_date) = month(:issDate)", nativeQuery = true)
//    public List<Payment> findByUserNoAndIssDate(int userNo, LocalDateTime issDate);
//
//    // 특정 유저의 특정 달에 해당하는 영수증 조회
//    @Query(value="SELECT * FROM t_payment p WHERE p.user_no = :userNo AND year(p.iss_date) = year(:issDate) AND month(p.iss_date) = month(:issDate)", nativeQuery = true)
//    public List<Payment> findByUserNoAndIsPayTrueAndIssDate(int userNo, LocalDateTime issDate);
//
//    @Query(value = "SELECT IFNULL(MAX(PAY_NO), 0) + 1 from t_payment", nativeQuery = true)
//    Long findMaxPayNo();
//}
