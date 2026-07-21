package com.yub.edu.biz.vo;

import lombok.Data;

@Data
public class ShareContentRespVO {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private String content;

    private Integer status;
}