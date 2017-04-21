/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author hoangkien
 */
public class Bill {
    private int id;
    private ArrayList<BillContent> billContents;
    private Timestamp createdDate;
    
    public Bill() {
        id = 0;
        billContents = new ArrayList<>();
        createdDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<BillContent> getBillContents() {
        return billContents;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }
    
    public void add(Drink drink, int quantity) {
        BillContent billContent = new BillContent();
        billContent.setDrink(drink);
        billContent.setQuantity(quantity);
        billContents.add(billContent);
    }
    
    public boolean save() {
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt;
            if (id == 0) {
                // Save new
                stmt = conn.prepareStatement(
                        "INSERT INTO bill SET createdDate = ?",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setTimestamp(1, createdDate);
                stmt.executeUpdate();
                ResultSet key = stmt.getGeneratedKeys();
                if (key.next()) {
                    id = key.getInt(1);
                }
                // Save BillContents
                // ...
                for (BillContent billContent : billContents) {
                    stmt = conn.prepareStatement(
                        "INSERT INTO billcontent SET billId = ?, drinkId = ?, quantity = ?",
                        Statement.RETURN_GENERATED_KEYS);
                    stmt.setInt(1, id);
                    stmt.setInt(2, billContent.getDrink().getId());
                    stmt.setInt(3, billContent.getQuantity());
                    stmt.executeUpdate();
                    key = stmt.getGeneratedKeys();
                    if (key.next()) {
                        billContent.setId(key.getInt(1));
                    }
                }
            } else {
                // Update
                // Save BillContents Only
                stmt = conn.prepareStatement(
                        "DELETE FROM billcontent WHERE billId = ?");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                for (BillContent billContent : billContents) {
                    stmt = conn.prepareStatement(
                        "INSERT INTO billcontent SET billId = ?, drinkId = ?, quantity = ?",
                        Statement.RETURN_GENERATED_KEYS);
                    stmt.setInt(1, id);
                    stmt.setInt(2, billContent.getDrink().getId());
                    stmt.setInt(3, billContent.getQuantity());
                    stmt.executeUpdate();
                    ResultSet key = stmt.getGeneratedKeys();
                    if (key.next()) {
                        billContent.setId(key.getInt(1));
                    }
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
    
    public static Bill findById(int id) {
        Bill bill = new Bill();
        Connection conn = Database.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM bill WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bill.createdDate = rs.getTimestamp("createdDate");
                bill.id = id;
                stmt = conn.prepareStatement(
                        "SELECT * FROM billcontent WHERE billId = ?");
                stmt.setInt(1, id);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    BillContent billContent = new BillContent();
                    billContent.setDrink(Drink.findById(rs.getInt("drinkId")));
                    billContent.setId(rs.getInt("id"));
                    billContent.setQuantity(rs.getInt("quantity"));
                    bill.billContents.add(billContent);
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        return bill;
    }
    
    public static void main(String[] args) {
        // Test only
        Database.initialize();
        Drink drink = Drink.findById(1);
        Bill bill = new Bill();
        bill.add(drink, 2);
        bill.save();
        bill.getBillContents().clear();
        bill.add(drink, 5);
        bill.save();
        System.out.println(bill.getId());
    }
}

class BillContent {
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

    public void setId(int id) {
        this.id = id;
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
}