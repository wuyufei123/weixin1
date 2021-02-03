package com.tencent.service;
import com.alibaba.fastjson.JSONObject;

public interface ApiService {
	JSONObject getUserList();

	JSONObject createChatGroup(JSONObject groupParam);

	JSONObject sendMsgChatGroup(String chat_id);

}
