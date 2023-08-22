package com.bit.eduventure.ES3_Course.Repository;

import com.bit.eduventure.ES3_Course.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//JpaRepository를 상속받으면 메소드를 구현하지 않아도
//제공되는 다양한 메소드들을 사용할 수 있다.
//List<T> findAll, List<T> findAll(Sort sort), saveAll, void flush,
//T findById....
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByCouNo(Integer couNo);

    Course findByCouNo(int couNo);

    Course findByClaName(String claName);
}