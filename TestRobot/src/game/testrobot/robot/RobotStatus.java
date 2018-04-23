/**
 * @date 2014/7/1
 * @author ChenLong
 */
package game.testrobot.robot;

/**
 * 机器人状态, 用于状态机运行
 *
 * @author ChenLong
 */
public enum RobotStatus
{
    NOT_CONNECT, // 未连接
    CONNECTED, // 已连接未登录
    LOGINING,
    LOGIN_FAILED,
    LOGIN_SUCCESS,
    PINGPONG,
    MAIN_MISSION,
}
