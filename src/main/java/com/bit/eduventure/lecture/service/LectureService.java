package com.bit.eduventure.lecture.service;

import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.entity.Lecture;
import com.bit.eduventure.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    public LectureDTO createLecture(LectureDTO lectureDTO) {
        Lecture lecture = lectureDTO.DTOTOEntity();
        return lectureRepository.save(lecture).EntityTODTO();
    }
}
