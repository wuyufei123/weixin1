package com.tencent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author: wuyufei
 * @Date: 2020/12/30 11:30
 * @Description: department_info部门定时跟新
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentInfo {
    //部门id
    private Integer id;
    //部门名
    private String name;
    //英文名
    private String nameEn;
    //父节点
    private Integer parentId;
    //排序
    private Long order;
    //部门树结构名称
    private String departmentName;
}
