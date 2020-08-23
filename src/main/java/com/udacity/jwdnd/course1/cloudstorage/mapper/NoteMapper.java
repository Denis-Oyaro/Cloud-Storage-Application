package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid=#{userid}")
    List<Note> getNotes(Integer userid);

    @Insert("INSERT INTO NOTES (notetitle,notedescription,userid) " +
            "VALUES(#{notetitle},#{notedescription},#{userid})")
    Integer addNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteid} AND userid=#{userid}")
    int deleteNote(int noteid, int userid);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription}" +
            "WHERE noteid=#{noteid} AND userid=#{userid}")
    int editNote(Note note);
}
