/* *
 * 2014/5/12 by ChenLong
 */

var jsFiles =
	[
	    "login/loginSuccess.js",
	    "item/item.js"
	];

var globalLog = Packages.org.apache.log4j.Logger.getLogger("global.js");

/**
 * 加载js文件
 * @param {type} filePath js文件相对路径(相对顶层目录)
 * @returns {undefined}
 */
function evalFile(filePath)
{
    globalLog.info("eval file: [" + filePath + "]");
    engine.eval(new java.io.FileReader(jsPath + '/' + filePath));
}

function evalAllFiles()
{
    for (i = 0; i < jsFiles.length; ++i)
	evalFile(jsFiles[i]);
}

evalAllFiles();
evalFile('main.js');
