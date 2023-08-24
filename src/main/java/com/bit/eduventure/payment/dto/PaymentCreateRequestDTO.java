//package com.bit.eduventure.payment.dto;
//
//import com.bit.eduventure.payment.entity.Payment;
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
///* 납부서 등록할 때 요청 받는 DTO */
//@Data
//public class PaymentCreateRequestDTO {
//    private int userNo;
//    private String userId;
//    private List<Integer>proNos;
//    private List<String> proNames;
//    private String payFrom;
//    private String payTo;
//    private String issDate;
//
//
//    public Payment toEntity() {
//
//        // 문자열 날짜 "230821"를 LocalDateTime으로 변환
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
//        LocalDate date = LocalDate.parse(this.issDate, formatter);
//        LocalDateTime formattedDate = date.atStartOfDay();
//
//        return Payment.builder()
//                .userNo(this.userNo)
//                .isPay(false)
//                .isCancel(false)
//                .issDate(formattedDate)
//                .payFrom(this.payFrom)
//                .payTo(this.payTo)
//                .build();
//    }
//
//}
