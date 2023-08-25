//package com.bit.eduventure.payment.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//@Entity
////@Table: 테이블 이름등을 지정
//@Table(name="T_PAYMENT")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@IdClass(PaymentId.class)
//public class Payment {
//
//    @Id
//    private int payNo;
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "PRO_NO")
//    private Product product;
//    private int userNo;
//    private String payMethod;
//    private int totalPrice;
//    private String payFrom;
//    private String payTo;
//    private LocalDateTime issDate;
//    private LocalDateTime payDate;
//    private LocalDateTime cancelDate;
//    private boolean isPay;
//    private boolean isCancel;
//    private String impUid;
//
//
//
//}
