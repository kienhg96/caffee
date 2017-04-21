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
import java.util.ArrayList;

/**
 *
 * @author hoangkien
 */
public class Manager {
    private String name;
    private String phone;
    private String address;
    private String username;
    private String password;
    
    public Manager() {
        this.name = "";
        this.phone = "";
        this.address = "";
        this.username = "";
        this.password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    
    public boolean save() {
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT username FROM manager WHERE username LIKE ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                stmt = conn.prepareStatement(
                        "UPDATE manager SET name = ?, phone = ?, address = ?,"
                                +" password = ? WHERE username = ?");
            } else {
                stmt = conn.prepareStatement(
                        "INSERT INTO manager SET name = ?, phone = ?, "
                                + "address = ?, password = ?, username = ?");
            }
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setString(4, password);
            stmt.setString(5, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }
    
    public static ArrayList<Manager> getAll() {
        ArrayList<Manager> result = new ArrayList<>();
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM manager");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Manager e = new Manager();
                e.setAddress(rs.getString("address"));
                e.setName(rs.getString("name"));
                e.setPhone(rs.getString("phone"));
                e.setUsername(rs.getString("username"));
                e.setPassword(rs.getString("password"));
                result.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        return result;
    }
    
    public void delete() {
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM manager WHERE username = ?");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    
    public static Manager findByUsername(String username) {
        Connection conn = Database.getConnection();
        Manager e = new Manager();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM manager WHERE username LIKE ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                e.setAddress(rs.getString("address"));
                e.setName(rs.getString("name"));
                e.setPhone(rs.getString("phone"));
                e.setUsername(rs.getString("username"));
                e.setPassword(rs.getString("password"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return e;
    } 
    
    public static void main(String[] args) {
        Database.initialize();
        Manager e = Manager.findByUsername("hoangkien");
        System.out.println(e.name);
    }
}
