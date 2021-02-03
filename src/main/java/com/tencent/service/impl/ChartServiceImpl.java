package com.tencent.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.constant.WeChatApiConstant;
import com.tencent.mapper.ChartMapper;
import com.tencent.mapper.DepartmentInfoMapper;
import com.tencent.mapper.TokenMapper;
import com.tencent.model.DepartmentInfo;
import com.tencent.service.ChartService;
import com.tencent.util.HttpClientHelper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartServiceImpl implements ChartService {
    private final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Autowired
    ChartMapper chartMapper;
    @Autowired
    HttpClientHelper httpClientHelper;
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    DepartmentInfoMapper departmentInfoMapper;

    @Override
    public List selectAllChart() {
        BufferedReader in = null;
        //读库
        List<String> charIdList = chartMapper.selectAllChartIdList();
        try {
            List list = new ArrayList();
            for (String chartId : charIdList) {
                Map map = new HashMap<>();
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
                JSONObject json1 = (JSONObject) json.get("chat_info");
                //用户列表
                JSONArray userlist = (JSONArray) json1.get("userlist");
                //群主
                String owner = json1.getString("owner");
                //群名称
                String name = json1.getString("name");
                //群id
                String chatid = json1.getString("chatid");
                map.put("owner", owner);
                map.put("name", name);
                map.put("chatid", chatid);
                List userList = new ArrayList();
                for (int i = 0; i < userlist.size(); i++) {
                    //查询用户的详细信息
                    userList.add(searchUserInfo(userlist.getString(i)));
                }
                map.put("userList", userList);
                list.add(map);
            }
            return list;
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
        return null;
    }

    //添加群成员
    @Override
    public JSONObject addUser(JSONObject jsonObject) {
        return addAndDeleteUserAll(null, jsonObject, "add_user_list");
    }

    //解散群聊
    @Override
    public JSONObject delete(JSONObject jsonObject) {
        BufferedReader in = null;
        //根据chatid查找用户
        String chatID = jsonObject.getString("chatId");
        String result = "";
        try {
            URL realUrl = new URL(WeChatApiConstant.CHARTINFO + tokenMapper.getToken().getToken() + "&chatid=" + chatID);
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
            JSONObject json1 = (JSONObject) json.get("chat_info");
            //删除查询到的所有用户
            addAndDeleteUserAll(chatID, json1, "del_user_list");
            //删除chat-info表中的id
            chartMapper.deleteById(chatID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List selectById(JSONObject jsonObject) {
        resolveDepartmentName((List) jsonObject.get("userIdList"));
        return (List) jsonObject.get("userIdList");
    }

    public JSONObject searchUserInfo(String user) {
        String result = "";
        BufferedReader in = null;
        try {
            //获取企业内部所有用户信息
            URL realUrl = new URL(WeChatApiConstant.USERINFO + tokenMapper.getToken().getToken() + "&userid=" + user);
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
        } catch (Exception e) {
            log.info("获取用户列表，发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    //新增成员或删除成员
    public JSONObject addAndDeleteUserAll(String chatId, JSONObject jsonObject, String info) {
        JSONObject param = new JSONObject();
        if (info.equals("add_user_list")) {
            param.put("add_user_list", jsonObject.get("add_user_list"));
            param.put("chatid", jsonObject.get("chatId"));
        } else {
            param.put("del_user_list", jsonObject.get("userlist"));
            param.put("chatid", chatId);
        }
        //发送post请求
        JSONObject result = new JSONObject();
        HttpPost post = new HttpPost(WeChatApiConstant.ADDUSER + tokenMapper.getToken().getToken());
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            post.setHeader("Content-Type", "application/json;charset=utf-8");
//			post.addHeader("Authorization", "Basic YWRtaW46");
            StringEntity postingString = new StringEntity(param.toString(), "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);

            InputStream in = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                strber.append(line).append('\n');
            }
            br.close();
            in.close();
            result = JSONObject.parseObject(strber.toString());
            if (chatId != null && chatId != "") {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    log.info("移出群失败，服务器异常");
                }
            } else {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    log.info("添加群成员失败，服务器异常");
                }
            }
        } catch (IOException e) {
            log.info("添加或移出群成员失败，请求异常");
            throw new RuntimeException(e);
        } finally {
            post.abort();
        }
        if (chatId != null && chatId != "") {
            if (result.get("errcode").equals(0)) {
                log.info("添加成员成功，返回结果：" + result);
            }
        } else {
            if (result.get("errcode").equals(0)) {
                log.info("添加成员成功，返回结果：" + result);
            }
        }
        return result;
    }

    //处理入库部门列表信息
    private void resolveDepartmentName(List userlist) {
        for (Object temp : userlist) {
            Map user=(Map)temp;
            List deptlist = new ArrayList();
            for (Object t : (List) user.get("department")) {
                DepartmentInfo departmentInfo = departmentInfoMapper.selectById((int) t);
                deptlist.add(departmentInfo.getName());
            }
            user.put("department", deptlist);
//            log.info(user.toString());
        }
        log.info("完成处理，用户接口返回部门名称转换");
    }
}

