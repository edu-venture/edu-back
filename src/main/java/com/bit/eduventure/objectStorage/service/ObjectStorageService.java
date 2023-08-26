package com.bit.eduventure.objectStorage.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;

//import com.example.finalexamplespring.vodBoard.entity.VodBoardFile;
//import com.example.finalexamplespring.vodBoard.repository.VodBoardFileRepository;
import com.bit.eduventure.vodBoard.entity.VodBoard;

import com.bit.eduventure.vodBoard.repository.VodBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObjectStorageService {
    private final VodBoardRepository vodBoardRepository;

    private final AmazonS3 s3;
    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    private String ncpAddress = "https://kr.object.ncloudstorage.com/";


    public ResponseEntity<?> getObject(String objectName) {
        try {
            S3Object o = s3.getObject(new GetObjectRequest(bucket, objectName));
            S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String fileName = URLEncoder.encode(objectName, "UTF-8").replaceAll("\\+", "%20");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        } catch (IOException e) {
            // IOException이 발생한 경우에는 오류 상태(400 Bad Request)와 오류 메시지를 포함한 응답을 클라이언트에게 반환
            return ResponseEntity.badRequest().body("Error occurred during file retrieval: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred during file retrieval: " + e.getMessage());
        }
    }

    public void deleteObject(String objectName) {
        try {
            s3.deleteObject(bucket, objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //파일 저장
    public String uploadFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        //기존값에다가 추가해서 변경하는것 StringBuilder
        StringBuilder uniqueFilename = new StringBuilder(String.valueOf(System.currentTimeMillis())); //시간으로 이름지정
        uniqueFilename.append("_"); //스트링빌더에 이름을 붙인다
        uniqueFilename.append(originalFilename); //오리지널 네임을 맨 마지막에 붙인다
        //시간값_원본명

        String saveFilename = uniqueFilename.toString(); //스트링빌더 -> toString으로 String값으로 변환

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        PutObjectRequest putObjectRequest = null;
        //공식 api 사용법
        try {
            putObjectRequest = new PutObjectRequest(bucket, saveFilename, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);;
            //버킷, 실제리소스파일, 오브젝트메타데이타

            s3.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new RuntimeException(e); //string값으로 반환을 해주면 밖에서 사용하고 있는 애는 정상적으로 저장이 된 줄 착각할 수 있다.
        }
        //파일을 오브젝트 스토리지에 저장하는 공식
        return saveFilename;
    }

    //리스트 목록 가져오기 메소드 -> 뷰 페이지에서 출력하면 됨.
//    public List<VodBoardFile> getListOfFiles(int vodBoardNo) {
//        List<VodBoardFile> fileList = vodBoardFileRepository.findAllByVodBoardNo(vodBoardNo);
//
//        return fileList;
//    }

    //상세보기
    public VodBoard getBoard(int boardNo) {
        return vodBoardRepository.findById(boardNo).get();
    }

    public List<VodBoard> getVodBoardList() {
        return vodBoardRepository.findAll();
    }

    public String setObjectSrc(String saveFileName) {
        StringBuilder storageAddress = new StringBuilder();
        storageAddress.append(ncpAddress);
        storageAddress.append(bucket);
        storageAddress.append("/");
        storageAddress.append(saveFileName);
        System.out.println("storageAddress.toString(): " + storageAddress.toString());
        return storageAddress.toString();
    }
}