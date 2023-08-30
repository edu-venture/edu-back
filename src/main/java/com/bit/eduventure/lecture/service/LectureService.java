package com.bit.eduventure.lecture.service;

import com.bit.eduventure.lecture.dto.LectureDTO;
import com.bit.eduventure.lecture.entity.Lecture;
import com.bit.eduventure.lecture.repository.LectureRepository;
import com.bit.eduventure.vodBoard.service.VodBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        // 강의 찾기
        Optional<Lecture> lectureOptional = lectureRepository.findById(lecId);

        if (lectureOptional.isPresent()) {
            Lecture lecture = lectureOptional.get();
            System.out.println(lecture);

            // 예를 들면: lecture.setTitle("새로운 제목");

            // 강의 또는 게시물 저장
            lectureRepository.save(lecture);
        } else {
            System.out.println("강의를 찾을 수 없습니다.");
        }
    }


    public void deleteLecture(int lecId) {
        lectureRepository.deleteById(lecId);
    }


}
