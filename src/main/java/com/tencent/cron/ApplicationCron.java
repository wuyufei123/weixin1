package com.tencent.cron;

import com.alibaba.fastjson.JSONObject;
import com.tencent.WeixinApplication;
import com.tencent.constant.WeChatApiConstant;
import com.tencent.mapper.DepartmentInfoMapper;
import com.tencent.mapper.TokenMapper;
import com.tencent.model.DepartmentInfo;
import com.tencent.service.ApiService;
import com.tencent.util.HttpClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ApplicationCron implements ApplicationRunner {
    @Autowired
    ApiService apiService;
    @Autowired
    HttpClientHelper httpClientHelper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    DepartmentInfoMapper departmentInfoMapper;

    private static final Logger LOG = LoggerFactory.getLogger(WeixinApplication.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("==========开始调用用户列表===========");
        DepartmentUpdate();
        apiService.getUserList();
    }

    public void DepartmentUpdate() {
        //删除旧数据
        departmentInfoMapper.delete();
        JSONObject jsonObject = httpClientHelper.doHttpGet(WeChatApiConstant.DEPARMENTLIST + tokenMapper.getToken().getToken()+WeChatApiConstant.DEPARMENTID, "{}");
        List<Map> info = (List) jsonObject.get("department");
        //遍历刷新入库
        for (Map a : info) {
            DepartmentInfo depart = new DepartmentInfo();
            depart.setName((String) a.get("name"));
            depart.setId(Integer.valueOf((Integer) a.get("id")));
            //父节点
            depart.setParentId(Integer.valueOf((Integer) a.get("parentid")));
            //顺序
            depart.setOrder( Long.parseLong(a.get("order").toString()));
            //英文名
            depart.setNameEn((String) a.get("name_en"));
            //组织名结构树
            //入库
            departmentInfoMapper.insert(depart);
        }
    }
}
