package epoint.hive2;


import java.sql.*;

/**
 *  Hive2 JAVA操作实例
 */

public class Apps {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection con = DriverManager.getConnection("jdbc:hive2://192.168.188.51:10000/default","app","");
        PreparedStatement sta = con.prepareStatement("select * from rk limit 20");
        ResultSet result = sta.executeQuery();
        while(result.next()){
            System.out.println(result.getString("name")+"      "+result.getString("rk.address"));
        }
    }
}








