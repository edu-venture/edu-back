package com.bit.eduventure.payment.service;

import com.bit.eduventure.payment.entity.Receipt;
import com.bit.eduventure.payment.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public Receipt saveReceipt(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public List<Receipt> getReceiptPayId(int payId) {
        return receiptRepository.findAllByPaymentId(payId);
    }
}
