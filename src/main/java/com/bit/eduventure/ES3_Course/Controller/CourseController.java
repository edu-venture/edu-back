package com.bit.eduventure.ES3_Course.Controller;


import com.bit.eduventure.ES1_User.DTO.ResponseDTO;
import com.bit.eduventure.ES1_User.DTO.UserDTO;
import com.bit.eduventure.ES1_User.Entity.CustomUserDetails;
import com.bit.eduventure.ES1_User.Service.UserService;
import com.bit.eduventure.ES3_Course.DTO.CourseDTO;
import com.bit.eduventure.ES3_Course.Entity.Course;
import com.bit.eduventure.ES3_Course.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @PostMapping("/getcourse")
    public ResponseEntity<?> getcourse(@RequestBody CourseDTO courseDTO) {
        ResponseDTO<CourseDTO> responseDTO = new ResponseDTO<>();

        Course course = courseService.getCourse(courseDTO.getCouNo());

        CourseDTO courseDTOtosend = course.EntityToDTO();
        responseDTO.setItem(courseDTOtosend);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/course-list")
    public ResponseEntity<?> getCourseList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<CourseDTO> responseDTO = new ResponseDTO<>();

        List<Course> courseList = courseService.getCourseList();

        List<CourseDTO> courseDTOList =  courseList.stream()
                .map(Course::EntityToDTO)
                .collect(Collectors.toList());

        responseDTO.setItems(courseDTOList);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/course/{teacherId}")
    public ResponseEntity<?> getCourse(@PathVariable int teacherId) {
        ResponseDTO<CourseDTO> responseDTO = new ResponseDTO<>();
        CourseDTO courseDTO = courseService.findByTeacherId(teacherId).EntityToDTO();

        responseDTO.setItem(courseDTO);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/course")
    public ResponseEntity<?> creatCourse(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestBody CourseDTO courseDTO){
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        int userNo = Integer.parseInt(customUserDetails.getUsername());
        UserDTO userDTO = userService.findById(userNo).EntityToDTO();
        courseDTO.setUserDTO(userDTO);

        courseService.createCourse(courseDTO);

        responseDTO.setItem("반 생성 완료");
        responseDTO.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(responseDTO);
    }










}
