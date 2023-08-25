package com.bit.eduventure.ES3_Course.Controller;


import com.bit.eduventure.ES1_User.DTO.ResponseDTO;
import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Repository.CourseRepository;
import com.bit.eduventure.ES3_Course.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/getcourse")
    public ResponseEntity<?> getcourse(@RequestBody CourseDTO courseDTO) {
        ResponseDTO<CourseDTO> responseDTO = new ResponseDTO<>();

        try {
            Course course = courseService.findByCouNo(courseDTO.getCouNo())
                    .orElseThrow(() -> new NoSuchElementException("Course not found"));
            CourseDTO courseDTOtosend = course.EntityToDTO();
            responseDTO.setItem(courseDTOtosend);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }


    }

    @GetMapping("/course-list")
    public ResponseEntity<?> getCourseList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ResponseDTO<CourseDTO> responseDTO = new ResponseDTO<>();
        System.out.println("겟 코스리스트 도착완료 트라이 직전");
        try {
            List<Course> courseList = courseService.getCourseList();
            List<CourseDTO> courseDTOList = new ArrayList<>();
            for(Course course : courseList) {
                courseDTOList.add(course.EntityToDTO());
            }
            responseDTO.setItems(courseDTOList);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/course/{teacherId}")
    public ResponseEntity<?> getCourse(@PathVariable int teacher) {
        ResponseDTO<CourseDTO> responseDTO = new ResponseDTO<>();
        try {
            CourseDTO courseDTO = courseService.findByTeacherId(teacher).EntityToDTO();

            responseDTO.setItem(courseDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }










}
