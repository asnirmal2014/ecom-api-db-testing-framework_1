package com.webprj.ecom.tests.utils;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility to clean test data when needed.
 * Using old-style resource management as requested.
 */
public class DatabaseCleanup
{
    public static void cleanAllTestData()
    {
        Connection conn = null;
        Statement stmt = null;
        
        try
        {
            conn = DBUtils.getConnection();
            stmt = conn.createStatement();

            // Delete in correct order to avoid foreign key issues
            stmt.executeUpdate("DELETE FROM payments WHERE order_id > 100");
            stmt.executeUpdate("DELETE FROM order_items WHERE order_id > 100");
            stmt.executeUpdate("DELETE FROM orders WHERE id > 100");
            stmt.executeUpdate("DELETE FROM products WHERE id > 100");
            stmt.executeUpdate("DELETE FROM users WHERE id > 100");

            System.out.println("🧹 Test data cleaned successfully.");
        }
        catch (Exception e)
        {
            System.out.println("❌ Cleanup failed: " + e.getMessage());
        }
        finally
        {
            // Close resources manually (old style)
            try
            {
                if (stmt != null) stmt.close();
            }
            catch (Exception e) { /* ignore */ }
            
            try
            {
                if (conn != null) conn.close();
            }
            catch (Exception e) { /* ignore */ }
        }
    }
}