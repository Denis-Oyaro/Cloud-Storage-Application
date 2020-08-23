package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid=#{userid}")
    List<File> getFiles(Integer userid);

    @Select("SELECT * FROM FILES WHERE filename=#{filename} AND userid=#{userid}")
    File getFile(String filename, int userid);

    @Insert("INSERT INTO FILES (filename,contenttype,filesize,userid,filedata) " +
            "VALUES(#{filename},#{contenttype},#{filesize},#{userid},#{filedata})")
    Integer addFile(File file);

    @Delete("DELETE FROM FILES WHERE filename=#{filename} AND userid=#{userid}")
    Integer deleteFile(String filename, int userid);
}
