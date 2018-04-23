/* *
 * 2014/5/12 by ChenLong
 * JS脚本入口
 */

var mainLog = Packages.org.apache.log4j.Logger.getLogger("main.js");

/**
 * JS脚本入口函数
 * @param {Object} arg
 * @returns {undefined}
 */
function main(arg)
{
    mainLog.info("call js script");
    dispatchCall(arg);
}

function dispatchCall(arg)
{
    /*
    if (arg instanceof Packages.java.util.EnumMap)
    {
	functionName = arg.get(Packages.game.server.logic.util.ScriptArgEnum.FUNCTION_NAME);
	println("functionName: [" + functionName + "]");
	loginSuccess();
    }
    else
    {
	mainLog.info("Unknow arg type: " + arg);
    }
    */
}


