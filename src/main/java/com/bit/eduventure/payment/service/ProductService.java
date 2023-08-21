package com.bit.eduventure.payment.service;

import com.bit.eduventure.payment.entity.Product;
import com.bit.eduventure.payment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;

    /* 납부서 등록할 때 상품명 불러오기 */

    public List<Product> findProductsByProNames(List<String> proNames) {
        return productRepository.findByProNameIn(proNames);
    }
}