//package com.bit.eduventure.payment.dto;
//
//import com.bit.eduventure.payment.entity.Payment;
//import com.bit.eduventure.payment.entity.Product;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Builder;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Data
//@Builder
//public class PaymentDTO {
//    private int payNo;
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
//    public Payment DTOTOEntity() {
//        Payment payment = Payment.builder()
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
//        return payment;
//    }
//}
