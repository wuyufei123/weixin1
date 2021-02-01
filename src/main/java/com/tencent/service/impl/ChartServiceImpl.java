package com.tencent.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.constant.WeChatApiConstant;
import com.tencent.mapper.ChartMapper;
import com.tencent.mapper.TokenMapper;
import com.tencent.service.ChartService;
import com.tencent.util.HttpClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Service
public class ChartServiceImpl implements ChartService {
    private final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Autowired
    ChartMapper chartMapper;
    @Autowired
    HttpClientHelper httpClientHelper;
    @Autowired
    TokenMapper tokenMapper;

    @Override
    public JSONArray selectAllChart() {
        BufferedReader in = null;
        JSONArray jsonObject1=new JSONArray();
        //读库
        List<String> charIdList = chartMapper.selectAllChartIdList();
        try {
            for (String chartId : charIdList) {
                String result = "";
                URL realUrl = new URL(WeChatApiConstant.CHARTINFO + tokenMapper.getToken().getToken() + "&chatid=" + chartId);
                // 打开和URL之间的连接
                URLConnection connection = realUrl.openConnection();
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 建立实际的连接
                connection.connect();
                // 定义 BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                JSONObject json = JSONObject.parseObject(result);

                jsonObject1.add(json.get("chat_info"));
                   // log.info("请求群聊列表成功，返回结果：" + jsonObject);

            }
        } catch (IOException e) {
            log.info(e.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return jsonObject1;
    }

}

