/**
 * @date 2014/6/9
 * @author ChenLong
 */
package game.gather.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.haowan.logger.service.ServicesFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.InflaterInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author ChenLong
 */
public class GatherServlet extends HttpServlet
{
    private final static Logger logger = Logger.getLogger(GatherServlet.class);

    public static String uncompressZip(byte[] b) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        InflaterInputStream zipStream = new InflaterInputStream(in);
        try
        {
            byte[] buffer = new byte[256];
            int n;
            while ((n = zipStream.read(buffer)) >= 0)
            {
                out.write(buffer, 0, n);
            }

            // toString()使用平台默认编码，也可以显式的指定如toString(\"GBK\")
            return out.toString("UTF-8");
        }
        finally
        {
            out.close();
            in.close();
            zipStream.close();
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            long startTime = System.currentTimeMillis();

            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String platformId = request.getParameter("platformId");
            //int serverId = Integer.parseInt(request.getParameter("serverId"));
            String content = request.getParameter("content");
            String isPlainText = request.getParameter("isplaintext");

            long startConvertTime = System.currentTimeMillis();

            if (content == null || content.isEmpty())
            {
                out.println();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "未提交content");
                return;
            }
            content = content.trim();
            if (isPlainText == null || isPlainText.isEmpty())
            {
                try
                {
                    String cryptContent = content;
                    byte[] b = Base64.decodeBase64(cryptContent);
                    content = uncompressZip(b);
                    //content = new String(ZipUtils.decompress(b), Charset.forName("UTF-8"));
                }
                catch (Exception ex)
                {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    ex.printStackTrace(out);
                    logger.error("Exception", ex);
                    return;
                }
            }

            try
            {
                JSONArray jsonArray = JSON.parseArray(content);
                if (jsonArray != null)
                {
                    for (int i = 0; i < jsonArray.size(); ++i)
                    {
                        try
                        {
                            JSONObject logJsonObj = jsonArray.getJSONObject(i);
                            //ServicesFactory.getSingleLogService().addCommonLog1(logJsonObj.toJSONString());
                            String fgi = logJsonObj.getString("fgi");
                            String serverId = logJsonObj.getString("serverId");
                            String playerId = logJsonObj.getString("playerId");
                            String fedId = logJsonObj.getString("fedId");
                            String type = logJsonObj.getString("type");
                            String group = logJsonObj.getString("group");
                            String event = logJsonObj.getString("event");
                            String time_spent = logJsonObj.getString("time_spent");
                            String order = logJsonObj.getString("order");
                            String param = logJsonObj.getString("param");
                            String unixtimestamp = logJsonObj.getString("unixtimestamp");

                            ServicesFactory.getSingleLogService().addLogPlayerBehavior(fgi, serverId, playerId, fedId, type, group, event, time_spent, order, param, unixtimestamp);
                        }
                        catch (JSONException ex)
                        {
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            ex.printStackTrace(out);
                            logger.error("Exception", ex);
                        }
                        catch (Throwable ex)
                        {
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            ex.printStackTrace(out);
                            logger.error("Exception", ex);
                        }
                    }
                }
                else
                {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.println("cannot convert to a json array, content :" + content);
                    logger.error("cannot convert to a json array, content :" + content);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace(out);
                logger.error("error", ex);
            }

            long endTime = System.currentTimeMillis();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NewServlet</title>");
            out.println("</head>");
            out.println("<body>");
            //out.println("<h1>Gather Servlet submit task, future:" + request.getContextPath() + "</h1>");
            out.println("time: " + (endTime - startTime) / 1000 + " second");
            out.println();
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
