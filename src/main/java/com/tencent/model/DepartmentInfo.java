package com.tencent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author: wuyufei
 * @Date: 2020/12/30 11:30
 * @Description: department_info
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
    private Integer order;
    //部门树结构名称
    private String departmentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
