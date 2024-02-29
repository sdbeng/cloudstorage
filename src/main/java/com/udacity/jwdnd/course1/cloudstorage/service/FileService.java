package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void addFile(MultipartFile fileUpload, int userid) throws IOException {
        File file = new File();
//        System.out.println("file instance: " + file);
        try {
            file.setContenttype(fileUpload.getContentType());
            file.setFiledata(fileUpload.getBytes());
            file.setFilename(fileUpload.getOriginalFilename());
            file.setFilesize(Long.toString(fileUpload.getSize()));
            file.setUserid(userid);
        } catch (IOException e) {
            System.out.println("Error in addFile"  + e.getMessage() + e.getCause());
            throw e;
        }
        fileMapper.storeFile(file);
    }

    public List<File> getUploadedFiles(int userid) {
        return fileMapper.getAllFiles(userid);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public File getFileByName(String filename) {
        return fileMapper.getFileByName(filename);
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }

    public boolean isFileNameAvailable(String filename, int userid) {
        File file = fileMapper.getFile(userid, filename);
        if(file != null) {
            return false;
        }
        return true;
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

    public List<File> getFilesForUSer(Integer userId) {
        return fileMapper.getFilesForUSer(userId);
    }
}
