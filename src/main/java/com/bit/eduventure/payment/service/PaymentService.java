//package com.bit.eduventure.payment.service;
//
//import com.bit.eduventure.payment.dto.PaymentCreateRequestDTO;
//import com.bit.eduventure.payment.dto.PaymentCreateResponseDTO;
//import com.bit.eduventure.payment.entity.Payment;
//import com.bit.eduventure.payment.entity.Product;
//import com.bit.eduventure.payment.repository.PaymentRepository;
//import com.bit.eduventure.ES1_User.Entity.User;
//import com.bit.eduventure.ES1_User.Repository.UserRepository;
//import com.siot.IamportRestClient.IamportClient;
//import com.siot.IamportRestClient.exception.IamportResponseException;
//import com.siot.IamportRestClient.response.AccessToken;
//import com.siot.IamportRestClient.response.IamportResponse;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//
//    @Value("${iamport.api-key}")
//    private String apiKey;
//    @Value("${iamport.api-secret}")
//    private String apiSecret;
//    private IamportClient api;
//
//    @PostConstruct
//    public void init() {
//        api = new IamportClient(apiKey, apiSecret);
//    }
//
//    private final PaymentRepository repository;
//    private final ProductService productService;
//    private final UserRepository userRepository;
//
//    /* 납부서 등록 - 납부서와 제품 리스트를 생성하고, 총 가격 계산 */
//    public PaymentCreateResponseDTO registerPayment(PaymentCreateRequestDTO requestDTO) {
//        List<Payment> payments = new ArrayList<>();
//
//        // 1. userEmail을 기반으로 User 엔터티 찾기
//        User user = userRepository.findByUserId(requestDTO.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found by email: " + requestDTO.getUserId()));
//
//        // 2. 찾은 User 엔터티에서 userNo를 가져와 PaymentCreateRequestDTO의 userNo에 할당.
//        requestDTO.setUserNo(user.getId());
//        requestDTO.setPayTo(user.getUserName());
//
//        System.out.println("서비스======================="+user.getId());
//
//        int payNo = Math.toIntExact(repository.findMaxPayNo());
//
//        // 여기서 proNames를 사용하여 제품 정보를 조회합니다.
//        List<Product> products = productService.findProductsByProNames(requestDTO.getProNames());
//
//        for (Product product : products) {
//            Payment payment = requestDTO.toEntity();
//            payment.setPayNo(payNo);
//            payment.setProduct(product);  // 조회한 제품 정보 할당
//            payment.setPayFrom(requestDTO.getPayFrom());
//            payments.add(payment);
//        }
//
//        repository.saveAll(payments);
//
//        int totalPrice = products.stream().mapToInt(p -> p.getProPrice()).sum();
//
//        PaymentCreateResponseDTO res = new PaymentCreateResponseDTO(payments.get(0));
//        res.setProducts(products);
//        res.setTotalPrice(totalPrice);
//
//        return res;
//    }
//
//
//    /* 납부서 조회 - 특정 userNo, 월별 */
//    public List<Payment> getPayment(int userNo, LocalDateTime issDate) {   //  1, 2023-08
//
//        return repository.findByUserNoAndIssDate(userNo, issDate);
//    }
//
//    /* 납부서 수정 */
//    public PaymentCreateResponseDTO modifyPayment(List<Payment> existingPayments, PaymentCreateRequestDTO requestDTO) {
//
//        User user = userRepository.findByUserId(requestDTO.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found by email: " + requestDTO.getUserId()));
//
//        List<Product> newProducts = productService.findProductsByProNames(requestDTO.getProNames());
//
//        // 문자열 날짜 "230821"를 LocalDateTime으로 변환
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
//        LocalDate date = LocalDate.parse(requestDTO.getIssDate(), formatter);
//        LocalDateTime updatedIssDate = date.atStartOfDay();
//
//        // 기존 Payment와 새로운 Product를 매핑하기 위한 로직
//        for (Payment payment : existingPayments) {
//            for (Product product : newProducts) {
//                if (payment.getProduct().getProName().equals(product.getProName())) {
//                    payment.setProduct(product);  // 제품 정보 업데이트
//                }
//            }
//            // 다른 필드들도 업데이트 가능 (예: payFrom, payTo 등)
//            payment.setIssDate(updatedIssDate);  // 날짜 정보 업데이트
//        }
//
//        repository.saveAll(existingPayments);  // 변경된 내용 저장
//
//        int updatedTotalPrice = newProducts.stream().mapToInt(Product::getProPrice).sum();
//
//        PaymentCreateResponseDTO responseDTO = new PaymentCreateResponseDTO(existingPayments.get(0));
//        responseDTO.setProducts(newProducts);
//        responseDTO.setTotalPrice(updatedTotalPrice);
//
//        return responseDTO;
//    }
//
//
//    /* 납부서 삭제 */
//
//    /* 영수증 조회 - 특정 userNo, 월별*/
//    public List<Payment> getReceipt(int userNo, LocalDateTime issDate) {
//        return repository.findByUserNoAndIsPayTrueAndIssDate(userNo, issDate);
//    }
//
//    /* 결제 성공 후 db 업데이트 */
//    public void updatePayment(int payNo, com.siot.IamportRestClient.response.Payment payment) {
//
//        List<Payment> payments = repository.findByPayNo(payNo);
//        if (payments.size() == 0) {
//            throw new IllegalArgumentException("Invalid payNo: " + payNo);
//        }
//
//        payments.stream().forEach(p -> {
//            p.setPayMethod(payment.getPayMethod());
//            p.setTotalPrice(payment.getAmount().intValue());
//            p.setImpUid(payment.getImpUid());
//            p.setPayDate(payment.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
//            p.setPay(true);
//            p.setPayTo(payment.getBuyerName());
//        });
//
//        repository.saveAll(payments);
//    }
//
//    /* 결제 취소 후 db 업데이트 */
//    public void refundPayment(int payNo, com.siot.IamportRestClient.response.Payment payment) {
//
//        List<Payment> payments = repository.findByPayNo(payNo);
//        if (payments.size() == 0) {
//            throw new IllegalArgumentException("Invalid payNo: " + payNo);
//        }
//
//        payments.stream().forEach(p -> {
//            p.setCancelDate(LocalDateTime.now());
//            p.setCancel(true);
//        });
//
//        repository.saveAll(payments);
//
//        //아이엠포트 결제 취소 요청
//        String accessToken = getIamportToken(); // 토큰 받아오기
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", accessToken);
//
//        Map<String, String> cancelData = new HashMap<>();
//        cancelData.put("imp_uid", payment.getImpUid());
//
//        HttpEntity<Map<String, String>> entity = new HttpEntity<>(cancelData, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity("https://api.iamport.kr/payments/cancel", entity, String.class);
//
//        if (response.getStatusCode() != HttpStatus.OK) {
//            throw new RuntimeException("Failed to cancel payment with Iamport");
//        }
//
//    }
//
//    /* 결제 취소 토큰 받아오기 */
//    private String getIamportToken() {
//        try {
//            IamportResponse<AccessToken> response = api.getAuth();
//            return response.getResponse().getToken();
//        } catch (IamportResponseException | IOException e) {
//            throw new RuntimeException("Failed to get iamport token", e);
//        }
//    }
//
//
//    public void deletePayment(int userNo, LocalDateTime dateTime) {
//        // 특정 사용자와 날짜에 해당하는 Payment 엔터티 목록 조회
//        List<Payment> paymentsToDelete = repository.findByUserNoAndIssDate(userNo, dateTime);
//
//        // 해당 Payment 엔터티가 존재하는지 검사
//        if (paymentsToDelete == null || paymentsToDelete.isEmpty()) {
//            throw new IllegalArgumentException("해당 날짜에 결제 정보가 없습니다.");
//        }
//
//        // 조회된 모든 Payment 엔터티 삭제
//        repository.deleteAll(paymentsToDelete);
//    }
//
//    public List<Payment> list() {
//        return repository.findAll();
//    }
//}
