package ijarvis_hdfs.hive2;


import java.sql.*;

/**
 *  Hive2 JAVA操作实例
 */

public class Apps {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection con = DriverManager.getConnection("jdbc:hive2://192.168.208.63:10000/default","app","");
        PreparedStatement sta = con.prepareStatement("select * from epoint_nginx_log_test where accessip='61.147.254.2'");
        ResultSet result = sta.executeQuery();
        while(result.next()){
            System.out.println(result.getString("accessip")+"      "+result.getString("requesturi"));
        }
    }
}








