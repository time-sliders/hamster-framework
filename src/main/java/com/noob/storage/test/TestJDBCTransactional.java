package com.noob.storage.test;

import java.sql.*;

/**
 * @author 卢云(luyun)
 * @version TPro
 * @since 2019.05.15
 */
public class TestJDBCTransactional {

    public static void testJDBCTrans() {
        try {
            /*
             * 1. Obtaining a connection from the DriverManager
             */
            Connection conn =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/zhw?" +
                            "user=root&password=root");

            conn.setAutoCommit(false);// open transactional

            /*
             * 2. Using JDBC Statement Objects to Execute SQL
             * https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-statements.html
             */
            Statement stmt = null;
            ResultSet rs = null;

            try {
                stmt = conn.createStatement();
                // Query
                rs = stmt.executeQuery("SELECT * FROM zhw.user_info");
                while (rs.next()) {
                    System.out.println("jdbc queried name: " + rs.getString("name"));
                }

                // Update
                if (stmt.execute("update zhw.user_info set gmt_modified = now() where name = '张伟'")) {
                    rs = stmt.getResultSet();
                    // Now do something with the ResultSet ....
                } else {
                    System.out.println("jdbc update count:" + stmt.getUpdateCount());
                }

                conn.commit();
                System.out.println("jdbc test successfully");
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                conn.rollback();

            } catch (Throwable e) {
                System.out.println(e.getMessage());
                conn.rollback();

            } finally {
                // it is a good idea to release
                // resources in a finally{} block
                // in reverse-order of their creation
                // if they are no-longer needed
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ignore) {
                    } // ignore
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ignore) {
                    } // ignore
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
