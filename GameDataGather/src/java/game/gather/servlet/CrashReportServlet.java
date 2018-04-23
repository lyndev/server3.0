/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gather.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;



/**
 *
 * 接收客户端的错误记录
 * @author Pengmian
 */
public class CrashReportServlet extends HttpServlet
{
    /// bug 记录相对
    private static final String DEFAULT_RECORD_PATH = "/bug_report/";

    /// 时间格式
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /// bug 记录的实际路径
    private String recordPath = "";
    
    /// 同步对象
    private static final Object obj = new Object();

    /**
     * 重写父类初始化，创建bug记录的目录
     *
     * @param cfg
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig cfg) throws ServletException
    {
        super.init(cfg);
        recordPath = getServletContext().getRealPath("/") + DEFAULT_RECORD_PATH;
        File recordDirectory = new File(recordPath);
        if (!recordDirectory.exists())
        {
            recordDirectory.mkdir();
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
            String key = request.getParameter("key");
            if (key == null || key.equals(""))
            {                
                //System.out.println("invalid parameters\n");
                return;
            }

            String message = request.getParameter("message");
            if (message == null || message.equals(""))
            {
                //System.out.println("invalid parameters\n");
                return;
            }
            
            synchronized (obj)
            {
                //System.out.println("1");
                if (!isBugExist(key))
                {
                    out.write("ok\n");
                    writeToFile(key, message);
                    //System.out.println("ok\n");
                }
                else
                {
                    out.write("dup\n");
                    //System.out.println("dup\n");
                }
            }

            
            
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

    /**
     * 记录bug信息到文件
     *
     * @param key
     * @param message
     */
    private void writeToFile(String key, String message)
    {
        FileWriter wr = null;
        
        try
        {
            //System.out.println(message);      
            File bugFile = new File(recordPath + "/" + getFilename(key));
            wr = new FileWriter(bugFile);
            Date date = new Date();
            BufferedWriter buffer = new BufferedWriter(wr);
            buffer.write("*********************\n");
            buffer.write("报告时间:" + DATE_FORMAT.format(date) + "\n");
            buffer.write("关键字:" + key + "\n");
            buffer.write("描述:" + decode(message) + "\n");
            buffer.write("*********************\n");
            buffer.flush();
            wr.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(CrashReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (wr != null)
                    wr.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(CrashReportServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 获得文件名
     *
     * @param key
     * @return
     */
    private String getFilename(String key)
    {
        return key + ".bug";
        /*
         String str[] = key.split("_");
         if (str.length != 2)
         {
         return "default.bug";
         }
         return str[0];
         */
    }

    /**
     * 通过文件名判断是否bug已经记录了
     *
     * @param key
     * @param message
     * @return
     */
    private boolean isBugExist(String key)
    {
        //System.out.println(key);
        File file = new File(recordPath + "/" + getFilename(key));
        return file.exists();
    }

    /**
     * base64 decode
     * @param message
     * @return 
     */
    private String decode(String message)
    {
        byte[] bytes = Base64.decodeBase64(message);
        return new String(bytes);
    }
}
