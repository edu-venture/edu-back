package com.bit.eduventure.lecture.service;

import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.entity.Lecture;
import com.bit.eduventure.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException());
    }

    public List<LectureDTO> getAllLecture() {
        List<Lecture> lectureList = lectureRepository.findAll();
        List<LectureDTO> returnList = new ArrayList<>();

        for(Lecture lecture : lectureList) {
            LectureDTO dto = LectureDTO.builder()
                            .id(lecture.getId())
                            .title(lecture.getTitle())
                            .liveStationId(lecture.getLiveStationId())
                            .couNo(lecture.getCouNo())
                            .build();
            returnList.add(dto);
        }

        return returnList;
    }

    //학생별 강의 주소 조회
    public Lecture getCouLecture(int couNo) {
        return lectureRepository.findAllByCouNo(couNo).get(0);

    }

    //종료 하기 전 강의 찾고 게시물 작성하기
    public void createRecord(int lecId) {

    }

    public void deleteLecture(int lecId) {
        lectureRepository.deleteById(lecId);
    }


}
