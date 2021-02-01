package com.tencent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author: wuyufei
 * @Date: 2021/2/1 10:50
 * @Description: 用户列表零时表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListTemport {
    private String userid;
    private String name;
    private String department;
    private String mobile;
}
