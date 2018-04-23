package game.server.logic.constant;

/**
 *
 * <b>各类操作原因定义.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public enum Reasons
{

    //------------  物品相关操作：1000 ~ 1099 ------------/
    /**
     * 使用物品
     */
    ITEM_USE(1000, "使用物品"),
    /**
     * 出售物品
     */
    ITEM_SELL(1001, "出售物品"),
    /**
     * 物品回收
     */
    ITEM_RECOVERY(1002, "物品回收"),
    /**
     * 物品分解
     */
    ITEM_CELL(1003, "物品分解"),
    /**
     * 物品合成
     */
    ITEM_COMBINE(1004, "物品合成"),
    /**
     * 物品购买
     */
    ITEM_BUY(1005, "物品购买"),
    /**
     * 卡牌激活消耗材料
     */
    ITEM_CARD_ACTIVE(1020, "卡牌招唤消耗材料"),
    /**
     * 卡牌进阶消耗材料
     */
    ITEM_CARD_QUALITY(1021, "卡牌进阶消耗材料"),
    /**
     * 卡牌碎片融合消耗材料
     */
    ITEM_CARD_CHIPMERGE(1022, "卡牌碎片融合消耗材料"),
    /**
     * 卡牌拆分获得道具
     */
    ITEM_CARD_SPLIT(1023, "卡牌拆分获得道具"),
    /**
     * 装备精炼
     */
    EQUIP_REFINE(1030, "装备精炼"),
    /**
     * 装备强化
     */
    EQUIP_LEVELUP(1031, "装备强化"),
    /**
     * GM添加道具
     */
    ITEM_GM(1032, "GM添加道具"),
    /**
     * 仙府采集
     */
    ITEM_XF(1033, "仙府采集"),
    /**
     * 掠夺/被掠夺
     */
    ITEM_PLUNDER(1034, "掠夺/被掠夺"),
    /**
     * 抽奖道具变化
     */
    ITEM_LOTTERY(1035, "抽奖道具变化"),
    /**
     * 打开宝箱
     */
    OPEN_BOXES(1036, "打开宝箱"),
    /**
     * 签到增加道具
     */
    ITEM_SIGNIN(1037, "签到增加道具"),
    /**
     * 打开礼包
     */
    OPEN_GIFTS(1038, "打开礼包"),
    /**
     * 天梯战斗奖励
     */
    SKYWARD_REWARD(1039, "天梯战斗奖励"),
    /**
     * 天梯通关奖励
     */
    SKYWARD_PASS_REWARD(1040, "天梯通关奖励"),
    /**
     * 新手引导
     */
    GAME_GUID(1041, "新手引导"),
    /**
     * 购买体力
     */
    BUY_ENERGY(1042, "购买体力"),
    /**
     * 装备穿戴
     */
    EQUIP_DRESS(1043, "装备穿戴"),
    /**
     * 装备卸下
     */
    EQUIP_COMBINE(1044, "装备合成"),
    /**
     * 使用激活码/兑换码
     */
    USE_KEY(1045, "使用激活码/兑换码"),
    /**
     * 购买技能点
     */
    BUY_SKILLPOINT(1046, "购买技能点"),
    BUY_ARENA(1047, "购买竞技场挑战次数"),
    GOLD_LOTTERY(1048, "金币抽奖"),
    GEM_LOTTERY(1049, "钻石抽奖"),
    GEM_INSET(1050, "镶嵌宝石"),
    GEM_UNSET(1051, "卸下宝石"),
    GEM_COMBINE(1052, "宝石合成"),
    //------------ 卡牌相关操作：1100~1199 ------------/
    /**
     * 卡牌激活
     */
    CARD_ACTIVE(1100, "卡牌激活"),
    /**
     * 卡牌进阶
     */
    CARD_QUALITY(1101, "卡牌进阶"),
    /**
     * 卡牌升级
     */
    CARD_LVLUP(1102, "卡牌升级"),
    /**
     * 卡牌继承
     */
    CARD_INHERIT(1103, "卡牌继承"),
    /**
     * 卡牌升级
     */
    CARD_LEVELUP(1104, "卡牌升级"),
    /**
     * 卡牌粹取
     */
    CARD_EXTRACT(1105, "卡牌粹取"),
    /**
     * 卡牌合成
     */
    CARD_COMPOUND(1106, "卡牌组合"),
    //卡牌升星
    CARD_UP_STAR_LV(1107, "卡牌升星"),
    //技能升级
    CARD_SKILL_UP(1108, "卡牌技能升级"),
    LEADER_STAR_UP(1109, "主角升星"),
    //------------ 任务相关操作 1200~1299 ------------/
    /**
     * 任务奖励
     */
    TASK_AWARD(1200, "任务奖励"),
    /**
     * 日常任务奖励
     */
    DAILT_TASK_AWARD(1201, "日常任务奖励"),
    //------------ 关卡相关操作 1300~1399 ------------/
    /**
     * 关卡掉落
     */
    MISSION_DROP(1300, "关卡掉落"),
    /**
     * 关卡开始
     */
    MISSION_START(1301, "关卡开始"),
    /**
     * 关卡战败
     */
    MISSION_DEFEAT(1302, "关卡战败"),
    /**
     * 关卡取消
     */
    MISSION_CANCEL(1304, "关卡取消"),
    /*
     * 关卡扫荡
     */
    MISSION_RAID(1305, "关卡扫荡"),
    /**
     * 重置关卡
     */
    MISSION_RESET(1306, "重置关卡"),
    /**
     * 日常关卡掉落
     */
    DAILYMISSION(1310, "日常关卡掉落"),
    /**
     * 趣味关卡奖励
     */
    FUNMISSION(1320, "趣味关卡奖励"),
    REWARD_SCOREMISSION(1330, "积分关卡发奖励"),
    FIGHT_SCOREMISSION(1331, "请求挑战积分关卡"),
    BUY_SCOREMISSION(1332, "购买积分关卡挑战次数"),
    //------------ 成就相关操作 1400~1499 ------------/
    /**
     * 成就奖励
     */
    ACHIEVEMENT_AWARD(1400, "成就奖励"),
    //------------ 仙府相关操作 1500~1599 ------------/
    /**
     * 进行仙府掠夺
     */
    XF_ATTACK(1500, "进行仙府掠夺"),
    /**
     * 领取仙府防守奖励
     */
    XF_DEFENSE_AWARD(1501, "领取仙府防守奖励"),
    //------------ 邮件相关操作 1600~1699 ------------/
    /**
     * 邮件附件获取
     */
    MAIL(1600, "邮件附件获取"),
    //------------ 商店相关操作 1700~1799 ------------/
    /**
     * 刷新神秘商店
     */
    STORE_FLUSH_MAGIC(1700, "刷新神秘商店"),
    /**
     * 刷新藏宝阁
     */
    STORE_FLUSH_COLLECTION(1701, "刷新藏宝阁"),
    /**
     * 购买商品
     */
    BUY_GOODS(1702, "购买商品"),
    /**
     * 刷新黑市商店
     */
    STORE_FLUSH_DARK(1703, "刷新黑市商店"),
    STORE_FLUSH_NORMAL(1704, "刷新普通商店"),
    STORE_FLUSH_ARENA(1705, "刷新竞技场商店"),
    STORE_FLUSH_EXPEDITION(1706, "刷新远征商店"),
    //------- 特殊目标关卡相关操作 1800~1899 -------/
    /**
     * 特殊目标关卡掉落
     */
    SPECIAL_MISSION_DROP(1800, "特殊目标关卡掉落"),
    //-------- 购买金币相关操作 1900~1999 --------/
    /**
     * 购买金币消耗
     */
    BUY_GOLD(1900, "购买金币消耗"),
    //-------- 召唤师相关操作 2000~2099 --------/
    /**
     * 召唤师升级
     */
    PLAYER_LEVEL_UP(2000, "主升升级"),
    //-------- 成长计划相关操作 2100~2199 --------/
    /**
     * 购买成长计划
     */
    BUY_GROWTH_PLANS(2100, "购买成长计划"),
    /**
     * 成长计划奖励
     */
    GROWTH_PLANS_AWARD(2101, "成长计划奖励"),
    //-------- 竞技场相关操作 2200~2299 --------/
    /**
     * 竞技场挑战
     */
    ARENA_ATTACK(2200, "竞技场挑战"),
    /**
     * 竞技场最高排名
     */
    ARENA_MAX_RANK(2201, "竞技场最高排名"),
    /**
     * 竞技场手动清除CD
     */
    ARENA_CLEAR_CD(2202, "竞技场手动清除CD"),
    //-------- 充值相关操作 2300~2399 --------/
    /**
     * 充值
     */
    RECHARGE(2300, "充值"), // 充值
    /**
     * 购买Vip礼包
     */
    BUY_VIP_GIFT(2301, "购买Vip礼包"),
    /**
     * 购买VIP礼包奖励
     */
    VIP_GIFT_AWARD(2302, "购买VIP礼包奖励"),
    /**
     * 首充奖励
     */
    FIRST_RECHARGE_AWARD(2303, "首充奖励"),
    //----------- 夺宝相关 2400 -2499----------------/
    /**
     * 夺宝失去道具
     */
    GRAB_DEL_ITEM(2400, "夺宝失去道具"),
    /**
     * 夺宝获得道具
     */
    GRAB_ADD_ITEM(2401, "夺宝获得道具"),
    /**
     * 夺宝失去资源
     */
    GRAB_DEL_RESOURCE(2402, "夺宝失去资源"),
    /**
     * 夺宝获得资源
     */
    GRAB_ADD_RESOURCE(2403, "夺宝获得资源"),
    /**
     * 返利
     */
    REBATE(2404, "返利"),
    //------------- 好友相关2500
    FRIEND_PRESENT(2500, "好友赠送"),
    /**
     * 问卷奖励2600
     */
    QUESTION_ADD_RESOURCE(2600, "问卷奖励"),
    /**
     * 资源副本
     */
    RESOURCE_MISSION(2601, "资源副本"),
    /**
     * 远征
     */
    EXPEDITION_MISSION(2602, "远征"),
    /**
     * 恶魔之魂
     */
    DEMON_SOUL(2603, "恶魔之魂"),
    /**
     * 点金手
     */
    GOLDEN_TOUCH(2604, "点金手"),
    /**
     * 修改名称
     */
    CHANGE_ROLENAME(2065, "修改名称"),
    /**
     * VIP奖励
     */
    VIP_REWARD(2066, "VIP等级奖励"),
    /**
     * 商城
     */
    GEM_STORE(2067, "商城"),
    /**
     * 运营活动
     */
    OPER_ACTIVITY(2068, "运营活动"),
    /**
     * 世界Boss
     */
    WORLD_BOSS(2069, "世界Boss"),
    /**
     * 创建房间
     */
    CRREATE_ROOM(2070, "创建房间");

    private final int value;
    private final String desc;

    Reasons(int value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public int value()
    {
        return value;
    }

    public String getFullDesc()
    {
        return Integer.toString(value) + desc;
    }

    public boolean compare(int value)
    {
        return this.value == value;
    }
}
