/**
 * @date 2014/6/9
 * @author ChenLong
 */
package game.gather.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Log4J配置加载, 开发环境下加载log4j_devel.xml, 生产环境下加载log4j_server.xml
 *
 * @author ChenLong
 */
public class Log4JInitServlet extends HttpServlet
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        System.out.println("Log4JInitServlet is initializing log4j");

        ServletContext sc = config.getServletContext();
        String configPath = sc.getRealPath("/") + "/WEB-INF/";
        String val = System.getProperty("ideDebug");
        if (val != null && val.equals("true"))
        {
            configPath += "log4j_devel.xml";

        }
        else
        {
            configPath += "log4j_server.xml";
        }

        File configFile = new File(configPath);
        if (configFile.exists())
        {
            System.out.println("Initialize log4j from " + configPath);
            DOMConfigurator.configureAndWatch(configPath);
        }
        else
        {
            System.out.println("Initialize log4j BasicConfigurator, cannot read config file " + configPath);
            BasicConfigurator.configure();
        }

        super.init(config);
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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Log4JInitServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Log4JInitServlet at " + request.getContextPath() + "</h1>");
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
