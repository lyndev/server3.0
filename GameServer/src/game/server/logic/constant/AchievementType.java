package game.server.logic.constant;

/**
 * 成就类型枚举
 * <p>该成就完成条件的类型:等级, 次数, 排名, etc
 * 
 * @author ZouZhaopeng
 */
public enum AchievementType
{
    /**
     * 1. 仙府星级
     */
    XF_STAR(1),
    /**
     * 2. 召唤师等级
     */
    INVOKER_LEVEL(2),
    /**
     * 3. 通过指定关卡
     */
    MISSION_PASS(3),
    /**
     * 4. 所有普通关卡通关次数
     */
    ORDINARY_MISSION_COUNT(4),
    /**
     * 5. 所有精英关卡通关次数
     */
    ELITE_MISSION_COUNT(5),
    /**
     * 6. 所有英雄关卡通关次数
     */
    HERO_MISSION_COUNT(6),
    /**
     * 7. 关卡评价(星级)
     */
    MISSION_STAR(7),
    /**
     * 8. 收集卡牌数量
     */
    CARD_COUNT(8),
    /**
     * 9. 提升卡牌星级(所有卡牌中星级最高的)
     */
    CARD_STAR(9),
    /**
     * 10. 提升卡牌品质(所有卡牌中品质最高的)
     */
    CARD_QUALITY(10),
    /**
     * 11. 收集金币(当前金币? 历史金币?)
     */
    GOLD_AMOUNT(11),
    /**
     * 12. 收集元宝(同上)
     */
    BULLION_AMOUNT(12),
    /**
     * 13. 收集武魂
     */
    HERO_SOUL_AMOUNT(13),
    /**
     * 14. 掠夺金币
     */
    PLUNDER_GOLD_AMOUNT(14),
    /**
     * 15. 被掠夺金币
     */
    PLUNDERED_GOLD_AMOUNT(15),
    /**
     * 16. 掠夺成功次数
     */
    PLUNDER_SUCCESS_COUNT(16),
    /**
     * 17. 防守掠夺成功次数
     */
    DEFEND_SUCCESS_COUNT(17),
    /**
     * 18. 获得指定品质指定数量装备
     */
    EQUIPMENT_QUALITY_AMOUNT(18),
    /**
     * 19. 竞技场挑战成功次数
     */
    PVP_CHALLENGE_SUCCESS_COUNT(19),
    /**
     * 20. 竞技场防守成功次数
     */
    PVP_DEFEND_SUCCESS_COUNT(20),
    /**
     * 21. 竞技场排名
     */
    PVP_RANK(21),
    /**
     * 22. 指定普通章节所有关卡达到指定星级
     */
    ORDINARY_CHAPTER_STAR(22),
    /**
     * 23. 签到
     */
    SIGNIN_COUNT(23),
    /**
     * 24. 加入战场
     */
    BATTLEFIELD_LEVEL(24),
    /**
     * 25. 进入讨伐战斗
     */
    BATTLE_COUNT(25),
    /**
     * 26. 指定精英章节所有关卡达到指定星级
     */
    ELITE_CHAPTER_STAR(26),
    /**
     * 27. 指定英雄章节所有关卡达到指定星级
     */
    HERO_CHAPTER_STAR(27),
    /**
     * 28. 指定普通关卡通关次数
     */
    DESIGNATE_ORDINARY_MISSION_COUNT(28),
    /**
     * 29. 指定精英关卡通关次数
     */
    DESIGNATE_ELITE_MISSION_COUNT(29),
    /**
     * 30. 趣味关卡的总通关次数
     */
    FUNMISSION_COUNT(30),
    /**
     * 31. 试练塔的总通关次数
     */
    TOWER_COUNT(31),
    /**
     * 32. 日常关卡总通关次数
     */
    DAILY_MISSION_COUNT(32),
    /**
     * 33. 天梯的挑战成功次数
     */
    SKYWARD_COUNT(33),
    /**
     * 34. 竞技排名
     */
    ARENA_RANK(34),
    /**
     * 35. 购买金币次数
     */
    BUY_GOLD_COUNT(35),
    /**
     * 36. 购买体力次数
     */
    BUY_ENERGY_COUNT(36),
    /**
     * 37. 开启宝箱次数
     */
    OPEN_BOX_COUNT(37),
    /**
     * 38. 卡牌升级到指定等级的数量
     */
    CARD_LEVEL_AMOUNT(38),
    /**
     * 39. 技能升级到指定等级的数量
     */
    SKILL_LEVEL_AMOUNT(39),
    /**
     * 40. 卡牌强化(+几)到等级的数量
     */
    CARD_STRENGTH_AMOUNT(40),
    /**
     * 41. 卡牌晋级(星级)到指定星级的数量
     */
    CARD_STAR_AMOUNT(41),
    /**
     * 42. 装备精炼次数
     */
    EQUIP_REFINE_AMOUNT(42),
    /**
     * 43. 装备精炼等级
     */
    EQUIP_REFINE_LEVEL(43),
    /**
     * 44. 装备强化到指定等级的数量
     */
    EQUIP_LEVELUP_AMOUNT(44),
    /**
     * 45. 夺宝次数
     */
    GRAB_AMOUNT(45),
    ;

    private AchievementType(int value)
    {
        this.value = value;
    }
    
    public int value()
    {
        return value;
    }
    
    public boolean compare(int value)
    {
        return this.value == value;
    }
    
    public static AchievementType getTypeByValue(int value)
    {
        for (AchievementType t : AchievementType.values())
        {
            if (t.value == value)
                return t;
        }
        throw new IllegalStateException("未知的成就类型值: " + value);
    }
    
    private final int value;
}
