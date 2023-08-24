//package com.bit.eduventure.payment.repository;
//
//import com.bit.eduventure.payment.entity.Product;
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//
//@Transactional
//public interface ProductRepository extends JpaRepository<Product, Long> {
//
//    public Optional<Product> findByProNo(Long proNo);
//
//    public List<Product> findByProNoIn(List<Integer> proNos);
//
//    List<Product> findByProNameIn(List<String> proNames);
//}
