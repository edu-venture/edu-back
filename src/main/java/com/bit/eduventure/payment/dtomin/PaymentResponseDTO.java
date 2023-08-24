//package com.bit.eduventure.payment.dtomin;
//
//import com.bit.eduventure.payment.entity.Product;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
////납부서 작성시 돌려줄 DTO
//public class PaymentResponseDTO {
//    private int payNo;  //디비에 저장된 납부서 번호
//    private int userNo; //학생 번호
//    List<Product> products; //상품명 리스트
//    private LocalDateTime issDate; //납부서 작성일
//    private int issMonth; //결제한 월
//    private String payFrom; //학원 이름
//    private String payTo;   //학생 이름
//    private int totalPrice; //총가격
//}
