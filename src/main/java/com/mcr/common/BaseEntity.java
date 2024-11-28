//package com.tf.common;
//
//import com.baomidou.mybatisplus.annotation.FieldFill;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableLogic;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//
//@Data
//public class BaseEntity implements Serializable {
//
////    private static final long serialVersionUID = 1L
//    /**
//     * 搜索值
//     */
//    @JsonIgnore
//    @TableField(exist = false)
//    private String searchValue;
//    @Hidden
//    @TableLogic
//    private Boolean deleted;
//    @Hidden
//    private LocalDateTime deleteTime;
//    @TableField(fill = FieldFill.INSERT)
//    @JsonIgnore
//    @Hidden
//    private Long createBy;
//    @TableField(fill = FieldFill.UPDATE)
//    @JsonIgnore
//    @Hidden
//    private Long updateBy;
//    @TableField(fill = FieldFill.INSERT)
//    @Hidden
//    private LocalDateTime createTime;
//    @TableField(fill = FieldFill.UPDATE)
//    @Hidden
//    private LocalDateTime updateTime;
//
//    @TableField(exist = false)
//    @Hidden
//    private String createByName;
//    @TableField(exist = false)
//    @Hidden
//    private String updateByName;
//}
