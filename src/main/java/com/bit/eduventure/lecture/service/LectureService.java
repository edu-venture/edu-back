package com.bit.eduventure.lecture.service;

import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.entity.Lecture;
import com.bit.eduventure.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    public Lecture createLecture(LectureDTO lectureDTO) {
        Lecture lecture = lectureDTO.DTOTOEntity();
        return lectureRepository.save(lecture);
    }

    public Lecture getLecture(int id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture를 못 찾았습니다."));
    }

    public List<Lecture> getAllLecture() {
        return lectureRepository.findAll();
    }


}
