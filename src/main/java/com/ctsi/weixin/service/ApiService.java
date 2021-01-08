package com.ctsi.weixin.service;
import com.alibaba.fastjson.JSONObject;

public interface ApiService {
	JSONObject getUserList();

	JSONObject createChatGroup(JSONObject groupParam);

	JSONObject sendMsgChatGroup(String chat_id);
}
