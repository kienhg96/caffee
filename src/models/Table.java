package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author hoangkien
 */
public class Table {
    private int id;
    private String name;
    private int status;
    private Bill bill;

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
    
    public Table() {
        name = "";
        status = 0;
        id = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public boolean save() {
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt;
            if (id != 0) {
                if (bill != null) {
                    bill.save();
                    stmt = conn.prepareStatement(
                        "UPDATE tables SET status = ?, billId = ?, name = ? WHERE id = ?");
                    stmt.setInt(1, status);
                    stmt.setInt(2, bill.getId());
                    stmt.setString(3, name);
                    stmt.setInt(4, id);
                } else {
                    stmt = conn.prepareStatement(
                        "UPDATE tables SET status = ?, name = ? WHERE id = ?");
                    stmt.setInt(1, status);
                    stmt.setString(2, name);
                    stmt.setInt(3, id);
                }
                stmt.executeUpdate();
            } else {
                stmt = conn.prepareStatement(
                        "INSERT INTO tables SET name = ?, status = 0", 
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, name);
                stmt.executeUpdate();
                ResultSet key = stmt.getGeneratedKeys();
                if (key.next()) {
                    id = key.getInt(1);
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }
    
    public void delete() {
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM tables WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    
    public static ArrayList<Table> getAll() {
        ArrayList<Table> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM tables");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Table t = new Table();
                t.setName(rs.getString("name"));
                t.setStatus(rs.getInt("status"));
                t.bill = Bill.findById(rs.getInt("billId"));
                t.id = rs.getInt("id");
                result.add(t);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        return result;
    }
    
    public static Table findByName(String name) {
        Connection conn = Database.getConnection();
        Table t = new Table();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM tables WHERE name LIKE ?");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t.setName(rs.getString("name"));
                t.setStatus(rs.getInt("status"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        return t;
    }
}
