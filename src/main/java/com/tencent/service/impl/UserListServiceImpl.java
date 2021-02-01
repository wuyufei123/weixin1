package com.tencent.service.impl;

import com.tencent.mapper.UserListMapper;
import com.tencent.model.UserListTemport;
import com.tencent.service.UserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserListServiceImpl implements UserListService {
    @Autowired
    UserListMapper userListMapper;
    @Override
    public List<UserListTemport> userListTemport(String name,String mobile) {
        return userListMapper.select(name,mobile);
    }
}
