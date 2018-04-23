/**
 * @date 2014/5/5
 * @author ChenLong
 */
package game.server.logic.support;

import com.alibaba.fastjson.JSON;


/**
 * 保存为JSON/从JSON初始化接口
 * @author ChenLong
 */
public interface IJsonConverter
{
    /**
     * 转为JSON对象
     * @return 
     */
    JSON toJson();

    /**
     * 从JSON对象初始化
     * @param json 
     */
    void fromJson(JSON json);
}
