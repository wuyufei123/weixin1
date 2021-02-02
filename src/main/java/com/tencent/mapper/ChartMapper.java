package com.tencent.mapper;

import com.tencent.model.ChatInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChartMapper {
    void insertChartId(ChatInfo c);

    List<String> selectAllChartIdList();

    void deleteById(String id);
}
