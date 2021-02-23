package com.tencent.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mapper.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class testController {
    @Autowired
    TokenMapper tokenMapper;


    @RequestMapping("/js")
    public JSONObject test() {
        JSONObject json = new JSONObject();
        String url = "http://rh32566182.51vip.biz/test.html";
        //签名的当前时间戳
        String timestamp = Long.toString((new Date().getTime()) / 1000);
        //签名的随机字符串
        String noncestr = UUID.randomUUID().toString();
        //获取JsapiTicket
        //获得加密后的签名
        String qianming = null;
        try {
            qianming = getJsSdkSign(noncestr, getJsapiTicket(), timestamp, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("timestamp", timestamp);
        json.put("nonceStr", noncestr);
        json.put("signature", qianming);
        return json;
    }

    @RequestMapping("/js1")
    public JSONObject test1() {
        JSONObject json = new JSONObject();
        String url = "http://rh32566182.51vip.biz/test.html";
        //签名的当前时间戳
        String timestamp = Long.toString((new Date().getTime()) / 1000);
        //签名的随机字符串
        String noncestr = UUID.randomUUID().toString();
        //获取JsapiTicket
        //获得加密后的签名
        String qianming = null;
        try {
            qianming = getJsSdkSign(noncestr, getJsapiTicket1(), timestamp, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("timestamp", timestamp);
        json.put("nonceStr", noncestr);
        json.put("signature", qianming);
        return json;
    }

    /**
     * @Description 获取应用jsapi_ticket
     * @Author wuyufei
     * @Date 2021/2/19 14:33
     * @Param * @param null
     * @Return
     * @Exception
     */
    public String getJsapiTicket1() throws Exception {
        //获取access_token
        String access_token = (tokenMapper.getToken()).getToken();
        String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/ticket/get?access_token=" + access_token+"&type=agent_config";
        URL url = new URL(urlStr);
        URLConnection URLconnection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
        int responseCode = httpConnection.getResponseCode();
        String ticket = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream urlStream = httpConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
            String sCurrentLine = "";
            String sTotalString = "";
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                sTotalString += sCurrentLine;
            }
            JSONObject obj = JSON.parseObject(sTotalString);
            ticket = (String) obj.get("ticket");
        }
        return ticket;
    }
    /**
     * @Description 获取企业jsapi_ticket
     * @Author wuyufei
     * @Date 2021/2/19 14:33
     * @Param * @param null
     * @Return
     * @Exception
     */
    public String getJsapiTicket() throws Exception {
        //获取access_token
        String access_token = (tokenMapper.getToken()).getToken();
        String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + access_token;
        URL url = new URL(urlStr);
        URLConnection URLconnection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
        int responseCode = httpConnection.getResponseCode();
        String ticket = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream urlStream = httpConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
            String sCurrentLine = "";
            String sTotalString = "";
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                sTotalString += sCurrentLine;
            }
            JSONObject obj = JSON.parseObject(sTotalString);
            ticket = (String) obj.get("ticket");
        }
        return ticket;
    }


    /**
     * @Description 获得加密后的签名
     * @Author wuyufei
     * @Date 2021/2/19 14:33
     * @Param * @param null
     * @Return
     * @Exception
     */
    public String getJsSdkSign(String noncestr, String tsapiTicket, String timestamp, String url) {
        String content = "jsapi_ticket=" + tsapiTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        String ciphertext = getSha1(content);
        return ciphertext;
    }

    /**
     * @Description 进行sha1加密
     * @Author wuyufei
     * @Date 2021/2/19 14:34
     * @Param * @param null
     * @Return
     * @Exception
     */

    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

}
