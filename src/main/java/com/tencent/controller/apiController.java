package com.tencent.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.service.ApiService;
import com.tencent.service.ChartService;
import com.tencent.service.UserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class apiController {

    @Autowired
    private ApiService apiService;
    @Autowired
    private UserListService listService;
    @Autowired
    private ChartService chartService;

    @GetMapping("/test")
    public String test() {
        return "HelloWorld";
    }

    //列出用户
    @GetMapping("/userlist")
    public JSONObject userList() {
        JSONObject result = apiService.getUserList();
//		System.out.println(result);
        return result;
    }

    //根据所选用户创建群聊
    @PostMapping("/createChat")
    public JSONObject createChat(@RequestBody JSONObject groupParam) {
//		List<String> user_list = new ArrayList<>(); //["cheng","chen","sang"]
        JSONObject result = apiService.createChatGroup(groupParam);
        return result;
    }

    //提供根据电话/用户姓名查询用户列表
    @RequestMapping("/search")
    public JSONObject search(@RequestParam String name, @RequestParam String mobile) {
        //查询user_list
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", listService.userListTemport(name, mobile));
        return jsonObject;
    }

    //获取当前应用创建的所有群聊(读库)，新增群聊时，chartId已入库
    @RequestMapping("getAllChart")
    public JSONArray getAllChart() {
        return chartService.selectAllChart();
    }
}
