package com.mycompany.btlcsdlpt;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    public static Connection dbConn() {
        Connection conn = null;
        try {
            //Nap driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //tao chuoi connection
            String connectionUrl = "jdbc:sqlserver://DESKTOP-U5AJRHS\\CSDLPTNHOM5:1433;" + 
                    "databaseName=QLTH_TrainRide21; user=sa; password=12345;encrypt=false";
            //connect DB
            conn = DriverManager.getConnection(connectionUrl);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}