/**
 * Auto generated, do not edit it
 *
 * q_guide
 */
package game.data.bean;

public class q_guideBean
{
    private int q_id; // 引导ID
    private String q_description; // 引导描述(策划用)
    private int q_mainitem; // 主项ID
    private int q_subitem; // 子项ID
    private int q_next_id; // 下一步引导ID
    private int q_isforce; // 是否是强制引导(0非强制引导,1强制引导)
    private String q_begin_conditions; // 开启条件(格式：触发器类型1_参数;触发器类型2_参数)，任一多个用";"隔开
    private String q_end_conditions; // 结束条件(格式：触发器类型1_参数;触发器类型2_参数)，任一多个用";"隔开
    private String q_main_building; // 点击的是不是建筑，枚举：见批注注：此项填写后，q_force_show栏的位置填写x和y填0即可，其他参数照常填写参数格式：建筑枚举_所在图层
    private int q_chapter_node; // 点击的是章节上的节点，此项填写后，q_force_show栏的位置填写x和y填0即可，其他参数照常填写
    private String q_force_show; // 强制引导漏洞显示位置以及区域（参数说明( 漏洞      1, 区域坐标x y 宽 高, 0 控件对齐方式，是否隐藏手指（0不隐藏，1隐藏）
    private String q_pre_finish_step; // 当前引导步骤开始需要的前置引导步骤(不填或者0,0代表没有前置引导步骤，填写格式为:主项ID_子项ID)
    private String q_restore_step; // 当终止这一步引导的时候，需要恢复的引导步骤(不填或者0,0代表不需要恢复, 填写格式为(主项ID_子项ID;主项ID_子项ID))
    private String q_move_by; // 提示UI坐标偏移量0从左边出现，1从右边出现(0_x_y)
    private int q_need_notice_ui; // 是否需要提示
    private int q_ui_zorder; // 提示UI显示的层级
    private int q_image_flip; // 提示图片是否需要翻转(0:不需要 1:需要)
    private int q_language; // 提示语言ID
    private int q_delay; // 引导出现延迟时间（毫秒）
    private String q_finger_offset; // 手指特效偏移量(第一位0代表不翻转，1代表翻转）
    private int q_guide_type; // 引导类型（1 普通引导，2 对话引导）
    private String q_hero_id; // 对话的时候显示的半身像英雄ID以及显示在左边还是右边(填0表示显示召唤师半身像_1表示左边;2表示右边)

    /**
     * get 引导ID
     * @return
     */
    public int getQ_id()
    {
        return q_id;
    }

    /**
     * set 引导ID
     */
    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    /**
     * get 引导描述(策划用)
     * @return
     */
    public String getQ_description()
    {
        return q_description;
    }

    /**
     * set 引导描述(策划用)
     */
    public void setQ_description(String q_description)
    {
        this.q_description = q_description;
    }

    /**
     * get 主项ID
     * @return
     */
    public int getQ_mainitem()
    {
        return q_mainitem;
    }

    /**
     * set 主项ID
     */
    public void setQ_mainitem(int q_mainitem)
    {
        this.q_mainitem = q_mainitem;
    }

    /**
     * get 子项ID
     * @return
     */
    public int getQ_subitem()
    {
        return q_subitem;
    }

    /**
     * set 子项ID
     */
    public void setQ_subitem(int q_subitem)
    {
        this.q_subitem = q_subitem;
    }

    /**
     * get 下一步引导ID
     * @return
     */
    public int getQ_next_id()
    {
        return q_next_id;
    }

    /**
     * set 下一步引导ID
     */
    public void setQ_next_id(int q_next_id)
    {
        this.q_next_id = q_next_id;
    }

    /**
     * get 是否是强制引导(0非强制引导,1强制引导)
     * @return
     */
    public int getQ_isforce()
    {
        return q_isforce;
    }

    /**
     * set 是否是强制引导(0非强制引导,1强制引导)
     */
    public void setQ_isforce(int q_isforce)
    {
        this.q_isforce = q_isforce;
    }

    /**
     * get 开启条件(格式：触发器类型1_参数;触发器类型2_参数)，任一多个用";"隔开
     * @return
     */
    public String getQ_begin_conditions()
    {
        return q_begin_conditions;
    }

    /**
     * set 开启条件(格式：触发器类型1_参数;触发器类型2_参数)，任一多个用";"隔开
     */
    public void setQ_begin_conditions(String q_begin_conditions)
    {
        this.q_begin_conditions = q_begin_conditions;
    }

    /**
     * get 结束条件(格式：触发器类型1_参数;触发器类型2_参数)，任一多个用";"隔开
     * @return
     */
    public String getQ_end_conditions()
    {
        return q_end_conditions;
    }

    /**
     * set 结束条件(格式：触发器类型1_参数;触发器类型2_参数)，任一多个用";"隔开
     */
    public void setQ_end_conditions(String q_end_conditions)
    {
        this.q_end_conditions = q_end_conditions;
    }

    /**
     * get 点击的是不是建筑，枚举：见批注注：此项填写后，q_force_show栏的位置填写x和y填0即可，其他参数照常填写参数格式：建筑枚举_所在图层
     * @return
     */
    public String getQ_main_building()
    {
        return q_main_building;
    }

    /**
     * set 点击的是不是建筑，枚举：见批注注：此项填写后，q_force_show栏的位置填写x和y填0即可，其他参数照常填写参数格式：建筑枚举_所在图层
     */
    public void setQ_main_building(String q_main_building)
    {
        this.q_main_building = q_main_building;
    }

    /**
     * get 点击的是章节上的节点，此项填写后，q_force_show栏的位置填写x和y填0即可，其他参数照常填写
     * @return
     */
    public int getQ_chapter_node()
    {
        return q_chapter_node;
    }

    /**
     * set 点击的是章节上的节点，此项填写后，q_force_show栏的位置填写x和y填0即可，其他参数照常填写
     */
    public void setQ_chapter_node(int q_chapter_node)
    {
        this.q_chapter_node = q_chapter_node;
    }

    /**
     * get 强制引导漏洞显示位置以及区域（参数说明( 漏洞      1, 区域坐标x y 宽 高, 0 控件对齐方式，是否隐藏手指（0不隐藏，1隐藏）
     * @return
     */
    public String getQ_force_show()
    {
        return q_force_show;
    }

    /**
     * set 强制引导漏洞显示位置以及区域（参数说明( 漏洞      1, 区域坐标x y 宽 高, 0 控件对齐方式，是否隐藏手指（0不隐藏，1隐藏）
     */
    public void setQ_force_show(String q_force_show)
    {
        this.q_force_show = q_force_show;
    }

    /**
     * get 当前引导步骤开始需要的前置引导步骤(不填或者0,0代表没有前置引导步骤，填写格式为:主项ID_子项ID)
     * @return
     */
    public String getQ_pre_finish_step()
    {
        return q_pre_finish_step;
    }

    /**
     * set 当前引导步骤开始需要的前置引导步骤(不填或者0,0代表没有前置引导步骤，填写格式为:主项ID_子项ID)
     */
    public void setQ_pre_finish_step(String q_pre_finish_step)
    {
        this.q_pre_finish_step = q_pre_finish_step;
    }

    /**
     * get 当终止这一步引导的时候，需要恢复的引导步骤(不填或者0,0代表不需要恢复, 填写格式为(主项ID_子项ID;主项ID_子项ID))
     * @return
     */
    public String getQ_restore_step()
    {
        return q_restore_step;
    }

    /**
     * set 当终止这一步引导的时候，需要恢复的引导步骤(不填或者0,0代表不需要恢复, 填写格式为(主项ID_子项ID;主项ID_子项ID))
     */
    public void setQ_restore_step(String q_restore_step)
    {
        this.q_restore_step = q_restore_step;
    }

    /**
     * get 提示UI坐标偏移量0从左边出现，1从右边出现(0_x_y)
     * @return
     */
    public String getQ_move_by()
    {
        return q_move_by;
    }

    /**
     * set 提示UI坐标偏移量0从左边出现，1从右边出现(0_x_y)
     */
    public void setQ_move_by(String q_move_by)
    {
        this.q_move_by = q_move_by;
    }

    /**
     * get 是否需要提示
     * @return
     */
    public int getQ_need_notice_ui()
    {
        return q_need_notice_ui;
    }

    /**
     * set 是否需要提示
     */
    public void setQ_need_notice_ui(int q_need_notice_ui)
    {
        this.q_need_notice_ui = q_need_notice_ui;
    }

    /**
     * get 提示UI显示的层级
     * @return
     */
    public int getQ_ui_zorder()
    {
        return q_ui_zorder;
    }

    /**
     * set 提示UI显示的层级
     */
    public void setQ_ui_zorder(int q_ui_zorder)
    {
        this.q_ui_zorder = q_ui_zorder;
    }

    /**
     * get 提示图片是否需要翻转(0:不需要 1:需要)
     * @return
     */
    public int getQ_image_flip()
    {
        return q_image_flip;
    }

    /**
     * set 提示图片是否需要翻转(0:不需要 1:需要)
     */
    public void setQ_image_flip(int q_image_flip)
    {
        this.q_image_flip = q_image_flip;
    }

    /**
     * get 提示语言ID
     * @return
     */
    public int getQ_language()
    {
        return q_language;
    }

    /**
     * set 提示语言ID
     */
    public void setQ_language(int q_language)
    {
        this.q_language = q_language;
    }

    /**
     * get 引导出现延迟时间（毫秒）
     * @return
     */
    public int getQ_delay()
    {
        return q_delay;
    }

    /**
     * set 引导出现延迟时间（毫秒）
     */
    public void setQ_delay(int q_delay)
    {
        this.q_delay = q_delay;
    }

    /**
     * get 手指特效偏移量(第一位0代表不翻转，1代表翻转）
     * @return
     */
    public String getQ_finger_offset()
    {
        return q_finger_offset;
    }

    /**
     * set 手指特效偏移量(第一位0代表不翻转，1代表翻转）
     */
    public void setQ_finger_offset(String q_finger_offset)
    {
        this.q_finger_offset = q_finger_offset;
    }

    /**
     * get 引导类型（1 普通引导，2 对话引导）
     * @return
     */
    public int getQ_guide_type()
    {
        return q_guide_type;
    }

    /**
     * set 引导类型（1 普通引导，2 对话引导）
     */
    public void setQ_guide_type(int q_guide_type)
    {
        this.q_guide_type = q_guide_type;
    }

    /**
     * get 对话的时候显示的半身像英雄ID以及显示在左边还是右边(填0表示显示召唤师半身像_1表示左边;2表示右边)
     * @return
     */
    public String getQ_hero_id()
    {
        return q_hero_id;
    }

    /**
     * set 对话的时候显示的半身像英雄ID以及显示在左边还是右边(填0表示显示召唤师半身像_1表示左边;2表示右边)
     */
    public void setQ_hero_id(String q_hero_id)
    {
        this.q_hero_id = q_hero_id;
    }
}
