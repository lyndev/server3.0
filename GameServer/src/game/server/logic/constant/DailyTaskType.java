/**
 * @date 2014/6/16
 * @author ChenLong
 */
package game.server.logic.constant;

import game.server.logic.task.daily.bean.DailyTask;

/**
 * 日常任务类型
 *
 * @author ChenLong
 */
public enum DailyTaskType
{
    DAILY_LOGIN(1, DailyTask.class, "每日登陆"),
    NORMAL_MISSION(2, DailyTask.class, "普通副本完成X次"),
    ELITE_MISSION(3, DailyTask.class, "精英副本完成X次"),
    RESOURCE_MISSION(4, DailyTask.class, "资源副本完成X次"),
    EXPEDITION_MISSION(5, DailyTask.class,"回廊完成一次"),
    SKILL_UP(6, DailyTask.class,"技能升级"),
    ARENA_COUNT(7, DailyTask.class,"完成竞技场挑战"),
    ARENA_WIN(8, DailyTask.class,"竞技场挑战胜利X次"),
    MAKE_MONEY(9, DailyTask.class,"完成招财次数"),
    VIP_LOGIN(10, DailyTask.class,"VIP玩家登陆");
    
    
//    MAIN_MISSION(1, DailyTask.class, "完成任意普通主线关卡x次"),
//    DAILY_MISSION(2, DailyTask.class, "完成任意日常关卡x次"),
//    HERO_LEVEL_UP(3, DailyTask.class, "升级任意英雄x级"),
//    HERO_STAR_LEVEL_UP(4, DailyTask.class, "升级任意英雄x星"),
//    HERO_CHARACTER_LEVEL_UP(5, DailyTask.class, "提升任意英雄品质x次"),
//    PLUNDER(6, DailyTask.class, "掠夺x次"),
//    FUN_MISSION(7, DailyTask.class, "完成任意趣味关卡x次"),
//    CHALLENGE_MISSION(8, DailyTask.class, "完成任意挑战关卡x次"),
//    SPECIAL_MISSION(9, DailyTask.class, "完成任意特殊目标关卡x次"),
//    HERO_SKILL_UP(10, DailyTask.class, "提升任意英雄技能x次"),
//    EQUIP_STRENGTHEN(11, DailyTask.class, "装备强化x次"),
//    STATUES_RECRUIT(12, DailyTask.class, "神将招募x次"),
//    ACCUMULATIVE_CONSUME(13, DailyTask.class, "消费累计x元宝"),
//    RECHARGE(14, DailyTask.class, "充值x元宝"),
//    OPEN_LUXURY(15, DailyTask.class, "开启豪华宝箱x次"),
//    LUNCH(16, DailyTask.class, "豪华午餐"),
//    DINNER(17, DailyTask.class, "超值晚餐"),
//    MAIN_MISSION_ELITE(18, DailyTask.class, "完成任意精英主线关卡x次"),
//    ARENA(19, DailyTask.class, "参加竞技场x次"),
//    BUY_GOLD(20, DailyTask.class, "购买金币x次"),
//    BUY_ENERGY(21, DailyTask.class, "购买体力x次"),
//    SUPPER(22, DailyTask.class, "美味夜宵");

    private final int value;
    private final Class<? extends DailyTask> clazz;
    private final String desc;

    DailyTaskType(int value, Class<? extends DailyTask> clazz, String desc)
    {
        this.value = value;
        this.clazz = clazz;
        this.desc = desc;
    }

    public final int getValue()
    {
        return value;
    }

    public final Class<? extends DailyTask> getClazz()
    {
        return clazz;
    }

    public static DailyTaskType getDailyTaskType(int value)
    {
        for (DailyTaskType type : DailyTaskType.values())
        {
            if (type.getValue() == value)
                return type;
        }
        return null;
    }
}
