package com.mcr.application.domain;

import lombok.Data;

import java.io.Serializable;


@Data
public class UploadVideoVo implements Serializable {
    private String uploadId;
    private String fileSuffix;
}
