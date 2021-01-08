package com.ctsi.weixin.mapper;

import com.ctsi.weixin.model.AccessToken;
import org.apache.ibatis.annotations.Mapper;

/**
 *  @author: wuyufei
 *  @Date: 2020/12/29 14:11
 *  @Description: token
 */
@Mapper
public interface TokenMapper {
    //获取token
    AccessToken getToken();

    //插入token
    void setToken(AccessToken accessToken);
}
