package com.txWeb.spider.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
    private Integer id;
    private String title;
    private String intro;
    private String source;
    private String url;
    private String publish_time;
}
