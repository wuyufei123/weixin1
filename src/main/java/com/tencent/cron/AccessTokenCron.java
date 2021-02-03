package com.tencent.cron;

import com.tencent.mapper.TokenMapper;
import com.tencent.model.AccessToken;
import com.tencent.util.HttpClientHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


/**
 * @author: wuyufei
 * @Date: 2020/12/30 11:13
 * @Description: 定时获取token
 */
@Configuration
@EnableScheduling
public class AccessTokenCron {
    public static final Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);
    //获取token接口
    public static final String GETTOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wxe8c2710f51e9ee56&corpsecret=r9E3qkMqqstMP_MRrMlk3GlZqLEwM5inGEmY1_Lp1QI";
    //public static final String GETTOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww353786818bbff142&corpsecret=dCG2rGujuYvbyMzBFikNglg0Y1YQAplu1B8oDp4XTMQ";
    @Autowired
    TokenMapper tokenMapper;
    @Autowired
    HttpClientHelper httpClientHelper;

    //半小时同步一次
    @Scheduled(cron = "0 * * * * ? ")
    public void taskToken() {
        Map<String, String> param = new HashMap<>();
        //读库
        AccessToken accessToken = tokenMapper.getToken();
        //获取token
        try {
            accessToken.setToken(httpClientHelper.get(GETTOKEN, param).split("\"access_token\":\"")[1].split("\",\"expires_in\"")[0].trim());
        } catch (IOException e) {
            logger.info(String.valueOf(e));
            logger.error("请求token异常");
        }
        accessToken.setTime(new Timestamp(System.currentTimeMillis()));
        //定时更新库
        tokenMapper.setToken(accessToken);
    }

}
