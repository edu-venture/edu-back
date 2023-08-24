package com.bit.eduventure.payment.service;

import com.bit.eduventure.payment.dto.PaymentRequestDTO;
import com.bit.eduventure.payment.entity.Payment;
import com.bit.eduventure.payment.entity.Receipt;
import com.bit.eduventure.payment.repository.PaymentRepository;
import com.bit.eduventure.payment.repository.ReceiptRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${iamport.api-key}")
    private String apiKey;
    @Value("${iamport.api-secret}")
    private String apiSecret;
    private IamportClient api;

    @PostConstruct
    public void init() {
        api = new IamportClient(apiKey, apiSecret);
    }

    private final PaymentRepository paymentRepository;
    private final ReceiptService receiptService;

    // 납부서 등록
    public Payment createPayment(PaymentRequestDTO requestDTO) {

        //디비에 저장할 형태 만들기
        Payment payment = Payment.builder()
                .userNo(requestDTO.getUserNo())
                .totalPrice(0)
                .payFrom("에듀벤처")
                .payTo(requestDTO.getPayTo())
                .createDate(LocalDateTime.now())
                .issDate(stringToLocalDateTime(requestDTO.getIssDate()))
                .isPay(false)
                .build();

        //영수증에 저장할 결제PK
        int payNo = savePayment(payment).getPayNo();

        Map<String, Integer> productList = jsonTOProductMap(requestDTO.getProductList());

        //상품과 가격 디비에 저장하면서 총합 구하기
        int totalPrice = productList.entrySet().stream()
                .map(entry -> {
                    Receipt receipt = Receipt.builder()
                            .paymentId(payNo)
                            .productName(entry.getKey())
                            .productPrice(entry.getValue())
                            .build();
                    receiptService.saveReceipt(receipt);
                    return entry.getValue();
                })
                .mapToInt(Integer::intValue)
                .sum();

        //상품의 총합을 구해서
        payment.setTotalPrice(totalPrice);

        //디비에 다시 저장
        return savePayment(payment);
    }

    //학생 개별 납부서 조회
    public Payment getPayment(int userNo, int month) {
        return paymentRepository.findByPayToAndIssDateMonth(userNo, month).get(0);
    }

    //학생 납부서 전체 조회
    public List<Payment> getPaymentList(int userNo) {
        return paymentRepository.findAllByPayTo(userNo);
    }

    //납부서 전체 조회
    public List<Payment> getPaymentList() {
        return paymentRepository.findAll();
    }

    public void deletePayment(int payNo) {
        paymentRepository.deleteById(payNo);
    }

    //년, 월, 일만 추출하기
    public String issDateMonth(String inputDate, String type) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);

        String result = "";

        if ("year".equals(type)) {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy");
            result = dateTime.format(outputFormatter);
        } else if ("month".equals(type)) {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM");
            result = dateTime.format(outputFormatter);
        } else if ("day".equals(type)) {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM");
            result = dateTime.format(outputFormatter);
        }

        return result;
    }

    //결제일 (문자열) -> LocalDateTime로 변환
    public LocalDateTime stringToLocalDateTime(String inputDate) {
        Instant instant = Instant.parse(inputDate);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDate = localDateTime.toString();

        LocalDateTime returnData = LocalDateTime.parse(formattedDate, formatter);

        System.out.println("localDateTime: " + returnData);
        return returnData;
    }

    //페이먼트 저장
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    //상품 정보와 가격 추출
    public Map<String, Integer> jsonTOProductMap(String productList) {
        Map<String, Integer> returnMap = new HashMap<>();

        JsonArray jsonArray = JsonParser.parseString(productList).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String detail = jsonObject.get("detail").getAsString();
            int price = jsonObject.get("price").getAsInt();

            returnMap.put(detail, price);
        }
        return returnMap;
    }
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


}
