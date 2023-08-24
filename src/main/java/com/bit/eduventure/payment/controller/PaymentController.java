package com.bit.eduventure.payment.controller;

import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES1_User.Entity.User;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.payment.dto.PaymentDTO;
import com.bit.eduventure.payment.dto.PaymentRequestDTO;
import com.bit.eduventure.payment.dto.PaymentResponseDTO;
import com.bit.eduventure.payment.dto.ReceiptDTO;
import com.bit.eduventure.payment.entity.Payment;
import com.bit.eduventure.payment.entity.Receipt;
import com.bit.eduventure.payment.service.PaymentService;
import com.bit.eduventure.payment.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ReceiptService receiptService;
    private final UserService userService;

    private String[] issDateArray = {"year", "month", "day"};

    //납부서 등록
    @PostMapping("/admin/bill")
    //클라이언트로부터 받은 HTTP POST 요청의 body 부분을 PaymentCreateRequestDTO 타입의 객체로 변환하고, 이를 requestDTO라는 매개변수로 전달
    public ResponseEntity<?> createPayment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @RequestBody PaymentRequestDTO requestDTO) {
        ResponseDTO<PaymentResponseDTO> responseDTO = new ResponseDTO<>();
        List<PaymentResponseDTO> returnList = new ArrayList<>();

        try {
            int userNo = Integer.parseInt(customUserDetails.getUsername());
            requestDTO.setUserNo(userNo);

            PaymentDTO paymentDTO = paymentService.createPayment(requestDTO).EntityTODTO();  // 서비스 메서드 호출
            User user = userService.findById(paymentDTO.getPayTo());

            PaymentResponseDTO paymentResponseDTO = PaymentResponseDTO.builder()
                    .userName(user.getUserName())
                    .couNo(user.getCourse().getCouNo())
                    .build();

            returnList.add(paymentResponseDTO);

            responseDTO.setItems(returnList); // 응답 DTO 설정
            responseDTO.setStatusCode(HttpStatus.OK.value()); // 상태 코드 설정

            System.out.println("responseDTO: " + responseDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage()); // 에러 메시지 설정
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value()); // 상태 코드 설정

            return ResponseEntity.badRequest().body(responseDTO); // 에러 발생시 응답 반환
        }
    }

    //납부서 상세 조회(학생)
    @GetMapping("/student/bill")
    public ResponseEntity<?> getPayment(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<PaymentResponseDTO> responseDTO = new ResponseDTO<>();
        try {
            //요청을 보낸 유저 데이터 확인하기
            int userNo = Integer.parseInt(customUserDetails.getUsername());
            LocalDateTime now = LocalDateTime.now();
            Month month = now.getMonth();

            //결제 정보 가져오기
            PaymentDTO paymentDTO = paymentService.getPayment(userNo, month.getValue()).EntityTODTO();

            //결제에 맞는 상품 리스트 가져와서 DTO리스트로 변환
            List<Receipt> receiptList = receiptService.getReceiptPayId(paymentDTO.getPayNo());

            List<ReceiptDTO> receiptDTOList = receiptList.stream()
                    .map(Receipt::EntityTODTO)
                    .collect(Collectors.toList());

            //리턴할 데이터 가공
            PaymentResponseDTO paymentResponseDTO = PaymentResponseDTO.builder()
                    .userNo(paymentDTO.getUserNo())
                    .payFrom(paymentDTO.getPayFrom())
                    .totalPrice(paymentDTO.getTotalPrice())
                    .userName(userService.findById(paymentDTO.getPayTo()).getUserName())
                    .issDay(paymentService.issDateMonth(paymentDTO.getIssDate().toString(), issDateArray[2]))
                    .issMonth(paymentService.issDateMonth(paymentDTO.getIssDate().toString(), issDateArray[1]))
                    .issYear(paymentService.issDateMonth(paymentDTO.getIssDate().toString(), issDateArray[0]))
                    .productList(receiptDTOList)
                    .isPay(paymentDTO.isPay())
                    .build();

            responseDTO.setItem(paymentResponseDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //납부서 리스트 보기 (학생)
    @GetMapping("/student/bill-list")
    public ResponseEntity<?> getPaymentList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<PaymentResponseDTO> responseDTO = new ResponseDTO<>();
        List<PaymentResponseDTO> returnList =new ArrayList<>();

        try {
            int userNo = Integer.parseInt(customUserDetails.getUsername());
            User user = userService.findById(userNo);
            User parentUser = userService.findById(user.getUserJoinId());
            List<Payment> paymentList = paymentService.getPaymentList(userNo);

            for (Payment payment : paymentList) {

                List<ReceiptDTO> receiptDTOList = new ArrayList<>();
                List<Receipt> receiptList = receiptService.getReceiptPayId(payment.getPayNo());

                receiptDTOList = receiptList.stream()
                        .map(receipt -> receipt.EntityTODTO())
                        .collect(Collectors.toList());

                PaymentResponseDTO paymentResponseDTO = PaymentResponseDTO.builder()
                        .payNo(payment.getPayNo())
                        .userName(user.getUserName())
                        .couNo(user.getCourse().getCouNo())
                        .claName(user.getCourse().getClaName())
                        .issDay(paymentService.issDateMonth(payment.getIssDate().toString(), issDateArray[2]))
                        .issMonth(paymentService.issDateMonth(payment.getIssDate().toString(), issDateArray[1]))
                        .issYear(paymentService.issDateMonth(payment.getIssDate().toString(), issDateArray[0]))
                        .totalPrice(payment.getTotalPrice())
                        .parentTel(parentUser.getUserTel())
                        .payMethod(payment.getPayMethod())
                        .isPay(payment.isPay())
                        .payFrom(payment.getPayFrom())
                        .productList(receiptDTOList)
                        .build();
                returnList.add(paymentResponseDTO);
            }

            responseDTO.setItems(returnList);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    //납부서 리스트
    @GetMapping("/admin/bill-list")
    public ResponseEntity<?> getPaymentList() {
        ResponseDTO<PaymentResponseDTO> responseDTO = new ResponseDTO<>();
        List<PaymentResponseDTO> returnList =new ArrayList<>();

        try {
            List<Payment> paymentList = paymentService.getPaymentList();

            for (Payment payment : paymentList) {

                User user = userService.findById(payment.getPayTo());
                User parentUser = userService.findById(user.getUserJoinId());

                List<Receipt> receiptList = receiptService.getReceiptPayId(payment.getPayNo());

                List<ReceiptDTO> receiptDTOList = receiptList.stream()
                        .map(Receipt::EntityTODTO)
                        .collect(Collectors.toList());

                PaymentResponseDTO paymentResponseDTO = PaymentResponseDTO.builder()
                        .payNo(payment.getPayNo())
                        .userName(user.getUserName())
                        .couNo(user.getCourse().getCouNo())
                        .claName(user.getCourse().getClaName())
                        .issDay(paymentService.issDateMonth(payment.getIssDate().toString(), issDateArray[2]))
                        .issMonth(paymentService.issDateMonth(payment.getIssDate().toString(), issDateArray[1]))
                        .issYear(paymentService.issDateMonth(payment.getIssDate().toString(), issDateArray[0]))
                        .totalPrice(payment.getTotalPrice())
                        .parentTel(parentUser.getUserTel())
                        .payMethod(payment.getPayMethod())
                        .isPay(payment.isPay())
                        .payFrom(payment.getPayFrom())
                        .productList(receiptDTOList)
                        .build();
                returnList.add(paymentResponseDTO);
            }

            responseDTO.setItems(returnList);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //납부서 삭제
//    @DeleteMapping("/admin/bill/{payNo}")
//    public ResponseEntity<?> deletePayment(@PathVariable int payNo) {
//        ResponseDTO<String> response = new ResponseDTO<>();
//        try {
//
//            paymentService.deletePayment(userNo, dateTime);
//
//            response.setItem("납부서 삭제 성공");
//            response.setStatusCode(HttpStatus.OK.value());
//
//            return ResponseEntity.ok().body(response);
//
//        } catch (Exception e) {
//            response.setErrorMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    /* 납부서 수정 */
//    @PutMapping("/{userNo}/bill/{issDate}")
//    public ResponseEntity<?> updatePayment(@PathVariable int userNo, @PathVariable String issDate, @RequestBody PaymentCreateRequestDTO requestDTO) {
//        // 클라이언트에게 전달할 최종응답 객체 생성
//        ResponseDTO<PaymentCreateResponseDTO> response = new ResponseDTO<>();
//
//        try {
//            LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(issDate.substring(0, 4)), Integer.parseInt(issDate.substring(4, 6)), 1, 0, 0);
//
//            // 기존의 Payment 정보 가져오기
//            List<Payment> existingPayments = paymentService.getPayment(userNo, dateTime);
//
//            if (existingPayments == null || existingPayments.isEmpty()) {
//                throw new IllegalArgumentException("해당 결제 정보가 없습니다.");
//            }
//
//            // 납부서 정보 수정 로직 (서비스 메서드 호출)
//            PaymentCreateResponseDTO res = paymentService.modifyPayment(existingPayments, requestDTO);
//            response.setItem(res); // 응답 DTO 설정
//            response.setStatusCode(HttpStatus.OK.value()); // 상태 코드 설정
//            return ResponseEntity.ok().body(response); // 성공적인 응답 반환
//        } catch (Exception e) {
//            response.setErrorMessage(e.getMessage()); // 에러 메시지 설정
//            response.setStatusCode(HttpStatus.BAD_REQUEST.value()); // 상태 코드 설정
//            return ResponseEntity.badRequest().body(response); // 에러 발생시 응답 반환
//        }
//    }
//
//    /* 영수증 조회 */
//    @GetMapping("/{userNo}/receipt/{issDate}")
//    public ResponseEntity<?> getReceipt(@PathVariable int userNo, @PathVariable String issDate) {
//
//        // 클라이언트에게 전달할 최종 응답 객체 생성
//        ResponseDTO<ReceiptGetResponseDTO> response = new ResponseDTO<>();
//
//        try {
//            LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(issDate.substring(0, 4)), Integer.parseInt(issDate.substring(4, 6)), 1, 0, 0);
//
//            //payNo에 해당하는 모든 Payment 객체들의 목록을 가져오기, payments라는 List<Payment> 타입의 변수에 저장
//            List<Payment> payments = paymentService.getReceipt(userNo, dateTime);
//
//            //payments 리스트가 null이거나 비어 있으면, 예외처리
//            if (ListUtils.isEmpty(payments)) {
//                throw new IllegalArgumentException("결제 정보가 없습니다.");
//            }
//
//            //PaymentGetResponseDTO의 생성자에 payments를 전달
//            ReceiptGetResponseDTO responseDTO = new ReceiptGetResponseDTO(payments);
//
//            // 유저 이름 가져오기
//            String userName = userService.getUserNo(responseDTO.getUserNo());
//            responseDTO.setPayTo(userName);
//
//            response.setItem(responseDTO);
//            response.setStatusCode(HttpStatus.OK.value());
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            response.setErrorMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @GetMapping("/list-list")
//    public ResponseEntity<?> getReceiptList() {
//        ResponseDTO<ReceiptListResponseDTO> response = new ResponseDTO<>();
//        try {
//            List<ReceiptListResponseDTO> returnList = new ArrayList<>();
//            List<Payment> paymentList = paymentService.list();
//            for (Payment payment : paymentList) {
//
//                //부모 아이디 찾아서 넘겨주기
//                int parenId = userService.findById(payment.getUserNo()).getUserJoinId();
//                String parentTel = userService.findById(parenId).getUserTel();
//
//                ReceiptListResponseDTO dto = ReceiptListResponseDTO.builder()
//                        .payNo(payment.getPayNo())
//                        .userNo(payment.getUserNo())
//                        .products(payment.getProduct())
//                        .payDate(payment.getPayDate())
//                        .payFrom(payment.getPayFrom())
//                        .payTo(payment.getPayTo())
//                        .payMethod(payment.getPayMethod())
//                        .isPay(payment.isPay())
//                        .totalPrice(payment.getTotalPrice())
//                        .parentTel(parentTel)
//                        .build();
//                returnList.add(dto);
//            }
//            response.setItems(returnList);
//            response.setStatusCode(HttpStatus.OK.value());
//
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            response.setErrorMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            return ResponseEntity.badRequest().body(response);
//        }
//
//
//    }
}
