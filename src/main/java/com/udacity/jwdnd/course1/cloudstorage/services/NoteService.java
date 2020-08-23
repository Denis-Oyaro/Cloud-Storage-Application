package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper){
        this.noteMapper = noteMapper;
    }

    public int saveNote(NoteForm noteForm, int userid) throws IOException {

        Integer noteid = noteForm.getNoteId();
        String notetitle = noteForm.getNoteTitle();
        String notedescription = noteForm.getNoteDescription();
        Note note = new Note(noteid,notetitle,notedescription,userid);

        if(noteid != null){
            return noteMapper.editNote(note);
        }
        return noteMapper.addNote(note);
    }

    public List<Note> getNotes(int userid){
        return noteMapper.getNotes(userid);
    }

    public int deleteNote(int noteid, int userid){

        return noteMapper.deleteNote(noteid,userid);
    }

}

