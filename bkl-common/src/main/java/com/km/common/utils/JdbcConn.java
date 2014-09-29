package com.km.common.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JdbcConn {
    private String url = "";
    private String username = "";
    private String password = "";
    private String driverName = "com.mysql.jdbc.Driver";

    private Connection conn;
    private ResultSet rs;
    private Statement stmt;

    public JdbcConn() {
        super();
    }
    
    public JdbcConn(String url,String username,String password) {
    	this.url = url;
    	this.username = username;
    	this.password = password;
    }

    public Connection openConnection() throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        conn = DriverManager.getConnection(url, username, password);
        executeUpdate("set sql_mode = '';");
        return conn;
    }

    public void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            rs =null;
            stmt = null;
            conn = null;
        }

    }

    public boolean executeUpdate(String strSql) throws SQLException {
        // getConnection(_DRIVER,_URL,_USER_NA,_PASSWORD);
        boolean flag = false;
        if (stmt == null)
            stmt = conn.createStatement();
        try {
            if (0 < stmt.executeUpdate(strSql)) {
                flag = true;
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return flag;

    }

    public ResultSet executeSql(String strSql) throws Exception {
        //if (stmt == null)
        stmt = conn.createStatement();

        rs = stmt.executeQuery(strSql);
        return rs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public static void main(String[] args) throws Exception {
        JdbcConn conn = new JdbcConn();
        conn.setDriverName("com.mysql.jdbc.Driver");
        conn.setUsername("root");
        conn.setPassword("123456");
        conn.setUrl("jdbc:mysql://localhost:3306/test");
        conn.openConnection();
        conn.executeUpdate("delete from student where name = 'guotianlian'");
        ResultSet rs = conn.executeSql("select * from student");
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        rs.close();
        conn.closeConnection();
        // String sql = "select * from students";
    }
}