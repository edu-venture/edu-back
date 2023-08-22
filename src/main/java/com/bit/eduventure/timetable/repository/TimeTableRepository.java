package com.bit.eduventure.timetable.repository;

import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {

    List<Course> findByClaNameAndTimeWeek(String claName, String couWeek);
}
