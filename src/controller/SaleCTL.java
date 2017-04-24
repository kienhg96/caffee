/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.Bill;
import models.Drink;
import models.Table;
import utils.Event;
import views.DrinkAmountDialog;
import views.Sale;

/**
 *
 * @author hoangkien
 */
public class SaleCTL {
    private Sale saleView;
    private ArrayList<Drink> drinks;
    private ArrayList<Table> tables;
    
    public SaleCTL() {
        saleView = new Sale();
        drinks = Drink.getAll();
        tables = Table.getAll();
        
        saleView.onSubmit((Event event) -> {
            switch (event.type()) {
                case "CLOSE":
                    saleView.dispose();
                    break;
                case "CHECKOUT":
                    checkout(event);
                    break;
                case "BOOK":
                    book(event);
                    break;
                case "ADD":
                    add(event);
                    break;
                case "DELETE":
                    remove(event);
                    break;
                default:
                    System.out.println("Action not listened " + event.type());
            }
        });
        saleView.setData(drinks, tables);
        saleView.setVisible(true);
        saleView.render();
    }
    
    public void book(Event e) {
        int index = saleView.getSelectedTableIndex();
        if (index != -1) {
            Table table = tables.get(index);
            table.setBill(new Bill());
            table.setStatus(1);
            table.save();
            saleView.renderTable();
        }
    }
    
    public void add(Event e) {
        int tableIndex = saleView.getSelectedTableIndex();
        int drinkIndex = saleView.getSelectedDrinkIndex();
        
        if (tableIndex != -1 && drinkIndex != -1) {
            Drink drink = drinks.get(drinkIndex);
            Table table = tables.get(tableIndex);
            DrinkAmountDialog amountDialog = new DrinkAmountDialog(saleView, true);
            amountDialog.onSubmit((Event event) -> {
                switch (event.type()) {
                    case "CANCEL":
                        amountDialog.dispose();
                        break;
                    case "ADD":
                        int amount = amountDialog.getAmount();
                        if (amount > 0) {
                            table.getBill().add(drink, amount);
                            table.save();
                            saleView.renderBill();
                        }
                        amountDialog.dispose();
                        break;
                    default:
                        System.out.println("Action not listened " + event.type());
                }
            });
            amountDialog.setName(drink.getName());
            amountDialog.setVisible(true);
        }
    }
    
    public void checkout(Event e) {
        int tableIndex = saleView.getSelectedTableIndex();
        if (tableIndex != -1) {
            Table table = tables.get(tableIndex);
            int charge = table.getBill().checkout();
            JOptionPane.showMessageDialog(saleView, "Tổng số tiền: " + charge, 
                    "Số tiền phải thanh toán", JOptionPane.INFORMATION_MESSAGE);
            table.setStatus(0);
            saleView.renderTable();
            saleView.renderBill();
        }
    }
    
    public void remove(Event e) {
        int selectedBillContent = saleView.getSelectedBillContentIndex();
        int selectedTable = saleView.getSelectedTableIndex();
        
        if (selectedBillContent != -1 && selectedTable != -1) {
            Table table = tables.get(selectedTable);
            table.getBill().remove(selectedBillContent);
            saleView.renderBill();
        }
    }
}
