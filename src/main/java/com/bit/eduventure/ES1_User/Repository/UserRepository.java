package com.bit.eduventure.ES1_User.Repository;

import com.bit.eduventure.ES1_User.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//update나 Delete가 발생했을 때 곧장 커밋 롤백 처리



@Transactional
public interface UserRepository extends JpaRepository<User,Integer> {
    //select * from t_user

    //where user_id =:userId


    Optional<User> findByUserId(String userId);
    Optional<User> findById(int id);
@Modifying
    @Query( value = "update t_user set user_name = :userName", nativeQuery = true)
    public void updateUser(@Param("userName") String userName);


    boolean existsByUserId(String userId);

    Page<User> findByUserNameContaining(String searchKeyword, Pageable pageable);

    Page<User> findByUserIdContaining(String searchKeyword, Pageable pageable);

//    @Query(value = "SELECT * FROM t_user WHERE CONCAT(user_no, '') LIKE :userNoKeyword", nativeQuery = true)
//    Page<User>findById(@Param("userNoKeyword") String userNoKeyword, Pageable pageable);


    Page<User> findById(Integer usernoKeyword, Pageable pageable);

    Page<User> findByUserTypeContaining(String searchKeyword, Pageable pageable);

    Page<User> findByUserBus(Integer busKeyword, Pageable pageable);

    Page<User> findByUserTelContaining(String searchKeyword, Pageable pageable);

    Page<User> findByUserNameContainingOrUserIdContainingOrUserTypeContainingOrUserTelContaining(String searchKeyword, String searchKeyword1, String searchKeyword2, String searchKeyword3, Pageable pageable);

    //권한에 맞는 유저 리스트 가져오기
    List<User> findAllByUserType(String userType);
//    @Query( value = "select * from t_user where user_name=:userId", nativeQuery = true)
//
//    Optional<User>  findByUserId(  @Param("userId") String userId);





}
