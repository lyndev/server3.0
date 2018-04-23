/**
 * Auto generated, do not edit it
 */
package game.data;

import game.data.container.q_aiConfigContainer;
import game.data.container.q_buff_newContainer;
import game.data.container.q_vip_marketContainer;
import game.data.container.q_vipContainer;
import game.data.container.q_special_equipContainer;
//import game.data.container.q_world_bossContainer;
import game.data.container.q_main_buildContainer;
import game.data.container.q_taskContainer;
import game.data.container.q_main_missionContainer;
import game.data.container.q_leader_upstarContainer;
import game.data.container.q_main_roleContainer;
import game.data.container.q_item_produce_monitContainer;
import game.data.container.q_resource_produce_monitContainer;
import game.data.container.q_rechargeContainer;
import game.data.container.q_globalContainer;
import game.data.container.q_mission_talkContainer;
import game.data.container.q_level_packageContainer;
import game.data.container.q_mission_chapterContainer;
import game.data.container.q_controlContainer;
import game.data.container.q_npc_summonContainer;
import game.data.container.q_store_packContainer;
import game.data.container.q_storeConfigContainer;
import game.data.container.q_game_mapContainer;
import game.data.container.q_positionContainer;
import game.data.container.q_gemContainer;
import game.data.container.q_color_attrContainer;
import game.data.container.q_box_itemsContainer;
import game.data.container.q_boxContainer;
import game.data.container.q_monster_posContainer;
import game.data.container.q_npc_monsterContainer;
import game.data.container.q_monster_level_attributeContainer;
import game.data.container.q_demon_soulContainer;
import game.data.container.q_fight_powerContainer;
import game.data.container.q_skill_buff_levelContainer;
import game.data.container.q_skill_costContainer;
import game.data.container.q_effectContainer;
import game.data.container.q_skill_growContainer;
import game.data.container.q_skillContainer;
import game.data.container.q_filterwordContainer;
import game.data.container.q_guideContainer;
import game.data.container.q_daily_taskContainer;
import game.data.container.q_exchange_coinContainer;
import game.data.container.q_itemContainer;
import game.data.container.q_special_entityContainer;
import game.data.container.q_act_headContainer;
import game.data.container.q_act_nameContainer;
import game.data.container.q_score_rewardContainer;
import game.data.container.q_score_missionContainer;
import game.data.container.q_robot_configContainer;
import game.data.container.q_arena_ranking_rewardContainer;
import game.data.container.q_qiandaoContainer;
import game.data.container.q_scriptContainer;
import game.data.container.q_npc_heroContainer;
import game.data.container.q_card_formulaContainer;
import game.data.container.q_hero_soulcostContainer;
import game.data.container.q_hero_expContainer;
import game.data.container.q_equipmentContainer;
import game.data.container.q_action_timeContainer;
import game.data.container.q_languageContainer;
import game.data.container.q_buy_costContainer;
import game.data.container.q_resource_missionContainer;
import game.data.container.q_oper_activityContainer;
import game.data.container.q_expeditionContainer;
import game.data.container.q_expedition_node_groupContainer;
import game.data.container.q_gold_LotteryContainer;
import game.data.container.q_gem_LotteryContainer;
import game.data.container.q_node_effectContainer;
import game.data.container.q_first_rechargeContainer;
import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSessionFactory;

public class GameDataManager
{
    private final Logger log = Logger.getLogger(GameDataManager.class);
    public volatile q_aiConfigContainer q_aiConfigContainer = new q_aiConfigContainer();
    public volatile q_buff_newContainer q_buff_newContainer = new q_buff_newContainer();
    public volatile q_vip_marketContainer q_vip_marketContainer = new q_vip_marketContainer();
    public volatile q_vipContainer q_vipContainer = new q_vipContainer();
    public volatile q_special_equipContainer q_special_equipContainer = new q_special_equipContainer();
    //public volatile q_world_bossContainer q_world_bossContainer = new q_world_bossContainer();
    //public volatile q_main_buildContainer q_main_buildContainer = new q_main_buildContainer();
    public volatile q_taskContainer q_taskContainer = new q_taskContainer();
    public volatile q_main_missionContainer q_main_missionContainer = new q_main_missionContainer();
    public volatile q_leader_upstarContainer q_leader_upstarContainer = new q_leader_upstarContainer();
    //public volatile q_main_roleContainer q_main_roleContainer = new q_main_roleContainer();
    public volatile q_item_produce_monitContainer q_item_produce_monitContainer = new q_item_produce_monitContainer();
    public volatile q_resource_produce_monitContainer q_resource_produce_monitContainer = new q_resource_produce_monitContainer();
    public volatile q_rechargeContainer q_rechargeContainer = new q_rechargeContainer();
    public volatile q_globalContainer q_globalContainer = new q_globalContainer();
    public volatile q_mission_talkContainer q_mission_talkContainer = new q_mission_talkContainer();
    public volatile q_level_packageContainer q_level_packageContainer = new q_level_packageContainer();
    public volatile q_mission_chapterContainer q_mission_chapterContainer = new q_mission_chapterContainer();
    public volatile q_controlContainer q_controlContainer = new q_controlContainer();
    public volatile q_npc_summonContainer q_npc_summonContainer = new q_npc_summonContainer();
    public volatile q_store_packContainer q_store_packContainer = new q_store_packContainer();
    public volatile q_storeConfigContainer q_storeConfigContainer = new q_storeConfigContainer();
    public volatile q_game_mapContainer q_game_mapContainer = new q_game_mapContainer();
    public volatile q_positionContainer q_positionContainer = new q_positionContainer();
    public volatile q_gemContainer q_gemContainer = new q_gemContainer();
    public volatile q_color_attrContainer q_color_attrContainer = new q_color_attrContainer();
    public volatile q_box_itemsContainer q_box_itemsContainer = new q_box_itemsContainer();
    public volatile q_boxContainer q_boxContainer = new q_boxContainer();
    public volatile q_monster_posContainer q_monster_posContainer = new q_monster_posContainer();
    public volatile q_npc_monsterContainer q_npc_monsterContainer = new q_npc_monsterContainer();
    public volatile q_monster_level_attributeContainer q_monster_level_attributeContainer = new q_monster_level_attributeContainer();
    public volatile q_demon_soulContainer q_demon_soulContainer = new q_demon_soulContainer();
    public volatile q_fight_powerContainer q_fight_powerContainer = new q_fight_powerContainer();
    public volatile q_skill_buff_levelContainer q_skill_buff_levelContainer = new q_skill_buff_levelContainer();
    public volatile q_skill_costContainer q_skill_costContainer = new q_skill_costContainer();
    public volatile q_effectContainer q_effectContainer = new q_effectContainer();
    public volatile q_skill_growContainer q_skill_growContainer = new q_skill_growContainer();
    public volatile q_skillContainer q_skillContainer = new q_skillContainer();
    public volatile q_filterwordContainer q_filterwordContainer = new q_filterwordContainer();
    public volatile q_guideContainer q_guideContainer = new q_guideContainer();
    public volatile q_daily_taskContainer q_daily_taskContainer = new q_daily_taskContainer();
    public volatile q_exchange_coinContainer q_exchange_coinContainer = new q_exchange_coinContainer();
    public volatile q_itemContainer q_itemContainer = new q_itemContainer();
    public volatile q_special_entityContainer q_special_entityContainer = new q_special_entityContainer();
    public volatile q_act_headContainer q_act_headContainer = new q_act_headContainer();
    public volatile q_act_nameContainer q_act_nameContainer = new q_act_nameContainer();
    public volatile q_score_rewardContainer q_score_rewardContainer = new q_score_rewardContainer();
    public volatile q_score_missionContainer q_score_missionContainer = new q_score_missionContainer();
    public volatile q_robot_configContainer q_robot_configContainer = new q_robot_configContainer();
    public volatile q_arena_ranking_rewardContainer q_arena_ranking_rewardContainer = new q_arena_ranking_rewardContainer();
    public volatile q_qiandaoContainer q_qiandaoContainer = new q_qiandaoContainer();
    public volatile q_scriptContainer q_scriptContainer = new q_scriptContainer();
    public volatile q_npc_heroContainer q_npc_heroContainer = new q_npc_heroContainer();
    public volatile q_card_formulaContainer q_card_formulaContainer = new q_card_formulaContainer();
    public volatile q_hero_soulcostContainer q_hero_soulcostContainer = new q_hero_soulcostContainer();
    public volatile q_hero_expContainer q_hero_expContainer = new q_hero_expContainer();
    public volatile q_equipmentContainer q_equipmentContainer = new q_equipmentContainer();
    public volatile q_action_timeContainer q_action_timeContainer = new q_action_timeContainer();
    public volatile q_languageContainer q_languageContainer = new q_languageContainer();
    public volatile q_buy_costContainer q_buy_costContainer = new q_buy_costContainer();
    public volatile q_resource_missionContainer q_resource_missionContainer = new q_resource_missionContainer();
    public volatile q_oper_activityContainer q_oper_activityContainer = new q_oper_activityContainer();
    public volatile q_expeditionContainer q_expeditionContainer = new q_expeditionContainer();
    public volatile q_expedition_node_groupContainer q_expedition_node_groupContainer = new q_expedition_node_groupContainer();
    public volatile q_gold_LotteryContainer q_gold_LotteryContainer = new q_gold_LotteryContainer();
    public volatile q_gem_LotteryContainer q_gem_LotteryContainer = new q_gem_LotteryContainer();
    public volatile q_node_effectContainer q_node_effectContainer = new q_node_effectContainer();
    public volatile q_first_rechargeContainer q_first_rechargeContainer = new q_first_rechargeContainer();

    private SqlSessionFactory sessionFactory;

    public void loadAll()
    {
	log.info("Start load all game data ...");
        //q_aiConfigContainer.load();
        q_buff_newContainer.load();
        //q_vip_marketContainer.load();
        q_vipContainer.load();
        q_special_equipContainer.load();
        //q_world_bossContainer.load();
        //q_main_buildContainer.load();
        q_taskContainer.load();
        q_main_missionContainer.load();
        q_leader_upstarContainer.load();
        //q_main_roleContainer.load();
        q_item_produce_monitContainer.load();
        q_resource_produce_monitContainer.load();
        q_rechargeContainer.load();
        q_globalContainer.load();
        q_mission_talkContainer.load();
        q_level_packageContainer.load();
        q_mission_chapterContainer.load();
        q_controlContainer.load();
        q_npc_summonContainer.load();
        q_store_packContainer.load();
       // q_storeConfigContainer.load();
        q_game_mapContainer.load();
        q_positionContainer.load();
        //q_gemContainer.load();
        //q_color_attrContainer.load();
        //q_box_itemsContainer.load();
        //q_boxContainer.load();
        q_monster_posContainer.load();
        q_npc_monsterContainer.load();
        //q_monster_level_attributeContainer.load();
        q_demon_soulContainer.load();
        //q_fight_powerContainer.load();
        q_skill_buff_levelContainer.load();
        q_skill_costContainer.load();
        q_effectContainer.load();
        q_skill_growContainer.load();
        q_skillContainer.load();
        q_filterwordContainer.load();
        q_guideContainer.load();
        q_daily_taskContainer.load();
        //q_exchange_coinContainer.load();
        q_itemContainer.load();
        //q_special_entityContainer.load();
        q_act_headContainer.load();
        q_act_nameContainer.load();
       // q_score_rewardContainer.load();
       // q_score_missionContainer.load();
        q_robot_configContainer.load();
        q_arena_ranking_rewardContainer.load();
        q_qiandaoContainer.load();
        q_scriptContainer.load();
        //q_npc_heroContainer.load();
        q_card_formulaContainer.load();
        q_hero_soulcostContainer.load();
        q_hero_expContainer.load();
        q_equipmentContainer.load();
        q_action_timeContainer.load();
        q_languageContainer.load();
        q_buy_costContainer.load();
        q_resource_missionContainer.load();
        //q_oper_activityContainer.load();
        q_expeditionContainer.load();
        q_expedition_node_groupContainer.load();
        //q_gold_LotteryContainer.load();
        //q_gem_LotteryContainer.load();
        //q_node_effectContainer.load();
        //q_first_rechargeContainer.load();
    }

    public GameDataManager setSqlSessionFactory(SqlSessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
        return this;
    }

    public SqlSessionFactory getSqlSessionFactory()
    {
        return sessionFactory;
    }

    /**
     * 鑾峰彇GameServer鐨勫疄渚嬪璞�.
     *
     * @return
     */
    public static GameDataManager getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 鐢ㄦ灇涓炬潵瀹炵幇鍗曚緥
     */
    private enum Singleton
    {
        INSTANCE;
        GameDataManager manager;

        Singleton()
        {
            this.manager = new GameDataManager();
        }

        GameDataManager getProcessor()
        {
            return manager;
        }
    }
}
