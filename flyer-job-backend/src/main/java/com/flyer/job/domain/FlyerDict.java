package com.flyer.job.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class FlyerDict implements Serializable {

    private static final long serialVersionUID = -5977154079788417900L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 分类
     */
    private Integer fieldType;
    /**
     * 字段code
     */
    private String fieldCode;
    /**
     * 字段值
     */
    private String fieldValue;
    /**
     * 字段描述
     */
    private String fieldDesc;
    /**
     * 0 可用 1不可用
     */
    private Integer isEnabled;
    /**
     * 创建时间
     */
    private java.util.Date createTime;

    private java.sql.Timestamp timestamp;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
