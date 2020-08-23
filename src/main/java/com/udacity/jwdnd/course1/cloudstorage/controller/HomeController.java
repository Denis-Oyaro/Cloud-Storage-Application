package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.DisplayedCredential;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final  EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService,
                          CredentialService credentialService, EncryptionService encryptionService){
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(Authentication authentication, Model model) {
        int userid = userService.getUserId(authentication);

        addModalAttributesForHome(model,userid);
        return "home";
    }

    @PostMapping("/file")
    public String uploadFile(Authentication authentication, MultipartFile fileUpload, Model model){
        int userid = userService.getUserId(authentication);
        String failureMsg = null;
        String successMsg = null;

        if(fileUpload.isEmpty()) {
            failureMsg = "Empty file could not be uploaded";
        }else if(!fileService.isFilenameAvailable(fileUpload.getOriginalFilename(),userid)) {
            failureMsg = "File name already exists";
        }
        else{
            try {
                int rowsAdded = fileService.saveFile(fileUpload, userid);
                if (rowsAdded < 0) {
                    failureMsg = "File could not be uploaded";
                }
            }
            catch(IOException e){
                failureMsg = "File could not be uploaded";
            }
        }

        if(failureMsg == null) {
            successMsg = "File uploaded successfully";
        }

        model.addAttribute("successMsg",successMsg);
        model.addAttribute("failureMsg",failureMsg);

        return "result";
    }

    @GetMapping("/file/{filename}/delete")
    public String deleteFile(Authentication authentication, @PathVariable("filename") String filename, Model model){
        int userid = userService.getUserId(authentication);
        String failureMsg = null;
        String successMsg = null;

        int rowsDeleted = fileService.deleteFile(filename,userid);

        if (rowsDeleted < 0) {
            failureMsg = "File could not be deleted";
        } else{
            successMsg = "File deleted successfully";
        }

        model.addAttribute("successMsg",successMsg);
        model.addAttribute("failureMsg",failureMsg);

        return "result";
    }

    @GetMapping("/file/{filename}/view")
    @ResponseBody
    public ResponseEntity<byte[]> viewFile(Authentication authentication, @PathVariable("filename") String filename){
        int userid = userService.getUserId(authentication);
        File file = fileService.getFile(filename,userid);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE,file.getContenttype())
                .header(HttpHeaders.CONTENT_LENGTH,file.getFilesize())
                .body(file.getFiledata());
    }

    @PostMapping("/note")
    public String saveNote(Authentication authentication, NoteForm noteForm, Model model) {
        int userid = userService.getUserId(authentication);
        String failureMsg = null;
        String successMsg = null;

        try {
            int rowsAdded =noteService.saveNote(noteForm, userid);
            if (rowsAdded < 0) {
                failureMsg = "Note could not be saved";
            }
        }
        catch(IOException e){
            failureMsg = "Note could not be saved";
        }

        if(failureMsg == null) {
            successMsg = "Note saved successfully";
        }

        model.addAttribute("successMsg",successMsg);
        model.addAttribute("failureMsg",failureMsg);

        return "result";
    }

    @GetMapping("/note/{noteid}/delete")
    public String deleteNote(Authentication authentication, @PathVariable("noteid") int noteid, Model model){
        int userid = userService.getUserId(authentication);
        String failureMsg = null;
        String successMsg = null;

        int rowsDeleted = noteService.deleteNote(noteid,userid);

        if (rowsDeleted < 0) {
            failureMsg = "Note could not be deleted";
        } else{
            successMsg = "Note deleted successfully";
        }

        model.addAttribute("successMsg",successMsg);
        model.addAttribute("failureMsg",failureMsg);

        return "result";
    }

    @PostMapping("/credential")
    public String saveCredential(Authentication authentication, CredentialForm credentialForm, Model model) {
        int userid = userService.getUserId(authentication);
        String failureMsg = null;
        String successMsg = null;

        try {
            int rowsAdded =credentialService.saveCredential(credentialForm, userid);
            if (rowsAdded < 0) {
                failureMsg = "Credential could not be saved";
            }
        }
        catch(IOException e){
            failureMsg = "Credential could not be saved";
        }

        if(failureMsg == null) {
            successMsg = "Credential saved successfully";
        }

        model.addAttribute("successMsg",successMsg);
        model.addAttribute("failureMsg",failureMsg);

        return "result";
    }

    @GetMapping("/credential/{credentialid}/delete")
    public String deleteCredential(Authentication authentication, @PathVariable("credentialid") int credentialid, Model model){
        int userid = userService.getUserId(authentication);
        String failureMsg = null;
        String successMsg = null;

        int rowsDeleted = credentialService.deleteCredential(credentialid,userid);
        if (rowsDeleted < 0) {
            failureMsg = "Credential could not be deleted";
        } else{
            successMsg = "Credential deleted successfully";
        }

        model.addAttribute("successMsg",successMsg);
        model.addAttribute("failureMsg",failureMsg);

        return "result";
    }


    private void addModalAttributesForHome(Model model, int userid){
        model.addAttribute("filenames",fileService.getFilenames(userid));
        model.addAttribute("notes", noteService.getNotes(userid));
        model.addAttribute("displayedCredentials",credentialService.getCredentials(userid).stream()
                .map(credential -> {
                    String decryptedPassword = encryptionService.decryptValue(credential.getPassword(),credential.getKey());
                    return new DisplayedCredential(credential,decryptedPassword);
                }).collect(toList()));
    }

}
