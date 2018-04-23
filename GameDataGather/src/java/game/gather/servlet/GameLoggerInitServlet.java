/**
 * @date 2014/7/24
 * @author ChenLong
 */
package game.gather.servlet;

import com.haowan.logger.service.ServicesFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 初始化GameLogger
 *
 * @author ChenLong
 */
public class GameLoggerInitServlet extends HttpServlet
{
    private final Logger logger = Logger.getLogger(GameLoggerInitServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        System.out.println("GameLoggerInitServlet is initializing GameLogger");
        logger.info("GameLoggerInitServlet is initializing GameLogger");

        ServletContext sc = config.getServletContext();
        String filePath = sc.getRealPath("/") + "/WEB-INF/server-config.xml";
        File file = new File(filePath);
        if (file.exists())
        {
            System.out.println("Initialize GameLogger from " + filePath);
            logger.info("Initialize GameLogger from " + filePath);

            try
            {
                MongoDBConfig mongoDBConfig = loadConfig(filePath);
                if (mongoDBConfig != null)
                {
                    ServicesFactory.getSingleLogService().initGameLogger(
                            mongoDBConfig.getGameId(),
                            mongoDBConfig.getMongodbHost(),
                            Integer.toString(mongoDBConfig.getMongodbPort()),
                            mongoDBConfig.getMongodbName(),
                            mongoDBConfig.getMongodbUser(),
                            mongoDBConfig.getMongodbPasswd());
                }
                else
                {
                    logger.error("mongoDBConfig is null");
                }
            }
            catch (ParserConfigurationException | SAXException | IOException ex)
            {
                logger.error("ParserConfigurationException | SAXException | IOException", ex);
            }
        }
        else
        {
            logger.error("Initialize GameLogger, cannot read config file " + filePath);
        }

        super.init(config);
    }

    private MongoDBConfig loadConfig(String filePath) throws ParserConfigurationException, SAXException, IOException
    {
        MongoDBConfig mongodbConfig = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i)
        {
            Node child = children.item(i);
            if (child instanceof Element)
            {
                String name = child.getNodeName();
                switch (name)
                {
                    case "game-logger":
                        mongodbConfig = getMongoDBConf(child);
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                        break;
                }
            }
        }
        return mongodbConfig;
    }

    private MongoDBConfig getMongoDBConf(Node node)
    {
        MongoDBConfig mongoDBConfig = null;
        String gameId = null;
        String mongodbHost = null;
        int mongodbPort = 0;
        String mongodbName = null;
        String mongodbUser = null;
        String mongodbPasswd = null;

        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "game-id":
                    gameId = attValue;
                    break;
                case "mongodb-host":
                    mongodbHost = attValue;
                    break;
                case "mongodb-port":
                    mongodbPort = Integer.parseInt(attValue);
                    break;
                case "mongodb-name":
                    mongodbName = attValue;
                    break;
                case "mongodb-user":
                    mongodbUser = attValue;
                    break;
                case "mongodb-passwd":
                    mongodbPasswd = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
        if (gameId == null || gameId.isEmpty()
                || mongodbHost == null || mongodbHost.isEmpty()
                || mongodbPort == 0
                || mongodbName == null || mongodbName.isEmpty()
                || mongodbUser == null
                || mongodbPasswd == null)
        {
            logger.error("Configure file error !");
        }
        else
        {
            mongoDBConfig = new MongoDBConfig(gameId, mongodbHost, mongodbPort, mongodbName, mongodbUser, mongodbPasswd);
        }
        return mongoDBConfig;
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
            out.println("<title>Servlet GameLoggerInitServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GameLoggerInitServlet at " + request.getContextPath() + "</h1>");
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
