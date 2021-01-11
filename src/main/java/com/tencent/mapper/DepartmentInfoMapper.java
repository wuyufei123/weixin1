package com.tencent.mapper;

import com.tencent.model.DepartmentInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wuyufei
 * @Date: 2020/12/29 14:11
 * @Description: department
 */
@Mapper
public interface DepartmentInfoMapper {
    //定时入库
    void insert(DepartmentInfo departmentInfo);
    void delete();
    DepartmentInfo selectById(int id);
}
