//package com.bit.eduventure.common;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//
//import com.bit.eduventure.ES7_Board.Entity.BoardFile;
//import com.bit.eduventure.ES8_Quiz.QuizBoardFile;
//import com.bit.eduventure.configuration.NaverConfiguration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//@Component
//public class FileUtilsForObjectStorage {
//    private final AmazonS3 s3;
//    //    기본형식
////    s3 = AmazonS3ClientBuilder.standard().withEndpointConfiguration().withCredentials().build();
//    public FileUtilsForObjectStorage(NaverConfiguration naverConfiguration) {
//        s3 = AmazonS3ClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(naverConfiguration.getEndPoint(), naverConfiguration.getRegionName())).withCredentials(new AWSStaticCredentialsProvider(
//
//                        new BasicAWSCredentials(naverConfiguration.getAccessKey(), naverConfiguration.getSecretKey())
//                )).build();
//    }
//    // MultipartFile 객체를 받아서 DTO형태로 변경 후 리턴
//    public QuizBoardFile parseFileInfo(MultipartFile file, String directoryPath) throws IOException {
//        String bucketName = "bitcamp-bucket-142";
//        // 리턴할 BoardDTO 객체 생성
//        QuizBoardFile quizBoardFile = new QuizBoardFile();
//        String boardFileOrigin = file.getOriginalFilename();
//        // 같은 파일명 파일이 올라오면 나중에 업로드 된 파일로 덮어써지기 때문에
//        // 파일명을 날짜_랜덤_...
//        SimpleDateFormat formmater = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date nowDate = new Date();
//        String nowDateStr = formmater.format(nowDate);
//        UUID uuid = UUID.randomUUID();
//        // 실제 db에 저장될 파일명
//        String boardFileName = nowDateStr + "_" + uuid.toString() + "_" + boardFileOrigin;
//        String boardFilePath = directoryPath;
//        // 이미지인지 아닌지 검사
//        File checkFile = new File(boardFileOrigin);
//        // 파일의 형식 가져오기
//        String type = Files.probeContentType(checkFile.toPath());
//
//
//        if (type != null) {
//            String category = type.split("/")[0];
//            quizBoardFile.setBoardFileCate(category);
//        } else {
//            quizBoardFile.setBoardFileCate("NoAttachedFile");
//        }
//        // 파일 업로드 처리
//        // 리턴될 DTO 셋팅
//        quizBoardFile.setBoardFileName(boardFileName);
//        quizBoardFile.setBoardFileOrigin(boardFileOrigin);
//        quizBoardFile.setBoardFilePath(boardFilePath);
////try 구문 안에서만 사용할 객체나 변수를 선언할 수 있다
//        //주로 사용후에 close해줘야되는 객체들을 선언한다.
//        try(InputStream fileInputStream = file.getInputStream()) {
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentType(file.getContentType());
//            PutObjectRequest putObjectRequest = new PutObjectRequest(
//                    bucketName, directoryPath + boardFileName, fileInputStream, objectMetadata
//            ).withCannedAcl(CannedAccessControlList.PublicRead);
//            s3.putObject(putObjectRequest);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return quizBoardFile;
//    }
//}
