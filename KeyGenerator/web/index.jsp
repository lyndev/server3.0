<%--
    Document   : index
    Created on : 2014-7-22, 16:07:28
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>卡牌三国激活码生成页面</h1>
        <form name="input" action="KeyGenerateServlet" method="get" accept-charset="UTF-8">
            <table>
                <tr>
                    <td valign="top">类型</td>
                    <td><input type="text" name="type" value="1" required/></td>
                </tr>
                <tr>
                    <td valign="top">个数</td>
                    <td><input type="text" name="count" value="20" required/></td>
                </tr>
                <tr>
                    <td colspan=2 align="center"><input type="submit" value="提交" /></td>
                </tr>
            </table>
        </form>
    </body>
</html>
