package com.mcr.common.enums;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author ruoyi
 */

@Getter
public enum TaskProgressStatus {
    PENDING("0", "Pending"),
    RECEIVED("1", "Received "),
    START("2", "Start"),
    PAUSE("3", "Pause"),
    COMPLETE("4", "Complete");

    private final String code;
    private final String info;

    TaskProgressStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
