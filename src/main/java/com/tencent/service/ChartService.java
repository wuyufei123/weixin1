package com.tencent.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ChartService {
    List selectAllChart();
    JSONObject addUser(JSONObject jsonObject);
    JSONObject delete(JSONObject jsonObject);
    List selectById(JSONObject jsonObject);
}
