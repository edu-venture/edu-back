//package com.bit.eduventure.payment.entity;
//
//import com.bit.eduventure.payment.dto.PaymentDTO;
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
////@IdClass(PaymentId.class)
//public class Payment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private int payNo;
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
//    @ManyToOne
//    @JoinColumn(name = "PRO_NO")
//    private Product product;
//
//
//    public PaymentDTO EntityTODTO() {
//        PaymentDTO paymentDTO = PaymentDTO.builder()
//                .payNo(this.payNo)
//                .product(this.product)
//                .userNo(this.userNo)
//                .payMethod(this.payMethod)
//                .totalPrice(this.totalPrice)
//                .payFrom(this.payFrom)
//                .payTo(this.payTo)
//                .issDate(this.issDate)
//                .payDate(this.payDate)
//                .cancelDate(this.cancelDate)
//                .isPay(this.isPay)
//                .isCancel(this.isCancel)
//                .impUid(this.impUid)
//                .build();
//        return paymentDTO;
//    }
//
//
//}
