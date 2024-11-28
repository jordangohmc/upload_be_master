package com.mcr.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TenantEmployee implements Serializable {
    private Long empId;
    private Integer empRoleUid;
    private Integer tenantUid;
    private String companyName;
    private String accountId;

    private String dbName;
    private String mediaPath;
}

