package com.bit.eduventure.payment.dto;

import com.bit.eduventure.payment.entity.Payment;
import com.bit.eduventure.payment.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/* 납부서 조회할 때 응답하는 DTO */
@Data
public class PaymentGetResponseDTO {
    private int payNo;
    private int userNo;
    List<Product> products;
    private LocalDateTime issDate;
    private int issMonth;
    private String payFrom;
    private String payTo;
    private int totalPrice;

    public PaymentGetResponseDTO(List<Payment> payments) {

        // 첫 번째 Payment의 번호를 가져와서 이 DTO 각각의 필드에 설정
        this.payNo = payments.get(0).getPayNo();
        this.userNo = payments.get(0).getUserNo();
        this.issMonth = payments.get(0).getIssDate().getMonthValue();
        this.payFrom = payments.get(0).getProduct().getProInfo();
        this.payTo = payments.get(0).getPayTo();

        // Java 8의 Stream API를 사용
        // 각 Payment의 Product를 가져와 리스트로 모은 후 이를 이 DTO의 products 필드에 설정.
        this.products = payments.stream().map(Payment::getProduct).collect(Collectors.toList());
        // 각 Product의 가격을 모두 더한 후 이를 이 DTO의 totalPrice 필드에 설정.
        this.totalPrice = products.stream().mapToInt(Product::getProPrice).sum();
    }
}
