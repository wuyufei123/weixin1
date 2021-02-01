package com.tencent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartAndUser {
    private String chartName;
    private List<String> users;
    //群主
    private String owner;
}
