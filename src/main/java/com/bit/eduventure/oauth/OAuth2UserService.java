//package com.bit.eduventure.oauth;
//
//
//import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
//import com.bit.eduventure.ES1_User.Entity.User;
//import com.bit.eduventure.ES1_User.Repository.UserRepository;
//import com.bit.eduventure.oauth.provider.KakaoUserInfo;
//import com.bit.eduventure.oauth.provider.OAuth2UserInfo;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class OAuth2UserService extends DefaultOAuth2UserService {
//
//
//    private PasswordEncoder passwordEncoder;
//
//    private UserRepository userRepository;
//
//    public OAuth2UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
//
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//
//
//    }
//
//    //소셜 로그인 버튼 클릭 -> 사용자 동의 -> 로그인 완료 -> code값 리턴
//    //->토큰 발행 및  수령 ->토큰을 통해 사용자 정보 취득 -> loaduser메소드 자동 호출
//    //loadUser메소드만 구현하면 된다. loadUser에서 처리할 내용은 취득한 사용자정보 커스터마이징
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        String userName = "";
//        String providerId = "";
//
//        OAuth2UserInfo oAuth2UserInfo = null;
//        //소셜 카테고리 검증
//
//        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
//            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
//            providerId = oAuth2UserInfo.getProviderId();
//            userName = oAuth2UserInfo.getName();
//
//        } else {
//            System.out.println("카카오 로그인만 지원합니다.");
//
//        }
//        String provider = oAuth2UserInfo.getProvider();
//        //kakao_123456
//        String userId = provider + "_" + providerId;
//        //소셜 로그인은 비밀번호가 필요하지 않기 때문에 NIckname값을 암호화해서 넣는다.
//        String password = passwordEncoder.encode(oAuth2UserInfo.getName());
//        String email = oAuth2UserInfo.getEmail();
//        String role = "ROLE_USER";
//        //소셜 로그인 기록이 있는지 검사할 객체
//        User user;
//
//        if(userRepository.findByUserId(userId).isPresent()){
//            //이미 소셜로그인한 기록이 있으면
//            //정보를 user엔티티에 담아준다.
//            user=userRepository.findByUserId(userId).get();
//
//
//        }else{
//
//            user = null;
//
//        }
//
//        //소셜 로그인 기록이 없으면 회원가입 처리
//        if(user==null){
//            user = User.builder().userId(userId).userPw(password).userName(userName).userRegdate(LocalDateTime.now()).role(role).build();
//            userRepository.save(user);
//
//
//        }
//
//        //security Context에 인증 정보 저장
//        return CustomUserDetails.builder(). user(user).attributes(oAuth2User.getAttributes()).build();
//
//
//
//
//
//    }
//
//
//}
