package com.bit.eduventure.payment.dto;

import com.bit.eduventure.payment.entity.Payment;
import com.bit.eduventure.payment.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/* 영수증 조회할 때 응답하는 DTO */
@Data
public class ReceiptGetResponseDTO {
    private int payNo;
    private int userNo;
    List<Product> products;
    private LocalDateTime payDate;
    private int payYear;
    private int payMonth;
    private String payFrom;
    private String payTo;
    private String payMethod;
    private boolean isPay;
    private int totalPrice;

    public ReceiptGetResponseDTO(List<Payment> payments) {

        // 첫 번째 Payment의 번호를 가져와서 이 DTO 각각의 필드에 설정
        this.payNo = payments.get(0).getPayNo();
        this.userNo = payments.get(0).getUserNo();
        this.payDate = payments.get(0).getPayDate();
        this.payMonth = payments.get(0).getPayDate().getMonthValue();
        this.payYear = payments.get(0).getPayDate().getYear();
        this.payFrom = payments.get(0).getProduct().getProInfo();
        this.payTo = payments.get(0).getPayTo();
        this.payMethod = payments.get(0).getPayMethod();

        // Java 8의 Stream API를 사용
        // 각 Payment의 Product를 가져와 리스트로 모은 후 이를 이 DTO의 products 필드에 설정.
        this.products = payments.stream().map(Payment::getProduct).collect(Collectors.toList());
        // 각 Product의 가격을 모두 더한 후 이를 이 DTO의 totalPrice 필드에 설정.
        this.totalPrice = products.stream().mapToInt(Product::getProPrice).sum();
    }
}
