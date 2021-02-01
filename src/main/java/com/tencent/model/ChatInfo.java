package com.tencent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * @author: wuyufei
 * @Date: 2021/2/1 15:04
 * @Description: 群聊信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatInfo {
    private String chartId;
    private Timestamp createTime;
}
