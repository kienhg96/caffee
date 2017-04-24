/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author hoangkien
 */
public class BillContent {
    private int id;
    private Drink drink;
    private int quantity;
    
    public BillContent() {
        drink = null;
        quantity = 0;
        id = 0;
    }
    
    public int getId() {
        return id;
    }

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public boolean save(int billId) {
        Connection conn = Database.getConnection();
        try {
            if (id == 0) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO billcontent SET billId = ?, drinkId = ?, quantity = ?",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, billId);
                stmt.setInt(2, drink.getId());
                stmt.setInt(3, quantity);
                stmt.executeUpdate();
                ResultSet key = stmt.getGeneratedKeys();
                if (key.next()) {
                    id = key.getInt(1);
                }
            } else {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE billcontent SET billId = ?, drinkId = ?, quantity = ? WHERE id = ?");
                stmt.setInt(1, billId);
                stmt.setInt(2, drink.getId());
                stmt.setInt(3, quantity);
                stmt.setInt(4, id);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }
    
    public boolean delete() {
        if (id != 0) {
            Connection conn = Database.getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE from billcontent WHERE id = ?");
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                return false;
            }
        }
        return true;
    }
    
    public static BillContent findById(int id) {
        Connection conn = Database.getConnection();
        BillContent bc;
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM billcontent WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bc = new BillContent();
                bc.id = rs.getInt("id");
                bc.drink = Drink.findById(rs.getInt("drinkId"));
                bc.quantity = rs.getInt("quantity");
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        return bc;
    }
    
}