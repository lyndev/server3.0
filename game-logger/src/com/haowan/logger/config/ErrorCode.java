/*
 * 文件名：ErrorCode.java
 * 版权：Copyright 2012-2014 Chengdu HaoWan123 Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 游戏数据采集平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.haowan.logger.config;

/**
 * 功能描述：游戏日志错误码枚举器。
 * <p>
 * 
 * @author andy.zheng 
 * @version 1.0, 2014年6月13日 下午4:09:37
 * @since Common-Platform/ 1.0
 */
public enum ErrorCode {
    
    /** 成功 */
    SUCCESS(0, "成功"),

    /** 非法参数错误 */
    INVALID_PARAMETER_ERROR(-1, "非法参数错误！"),
    
    /** 写入日志失败 */
    SAVE_LOG_ERROR(-2, "记录日志失败 ！"),
    
    /** 数据库连接失败 */
    CONNECT_DB_FAIL(-3, "数据库连接失败 ！");

    /** 错误码 */
    private int code;

    /** 错误描述 */
    private String desc;

    /**
     * 功能描述：
     * 
     * @param code
     * @param desc
     */
    private ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
