package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }

    public boolean isFilenameAvailable(String filename, int userid) {
        return getFile(filename,userid) == null;
    }

    public int saveFile(MultipartFile fileUpload, int userid) throws IOException {

        File file = new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(),
                String.valueOf(fileUpload.getSize()), userid, fileUpload.getBytes());
        return fileMapper.addFile(file);
    }

    public List<String> getFilenames(int userid){
        return fileMapper.getFiles(userid).stream()
                .map(file -> file.getFilename().toString()).collect(Collectors.toList());
    }

    public int deleteFile(String filename, int userid){

        return fileMapper.deleteFile(filename,userid);
    }

    public File getFile(String filename, int userid){
        return fileMapper.getFile(filename,userid);
    }

}
