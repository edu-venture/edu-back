package com.bit.eduventure.timetable.repository;

import com.bit.eduventure.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {


    List<TimeTable> findByClaNameAndTimeWeek(String claName, String timeWeek);

    List<TimeTable> findAllByClaName(String claName);

}
