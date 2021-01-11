package com.tencent.cron;

import com.alibaba.fastjson.JSONObject;
import com.tencent.constant.WeChatApiConstant;
import com.tencent.mapper.DepartmentInfoMapper;
import com.tencent.mapper.TokenMapper;
import com.tencent.model.DepartmentInfo;
import com.tencent.util.HttpClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * @author: wuyufei
 * @Date: 2020/12/30 11:13
 * @Description: 定时更新部门信息
 */
@Configuration
@EnableScheduling
public class DepartmentUpdateCron {
    @Autowired
    HttpClientHelper httpClientHelper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    DepartmentInfoMapper departmentInfoMapper;
    public static final Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);

    //半小时同步一次
    @Scheduled(cron = "0 * * * * ? ")
    public void DepartmentUpdate() {
        //删除旧数据
        departmentInfoMapper.delete();
        JSONObject jsonObject = httpClientHelper.doHttpGet(WeChatApiConstant.DEPARMENTLIST + tokenMapper.getToken().getToken(), "{}");
        List<Map> info = (List) jsonObject.get("department");
        //遍历刷新入库
        for (Map a : info) {
            DepartmentInfo depart = new DepartmentInfo();
            depart.setName((String) a.get("name"));
            depart.setId(Integer.valueOf((Integer) a.get("id")));
            //父节点
            depart.setParentId(Integer.valueOf((Integer) a.get("parentid")));
            //顺序
            depart.setOrder(Integer.valueOf((Integer) a.get("order")));
            //英文名
            depart.setNameEn((String) a.get("name_en"));

            //组织名结构树
            if(Integer.valueOf((Integer) a.get("parentid")) == 0){
                depart.setDepartmentName((String) a.get("name"));
            }else{
                depart.setDepartmentName(departmentInfoMapper.selectById(Integer.valueOf((Integer) a.get("parentid"))).getName() + "/" + (String) a.get("name"));
            }
            //入库
            departmentInfoMapper.insert(depart);
        }
    }
}
