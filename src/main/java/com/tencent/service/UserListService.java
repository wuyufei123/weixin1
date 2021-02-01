package com.tencent.service;

import com.tencent.model.UserListTemport;

import java.util.List;

public interface UserListService {
    List<UserListTemport> userListTemport(String name, String mobile);
}
