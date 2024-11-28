package com.mcr.application.domain.body;

import lombok.Data;

import java.io.Serializable;

@Data
public class DestinationFilePath implements Serializable {
    private String sourceFileId;
    private String fileName;
    private String destinationPath;
}
