/**
 * @date 2014/8/11
 * @author ChenLong
 */
package game.server.logic.util;

import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import game.server.thread.BackLogProcessor;
import java.util.Date;

/**
 *
 * @author ChenLong
 */
public class LoggingMisc
{
    private final static LoggingMisc instance;

    static
    {
        instance = new LoggingMisc();
    }

    public static LoggingMisc getInstance()
    {
        return instance;
    }

    public void addLogFuncJoin(String fgi, String serverId, String roleId, String fedId,
            String type, String group, String module, String order)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String type;
            private String group;
            private String module;
            private String order;
            private final Date date = new Date();

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String type, String group, String module, String order)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.type = type;
                this.group = group;
                this.module = module;
                this.order = order;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogFuncJoin(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        type,
                        group,
                        module,
                        order,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(fgi, serverId, roleId, fedId, type, group, module, order));
    }

    public void addLogFuncComplete(String fgi, String serverId, String roleId, String fedId,
            String type, String group, String module, String order)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String type;
            private String group;
            private String module;
            private String order;
            private final Date date = new Date();

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String type, String group, String module, String order)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.type = type;
                this.group = group;
                this.module = module;
                this.order = order;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogFuncComplete(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        type,
                        group,
                        module,
                        order,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(fgi, serverId, roleId, fedId, type, group, module, order));
    }

    public void addLogFuncFailed(String fgi, String serverId, String roleId, String fedId,
            String type, String group, String module, String order, String reason)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String type;
            private String group;
            private String module;
            private String order;
            private String reason;
            private final Date date = new Date();

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String type, String group, String module, String order, String reason)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.type = type;
                this.group = group;
                this.module = module;
                this.order = order;
                this.reason = reason;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogFuncFailed(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        type,
                        group,
                        module,
                        order,
                        reason,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(fgi, serverId, roleId, fedId, type, group, module, order, reason));
    }

    /**
     * 功能描述：采集用户获得资产日志。
     *
     * @param fgi 游戏和渠道的关联ID。
     * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
     * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
     * @param fedId 用户ID，由登录验证verfiy返回account字段
     * @param assetsId 资源唯一ID。
     * @param assetsName 资源名称,推荐填写中文
     * @param assetsType 资源类型,推荐填写中文。如：宠物/坐骑/卡牌/英雄等
     * @param assetsLevel 资源等级,如分X阶X星 请用小数表示
     * @param reason 获得原因,推荐填写中文
     */
    public void addLogGainAssets(String fgi, String serverId, String roleId, String fedId,
            String assetsId, String assetsName, String assetsType, String assetsLevel,
            String reason)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String assetsId;
            private String assetsName;
            private String assetsType;
            private String assetsLevel;
            private String reason;
            private final Date date = new Date();

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String assetsId, String assetsName, String assetsType, String assetsLevel,
                    String reason)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.assetsId = assetsId;
                this.assetsName = assetsName;
                this.assetsType = assetsType;
                this.assetsLevel = assetsLevel;
                this.reason = reason;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogGainAssets(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        assetsId,
                        assetsName,
                        assetsType,
                        assetsLevel,
                        reason,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(fgi, serverId, roleId, fedId, assetsId, assetsName, assetsType, assetsLevel, reason));
    }

    /**
     * 功能描述：采集用户资产升级日志。
     *
     * @param fgi 游戏和渠道的关联ID。
     * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
     * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
     * @param fedId 用户ID，由登录验证verfiy返回account字段
     * @param assetsId 资源唯一ID。
     * @param assetsName 资源名称。
     * @param assetsType 资源类型,推荐填写中文。如：宠物/坐骑/卡牌/英雄等
     * @param assetsLevel 资源等级，推荐填写中文
     */
    public void addLogAssetsLevelUp(String fgi, String serverId, String roleId, String fedId,
            String assetsId, String assetsName, String assetsType, String assetsLevel)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String assetsId;
            private String assetsName;
            private String assetsType;
            private String assetsLevel;
            private final Date date = new Date();

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String assetsId, String assetsName, String assetsType, String assetsLevel)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.assetsId = assetsId;
                this.assetsName = assetsName;
                this.assetsType = assetsType;
                this.assetsLevel = assetsLevel;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogAssetsLevelUp(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        assetsId,
                        assetsName,
                        assetsType,
                        assetsLevel,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(fgi, serverId, roleId, fedId, assetsId, assetsName, assetsType, assetsLevel));
    }

    /**
     * 功能描述：采集用户失去资产日志。
     *
     * @param fgi 游戏和渠道的关联ID。
     * @param serverId 区服ID，对于无区服游戏，请统一设置一个GSID,如：0
     * @param playerId 角色编号(无角色ID的游戏请设置为game_uid)
     * @param fedId 用户ID，由登录验证verfiy返回account字段
     * @param assetsId 资源唯一ID。
     * @param assetsName 资源名称。
     * @param assetsType 资源类型,推荐填写中文。如：宠物/坐骑/卡牌/英雄等
     * @param reason 失去原因。
     */
    public void addLogLoseAssets(String fgi, String serverId, String roleId, String fedId,
            String assetsId, String assetsName, String assetsType, String reason)
    {
        BackLogProcessor.getInstance().addCommand(new ICommand()
        {
            private String fgi;
            private String serverId;
            private String roleId;
            private String fedId;
            private String assetsId;
            private String assetsName;
            private String assetsType;
            private String reason;
            private final Date date = new Date();

            public ICommand set(String fgi, String serverId, String roleId, String fedId,
                    String assetsId, String assetsName, String assetsType, String reason)
            {
                this.fgi = fgi;
                this.serverId = serverId;
                this.roleId = roleId;
                this.fedId = fedId;
                this.assetsId = assetsId;
                this.assetsName = assetsName;
                this.assetsType = assetsType;
                this.reason = reason;

                return this;
            }

            @Override
            public void action()
            {
                ServicesFactory.getSingleLogService().addLogLoseAssets(
                        fgi,
                        serverId,
                        roleId,
                        fedId,
                        assetsId,
                        assetsName,
                        assetsType,
                        reason,
                        Long.toString(date.getTime() / 1000));
            }
        }.set(fgi, serverId, roleId, fedId, assetsId, assetsName, assetsType, reason));
    }
}
