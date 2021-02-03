package com.tencent.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.constant.WeChatApiConstant;
import com.tencent.mapper.ChartMapper;
import com.tencent.mapper.DepartmentInfoMapper;
import com.tencent.mapper.TokenMapper;
import com.tencent.mapper.UserListMapper;
import com.tencent.model.ChatInfo;
import com.tencent.model.DepartmentInfo;
import com.tencent.model.UserListTemport;
import com.tencent.service.ApiService;
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
import java.sql.Timestamp;

@Service
public class ApiServiceImpl implements ApiService {
    private final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    DepartmentInfoMapper departmentInfoMapper;
    @Autowired
    UserListMapper userListMapper;
    @Autowired
    ChartMapper chartMapper;

    /**
     * @Description 获取用户列表
     * @Date 2020/12/28 15:11
     * @Param * @param
     * @Return com.alibaba.fastjson.JSONObject
     * @Exception
     */
    @Override
    public JSONObject getUserList() {
        String result = "";
        BufferedReader in = null;
        try {
            //获取企业内部所有用户信息
            URL realUrl = new URL(WeChatApiConstant.USERLISTSTART + tokenMapper.getToken().getToken() + WeChatApiConstant.USERLISTEND);
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
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(result);

        if (jsonObject.get("errcode").equals(0)) {
            log.info("请求用户列表成功，返回结果：" + jsonObject);
            JSONArray userlist = (JSONArray) jsonObject.get("userlist");
            resolveDepartmentName(userlist);
            //数据更新入库
            updateTable(userlist);
        } else {
            log.info("请求用户列表失败，错误原因：" + jsonObject.get("errmsg"));
        }
        return jsonObject;
    }

    //创建群聊
    @Override
    public JSONObject createChatGroup(JSONObject groupParam) {
        JSONObject param = new JSONObject();
        param.put("name", groupParam.get("groupName"));
        param.put("owner", groupParam.get("owner"));
        param.put("userlist", groupParam.get("userlist"));
        log.info("创建群聊参数：" + param);

        JSONObject result = new JSONObject();
        HttpPost post = new HttpPost(WeChatApiConstant.CREATEGROUP + tokenMapper.getToken().getToken());
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
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("创建群聊请求，服务器异常");
            }
        } catch (Exception e) {
            log.info("创建群聊，请求异常");
            throw new RuntimeException(e);
        } finally {
            post.abort();
        }
        log.info("创建群聊，返回结果：" + result);
        if (result.get("errcode").equals(0)) {
            ChatInfo chatInfo=new ChatInfo();
            chatInfo.setChartId(String.valueOf(result.get("chatid")));
            chatInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            //chartId入库
            chartMapper.insertChartId(chatInfo);
            //在群聊中发送一句话
            JSONObject sendMsg = sendMsgChatGroup(result.get("chatid").toString());
            result.put("sendMsg", sendMsg.get("errcode"));
            open();
        }
        return result;
    }

    //	@Override
    public JSONObject sendMsgChatGroup(String chat_id) {

        JSONObject param = new JSONObject();
        param.put("chatid", chat_id);
        param.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", "您已加入群聊，开始聊天吧~");
        param.put("text", text);

        JSONObject result = new JSONObject();
        HttpPost post = new HttpPost(WeChatApiConstant.GROUPTALK + tokenMapper.getToken().getToken());
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            post.setHeader("Content-Type", "application/json;charset=utf-8");
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
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("向群聊" + chat_id + "发送初始消息，服务器异常");
            }
        } catch (Exception e) {
            log.info("向群聊" + chat_id + "发送初始消息，请求异常");
            throw new RuntimeException(e);
        } finally {
            post.abort();
        }
        log.info("向群聊" + chat_id + "发送初始消息，返回结果：" + result);
        return result;
    }

    //处理入库部门列表信息
    private void resolveDepartmentName(JSONArray userlist) {
        for (Object temp : userlist) {
            JSONObject user = (JSONObject) temp;
            JSONArray deptlist = new JSONArray();
            for (Object t : (JSONArray) user.get("department")) {
                DepartmentInfo departmentInfo = departmentInfoMapper.selectById((Integer) t);
                if(departmentInfo!=null && departmentInfo.getName()!=null) {
                    deptlist.add(departmentInfo.getName());
                }
            }
            user.put("department", deptlist);
//            log.info(user.toString());
        }
        log.info("完成处理，用户接口返回部门名称转换");
    }


    public void open() {
        Runtime runtime = Runtime.getRuntime();
        Process p = null;
        try {
            p = runtime.exec(WeChatApiConstant.WEIXINDIR);
        } catch (IOException e) {
            log.error("应用打开失败");
        }

    }

    //前端查询用，打开页面先入库用户信息
    public void updateTable(JSONArray jsonArray) {
        //清除临时表数据
        userListMapper.deleteAll();
        if (jsonArray != null && jsonArray.size() != 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                UserListTemport userListTemport = new UserListTemport();
                userListTemport.setUserid(jsonArray.getJSONObject(i).getString("userid"));
                userListTemport.setName(jsonArray.getJSONObject(i).getString("name"));
                userListTemport.setDepartment(jsonArray.getJSONObject(i).getString("department").replace("\"]","").replace("[\"",""));
                userListTemport.setMobile(jsonArray.getJSONObject(i).getString("mobile"));
                //入库
                userListMapper.UpdateList(userListTemport);
            }
        }
    }
}
