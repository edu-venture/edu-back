package com.bit.eduventure.payment.controller;

import com.bit.eduventure.payment.dto.*;
import com.bit.eduventure.payment.entity.Payment;
import com.bit.eduventure.payment.service.PaymentService;
import com.bit.eduventure.ES1_User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    /* 납부서 등록 */
    @PostMapping("/bill")
    //클라이언트로부터 받은 HTTP POST 요청의 body 부분을 PaymentCreateRequestDTO 타입의 객체로 변환하고, 이를 requestDTO라는 매개변수로 전달
    public ResponseEntity<?> createPayment(@RequestBody PaymentCreateRequestDTO requestDTO) {

        // 클라이언트에게 전달할 최종 응답 객체 생성
        ResponseDTO<PaymentCreateResponseDTO> response = new ResponseDTO<>();

        try {
            PaymentCreateResponseDTO res = paymentService.registerPayment(requestDTO);  // 서비스 메서드 호출
            response.setItem(res); // 응답 DTO 설정
            response.setStatusCode(HttpStatus.CREATED.value()); // 상태 코드 설정
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 성공적인 응답 반환
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage()); // 에러 메시지 설정
            response.setStatusCode(HttpStatus.BAD_REQUEST.value()); // 상태 코드 설정
            return ResponseEntity.badRequest().body(response); // 에러 발생시 응답 반환
        }
    }

    /* 납부서 조회 */
    @GetMapping("/{userNo}/bill/{issDate}")
    public ResponseEntity<?> getPayment(@PathVariable int userNo, @PathVariable String issDate) {
        System.out.println(userNo);
        System.out.println(issDate);
        // 클라이언트에게 전달할 최종 응답 객체 생성
        ResponseDTO<PaymentGetResponseDTO> response = new ResponseDTO<>();

        try {
            LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(issDate.substring(0, 4)), Integer.parseInt(issDate.substring(4, 6)), 1, 0, 0);

            //payNo에 해당하는 모든 Payment 객체들의 목록을 가져오기, payments라는 List<Payment> 타입의 변수에 저장
            List<Payment> payments = paymentService.getPayment(userNo, dateTime);

            //payments 리스트가 null이거나 비어 있으면, 예외처리
            if (payments == null || payments.isEmpty()) {
                throw new IllegalArgumentException("결제 정보가 없습니다.");
            }

            //PaymentGetResponseDTO의 생성자에 payments를 전달
            PaymentGetResponseDTO responseDTO = new PaymentGetResponseDTO(payments);

            // 유저 이름 가져오기
            String userName = userService.getUserNo(responseDTO.getUserNo());
            responseDTO.setPayTo(userName);

            response.setItem(responseDTO);
            response.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /* 납부서 수정 */
    @PutMapping("/{userNo}/bill/{issDate}")
    public ResponseEntity<?> updatePayment(@PathVariable int userNo, @PathVariable String issDate, @RequestBody PaymentCreateRequestDTO requestDTO) {
        // 클라이언트에게 전달할 최종응답 객체 생성
        ResponseDTO<PaymentCreateResponseDTO> response = new ResponseDTO<>();

        try {
            LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(issDate.substring(0, 4)), Integer.parseInt(issDate.substring(4, 6)), 1, 0, 0);

            // 기존의 Payment 정보 가져오기
            List<Payment> existingPayments = paymentService.getPayment(userNo, dateTime);

            if (existingPayments == null || existingPayments.isEmpty()) {
                throw new IllegalArgumentException("해당 결제 정보가 없습니다.");
            }

            // 납부서 정보 수정 로직 (서비스 메서드 호출)
            PaymentCreateResponseDTO res = paymentService.modifyPayment(existingPayments, requestDTO);
            response.setItem(res); // 응답 DTO 설정
            response.setStatusCode(HttpStatus.OK.value()); // 상태 코드 설정
            return ResponseEntity.ok().body(response); // 성공적인 응답 반환
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage()); // 에러 메시지 설정
            response.setStatusCode(HttpStatus.BAD_REQUEST.value()); // 상태 코드 설정
            return ResponseEntity.badRequest().body(response); // 에러 발생시 응답 반환
        }
    }


    /* 납부서 삭제 */
    @DeleteMapping("/{userNo}/bill/{issDate}")
    public ResponseEntity<?> deletePayment(@PathVariable int userNo, @PathVariable String issDate) {
        ResponseDTO<String> response = new ResponseDTO<>();

        try {
            LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(issDate.substring(0, 4)), Integer.parseInt(issDate.substring(4, 6)), 1, 0, 0);

            paymentService.deletePayment(userNo, dateTime);

            response.setStatusCode(HttpStatus.OK.value());
            response.setItem("납부서 삭제 성공");
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            response.setErrorMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /* 영수증 조회 */
    @GetMapping("/{userNo}/receipt/{issDate}")
    public ResponseEntity<?> getReceipt(@PathVariable int userNo, @PathVariable String issDate) {

        // 클라이언트에게 전달할 최종 응답 객체 생성
        ResponseDTO<ReceiptGetResponseDTO> response = new ResponseDTO<>();

        try {
            LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(issDate.substring(0, 4)), Integer.parseInt(issDate.substring(4, 6)), 1, 0, 0);

            //payNo에 해당하는 모든 Payment 객체들의 목록을 가져오기, payments라는 List<Payment> 타입의 변수에 저장
            List<Payment> payments = paymentService.getReceipt(userNo, dateTime);

            //payments 리스트가 null이거나 비어 있으면, 예외처리
            if (ListUtils.isEmpty(payments)) {
                throw new IllegalArgumentException("결제 정보가 없습니다.");
            }

            //PaymentGetResponseDTO의 생성자에 payments를 전달
            ReceiptGetResponseDTO responseDTO = new ReceiptGetResponseDTO(payments);

            // 유저 이름 가져오기
            String userName = userService.getUserNo(responseDTO.getUserNo());
            responseDTO.setPayTo(userName);

            response.setItem(responseDTO);
            response.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list-list")
    public ResponseEntity<?> getReceiptList() {
        ResponseDTO<ReceiptListResponseDTO> response = new ResponseDTO<>();
        try {
            List<ReceiptListResponseDTO> returnList = new ArrayList<>();
            List<Payment> paymentList = paymentService.list();
            for (Payment payment : paymentList) {

                //부모 아이디 찾아서 넘겨주기
                int parenId = userService.findById(payment.getUserNo()).getUserJoinId();
                String parentTel = userService.findById(parenId).getUserTel();

                ReceiptListResponseDTO dto = ReceiptListResponseDTO.builder()
                        .payNo(payment.getPayNo())
                        .userNo(payment.getUserNo())
                        .products(payment.getProduct())
                        .payDate(payment.getPayDate())
                        .payFrom(payment.getPayFrom())
                        .payTo(payment.getPayTo())
                        .payMethod(payment.getPayMethod())
                        .isPay(payment.isPay())
                        .totalPrice(payment.getTotalPrice())
                        .parentTel(parentTel)
                        .build();
                returnList.add(dto);
            }
            response.setItems(returnList);
            response.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setErrorMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.badRequest().body(response);
        }


    }
}
