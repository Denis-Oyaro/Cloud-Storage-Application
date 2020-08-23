package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid=#{userid}")
    List<Credential> getCredentials(Integer userid);

    @Insert("INSERT INTO CREDENTIALS (url,username,key,password,userid) " +
            "VALUES(#{url},#{username},#{key},#{password},#{userid})")
    Integer addCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{credentialid} AND userid=#{userid}")
    int deleteCredential(int credentialid, int userid);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key=#{key}, password=#{password}" +
            "WHERE credentialid=#{credentialid} AND userid=#{userid}")
    int editCredential(Credential credential);
}

