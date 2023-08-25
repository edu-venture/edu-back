package com.bit.eduventure.livestation.service;

import com.bit.eduventure.dto.ResponseDTO;
import com.bit.eduventure.livestation.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LiveStationService {
    @Value("${cloud.ncp.live.access.key}")
    String accessKey;
    @Value("${cloud.ncp.live.secret.key}")
    String secretKey;
    @Value("${cloud.ncp.live.bucket.name}")
    String bucket;

    String liveStationUrl = "https://livestation.apigw.ntruss.com/api/v2/channels";

    public String makeSignature(String timestamp, String method, String signUrl) {
        try {
            String space = " ";
            String newLine = "\n";

            String message = new StringBuilder()
                    .append(method)
                    .append(space)
                    .append(signUrl)
                    .append(newLine)
                    .append(timestamp)
                    .append(newLine)
                    .append(accessKey)
                    .toString();

            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.encodeBase64String(rawHmac);

            return encodeBase64String;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String createChannel(String name) throws JsonProcessingException, URISyntaxException {
        ResponseDTO<LiveStationInfoDTO> responseDTO = new ResponseDTO<>();
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            String url = urlBuilder.toString();
            String signUrl = url.substring(url.indexOf(".com") + 4);

            //요청 바디 만들기
            CdnDTO cdnDTO = CdnDTO.builder()
                    .createCdn(true)
                    .cdnType("CDN_PLUS")
                    .build();
            RecordDTO recordDTO = RecordDTO.builder()
                    .type("AUTO_UPLOAD")
                    .format("MP4")
                    .bucketName(bucket)
                    .filePath("/")
                    .accessControl("PRIVATE")
                    .build();
            LiveStationRequestDTO requestDTO = LiveStationRequestDTO.builder()
                    .channelName(name)
                    .cdn(cdnDTO)
                    .qualitySetId(3)
                    .useDvr(true)
                    .immediateOnAir(true)
                    .timemachineMin(360)
                    .record(recordDTO)
                    .isStreamFailOver(false)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestDTO);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "POST";
            String sig = makeSignature(timestamp, method, signUrl);

            //요청 헤더 만들기
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            ResponseEntity<LiveStationResponseDTO> response = restTemplate.exchange(new URI(url), HttpMethod.POST, body, LiveStationResponseDTO.class);


            return response.getBody().getContent().getChannelId();  // channelId 반환
    }

    public ResponseEntity<?> getChannelInfo(String channelID) {
        ResponseDTO<LiveStationInfoDTO> responseDTO = new ResponseDTO<>();
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            urlBuilder.append("/");
            urlBuilder.append(channelID);
            String url = urlBuilder.toString();

            String signUrl = url.substring(url.indexOf(".com") + 4);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "GET";
            String sig = makeSignature(timestamp, method, signUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<LiveStationResponseDTO> httpEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<LiveStationResponseDTO> response = restTemplate.exchange(new URI(url), HttpMethod.GET, httpEntity, LiveStationResponseDTO.class);

            System.out.println(response);

            //보내줄 데이터 가공
            LiveStationInfoDTO dto = LiveStationInfoDTO.builder()
                    .channelId(channelID)
                    .channelName(response.getBody().getContent().getChannelName())
                    .cdnInstanceNo(response.getBody().getContent().getCdn().getInstanceNo())
                    .cdnStatus(response.getBody().getContent().getCdn().getStatusName())
                    .publishUrl(response.getBody().getContent().getPublishUrl())
                    .streamKey(response.getBody().getContent().getStreamKey())
                    .build();

            responseDTO.setItem(dto);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public ResponseEntity<?> getServiceURL(String channelID) {
        ResponseDTO<LiveStationUrlDTO> responseDTO = new ResponseDTO<>();
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            urlBuilder.append("/");
            urlBuilder.append(channelID);
            urlBuilder.append("/serviceUrls?serviceUrlType=");
            urlBuilder.append("GENERAL");
            String url = urlBuilder.toString();

            String signUrl = url.substring(url.indexOf(".com") + 4);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "GET";
            String sig = makeSignature(timestamp, method, signUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<ServiceUrlDTO> httpEntity = new HttpEntity<>(headers);

            // HTTP 요청을 보내기 위해 RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<ServiceUrlDTO> response = restTemplate.exchange(new URI(url), HttpMethod.GET, httpEntity, ServiceUrlDTO.class);
            System.out.println(response);
            //보내줄 데이터 가공

            List<LiveStationUrlDTO> dtoList = new ArrayList<>();

            for (ContentDTO contentDTO : response.getBody().getContents()) {
                LiveStationUrlDTO dto = LiveStationUrlDTO.builder()
                        .channelId(channelID)
                        .name(contentDTO.getName())
                        .url(contentDTO.getUrl())
                        .build();
                dtoList.add(dto);
            }

            responseDTO.setItems(dtoList);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public ResponseEntity<?> deleteChannel(String channelID) {
        ResponseDTO<LiveStationInfoDTO> responseDTO = new ResponseDTO<>();
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(liveStationUrl);
            urlBuilder.append("/");
            urlBuilder.append(channelID);
            String url = urlBuilder.toString();

            String signUrl = url.substring(url.indexOf(".com") + 4);

            String timestamp = String.valueOf(System.currentTimeMillis());
            String method = "DELETE";
            String sig = makeSignature(timestamp, method, signUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", timestamp);
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", sig);
            headers.set("x-ncp-region_code", "KR");

            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            // HTTP 요청을 보내기 위해 RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<LiveStationResponseDTO> response = restTemplate.exchange(new URI(url), HttpMethod.DELETE, httpEntity, LiveStationResponseDTO.class);

            LiveStationInfoDTO dto = LiveStationInfoDTO.builder()
                    .channelId(channelID)
                    .channelName(response.getBody().getContent().getChannelName())
                    .cdnInstanceNo(response.getBody().getContent().getCdn().getInstanceNo())
                    .build();

            responseDTO.setItem(dto);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

//    public ResponseEntity<?> getRecord(String channelID) {
//        ResponseDTO<RecordVodDTO> responseDTO = new ResponseDTO<>();
//        try {
//            StringBuilder urlBuilder = new StringBuilder();
//            urlBuilder.append(liveStationUrl);
//            urlBuilder.append("/");
//            urlBuilder.append(channelID);
//            urlBuilder.append("/records");
////            urlBuilder.append("/");
////            urlBuilder.append(recordID);
//            String url = urlBuilder.toString();
//
//            String signUrl = url.substring(url.indexOf(".com") + 4);
//
//            String timestamp = String.valueOf(System.currentTimeMillis());
//            String method = "GET";
//            String sig = makeSignature(timestamp, method, signUrl);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("x-ncp-apigw-timestamp", timestamp);
//            headers.set("x-ncp-iam-access-key", accessKey);
//            headers.set("x-ncp-apigw-signature-v2", sig);
//            headers.set("x-ncp-region_code", "KR");
//
//            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<RecordResponseDTO> response = restTemplate.exchange(new URI(url), HttpMethod.GET, httpEntity, RecordResponseDTO.class);
//            RecordVodDTO dto = null;
//            System.out.println(response.getBody().getContentDTO().getRecordList());
//            for(String key : response.getBody().getContentDTO().getRecordList().keySet()){
//                RecordInfoDTO recordInfoDTO = response.getBody().getContentDTO().getRecordList().get(key);
//                if (recordInfoDTO.getRecordType().equals("MP4")) {
//                    dto = RecordVodDTO.builder()
//                            .channelId(recordInfoDTO.getChannelId())
//                            .fileName(recordInfoDTO.getFileName())
//                            .uploadPath(recordInfoDTO.getUploadPath())
//                            .recordType(recordInfoDTO.getRecordType())
//                            .build();
//                }
//            }
//
//            System.out.println(dto.toString());
////            RecordVodDTO dto = RecordVodDTO.builder()
////                    .
////                    .build();
//
//
//            responseDTO.setStatusCode(HttpStatus.OK.value());
////            responseDTO.setItem(dto);
//
//            return ResponseEntity.ok().body(responseDTO);
//        } catch (Exception e) {
//            responseDTO.setErrorMessage(e.getMessage());
//            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//
//            return ResponseEntity.badRequest().body(responseDTO);
//
//        }
//    }
}
