/**
 * @date 2014/5/15
 * @author ChenLong
 */
package game.server.logic.constant;

/**
 *
 * @author ChenLong
 */
public enum JsonKey
{
    ROLE_HEAD("head"),
    ROLE_EXP("roleExp"),
    LAST_CONNECT_TIME("lastConnectTime"),
    CREATE_ROLE_TIME("createRoleTime"),
    SCRIPT_FLAG("scriptFlags"),
    BACKPACK_JSON_KEY("backpack"),
    CARD_JSON_KEY("card"),
    MAIN_TASK_JSON_KEY("mainTask"),
    DAILY_TASK_JSON_KEY("dailyTask"),
    MISSION_JSON_KEY("mission"),
    DAILY_MISSION_JSON_KEY("dailyMission"),
    XF_INFO_JSON_KEY("xfInfo"),
    STORE_JSON_KEY("store"),
    LOTTERY_JSON_KEY("lottery"),
    SIGNIN_JSON_KEY("signIn"),
    ACHIEVEMENT_JSON_KEY("achievement"),
    ENDLESSTOWER_JSON_KEY("endlesstower"),
    SKYWARD_JSON_KEY("skyward"),
    BUYGOLD_JSON_KEY("buyGold"),
    LAST_RESET_BUY_NUM("lastResetBuyNum"),
    FUNMISSION_JSON_KEY("funMission"),
    ENERGY_BUY_NUM("energyBuyNum"),
    SKILLPOINT_BY_NUM("skillBuyNum"),
    LAST_RESET_BUY_SKILL("lastResetBuySkill"),
    LAST_WORLD_CHAT_TIME("lastWorldChatTime"),
    MONTH_CARD_EXPIRY_DATE("monthCardExpiryDate"),
    // 房间信息
    ROOM_NUMBER("roomNumber"),
    ROOM_GAME_TYPE("roomGameType"),
    ROOM_TYPE("roomType"),
    GUIDE("guide"),
    GROWTH_PLANS("growthPlans"),
    RECHARGE("recharge"),
    VIP_MGR("vipMgr"),
    GRAB_TREASURE("grabTreasure"),
    ARENA_JSON_KEY("arena"),
    REBATE_JSON_KEY("rebate"),
    FRIEND_NUM("friendNum"),
    QUESTION_LIST("questionFinishedList"),
    KEYS_JSON_KEY("keys"),
    LOGINGIFT_JSON_KEY("loginGift"),
    RES_MISSION("resMission"),
    EXPEDITION_MISSION("expeditionMission"),
    DEMON_SOUL("demonSoul"),
    DENY_CHAT_JSON_KEY("deny_chat_time"),
    GOLDEN_TOUCH("golden_touch"),
    GEM_STORE("gem_store"),
    OPER_ACTIVITY("oper_activity"),
    SCORE_MISSION("scoreMission"),
    WORLD_BOSS("worldBoss"),
    ;

    private final String key;

    JsonKey(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}
