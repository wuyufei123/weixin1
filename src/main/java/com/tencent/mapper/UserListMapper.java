package com.tencent.mapper;

import com.tencent.model.UserListTemport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserListMapper {
    void UpdateList(UserListTemport userListTemport);
    void deleteAll();
    List<UserListTemport> select(String name,String mobile);
}
