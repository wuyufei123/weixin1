package com.tencent.constant;

import org.springframework.stereotype.Component;


/**
 * @author: wuyufei
 * @Date: 2020/12/29 14:16
 * @Description:内部api
 */
@Component
public class WeChatApiConstant {
    //用户列表头(全量)
    public static final String USERLISTSTART = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=";
    //用户列表尾
    public static final String USERLISTEND = "&department_id=1&fetch_child=1";
    //创建群聊
    public static final String CREATEGROUP = "https://qyapi.weixin.qq.com/cgi-bin/appchat/create?access_token=";
    //进入群聊发送一段话
    public static final String GROUPTALK = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send?access_token=";
    //获取部门列表
    public static final String DEPARMENTLIST="https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=";
    //企业微信路径
    public static final String WEIXINDIR="D:\\Program Files (x86)\\WXWork\\WXWork.exe";
    //根据token和chartId查询信息
    public static final String CHARTINFO="https://qyapi.weixin.qq.com/cgi-bin/appchat/get?access_token=";
}
