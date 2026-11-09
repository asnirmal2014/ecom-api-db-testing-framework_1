package com.webprj.ecom.tests.utils;

import com.webprj.ecom.tests.model.DBOrder;
import com.webprj.ecom.tests.model.DBOrderItem;
import com.webprj.ecom.tests.model.DBPayment;
import com.webprj.ecom.tests.model.DBProduct;
import com.webprj.ecom.tests.model.DBUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for direct database operations used in API + DB validation tests.
 * Supports all tables: users, products, orders, order_items, payments.
 */
public class DBUtils
{
    private static final String URL = "jdbc:mysql://localhost:3306/ecom_test_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Full cleanup - Only use manually when needed.
     * Tables are deleted in correct order to avoid foreign key constraint errors.
     */
    public static void cleanDatabase()
    {
        // Important: Delete child tables first, then parent tables
        String[] tables = {"payments", "order_items", "orders", "products", "users"};
        
        Connection conn = null;
        Statement stmt = null;
       
        try
        {
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            System.out.println("🧹 FULL DATABASE CLEANUP STARTED (Manual call only)...");

            for (String table : tables)
            {
                stmt.executeUpdate("DELETE FROM " + table);
                System.out.println("   Deleted all rows from " + table);
            }
           
            conn.commit();
            System.out.println("✅ Full database cleaned.");
        }
        catch (SQLException e)
        {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            System.err.println("❌ Failed to clean database - " + e.getMessage());
            throw new RuntimeException("Database cleanup failed", e);
        }
        finally
        {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    // ==================== USER ====================
    public static boolean isUserPresentById(Long userId)
    {
        String query = "SELECT 1 FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
       
        try
        {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
           
            return rs.next();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to check user existence", e);
        }
        finally
        {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conn);
        }
    }

    public static DBUser getUserById(Long userId)
    {
        String query = "SELECT id, name, email, created_at, updated_at FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            if (rs.next())
            {
                DBUser user = new DBUser();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
               
                if (rs.getTimestamp("created_at") != null)
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
               
                if (rs.getTimestamp("updated_at") != null)
                    user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                
                System.out.println("🔍 DB User fetched: " + user);
                return user;
            }
           
            System.out.println("❌ User not found in DB with ID: " + userId);
            return null;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to fetch user from DB", e);
        }
        finally
        {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conn);
        }
    }

    // ==================== PRODUCT ====================
    public static DBProduct getProductById(Long productId)
    {
        String query = "SELECT id, name, description, price, stock, created_at, updated_at " +
                       "FROM products WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setLong(1, productId);
            rs = ps.executeQuery();
            if (rs.next())
            {
                DBProduct product = new DBProduct();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setStock(rs.getInt("stock"));
                
                if (rs.getTimestamp("created_at") != null)
                {
                    product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
               
                if (rs.getTimestamp("updated_at") != null)
                {
                    product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                return product;
            }
            return null;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to fetch product from DB", e);
        }
        finally
        {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conn);
        }
    }
    
    public static boolean isProductPresentById(Long productId)
    {
        String query = "SELECT 1 FROM products WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
      
        try
        {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setLong(1, productId);
            rs = ps.executeQuery();
          
            return rs.next();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to check product existence", e);
        }
        finally
        {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conn);
        }
    }

    // ==================== ORDER ====================
    public static DBOrder getOrderById(Long orderId)
    {
        String query = "SELECT id, user_id, total_amount, status, cancelled_at, created_at, updated_at " +
                       "FROM orders WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setLong(1, orderId);
            rs = ps.executeQuery();
            if (rs.next())
            {
                DBOrder order = new DBOrder();
                order.setId(rs.getLong("id"));
                order.setUserId(rs.getLong("user_id"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setStatus(rs.getString("status"));
                
                if (rs.getTimestamp("cancelled_at") != null)
                    order.setCancelledAt(rs.getTimestamp("cancelled_at").toLocalDateTime());
                
                if (rs.getTimestamp("created_at") != null)
                    order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                if (rs.getTimestamp("updated_at") != null)
                    order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                
                System.out.println("🔍 DB Order fetched: " + order);
                return order;
            }
            return null;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to fetch order from DB", e);
        }
        finally
        {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conn);
        }
    }

    // ==================== ORDER ITEM ====================
    public static DBOrderItem getOrderItemById(Long orderItemId)
    {
        String query = "SELECT id, order_id, product_id, quantity, item_price, created_at, updated_at " +
                       "FROM order_items WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setLong(1, orderItemId);
            rs = ps.executeQuery();
            if (rs.next())
            {
                DBOrderItem item = new DBOrderItem();
                item.setId(rs.getLong("id"));
                item.setOrderId(rs.getLong("order_id"));
                item.setProductId(rs.getLong("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setItemPrice(rs.getBigDecimal("item_price"));
                
                if (rs.getTimestamp("created_at") != null)
                    item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                if (rs.getTimestamp("updated_at") != null)
                    item.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                
                return item;
            }
            return null;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to fetch order item from DB", e);
        }
        finally
        {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conn);
        }
    }

    // ====================== PAYMENT METHODS ======================
    public static DBPayment getPaymentById(Long paymentId)
    {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setLong(1, paymentId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    DBPayment payment = new DBPayment();
                    payment.setId(rs.getLong("id"));
                    payment.setOrderId(rs.getLong("order_id"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setPaymentMethod(rs.getString("payment_method"));
                    payment.setStatus(rs.getString("status"));
                    payment.setTransactionId(rs.getString("transaction_id"));
                    payment.setPaymentDate(rs.getTimestamp("payment_date") != null ? 
                            rs.getTimestamp("payment_date").toLocalDateTime() : null);
                    payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return payment;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("❌ Error fetching payment: " + e.getMessage());
        }
        return null;
    }

    // ==================== HELPER METHODS ====================
    private static void closeConnection(Connection conn)
    {
        if (conn != null)
        {
            try 
            { 
            	conn.close(); 
            } 
            catch (Exception ignored) {}
        }
    }

    private static void closeStatement(Statement stmt)
    {
        if (stmt != null)
        {
            try 
            { 
            	stmt.close(); 
            } 
            catch (Exception ignored) {}
        }
    }

    private static void closePreparedStatement(PreparedStatement ps)
    {
        if (ps != null)
        {
            try 
            { 
            	ps.close(); 
            } 
            catch (Exception ignored) {}
        }
    }

    private static void closeResultSet(ResultSet rs)
    {
        if (rs != null)
        {
            try 
            { 
            	rs.close(); 
            } 
            catch (Exception ignored) {}
        }
    }
}