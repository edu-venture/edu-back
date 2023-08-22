package com.bit.eduventure.payment.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class PaymentId implements Serializable {

    private int payNo;
    private int product;

}
