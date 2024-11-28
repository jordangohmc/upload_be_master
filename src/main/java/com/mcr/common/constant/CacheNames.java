package com.mcr.common.constant;

/**
 * 缓存组名称常量
 * <p>
 * key 格式为 cacheNames#ttl#maxIdleTime#maxSize
 * <p>
 * ttl 过期时间 如果设置为0则不过期 默认为0
 * maxIdleTime 最大空闲时间 根据LRU算法清理空闲数据 如果设置为0则不检测 默认为0
 * maxSize 组最大长度 根据LRU算法清理溢出数据 如果设置为0则无限长 默认为0
 * <p>
 * 例子: test#60s、test#0#60s、test#0#1m#1000、test#1h#0#500
 *
 * @author Lion Li
 */
public interface CacheNames {
    /**
     * 系统 缓存
     */
    String TENANT = "SYS:TENANT#5m";
    String SYS_CONFIG = "sys_config";
    // 系统 租客 参数
    String TENANT_SETTING = "SYS:TENANT_SETTING#30s";

    String IOT_HUB = "IOT:iot_hub#30m";
    /**
     * 住户 缓存
     */
    // 单号 SERIAL NUMBER & PREFIX
    String DO_INT_NUMBER = "SYS:SN:DO_INT_NO#T";
    // Inventory
    String INV_REQUISITION_SN = "SYS:SN:INV_REQ_NO#T";
    String INV_RCV_SN = "SYS:SN:RCV_EXT_NO#T";
    String PO_SN = "SYS:SN:PO_NO#T";
    String TASK_SN = "SYS:SN:TASK_NO#T";
    String FISP_SN = "SYS:SN:F_INS_NO#T";
    String HV_COL_SN = "SYS:SN:HV_COL_NO#T";
    String HV_GBK_SN = "SYS:SN:HV_GBK_NO#T";
    // PREFIX
    String PFX_SN_RQT = "SYS:PFX_SN:RQT#10m#T";
    String PFX_SN_RCV = "SYS:PFX_SN:RCV#10m#T";
    String PFX_SN_PO = "SYS:PFX_SN:PO#10m#T";
    String PFX_SN_FISP = "SYS:PFX_SN:PO#10m#T";
    String PFX_SN_HV_COL = "SYS:PFX_SN:COL#1d#T";
    String PFX_SN_HV_GBK = "SYS:PFX_SN:GBK#1d#T";
    String PFX_SN_TASK = "SYS:PFX_SN:TASK#10m#T";
    /**
     * 用户账户
     */
    String SYS_USER_NAME = "SYS:USER:name#5m#T";
    String SYS_USER_LABEL = "SYS:USER:label#5m#T";
    String SYS_USER_STAFF_ID = "SYS:USER:staff_id#5m#T";
    String SYS_USER_INFO = "SYS:USER:user_info#5m#T";
    String SYS_DEPT_PST_TITLE = "SYS:USER:dept_pst#5m#T";
    /**
     * 果园 Profile Category
     */
    String PT_FI_TYPE_LIST = "pt_inspection_type_list#5m#T";
    /**
     * 采购 Procurement
     */
    String PCM_SUPPLIER = "pcm_supplier#5m#T";
    /**
     * 仓库 Inventory
     */
    String INV_CONSUMABLE_CTG = "inv_consumable_ctg#5m#T";
    String INV_ASSET_CTG = "inv_asset_ctg#5m#T";
    String INV_CS_MS_ITEM_LIST = "inv_consumable_item_list#5m#T";
    String INV_ASSET_MS_ITEM_LIST = "inv_asset_item_list#5m#T";

    String INV_FULL_MS_ITEM_INFO = "inv_ms_item_info#5m#T";
    String INV_ITEM_LIST = "inv_item_list#5m#T";
    String INV_STORAGE_NAME = "inv_storage_point_name#5m#T";
    String INV_STORAGE_LIST = "inv_storage_point_list#5m#T";
    String INV_SUPPLIER_LIST = "inv_supplier_list#5m#T";
}
