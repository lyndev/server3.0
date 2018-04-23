/*
 * 文件名：Constants.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 游戏日志数据分析系统V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.common.platform.config;

/**
 * 功能描述：<code>Constants</code>是本系统全局常量类。
 * <p>
 * 
 * @author 
 * @version 1.0, 2014年4月3日 下午2:21:11
 * @since GameLoggerAnalyzer/ConfigCompenent 1.0
 */
public final class Constants {

    /**
     * 功能描述：<code>GameEventPool</code>为游戏事件常量池。
     * <p>
     * 本类定义了游戏日志采集系统采集的相关游戏事件ID。
     * 
     * @author 
     * @version 1.0, 2014年6月24日 下午2:43:28
     * @since GameLoggerAnalyzer/ConfigCompenent 1.0
     */
    public static class GameEventPool {
    	

        /** 客户端激活事件ID */
        public final static String CLIENT_GAME_ACTIVE_EVENT_ID = "U1001";

        /** 客户端内存占用事件ID */
        public final static String CLIENT_MEMORY_USAGE_EVENT_ID = "U9004";

        /** 客户端CPU占用事件ID */
        public final static String CLIENT_CPU_USAGE_EVENT_ID = "U9005";
        
        /** 玩家行为统计事件ID */
        public final static String CLIENT_PALYER_BEHAVIOR_EVENT_ID = "U9006";

        /** 登陆事件ID */
        public final static String SERVER_LOGIN_EVENT_ID = "U2002";

        /** 登出事件ID */
        public final static String SERVER_LOGOUT_EVENT_ID = "U2003";

        /** 新建角色事件ID */
        public final static String CREATE_ROLE_EVENT_ID = "U3001";

        /** 角色登录事件ID */
        public final static String ROLE_LOGIN_EVENT_ID = "U3002";

        /** 角色登出事件ID */
        public final static String ROLE_LOGOUT_EVENT_ID = "U3003";

        /** 删除角色事件ID */
        public final static String DEL_ROLE_EVENT_ID = "U3004";

        /** 更改角色等级事件ID */
        public final static String SET_ROLE_LEVEL_EVENT_ID = "U3005";

        /** 设置角色名事件ID */
        public final static String SET_ROLE_NAME_EVENT_ID = "U3006";

        /** 设置角色性别事件ID */
        public final static String SET_ROLE_SEX_EVENT_ID = "U3007";

        /** 设置角色职业事件ID */
        public final static String SET_ROLE_JOB_EVENT_ID = "U3008";

        /** 设置角色阵营事件ID */
        public final static String SET_ROLE_CAMP_EVENT_ID = "U3009";

        /** 设置角色VIP等级事件ID */
        public final static String SET_ROLE_VIP_EVENT_ID = "U3010";

        /** 角色签到事件ID */
        public final static String ROLE_SIGN_EVENT_ID = "U3011";

        /** 角色绑定事件ID */
        public final static String ROLE_BIND_EVENT_ID = "U3012";

        /** 角色转移事件ID */
        public final static String ROLE_TRANSFER_EVENT_ID = "U3013";

        /** 充值购买成功事件ID */
        public final static String PAY_BUY_EVENT_ID = "U4001";

        /** 货币变化事件ID */
        public final static String CURRENCY_CHANAGE_EVENT_ID = "U4002";

        /** 商城购买事件ID */
        public final static String STORE_BUY_EVENT_ID = "U5001";

        /** 道具变化事件ID */
        public final static String PROP_CHANAGE_EVENT_ID = "U5002";

        /** 接受任务事件ID */
        public final static String RECEIVE_TASK_EVENT_ID = "U6001";

        /** 任务完成事件ID */
        public final static String TASK_FINISH_EVENT_ID = "U6002";

        /** 任务失败事件ID */
        public final static String TASK_FAIL_EVENT_ID = "U6003";

        /** 开始关卡事件ID */
        public final static String START_LEVEL_EVENT_ID = "U7001";

        /** 关卡完成事件ID */
        public final static String LEVEL_FINISH_EVENT_ID = "U7002";

        /** 关卡失败事件ID */
        public final static String LEVEL_FAIL_EVENT_ID = "U7003";

        /** 获得资产事件ID */
        public final static String OBTAIN_RES_EVENT_ID = "U8001";

        /** 资产升级事件ID */
        public final static String RES_UPGRADE_EVENT_ID = "U8002";

        /** 失去资产事件ID */
        public final static String LOSE_RES_EVENT_ID = "U8003";

        /** 服务器在线统计事件ID */
        public final static String SERVER_ONLINE_STATS_EVENT_ID = "U9001";

        /** 服务器内存占用事件ID */
        public final static String SERVER_MEMORY_USAGE_EVENT_ID = "U9002";

        /** 服务器CPU占用事件ID */
        public final static String SERVER_CPU_USAGE_EVENT_ID = "U9003";

    }


}
