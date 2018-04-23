
import com.mysql.jdbc.Statement;
import game.server.world.room.bean.ZJHRoom;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.mariadb.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.Connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Test_ZJH
{
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
        /*
        //ZJH 测试发牌
        ZJHRoom _zjhRoom = new ZJHRoom();
        // 测试50组
        int TEST_COUNT = 1;
        for(int i = 0; i < TEST_COUNT; i++){
            System.out.println("=========================================");
            _zjhRoom.Test_DealHandCard();    
            System.out.println("=========================================");
        }*/
        
        String driver ="org.mariadb.jdbc.Driver";
        //从配置参数中获取数据库url
        String url = "jdbc:mariadb://47.106.91.153:3306/game_data";
        //从配置参数中获取用户名
        String user = "root";
        //从配置参数中获取密码
        String pass = "111111";

        //注册驱动
        Class.forName(driver);
        //获取数据库连接
         Connection conn = DriverManager.getConnection(url,user,pass);
          System.out.println("It works !");
        //创建Statement对象
//        Statement stmt = conn.createStatement();
        //执行查询
 //       ResultSet rs = stmt.executeQuery("select * from news_inf");
    }
}
