package com.bit.eduventure.objectStorage.controller;


import com.bit.eduventure.objectStorage.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
