/**
 * @date 2014/7/23
 */
package game.keygenerator.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class KeyGenerateServlet extends HttpServlet
{
    private final static Logger logger = Logger.getLogger(KeyGenerateServlet.class);
    private SqlSessionFactory sqlSessionFactory = null;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        logger.info("in KeyGenerateServlet.init");
        ServletContext sc = config.getServletContext();
        String configPath = sc.getRealPath("/") + "WEB-INF\\db-keygenerator.xml";
        File configFile = new File(configPath);
        if (configFile.exists())
        {
            logger.info("initialize mybatic from: [" + configPath + "]");
            try
            {
                try (InputStream in = new FileInputStream(configPath))
                {
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
                }
            }
            catch (IOException ex)
            {
                logger.error("initialize mybatic failure from: [" + configPath + "]", ex);
            }
        }
        else
        {
            logger.error("initialize mybatic failure from: [" + configPath + "]");
            throw new IllegalStateException();
        }

        super.init(config);
    }

    public int insert(KeyBean keyBean)
    {
        try (SqlSession session = sqlSessionFactory.openSession())
        {
            int rows = session.insert("keyTable.insert", keyBean);
            logger.info("插入新的激活码：" + Long.toString(keyBean.getSerialNo(), 36).toUpperCase());
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        return 0;
    }
    
    public int update(KeyBean keyBean)
    {
        try (SqlSession session = sqlSessionFactory.openSession())
        {
            int rows = session.insert("keyTable.update", keyBean);
            logger.info("更新激活码：" + Long.toString(keyBean.getSerialNo(), 36).toUpperCase());
            session.commit();
            return rows;
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        return 0;
    }
    
    public KeyBean select(long serialNo)
    {
        try (SqlSession session = sqlSessionFactory.openSession())
        {
            KeyBean keyBean = (KeyBean) session.selectOne("keyTable.selectByKey", serialNo);
            return keyBean;
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        return null;
    }

    /**
     * 对指定数字取余, 使余数落在指定范围
     * 
     * @param resource 指定数字
     * @param min
     * @param max
     * @return 
     */
    public long longToScope(long resource, long min, long max)
    {
        Validate.isTrue(min >= 0);
        Validate.isTrue(max >= min);
        
        return Math.abs(resource) % (max - min + 1) + min;
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
            String typeStr = request.getParameter("type");
            String countStr = request.getParameter("count");

            final int count = Integer.valueOf(countStr);
            final int type = Integer.valueOf(typeStr);
            
            long minVal = Long.parseLong("A0000000", 36);
            long maxVal = Long.parseLong("ZZZZZZZZ", 36);
            List<Long> list = new ArrayList<>(count);
            Random rd = new Random(System.currentTimeMillis());
            
            //开始产生鸡和马
            long rand;
            long startTime = System.currentTimeMillis();
            for (int index = 0; index < count; ++index)
            {
                rand = longToScope(rd.nextLong(), minVal, maxVal);
                while (select(rand) != null) //如果数据库中已经存在, 重新产生
                {
                    rand = longToScope(rd.nextLong(), minVal, maxVal);
                }
                list.add(rand);
            }
            long timeUsed = System.currentTimeMillis() - startTime;

            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet KeyGeneratServlet</title>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>生成的序列号:</h1>");
            StringBuilder builder = new StringBuilder();
            Date time = Calendar.getInstance().getTime();
            
            //开始插入到数据库, 插入成功就输出到页面
            startTime = System.currentTimeMillis();
            for (int index = 0; index < list.size(); ++index)
            {
                long key = list.get(index);
                KeyBean keyBean = new KeyBean();
                keyBean.setSerialNo(key);
                keyBean.setSerialType(type);
                keyBean.setCreateTime(time);
                keyBean.setUsed(0);
                keyBean.setUseTime(time);
                keyBean.setRoleId(0);
                
                if (insert(keyBean) == 0)
                    logger.error("插入数据库时出错, 出错的激活码为: " + Long.toString(key, 36).toUpperCase());
                else
                {
                    builder.append(Long.toString(key, 36).toUpperCase());
                    if (index != list.size() - 1)
                        builder.append(" ");
                }
            }
           long dbTimeUsed = System.currentTimeMillis() - startTime;
           
            out.println("<h3>" + builder.toString() + "</h3>");
            out.println("<h1> 生成 " + count + " 个, 用时 " + timeUsed  + " 毫秒, 数据库操作用时: " + dbTimeUsed + " 毫秒</h1>");
            
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
