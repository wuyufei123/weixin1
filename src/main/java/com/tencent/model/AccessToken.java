package com.tencent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * @author: wuyufei
 * @Date: 2020/12/29 10:50
 * @Description: access_token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private String token;
    private Timestamp time;
    //企业id
    private String corpId;
    //应用密钥
    private String corpSecret;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }
}
