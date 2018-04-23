/* *
 * 2014/5/12 by ChenLong
 */


function loginSuccess(arg)
{
    var log = Packages.org.apache.log4j.Logger.getLogger("loginSuccess.js");
    log.info("call JS function: loginSuccess");
    var session = arg.get(Packages.game.server.logic.util.ScriptArgs.Key.IO_SESSION);
    var player = arg.get(Packages.game.server.logic.util.ScriptArgs.Key.PLAYER);
    var functionName = arg.get(Packages.game.server.logic.util.ScriptArgs.Key.FUNCTION_NAME);
//    println("userId: " + session.getAttribute("GAME_USER_ID"));
//    println("userName: " + session.getAttribute("GAME_USER_NAME"));
//    println("player: " + player.toString());
//    addItem();
}