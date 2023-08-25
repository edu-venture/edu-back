package com.bit.eduventure.objectStorage.controller;


import com.bit.eduventure.objectStorage.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class ObjectStorageController {
    private final ObjectStorageService objectStorageService;

    @GetMapping("/upload")
    public String getUpload() {
        return "storage/insert";
    }

    @PostMapping("/upload")
    public String putFile(@RequestParam("insertFile") MultipartFile file,
                          Model model) {
        String saveName = objectStorageService.uploadFile(file);
        model.addAttribute("saveName", saveName);
        return "storage/success";
    }


}
