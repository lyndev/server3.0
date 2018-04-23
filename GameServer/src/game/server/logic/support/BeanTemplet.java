package game.server.logic.support;

import game.data.GameDataManager;
//import game.data.bean.q_EndlessTowerBean;
//import game.data.bean.q_SpecialMissionBean;
//import game.data.bean.q_achievementBean;
//import game.data.bean.q_achievement_labelBean;
//import game.data.bean.q_attributeBean;
//import game.data.bean.q_battlerewardBean;
import game.data.bean.q_boxBean;
import game.data.bean.q_box_itemsBean;
import game.data.bean.q_buy_costBean;
//import game.data.bean.q_buy_physicalBean;
//import game.data.bean.q_card_combinationBean;
//import game.data.bean.q_card_composeBean;
import game.data.bean.q_color_attrBean;
import game.data.bean.q_controlBean;
//import game.data.bean.q_daily_missionBean;
import game.data.bean.q_daily_taskBean;
//import game.data.bean.q_debrisBean;
//import game.data.bean.q_debris_compoundBean;
import game.data.bean.q_equipmentBean;
//import game.data.bean.q_equipment_compoundBean;
//import game.data.bean.q_equipment_growthBean;
//import game.data.bean.q_equipment_materialBean;
//import game.data.bean.q_equipment_refiningBean;
//import game.data.bean.q_equipment_strengthenBean;
import game.data.bean.q_globalBean;
//import game.data.bean.q_growth_plansBean;
//import game.data.bean.q_inheritanceBean;
import game.data.bean.q_itemBean;
//import game.data.bean.q_item_compoundBean;
//import game.data.bean.q_item_decomposeBean;
import game.data.bean.q_item_produce_monitBean;
import game.data.bean.q_languageBean;
import game.data.bean.q_main_missionBean;
//import game.data.bean.q_material_compoundBean;
import game.data.bean.q_mission_chapterBean;
//import game.data.bean.q_parkourBean;
import game.data.bean.q_qiandaoBean;
//import game.data.bean.q_quality_bonusBean;
//import game.data.bean.q_rebateBean;
//import game.data.bean.q_rebateRewardBean;
import game.data.bean.q_rechargeBean;
//import game.data.bean.q_res_buildBean;
import game.data.bean.q_resource_produce_monitBean;
import game.data.bean.q_skillBean;
//import game.data.bean.q_skill_bonusBean;
//import game.data.bean.q_store_collectionBean;
import game.data.bean.q_taskBean;
//import game.data.bean.q_tianjianbaoxiangBean;
import game.data.bean.q_vipBean;
//import game.data.bean.q_vipgiftBean;
//import game.data.bean.q_xf_baseBean;
import game.data.container.q_itemContainer;
import game.data.container.q_oper_activityContainer;
import game.data.container.q_world_bossContainer;

//new------------------------------------
import game.data.bean.q_npc_heroBean;
import game.data.bean.q_level_packageBean;
import game.data.bean.q_hero_expBean;
import game.data.bean.q_hero_soulcostBean;
import game.data.bean.q_skill_costBean;
import game.data.bean.q_special_equipBean;
import game.data.bean.q_leader_upstarBean;
import game.data.bean.q_resource_missionBean;
import game.data.bean.q_expeditionBean;
import game.data.bean.q_expedition_node_groupBean;
import game.data.bean.q_storeConfigBean;
import game.data.bean.q_store_packBean;
import game.data.bean.q_monster_posBean;
import game.data.bean.q_demon_soulBean;
import game.data.bean.q_exchange_coinBean;
import game.data.bean.q_gemBean;
import game.data.bean.q_gem_LotteryBean;
import game.data.bean.q_gold_LotteryBean;
import game.data.bean.q_oper_activityBean;
import game.data.bean.q_score_missionBean;
import game.data.bean.q_score_rewardBean;
import game.data.bean.q_vip_marketBean;
//import game.data.bean.q_world_bossBean;

import game.server.logic.constant.ItemType;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * <b>数据Bean模板类.</b>
 * <p>
 * 负责获取各种数据Bean的模板对象. 
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class BeanTemplet
{
    private final static Logger logger = Logger.getLogger(BeanTemplet.class);

    /**
     * 获取指定卡牌的模板数据
     *
     * @param id 卡牌模板ID
     * @return
     */
    public static q_npc_heroBean getNpcHeroBean(int id)
    {
        return GameDataManager.getInstance().q_npc_heroContainer.getMap().get(id);
    }

    /**
     * 获取指定掉落包数据
     *
     * @param id
     * @return
     */
    public static q_level_packageBean getLevelPackageBean(int id)
    {
        return GameDataManager.getInstance().q_level_packageContainer.getMap().get(id);
    }

    /**
     * 获取指定等级的经验数据
     *
     * @param lv
     * @return
     */
    public static q_hero_expBean getHeroExpBean(int lv)
    {
        return GameDataManager.getInstance().q_hero_expContainer.getMap().get(lv);
    }

    /**
     * 根据星级获取升星消耗
     *
     * @param starLv
     * @return
     */
    public static q_hero_soulcostBean getHeroSoulCostBean(int starLv)
    {
        return GameDataManager.getInstance().q_hero_soulcostContainer.getMap().get(starLv);
    }

    /**
     * 根据技能等级获取消耗
     *
     * @param skillLv
     * @return
     */
    public static q_skill_costBean getSkillCostBean(int skillLv)
    {
        return GameDataManager.getInstance().q_skill_costContainer.getMap().get(skillLv);
    }

    /**
     * 获取指定物品的模板数据.
     *
     * @param itemId 物品ID
     * @return
     */
    public static q_itemBean getItemBean(int itemId)
    {
        return GameDataManager.getInstance().q_itemContainer.getMap().get(itemId);
    }

    /**
     * 获取指定装备的模板数据.
     *
     * @param equipId
     * @return
     */
    public static q_equipmentBean getEquipmentBean(int equipId)
    {
        return GameDataManager.getInstance().q_equipmentContainer.getMap().get(equipId);
    }

    /**
     * 获取每个英雄的专属装备
     *
     * @param specialEquipId
     * @return
     */
    public static q_special_equipBean getSpecoalEquipBean(int specialEquipId)
    {
        return GameDataManager.getInstance().q_special_equipContainer.getMap().get(specialEquipId);
    }

    /**
     * 获取宝石配置表的模板数据
     *
     * @param gemId
     * @return
     */
    public static q_gemBean getGemBean(int gemId)
    {
        return GameDataManager.getInstance().q_gemContainer.getMap().get(gemId);
    }

    /**
     * 获取宝石颜色属性对应配置表的模板数据
     *
     * @param gemId
     * @return
     */
    public static q_color_attrBean getGemColorAttrBean(int colorId)
    {
        return GameDataManager.getInstance().q_color_attrContainer.getMap().get(colorId);
    }

    /**
     * 获取钻石商城物品
     *
     * @param id
     * @return
     */
    public static q_vip_marketBean getGemItemBean(int id)
    {
        return GameDataManager.getInstance().q_vip_marketContainer.getMap().get(id);
    }

    /**
     * 读取装备模板数据、宝石模板数据后装载到物品容器中.
     */
    public synchronized static void loadAllItemContainer()
    {
        /*
        q_itemContainer newContainer = new q_itemContainer();
        //徐能强：先将背包中的道具load一下
        newContainer.load();
        //加载装备的数据
        List<q_equipmentBean> equipList = GameDataManager.getInstance().q_equipmentContainer.getList();
        for (q_equipmentBean obj : equipList)
        {
            q_itemBean bean = new q_itemBean();
            bean.setQ_id(obj.getQ_id());
            bean.setQ_name(obj.getQ_name());
            bean.setQ_log(obj.getQ_log());
            bean.setQ_default(obj.getQ_default());
            bean.setQ_tiny_icon(obj.getQ_tiny_icon());
            bean.setQ_goods_star(obj.getQ_goods_star());
            bean.setQ_level(obj.getQ_level());
            bean.setQ_cardlevel(obj.getQ_cardlevel());
            bean.setQ_sell_confirm(obj.getQ_sell_confirm());
            bean.setQ_sell_price(obj.getQ_sell_price());
            bean.setQ_type(ItemType.EQUIPMENT.value());
            bean.setQ_max(obj.getQ_max());
            bean.setQ_log(obj.getQ_log());
            //徐能强：将装备道具添加到存储背包的结构中
            newContainer.getList().add(bean);
            newContainer.getMap().put(bean.getQ_id(), bean);
        }
        //加载宝石的数据
        List<q_gemBean> gemList = GameDataManager.getInstance().q_gemContainer.getList();
        for (q_gemBean gemBean : gemList)
        {
            q_itemBean bean = new q_itemBean();
            bean.setQ_id(gemBean.getQ_id());
            bean.setQ_name(gemBean.getQ_name());
            bean.setQ_log(gemBean.getQ_log());
            bean.setQ_default(gemBean.getQ_default());
            bean.setQ_tiny_icon(gemBean.getQ_tiny_icon());
            bean.setQ_sell_confirm(gemBean.getQ_sell_confirm());
            bean.setQ_sell_price(gemBean.getQ_sell_price());
            bean.setQ_type(ItemType.GEMS.value());
            bean.setQ_max(gemBean.getQ_max());
            bean.setQ_log(gemBean.getQ_log());
            //徐能强：将装备道具添加到存储背包的结构中
            newContainer.getList().add(bean);
            newContainer.getMap().put(bean.getQ_id(), bean);
        }
        //徐能强：重新将新的道具配置赋值给q_itemContainer，之前GameDataManager里面加载的无效
        GameDataManager.getInstance().q_itemContainer = newContainer;
        */
    }

//    /**
//     * 读取装备模板数据后装载到物品容器中.
//     */
//    public synchronized static void loadEquipmentsToItemContainer()
//    {
//        q_itemContainer newContainer = new q_itemContainer();
//        //徐能强：先将背包中的道具load一下
//        newContainer.load();
//
//        List<q_equipmentBean> list = GameDataManager.getInstance().q_equipmentContainer.getList();
//        for (q_equipmentBean obj : list)
//        {
//            q_itemBean bean = new q_itemBean();
//            bean.setQ_id(obj.getQ_id());
//            bean.setQ_name(obj.getQ_name());
//            bean.setQ_log(obj.getQ_log());
//            bean.setQ_default(obj.getQ_default());
//            bean.setQ_tiny_icon(obj.getQ_tiny_icon());
//            bean.setQ_goods_star(obj.getQ_goods_star());
//            bean.setQ_level(obj.getQ_level());
//            bean.setQ_cardlevel(obj.getQ_cardlevel());
//            bean.setQ_sell_confirm(obj.getQ_sell_confirm());
//            bean.setQ_sell_price(obj.getQ_sell_price());
//            //bean.setQ_buy_price(obj.getQ_buy_price());
//            //徐能强注释
//            //bean.setQ_tiny_effect(obj.getQ_tiny_effect());
//            //bean.setQ_equipment_attribute(obj.getQ_equipment_attribute());
//            //bean.setQ_equipment_position(obj.getQ_equipment_position());
//
//            bean.setQ_type(ItemType.EQUIPMENT.value());
//            bean.setQ_max(obj.getQ_max());
//            bean.setQ_log(obj.getQ_log());
//            //徐能强：将装备道具添加到存储背包的结构中
//            newContainer.getList().add(bean);
//            newContainer.getMap().put(bean.getQ_id(), bean);
//        }
//        //徐能强：重新将新的道具配置赋值给q_itemContainer，之前GameDataManager里面加载的无效
//        GameDataManager.getInstance().q_itemContainer = newContainer;
//    }
    /**
     * 获取领袖点亮星点信息
     *
     * @param number
     * @return
     */
    public static q_leader_upstarBean getLeaderUpstarBean(int number)
    {
        return GameDataManager.getInstance().q_leader_upstarContainer.getMap().get(number);
    }

    /**
     * 通过次数获取钻石消耗实例
     *
     * @param times
     * @return
     */
    public static q_buy_costBean getGoldCostBean(int times)
    {
        return GameDataManager.getInstance().q_buy_costContainer.getMap().get(times);
    }

    /**
     * 获取资源本模板数据
     *
     * @param id
     * @return
     */
    public static q_resource_missionBean getResourceMissionBean(int id)
    {
        return GameDataManager.getInstance().q_resource_missionContainer.getMap().get(id);
    }

    /**
     * 获取远征节点数据
     *
     * @param id
     * @return
     */
    public static q_expeditionBean getExpeditionBean(int id)
    {
        return GameDataManager.getInstance().q_expeditionContainer.getMap().get(id);
    }

    /**
     * 远征随机关卡节点配置
     *
     * @param lv
     * @return
     */
    public static q_expedition_node_groupBean getExpeditionNodeBean(int lv)
    {
        return GameDataManager.getInstance().q_expedition_node_groupContainer.getMap().get(lv);
    }

    /**
     * 获取怪物信息
     *
     * @param posId
     * @return
     */
    public static q_monster_posBean getMonsterInfo(int posId)
    {
        return GameDataManager.getInstance().q_monster_posContainer.getMap().get(posId);
    }

    /**
     * 获取恶魔之魂配置
     *
     * @param id
     * @return
     */
    public static q_demon_soulBean getDemonSoulBean(int id)
    {
        return GameDataManager.getInstance().q_demon_soulContainer.getMap().get(id);
    }

    /**
     * 获取金币抽卡的所有配置数据
     *
     * @return
     */
    public static List<q_gold_LotteryBean> getGoldLotteryBeans()
    {
        return GameDataManager.getInstance().q_gold_LotteryContainer.getList();
    }

    /**
     * 获取钻石抽卡的所有配置数据
     *
     * @return
     */
    public static List<q_gem_LotteryBean> getGemLotteryBeans()
    {
        return GameDataManager.getInstance().q_gem_LotteryContainer.getList();
    }

    /**
     * 获取点金手配置数据
     *
     * @return
     */
    public static q_exchange_coinBean getExchangeCoinBeans(int tms)
    {
        return GameDataManager.getInstance().q_exchange_coinContainer.getMap().get(tms);
    }

    public static q_score_missionBean getScoreMissionBean(int id)
    {
        return GameDataManager.getInstance().q_score_missionContainer.getMap().get(id);
    }

    public static List<q_score_rewardBean> getScoreRewadrBeans()
    {
        return GameDataManager.getInstance().q_score_rewardContainer.getList();
    }
    
    /**
     * 运营活动配置数据
     * @return 
     */
    public static List<q_oper_activityBean> getOperActivityBeansList(){
        return GameDataManager.getInstance().q_oper_activityContainer.getList();
    }
    public static q_oper_activityBean getOperActivityBeans(int id){
        return GameDataManager.getInstance().q_oper_activityContainer.getMap().get(id);
    }
    
    /**
     * 世界Boss
     * @param missionId
     * @return 
    
    public static q_world_bossBean getWorldBossBean(int missionId){
        return GameDataManager.getInstance().q_world_bossContainer.getMap().get(missionId);
    }
    
    public static List<q_world_bossBean> getWorldBossBeansList(){
        return GameDataManager.getInstance().q_world_bossContainer.getList();
    }
     */
        /**
     * 获取宝箱配置表
     * @param id
     * @return 
     */
    public static q_boxBean getBoxBean(int id){
        return GameDataManager.getInstance().q_boxContainer.getMap().get(id);
    }
    
    /**
     * 宝箱道具配置
     * @param id
     * @return 
     */
    public static q_box_itemsBean getBoxItemBean(int id){
        return GameDataManager.getInstance().q_box_itemsContainer.getMap().get(id);
    }    

    
   //--------------------------------------------------------------------------- 
    /**
     * 获取分解指定物品获得的模板数据.
     *
     * @param itemId 物品ID
     * @return
     */
//    public static q_item_decomposeBean getItemCellBean(int itemId)
//    {
//        return GameDataManager.getInstance().q_item_decomposeContainer.getMap().get(itemId);
//    }
//
//    /**
//     * 获取指定图纸可合成的模板数据.
//     *
//     * @param drawingId 图纸ID
//     * @return
//     */
//    public static q_item_compoundBean getItemCombineBean(int drawingId)
//    {
//        return GameDataManager.getInstance().q_item_compoundContainer.getMap().get(drawingId);
//    }

    /**
     * 获取指定材料可合成的模板数据.
     *
     * @param materialId 材料ID
     * @return
     */
//    public static q_material_compoundBean getMaterialCombineBean(int materialId)
//    {
//        return GameDataManager.getInstance().q_material_compoundContainer.getMap().get(materialId);
//    }
//
//    /**
//     * 获取合成指定碎片所需的模板数据.
//     *
//     * @param debrisId 碎片ID
//     * @return
//     */
//    public static q_debris_compoundBean getDebrisCombineBean(int debrisId)
//    {
//        return GameDataManager.getInstance().q_debris_compoundContainer.getMap().get(debrisId);
//    }
//
//    /**
//     * 获取指定装备可吞噬的材料模板数据.
//     *
//     * @param equipId 装备ID
//     * @return
//     */
//    public static q_equipment_materialBean getEquipmentMaterialBean(int equipId)
//    {
//        return GameDataManager.getInstance().q_equipment_materialContainer.getMap().get(equipId);
//    }

    /**
     * 获取可用于强化指定装备位的材料模板数据.
     *
     * @param positionId 装备位ID
     * @param starLevel 装备位星级
     * @return
     */
//    public static q_equipment_strengthenBean getEquipmentStrengthenBean(int positionId, int starLevel)
//    {
//        return GameDataManager.getInstance().q_equipment_strengthenContainer.getMap().get(
//                Integer.parseInt(String.valueOf(positionId) + String.valueOf(starLevel)));
//    }

    /**
     * 获取指定关卡的模板数据
     *
     * @param missionId 关卡ID
     * @return
     */
    public static q_main_missionBean getMissionBean(int missionId)
    {
        return GameDataManager.getInstance().q_main_missionContainer.getMap().get(missionId);
    }

    /**
     * 获取指定章节的模板数据
     *
     * @param chapterId 章节ID
     * @return
     */
    public static q_mission_chapterBean getChapterBean(int chapterId)
    {
        return GameDataManager.getInstance().q_mission_chapterContainer.getMap().get(chapterId);
    }

    /**
     * 获取指定卡牌的模板数据
     *
     * @param id 卡牌模板ID
     * @return
     */
//    public static q_attributeBean getAttributeBean(int id)
//    {
//        return GameDataManager.getInstance().q_attributeContainer.getMap().get(id);
//    }

    /**
     * 获取指定技能的模板数据
     *
     * @param id 技能模板ID
     * @return
     */
    public static q_skillBean getSkillBean(int id)
    {
        return GameDataManager.getInstance().q_skillContainer.getMap().get(id);
    }

//    /**
//     * 获取指定技能的进阶模板数据
//     *
//     * @param id 序列号
//     * @return
//     */
//    public static q_skill_bonusBean getSkillBonusBean(String id)
//    {
//        return GameDataManager.getInstance().q_skill_bonusContainer.getMap().get(id);
//    }

//    /**
//     * 获取指定碎片的模板数据
//     *
//     * @param id 碎片模板ID
//     * @return
//     */
//    public static q_debrisBean getDebrisBean(int id)
//    {
//        return GameDataManager.getInstance().q_debrisContainer.getMap().get(id);
//    }

//    /**
//     * 获取指定经验的模板数据
//     *
//     * @param id 卡牌经验模板ID
//     * @return
//     */
//    public static q_experienceBean getExperienceBean(int id)
//    {
//        return GameDataManager.getInstance().q_experienceContainer.getMap().get(id);
//    }
//    /**
//     * 获取指定经验的模板数据(召唤师经验表)
//     *
//     * @param id 召唤师经验模板ID
//     * @return
//     */
//    public static q_act_levelBean getPlayerExperienceBean(int id)
//    {
//        return GameDataManager.getInstance().q_act_levelContainer.getMap().get(id);
//    }
    /**
     * 获取指定全局的模板数据
     *
     * @param id 模板id
     * @return
     */
    public static q_globalBean getGlobalBean(int id)
    {
        q_globalBean bean = GameDataManager.getInstance().q_globalContainer.getMap().get(id);
        if (bean == null)
        {
            logger.error("全局配置为空, id = " + id);
        }
        return bean;
    }

    /**
     * 获取任务模版数据
     *
     * @param taskId 任务id
     * @return
     */
    public static q_taskBean getTaskBean(int taskId)
    {
        return GameDataManager.getInstance().q_taskContainer.getMap().get(taskId);
    }

    /**
     * 获取任务模版数据
     *
     * @return
     */
    public static List<q_taskBean> getTaskBeanList()
    {
        return GameDataManager.getInstance().q_taskContainer.getList();
    }

    /**
     * 获取日常任务模版数据
     *
     * @param taskId
     * @return
     */
    public static q_daily_taskBean getDailyTaskBean(int taskId)
    {
        return GameDataManager.getInstance().q_daily_taskContainer.getMap().get(taskId);
    }

    public static Map<Integer, q_daily_taskBean> getDailyTaskBeanMap()
    {
        return GameDataManager.getInstance().q_daily_taskContainer.getMap();
    }

//    /**
//     * 获取成就模版数据
//     *
//     * @param achievementId 成就Id
//     * @return
//     */
//    public static q_achievementBean getAchievementBean(int achievementId)
//    {
//        return GameDataManager.getInstance().q_achievementContainer.getMap().get(achievementId);
//    }
//
//    /**
//     * 获取日常关卡模板数据
//     *
//     * @param dlyMsnId 日常关卡id
//     * @return
//     */
//    public static q_daily_missionBean getDailyMissionBean(int dlyMsnId)
//    {
//        return GameDataManager.getInstance().q_daily_missionContainer.getMap().get(dlyMsnId);
//    }
//
//    /**
//     * 获取日常关卡模板数据长度
//     *
//     * @return
//     */
//    public static int getDailyMissionBeanLength()
//    {
//        return GameDataManager.getInstance().q_daily_missionContainer.getList().size();
//    }
//
//    /**
//     * 获取仙府配置数据
//     *
//     * @param starLevel
//     * @return
//     */
//    public static q_xf_baseBean getXFBaseBean(int starLevel)
//    {
//        return GameDataManager.getInstance().q_xf_baseContainer.getMap().get(starLevel);
//    }
//
//    /**
//     * 获取资源建筑配置数据
//     *
//     * @param level
//     * @return
//     */
//    public static q_res_buildBean getResBuildBaseBean(String level)
//    {
//        return GameDataManager.getInstance().q_res_buildContainer.getMap().get(level);
//    }
//
//    /**
//     * 获取成就标签配数据
//     *
//     * @param labelId 成就标签id
//     * @return
//     */
//    public static q_achievement_labelBean getAchievementLabelBean(int labelId)
//    {
//        return GameDataManager.getInstance().q_achievement_labelContainer.getMap().get(labelId);
//    }
//
//    public static List<q_achievement_labelBean> getAchievementLabelList()
//    {
//        return GameDataManager.getInstance().q_achievement_labelContainer.getList();
//    }
//
//    /**
//     * 获取天降宝箱配置数据
//     *
//     * @return
//     */
//    public static q_tianjianbaoxiangBean getTJBXBean()
//    {
//        return GameDataManager.getInstance().q_tianjianbaoxiangContainer.getMap().get(1);
//    }
//
//    /**
//     * 获取天降宝箱中掉落物品接住后的得分, 可正可负
//     *
//     * @param id
//     * @return
//     */
//    public static int getTJBXItem(int id)
//    {
//        return GameDataManager.getInstance().q_baoxiangwujianContainer.getMap().get(id).getQ_defen();
//    }
//
//    /**
//     * 获取趣味跑酷配置数据
//     *
//     * @return
//     */
//    public static q_parkourBean getQWPKBean()
//    {
//        return GameDataManager.getInstance().q_parkourContainer.getMap().get(1);
//    }
//
//    /**
//     * 获取尖叫跑酷中障碍物得分, 分为击碎障碍物和碰撞上障碍物
//     *
//     * @param id
//     * @param breakORcaught 击碎0或者碰撞上1
//     * @return
//     */
//    public static int getQWPKItem(int id, int breakORcaught)
//    {
//        if (breakORcaught == 0)
//            return GameDataManager.getInstance().q_paokuwujianContainer.getMap().get(id).getQ_jisuidefen();
//        else if (breakORcaught == 1)
//            return GameDataManager.getInstance().q_paokuwujianContainer.getMap().get(id).getQ_pengzhuangdefen();
//        else
//            return 0;
//    }

//    /**
//     * 根据仙府最高等级获取趣味关卡金币上限
//     *
//     * @param xfLevel 仙府等级
//     * @return
//     */
//    public static int getFunMissionMaxGold(int xfLevel)
//    {
//        return GameDataManager.getInstance().q_quweijinbiContainer.getMap().get(xfLevel) == null ? 0
//                : GameDataManager.getInstance().q_quweijinbiContainer.getMap().get(xfLevel).getQ_money();
//    }

    /**
     * 获取功能开关配置
     *
     * @param funId
     * @return
     */
    public static q_controlBean getControlBean(String funId)
    {
        return GameDataManager.getInstance().q_controlContainer.getMap().get(funId);
    }

    /**
     * 获取所有的功能开关配置
     *
     * @return
     */
    public static List<q_controlBean> getAllControlBean()
    {
        return GameDataManager.getInstance().q_controlContainer.getList();
    }

    /**
     * 获取所有的商店配置
     *
     * @return
     */
    public static List<q_storeConfigBean> getAllStoreConfig()
    {
        return GameDataManager.getInstance().q_storeConfigContainer.getList();
    }

    /**
     * 获取商品包中商品配置信息
     *
     * @param packId
     * @return
     */
    public static q_store_packBean getStorePack(int packId)
    {
        return GameDataManager.getInstance().q_store_packContainer.getMap().get(packId);
    }

//    /**
//     * 获取藏宝阁的所有商品.
//     *
//     * @return
//     */
//    public static List<q_store_collectionBean> getAllGoodsByCollectionStore()
//    {
//        return GameDataManager.getInstance().q_store_collectionContainer.getList();
//    }

//    /**
//     * 从藏宝阁商店中获取指定商品.
//     *
//     * @param goodsId 商品ID
//     * @return
//     */
//    public static q_store_collectionBean getGoodsByCollectionStore(int goodsId)
//    {
//        return GameDataManager.getInstance().q_store_collectionContainer.getMap().get(goodsId);
//    }

    /**
     * 语言包.
     *
     * @param Id 语言id
     * @return
     */
    public static q_languageBean getLanguage(int Id)
    {
        return GameDataManager.getInstance().q_languageContainer.getMap().get(Id);
    }

    public static String getFullLanguageText(int Id)
    {
        String result = Integer.toString(Id);
        q_languageBean bean = getLanguage(Id);
        if (bean != null && !bean.getQ_lgtext().trim().isEmpty())
            result += bean.getQ_lgtext().trim();
        return result;
    }

    public static String getFullLanguageText(String id)
    {
        String result = id;
        try
        {
            int idVal = Integer.parseInt(id);
            q_languageBean bean = getLanguage(idVal);
            if (bean != null && !bean.getQ_lgtext().trim().isEmpty())
                result += bean.getQ_lgtext().trim();
        }
        catch (NumberFormatException ex)
        {
            logger.error("获取语言包出错！id = " + id, ex);
        }
        return result;
    }

    /**
     * 获取签到配置数据.
     *
     * @param id
     * @return
     */
    public static q_qiandaoBean getSignInBean(int id)
    {
        return GameDataManager.getInstance().q_qiandaoContainer.getMap().get(id);
    }

//    /**
//     * 获取特殊目标关卡的关卡配置数据
//     *
//     * @param id 关卡id
//     * @return
//     */
//    public static q_SpecialMissionBean getSpecialMissionBean(int id)
//    {
//        return GameDataManager.getInstance().q_SpecialMissionContainer.getMap().get(id);
//    }

//    /**
//     * 获取特殊目标关卡的塔的配置数据
//     *
//     * @param id 塔id
//     * @return
//     */
//    public static q_EndlessTowerBean getTowerBean(int id)
//    {
//        return GameDataManager.getInstance().q_EndlessTowerContainer.getMap().get(id);
//    }

//    /**
//     * 获取购买体力配置数据
//     *
//     * @param id 购买次数
//     * @return
//     */
//    public static q_buy_physicalBean getBuyEnergy(int id)
//    {
//        return GameDataManager.getInstance().q_buy_physicalContainer.getMap().get(id);
//    }
//
//    /**
//     * 获取战场奖励配置数据
//     *
//     * @param id
//     * @return
//     */
//    public static q_battlerewardBean getBattleRewardBean(int id)
//    {
//        return GameDataManager.getInstance().q_battlerewardContainer.getMap().get(id);
//    }

//    /**
//     * 获取卡牌进阶数据
//     *
//     * @param id
//     * @return
//     */
//    public static q_quality_bonusBean getQualityBonusBean(String id)
//    {
//        return GameDataManager.getInstance().q_quality_bonusContainer.getMap().get(id);
//    }
//
//    /**
//     * 获取卡牌继承数据
//     *
//     * @param id
//     * @return
//     */
//    public static q_inheritanceBean getInheritanceBean(int id)
//    {
//        return GameDataManager.getInstance().q_inheritanceContainer.getMap().get(id);
//    }
//
//    /**
//     * 获取卡牌高级合成数据
//     *
//     * @param id
//     * @return
//     */
//    public static q_card_composeBean getCardCompoundBean(int id)
//    {
//        return GameDataManager.getInstance().q_card_composeContainer.getMap().get(id);
//    }
//
//    /**
//     * 获取卡牌组合数据
//     *
//     * @param id
//     * @return
//     */
//    public static List<q_card_combinationBean> getCardCombinationList()
//    {
//        return GameDataManager.getInstance().q_card_combinationContainer.getList();
//    }
//
//    /**
//     * 获取所有成长计划配置
//     *
//     * @return
//     */
//    public static List<q_growth_plansBean> getAllGrowthPlans()
//    {
//        return GameDataManager.getInstance().q_growth_plansContainer.getList();
//    }
//
//    /**
//     * 获取一个计划配置
//     *
//     * @param level 成长等级
//     * @return
//     */
//    public static q_growth_plansBean getGrowthPlans(int level)
//    {
//        return GameDataManager.getInstance().q_growth_plansContainer.getMap().get(level);
//    }

    public static List<q_vipBean> getAllVipBean()
    {
        return GameDataManager.getInstance().q_vipContainer.getList();
    }

    public static q_vipBean getVipBean(int vipLv)
    {
        return GameDataManager.getInstance().q_vipContainer.getMap().get(vipLv);
    }

//    public static List<q_vipgiftBean> getAllVipGiftBean()
//    {
//        return GameDataManager.getInstance().q_vipgiftContainer.getList();
//    }
//
//    public static q_vipgiftBean getVipGiftBean(int id)
//    {
//
//        return GameDataManager.getInstance().q_vipgiftContainer.getMap().get(id);
//    }
//
//    public static q_equipment_compoundBean getEquipmentCompoundBean(int id)
//    {
//        return GameDataManager.getInstance().q_equipment_compoundContainer.getMap().get(id);
//    }
//
//    public static List<q_equipment_compoundBean> getEquipmentCompoundList()
//    {
//        return GameDataManager.getInstance().q_equipment_compoundContainer.getList();
//    }
//
//    /**
//     * 获取装备升级(强化)配置
//     *
//     * @param level
//     * @return
//     */
//    public static q_equipment_growthBean getEquipmentLevelupBean(String level)
//    {
//        return GameDataManager.getInstance().q_equipment_growthContainer.getMap().get(level);
//    }
//
//    /**
//     * 获取装备精炼配置
//     *
//     * @param id_lvl
//     * @return
//     */
//    public static q_equipment_refiningBean getEquipmentRefineBean(String id_lvl)
//    {
//        return GameDataManager.getInstance().q_equipment_refiningContainer.getMap().get(id_lvl);
//    }

    public static List<q_rechargeBean> getAllRechargeBean()
    {
        return GameDataManager.getInstance().q_rechargeContainer.getList();
    }

    public static q_rechargeBean getRechargeBean(int id)
    {
        return GameDataManager.getInstance().q_rechargeContainer.getMap().get(id);
    }

//    public static q_rebateBean getRebateBean(int id)
//    {
//        return GameDataManager.getInstance().q_rebateContainer.getMap().get(id);
//    }
//
//    public static List<q_rebateBean> getRebateBeanList()
//    {
//        return GameDataManager.getInstance().q_rebateContainer.getList();
//    }
//
//    public static q_rebateRewardBean getRebateRewardBean(int id)
//    {
//        return GameDataManager.getInstance().q_rebateRewardContainer.getMap().get(id);
//    }

    /**
     * 召唤师开启好友系统等级
     *
     * @return
     */
    public static int getFriendOpenLevel()
    {
        int result = 10;
        q_globalBean bean = BeanTemplet.getGlobalBean(2071);
        if (bean != null)
        {
            result = bean.getQ_int_value();
            if (result < 0)
                logger.error("getFriendOpenLevel result = " + result);
        }
        else
        {
            logger.error("getFriendOpenLevel 全局配置2071缺失");
        }
        return result;
    }

    /**
     * 0 &lt= 好友数量 &lt= n, 体力上限+2n
     *
     * @return
     */
    public static int getFriendNumEnergyAddition()
    {
        // 0 <= 好友数量 <= n, 体力上限+2n
        int result = 10;
        q_globalBean bean = BeanTemplet.getGlobalBean(1171);
        if (bean != null)
        {
            result = bean.getQ_int_value();
            if (result < 0)
                logger.error("getFriendNumEnergyAddition result = " + result);
        }
        else
        {
            logger.error("getFriendNumEnergyAddition 全局配置1171缺失");
        }
        return result;
    }

    /**
     * 召唤师体力上限
     *
     * @return
     */
    public static int getMaxEnergyLimit()
    {
        int result = 120;
        q_globalBean bean = BeanTemplet.getGlobalBean(1047);
        if (bean != null)
        {
            result = bean.getQ_int_value();
            if (result < 0)
                logger.error("getMaxEnergyLimit result = " + result);
        }
        else
        {
            logger.error("getMaxEnergyLimit 全局配置1047缺失");
        }
        return result;
    }

    /**
     * 获取指定物品在一段时间内的正常产出上限.
     *
     * @param itemId 物品ID
     * @return 小于等于0表示没有配置监测数据
     */
    public static int getItemProduceLimit(int itemId)
    {
        q_item_produce_monitBean bean = GameDataManager.getInstance().q_item_produce_monitContainer.getMap().get(itemId);
        return bean != null ? bean.getLimit() : 0;
    }
//
    /**
     * 获取指定资源在一段时间内的正常产出上限.
     *
     * @param resType 资源类型
     * @return 小于等于0表示没有配置监测数据
     */
    public static int getResourceProduceLimit(int resType)
    {
        q_resource_produce_monitBean bean = GameDataManager.getInstance().q_resource_produce_monitContainer.getMap().get(resType);
        return bean != null ? bean.getLimit() : 0;
    }

}
