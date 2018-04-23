package game.server.logic.keys.handler;

import com.haowan.logger.service.ServicesFactory;
import game.core.command.ICommand;
import game.server.logic.keys.bean.KeyUseInfo;
import org.apache.log4j.Logger;

/**
 *
 * <b>验证并使用激活码/兑换码的Handler.</b>
 * <p>
 * Description...
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class KeysVerifyHandler implements ICommand
{

    private final static Logger LOG = Logger.getLogger(KeysVerifyHandler.class);

    private final KeyUseInfo useInfo;

    public KeysVerifyHandler(KeyUseInfo useInfo)
    {
        this.useInfo = useInfo;
    }

    @Override
    public void action()
    {
        LOG.info("发送激活码验证信息：" + useInfo.toString());
        ServicesFactory.getSingleVerifyService().addDecodeActiveCode(
                useInfo.getKey(), useInfo.getToken(), useInfo.getServerId(), useInfo.getPlayerId());
    }

}
