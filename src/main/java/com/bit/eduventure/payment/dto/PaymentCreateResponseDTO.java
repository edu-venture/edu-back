package com.bit.eduventure.payment.dto;

import com.bit.eduventure.payment.entity.Payment;
import com.bit.eduventure.payment.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/* 납부서 등록할 때 응답하는 DTO */
@Data
public class PaymentCreateResponseDTO {
    private int payNo;
    private int userNo;
    List<Product> products;
    private LocalDateTime issDate;
    private String payFrom;
    private String payTo;
    private int totalPrice;

    public PaymentCreateResponseDTO(Payment payment) {
        this.payNo = payment.getPayNo();
        this.userNo = payment.getUserNo();
        this.payFrom = payment.getProduct().getProInfo();
        this.payTo = payment.getPayTo();
        this.issDate = payment.getIssDate();
    }
}
