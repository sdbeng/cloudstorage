package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class FileController {
    private FileService fileService;
    private UserMapper userMapper;
    String successMessage = null;

    public FileController(FileService fileService, UserMapper userMapper) {
        this.fileService = fileService;
        this.userMapper = userMapper;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        Integer userid = user.getUserId();
        String filename = fileUpload.getOriginalFilename();

//        if (!fileService.isFileNameAvailable(filename, userid)) {
//            model.addAttribute("error", "File already exists");
//            return "redirect:/result?error";
//        }

        if (fileUpload.getSize() > 0) {
            System.out.println("====fileUpload.getSize() > 0");
            fileService.addFile(fileUpload, userid);
            return "redirect:/result?success";
        }else{
            model.addAttribute("error", "Unable to upload file");
            return "redirect:/result?error";
        }

    }

//    @GetMapping("/delete")
//    public String deleteFile(@RequestParam(value="id", required = true) int fileId, Authentication authentication, RedirectAttributes redirectAttributes) {
//        String username = authentication.getName();
//        User user = userMapper.getUser(username);
//        String deleteError = null;
//        System.out.println("deleteFile ->fileId: " + fileId);
//        if (fileId > 0) {
//            fileService.deleteFile(fileId);
//            return "redirect:/result?success";
//        }
//        redirectAttributes.addAttribute("error", "Unable to delete. File not found");
//        return "redirect:/result?error";
//    }

    @GetMapping("/handleDelete/{id}")
    public String handleDelete(@PathVariable(value = "id") Integer fileId, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        String deleteError = null;
        System.out.println("HANDLER*****deleteFile ->fileId: " + fileId);
        if (fileId > 0) {
            fileService.deleteFile(fileId);
            return "redirect:/result?success";
        }else{
            redirectAttributes.addAttribute("error", "Unable to delete. File not found");
            return "redirect:/result?error";
        }
    }
    //method responds to UI View.
    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        System.out.println("View-> fileId: " + fileId);
        File file = fileService.getFileById(fileId);
        System.out.println("View-> file: " + file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));

    }
}
