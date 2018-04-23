package game.server.logic.constant;

/**
 *
 * <b>错误码定义.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public enum ErrorCode
{
    //------------ 系统错误码：100 ~ 199 ------------/
    /**
     * 请求已过期
     */
    REQUEST_OVERTIME(100),
    /**
     * 已达上限
     */
    IS_LIMIT(101),
    /**
     * 服务器繁忙
     */
    SERVER_BUSY(102),
    /**
     * 配置错误
     */
    WRONG_CONFIG(103),
    //------------ 通用错误码：200 ~ 299 ------------/
    /**
     * 操作失败
     */
    OPERATION_FAILED(200),
    /**
     * 重复操作
     */
    OPERATION_REPETITIVE(201),
    /**
     * 金币不够
     */
    GOLD_NOT_ENOUGH(202),
    /**
     * 元宝不够
     */
    GOLD_BULLION_NOT_ENOUGH(203),
    /**
     * 体力不够
     */
    ENERGY_NOT_ENOUGH(204),
    /**
     * 精力不够
     */
    MANA_NOT_ENOUGH(205),
    /**
     * 星魂不够
     */
    STAR_SOUL_NOT_ENOUGH(206),
    /**
     * 荣誉值不够
     */
    HONOR_NOT_ENOUGH(207),
    /**
     * 没有匹配到对手
     */
    MATCHING_FAILED(220),
    /**
     * 记录已失效
     */
    RECORD_OVERTIME(221),
    /**
     * 奖励已经领取
     */
    AWARD_ALREADY(222),
    /**
     * 没有找到指定对手
     */
    ENEMY_NOT_FOUND(223),
    /**
     * 指定对手已经阵亡
     */
    ENEMY_IS_DEAD(224),
    /**
     * 关卡尚未通关
     */
    LEVEL_NOT_PASS(225),
    /**
     * 今日已无次数
     */
    TODAY_NOT_NUM(226),
    /**
     * 功能冷却中
     */
    FUNCTION_IS_CD(229),
    /**
     * 资源不够
     */
    RESOURCE_NOT_ENOUGH(230),
    //------------ 角色错误码：300 ~ 399 ------------/
    /**
     * 角色等级不够
     */
    ROLE_LEVEL_NOT_ENOUGH(300),
    //------------ 卡牌错误码：400 ~ 499 ------------/
    /**
     * 没有选择卡牌
     */
    CARD_NOT_SELECT(400),
    /**
     * 卡牌等级不够
     */
    CARD_LEVLE_NOT_ENOUGH(401),
    /**
     * 卡牌星级不够
     */
    CARD_STAR_NOT_ENOUGH(402),
    /**
     * 卡牌职业不符
     */
    CARD_PROFESSION_INCONSISTENT(403),
    /**
     * 卡牌不存在
     */
    CARD_NOT_FOUND(404),
    /**
     * 卡牌已在别处驻防
     */
    CARD_IS_DEFENSE(405),
    /**
     * 卡牌已经阵亡
     */
    CARD_IS_DEAD(406),
    //------------ 背包错误码：500 ~ 599 ------------/
    /**
     * 该格子已满
     */
    GRID_IS_FULL(500),
    /**
     * 物品不存在
     */
    ITEM_NOT_EXIST(501),
    /**
     * 物品数量错误
     */
    ITEM_NUM_ERROR(502),
    /**
     * 物品数量不够
     */
    ITEM_NOT_ENOUGH(503),
    /**
     * 物品不能使用
     */
    ITEM_CAN_NOT_USE(504),
    /**
     * 物品未到使用时间
     */
    ITEM_NOT_USE_TIME(505),
    /**
     * 物品使用时间已过
     */
    ITEM_IS_EXPIRED(506),
    /**
     * 没有解锁物品
     */
    NOT_UNLOCK_ITEM(507),
    /**
     * 道具重复
     */
    PROPS_DOPLICATE(508),
    /**
     * 装备库已满
     */
    EQUIP_PACK_IS_FULL(509),
    /**
     * 格子不存在
     */
    GRID_NOT_EXIST(510),
    //------------ 装备错误码：600 ~ 649 ------------/  
    /**
     * 找不到装备
     */
    EQUIP_NOT_FOUND(601),
    /** 背包中装备已经存在，无需合成 */
    EQUIP_IS_EXIST(602),
    /**
     * 装备等级大于等于主角等级
     */
    EQUIP_LEVEL_GE_ROLE(604),
    /**
     * 专属装备已达最高品质
     */
    EQUIP_MAX_QUALITY(606),
    /**
     * 穿戴装备的位置不对 
     */
    EQUIP_DRESS_POS_ERROR(610),
    /** 
     * 该位置已经穿戴装备 
     */
    EQUIP_DRESS_EXIST(611),
    /** 
     * 装备穿戴出错 
     */
    EQUIP_DRESS_REEOR(612),
    /** 
     * 装备融合出错 
     */
    EQUIP_QUALITYUP_REEOR(613),
    /** 
     * 装备合成错误 
     */
    EQUIP_COMBINE_ERROR(614),

    //------------ 宝石错误码：650 ~ 699 ------------/  
    /**
     * 找不到宝石
     */
    GEM_NOT_FOUND(650),
    /**
     * 错误的镶嵌位置
     */
    GEM_ERROR_POS(651),
    /**
     * 宝石位置没解锁
     */
    GEM_POS_UNLOCK(652),
    /**
     * 改位置已经镶嵌宝石
     */
    GEM_ALREADY_INSET(653),
    /**
     * 该位置没有镶嵌宝石
     */
    GEM_NOT_INSET(654),
    /**
     * 必须是三个宝石才能合成一个
     */
    GEM_COMBINE_NUM_3(655),
    /**
     * 配置表中找不到宝石的数据
     */
    GEM_NOT_FOUND_TEMPLATE(656),
    /**
     * 三个宝石的等级不相同
     */
    GEM_LEVEL_DIFFERENT(657),
    /**
     * 宝石颜色属性对应表模板数据找不到
     */
    GEM_COLOR_ATTR_NOT_FOUNT(658),
    /**
     * 不能镶嵌相同类型的宝石
     */
    GEM_ATTR_IS_SAME(659),
   
    //------------ 关卡错误码：700 ~ 799 ------------/
    //------------ 任务错误码：800 ~ 899 ------------/
    /**
     * 找不到该日常任务
     */
    CAN_NOT_FIND_DAILY_TASK(800), 
    /**
     * 不能领取日常任务奖励
     */
    CAN_NOT_GET_ONE_DAILY_TASK_AWARD(801), 
    /**
     * 找不到该主线任务
     */
    CAN_NOT_FIND_MAIN_TASK(802), 
    /**
     * 不能领取主线任务奖励
     */
    CAN_NOT_GET_ONE_MAIN_TASK_AWARD(803),
    //------------ 仙府错误码：900 ~ 999 ------------/
    /**
     * 仙府不存在
     */
    XF_NOT_FOUND(900),
    /**
     * 未解锁仙府战场
     */
    XF_BF_UNLOCK(901),
    /**
     * 仙府星级不匹配
     */
    XF_NOT_MATCH(902),
    /**
     * 仙府正在战斗中
     */
    XF_IS_FIGHTING(903),
    /**
     * 仙府处于保护期
     */
    XF_IS_SAFE(904),
    /**
     * 每次被掠夺后只能报复一次
     */
    XF_REVENGE_ALREADY(905),
    /**
     * 仙府被他人锁定
     */
    XF_IS_OTHER_LOCK(906),
    /**
     * 仙府被锁定或者战斗中
     */
    XF_IS_LOCK_OR_FIGHTING(907),
    /**
     * 仙府等级不够
     */
    XF_LEVEL_NOT_ENOUGH(908),
    /**
     * 未选择仙府战场
     */
    XF_NOT_CHOOSE_BF(909),
    /**
     * 卡牌星级大于战场星级
     */
    XF_CARD_STAR_ERROR(910),
    //------------ 商店错误码：1000 ~ 1099 ------------/
    /**
     * 藏宝阁没有开启或者已经关闭
     */
    STORE_COLLECTION_NOT_FOUND(1000),
    /**
     * 神秘商店尚未开启
     */
    STORE_MAGIC_NOT_ENABLE(1001),
    /**
     * 没有找到指定商品
     */
    GOODS_NOT_FOUND(1002),
    /**
     * 指定商品已经售罄
     */
    GOODS_IS_EMPTY(1003),
    /**
     * 已达到购买上限
     */
    BUY_IS_LIMIT(1004),
    /**
     * 黑市商店尚未开启
     */
    STORE_DARK_NOT_ENABLE(1005),
    /** 商店未开启 */
    STORE_NOT_ENABLE(1006),
    //------------ 天梯错误码：1100 ~ 1199 ------------/
    /**
     * 天梯尚未激活
     */
    SKYWARD_LOCK(1100),
    /**
     * 没有关卡数据
     */
    SKYWARD_NOT_LEVLE_DATA(1101),
    /**
     * 正在挑战其他关卡
     */
    SKYWARD_IS_FIGHTING(1102),
    /**
     * 挑战已经结束
     */
    SKYWARD_IS_OVER(1103),
    //------------ 竞技场错误码：1200 ~ 1299 ------------/
    /**
     * 正在发起挑战请求
     */
    ARENA_IS_FIGHTING(1200),
    /**
     * 挑战已经结束
     */
    ARENA_IS_OVER(1201),
    /**
     * 尚未初始化名次
     */
    ARENA_NOT_RANK(1202),
    /**
     * 对手正在战斗中
     */
    ENEMY_IS_FIGHTING(1203),
    /**
     * 不能挑战指定对手
     */
    ENEMY_NOT_ATTACK(1204),
    /**
     * 对手名次发生改变
     */
    ENEMY_RANK_CHANGE(1025),
    
    //------------ 返利错误码：1300 ~ 1399 ------------/
    /**
     * 找不到返利活动
     */
    REBATE_NOT_FOUND(1300),
    /**
     * 找不到返利奖励
     */
    REBATE_REWARD_NOT_FOUND(1301),
    /**
     * 返利活动未开启
     */
    REBATE_NOT_OPEN(1302),
    /**
     * 额度未达到领奖要求
     */
    AMOUNT_NOT_ENOUGH(1303),
    /**
     * 导演等级不够
     */
    LEVEL_NOT_ENOUGH(1304),
    /**
     * VIP等级不够
     */
    VIP_NOT_ENOUGH(1305),
    /**
     * 已经领取
     */
    REWARD_ALREADY_GET(1306),
    //------------ 特殊关卡错误码：1400 ~ 1499 ------------/
    /**
     * 错误的关卡id
     */
    WRONG_MISSION_ID(1400),
    /**
     * 塔未开启
     */
    TOWER_NOT_OPEN(1401),
    /**
     * 关卡配置缺失
     */
    MISSION_CONFIG_NOT_FOUND(1402),
    /**
     * 塔当日已挑战通过
     */
    TOWER_ALREADY_PASS(1403),
    /**
     * 当日挑战次数已用完
     */
    OUT_OF_COUNT(1404),
    /**
     * 卡牌不符合关卡的特定要求
     */
    CARDS_NOT_FIT(1405),
    /**
     * 冷却时间未到
     */
    NOT_COOL_DOWN(1406),
    /**
     * 挑战未通过(正常挑战, 结果失败)
     */
    FIGHT_FAILED(1408),
    //------------ 激活码错误码：1500 ~ 1599 ------------/
    /**
     * 激活码不存在
     */
    KEY_NOT_FOUND(1500),
    /**
     * 激活码已经过期
     */
    KEY_TIME_EXPIRED(1501),
    /**
     * 激活码使用失败
     */
    KEY_USE_FAILED(1502),
    /**
     * 激活码已经被使用
     */
    KEY_IS_USED(1503),
    /**
     * 你已经使用过该类激活码
     */
    KEY_HAS_BEEN_USED(1504),
    /**
     * 当前渠道不能使用该激活码
     */
    KEY_PLATFORM_ERROR(1505),
    /**
     * 当前游戏不能使用该激活码
     */
    KEY_GAME_ERROR(1506),
    /**
     * 激活码未到使用时间
     */
    KEY_TIME_BEFORE(1507),
    
    //-------------------------通用错误码2000 ~ 2099-------------------------//
    /**
     * 操作成功
     */
    OK(2000),
    
    /**
     * 配置表错误
     */
    TEMPLATE_DATA_ERROR(2001),
    
    /**
     * 功能没开放
     */
    FUNCTION_NOT_OPEN(2002),
    
    /**
     * 请求消息为空
     */
    NULL_MESSAGE(2003),
    
    /**
     * 没有找到玩家数据
     */
    WPLAYER_NOT_FOUND(2004),
    
    /**
     * 金币不够
     */
    LACK_COIN(2005),
    
    /**
     * 钻石不够
     */
    LACK_GEM(2006),
    
    /**
     * 次数用完
     */
    LACK_TIMES(2007),
    
    /**
     * 材料不足
     */
    LACK_MATERIAL(2008),
    
    /**
     * 消息参数错误
     */
    PARAM_ERROR(2009),
    
    /**
     * 功能未开放
     */
    FUNC_NOT_OPEN(2010),
    
    //------------------------角色错误码 2100 ~ 2199---------------------------
    
    /**
     * 角色名称是空的
     */
    USERNAME_IS_EMPTY(2100),
    /**
     * 角色名称含有空格
     */
    USERNAME_HAS_SPACE(2101),
    /**
     * 昵称相同
     */
    USERNAME_IS_SAME(2102),
    /**
     * 角色名称包含特殊字符
     */
    USERNAME_HAS_SPECIAL_CHAR(2103),
    /**
     * 角色昵称包含敏感词
     */
    USERNAME_HAS_DIRTY_WORD(2104),
    /**
     * 角色已经被创建
     */
    USERNAME_IS_CREATED(2105),
    /**
     * 角色已经被创建
     */
    USERNAME_LENGTH_OVER(2106),
    
    //-------------------------远征误码2200 ~ 2299-------------------------//
    /**
     * 不能进入节点
     */
    CAN_NOT_ENTER(2200),
    
    //-------------------------聊天、好友误码2300 ~ 2399-------------------------//
    /**
     * 消息太频繁
     */
    CHAT_FORBIDDEN(2301),
    
    /**
     * 功能没开启
     */
    CHAT_NOT_OPEN(2302),
    
    /**
     * 语音消息为空
     */
    NULL_AUDIO_MESSAGE(2303),
    
    /**
     * 语音消息数据为空
     */
    AUDIO_MESSAGE_NO_DATA(2304),
    
    /**
     * 发送消息到未知的频道
     */
    UNKOWN_CHANNEL(2305),
    
    /**
     * 世界聊天未开启
     */
    WORLD_CHAT_NOT_OPEN(2306),
    
    /**
     * 世界频道CD没到
     */
    WORLD_CHAT_CD(2307),
    
    /**
     * 私聊未开启
     */
    PRIVATE_CHAT_NOT_OPEN(2308),
    
    /**
     * 目标玩家没有找到, 玩家userId为空
     */
    NO_TARGET_WPLAYER(2309),
    
    /**
     * 不能和自己聊天
     */
    CHAT_ONESELF(2310),
    
    /**
     * 添加聊天会话失败
     */
    NO_CHAT_CONVERSATION(2311),
    
    /**
     * 数据库中没有找到对应的语音消息记录
     */
    AUDIO_MSG_NOT_FOUND(2312),
    
    /**
     * 主角等级未达到开启好友系统等级限制
     */
    FRIEND_LEADER_LV_TOO_LOW(2350),
    
    /**
     * 找不到对方信息
     */
    FRIEND_NO_USER_INFO(2351),
    
    /**
     * 对方已经是好友
     */
    FRINED_ALREADY_FRIEND(2352),
    
    /**
     * 自己好友已满
     */
    FRINED_CNT_MAX(2353),
    
    /**
     * 不能添加自己为好友
     */
    FRINED_CAN_NOT_ADD_SELF(2354),
    
    /**
     * 自己的好友数据异常
     */
    SELF_DETAIL_EXCEPTION(2355),
    
    /**
     * 待加添加好友的数据异常
     */
    FRIEND_DETAIL_EXCEPTION(2356),
    
    /**
     * 对方还不是自己的好友
     */
    NOT_YOU_FRIEND(2357),
    
    //-------------------------关卡错误码2400 ~ 2499-------------------------//
    //----------宝箱
    /**
     * 无此章节数据
     */
    NO_CHAPTER_DATA(2400),
    
    /**
     * 章节奖励包索引错误
     */
    CHAPTER_BAG_INDEX_ERROR(2401),
    
    /**
     * 已经领取过章节宝箱
     */
    HAVE_GET_CHAPTER_BOX(2402),
    
    /**
     * 星级不够
     */
    NOT_ENOUGTH_STAR_CNT(2403),
    
    /**
     * 领取奖励失败
     */
    CHAPTER_GET_AWARD_FAILED(2404),

    //---------挑战关卡
    /**
     * 可以进入关卡
     */
    ENTER_OK(2405),
    /**
     * 体力不足
     */
    SHORT_OF_ENERGE(2406),
    /**
     * 次数不足
     */
    SHORT_OF_COUNT(2407),
    /**
     * 小关不可重复挑战
     */
    MINI_MISSION(2408),
    /**
     * 客户端发送了错误的关卡ID, 可能找不到关卡, 可能关卡还不能挑战
     */
    WRONG_ID(2409),
    /**
     * 主角等级不够开启对应章节
     */
    XF_LEVEL(2410),

    /**
     * 精英关卡没解锁
     */
    ELITE_UNLOCKED(2411),
    
    //-----------扫荡
    /**
     * 体力不足
     */
    RAID_ENERGY(2412),
    /**
     * 黄牛票不足
     */
    RAID_TICKET(2413),
    /**
     * 关卡未通关
     */
    RAID_NOTPASS(2414),
    /**
     * 关卡每日次数不足
     */
    RAID_MISSION_COUNT(2415),
    /**
     * 小关不可扫荡
     */
    RAID_MINI(2416),
    
    //-------验证
    /**
     * 验证失败
     */
    VERIFY_FAILED(2416),
    /**
     * 其他情况
     */
    VERIFY_OTHER(2417),
    
    //-------------------------关卡错误码2500 ~ 2599-------------------------//
    /**
     * 功能没开启
     */
    NOT_OPEN(2500),
    /**
     * 已达到最高级
     */
    TOP_LEVEL(2501),
    /**
     * 金币或钻石不够
     */
    LACK_MONEY(2502),
    /**
     * 本阶经验已满，不能再填充
     */
    FULL_EXP(2503),
    /**
     * 主角等级不够
     */
    LOW_LEVEL(2505),
    /**
     * 经验没满，不能进阶
     */
    LOW_EXP(2506),
    /**
     * 不是本阶的最后等级，不能进阶
     */
    CAN_NOT_STAGE(2507),
    
    //-------------------------VIP错误码2600 ~ 2699-------------------------//
    /**
     * 请求等级>已经开通等级
     */
    VIPLV_NOT_OPEN(2600),

    /**
     * 此等级已经领了奖励
     */
     VIP_HAVE_REWARD(2601),
     
    //----------------------商城错误码2700 ~ 2799--------------------------//
     /**
      * 此次购买已经数量已经超过今日购买最大次数
      */
     GEM_STORE_OUT_TIMES(2701),
     
     /**
      * 无权限购买此物品
      */
     GEM_STORE_NORIGHT_TO_BUY(2702),
     
     /**
      * 当前VIP等级没有可购买的商品
      */
     GEM_STORE_NOITEM_TO_BUY(2703),
     
     //----------------------签到错误码2800 ~ 2899--------------------------//
     /**
      * 今日重复签到
      */
     SIGN_REPEATED(2801),
     
     /**
      * 客户端签到次数错误
      */
     SIGN_TIMES_ERROR(2802),
     
     /**
      * 签到奖励数据为空
      */
     SIGN_AWARD_EMPTY(2803),
     
     /**
      * 签到奖励数据格式错误
      */
     SIGN_AWARD_FORMAT_ERROR(2804),
     
     /**
      * 不能补领签到奖励
      */
     SIGN_CAN_NOT_EXTRA_GET(2805),
     
     /**
      * 只能补签当天的
      */
     CAN_NOT_BEFORE(2806),
     
     //----------------------签到错误码2900 ~ 2999-------------------------// 
     /**
      * 积分关卡未解锁（等级解锁）
      */
    SCOREMISSION_UNLOCK(2900),
     
    /**
     * 挑战次数不够 
     */
    FIGHTNUM_NOT_ENOUGH(2901),
    
    /**
     * 不是正在挑战的关卡
     */
    ISNT_FIGHT_MISSION(2902),
     /**
      * 当前
      */
     
    //----------------------活动错误码3000 ~ 3099--------------------------//
    /**
     * 不能领取奖励
     */ 
     OPER_CANT_REWARD(3000),
     
    //----------------------世界Boss错误码3100 ~ 3199--------------------------// 
    /**
     * 次数不足
     */
    BOSS_LACK_TIMES(3101),
   
    /**
     * 功能未解锁
     */
    BOSS_FUNC_UNACTIVE(3102),
     
    /**
     * 当前时间未开启
     */
    BOSS_DATE_UNOPEN(3103), 

    /**
     * 难度是否解锁
     */
    BOSS_DIFFCULT_UNLOCK(3104),
    
    /**
     * 没有请求挑战，直接提交数据
     */
    BOSS_CLG_NO_REQFIGHT(3105),
    ; 
    private final int value;

    ErrorCode(int value)
    {
        this.value = value;
    }

    public int getCode()
    {
        return value;
    }

    public boolean compare(int value)
    {
        return this.value == value;
    }

}
