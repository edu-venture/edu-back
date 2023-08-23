package com.bit.eduventure.ES1_User.Service;

import com.bit.eduventure.ES1_User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User idCheck(String userId);

    User join(User user);

    User joinforgivingjoinidforparent(User user);

    User update(User user);

    User findById(int id);

    User findByUserId(String userId);

    void modify(User modifyUser);

    User login(String userId, String userPw);

    Page<User> getUserList(Pageable pageable, String searchCondition, String searchKeyword);


    void deleteUser(int id);

    /* 납부서 조회할 때 쓰는 거 */
    public String getUserNo(int userNo);

    //권한에 맞는 유저 리스트 찾기
    List<User> getTeacherList(String userType);

}
