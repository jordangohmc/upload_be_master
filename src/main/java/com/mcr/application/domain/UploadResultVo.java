package com.mcr.application.domain;

import lombok.Data;

import java.io.Serializable;


@Data
public class UploadResultVo implements Serializable {
    private String uploadId;
    private String description;
    private String fileSuffix;
}
