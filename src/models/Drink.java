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
import java.util.ArrayList;

/**
 *
 * @author hoangkien
 */
public class Drink {
    private int id;
    private String name;
    private int price;
    
    public Drink() {
        name = "";
        price = 0;
        id = 0;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    public boolean save() {
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt;
            if (id != 0) {
                stmt = conn.prepareStatement(
                        "UPDATE drink SET price = ?, name = ? WHERE id = ?");
                stmt.setInt(1, price);
                stmt.setString(2, name);            
                stmt.setInt(3, id);            
                stmt.executeUpdate();
            } else {
                stmt = conn.prepareStatement(
                        "INSERT INTO drink SET price = ?, name = ?",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, price);
                stmt.setString(2, name);            
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
                    "DELETE FROM drink WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    
    public static Drink findById(int id) {
        Connection conn = Database.getConnection();
        Drink t = new Drink();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM drink WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t.id = rs.getInt("id");
                t.name = rs.getString("name");
                t.price = rs.getInt("price");
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
    
    public static ArrayList<Drink> getAll() {
        ArrayList<Drink> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM drink");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Drink t = new Drink();
                t.id = rs.getInt("id");
                t.name = rs.getString("name");
                t.price = rs.getInt("price");
                result.add(t);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return result;
    }
    
    public static void main(String[] args) {
        Database.initialize();
        Drink d = Drink.findById(3);
        if (d == null) {
            System.out.println("Null");
        }
        System.out.println(d.getName());
    }
}
