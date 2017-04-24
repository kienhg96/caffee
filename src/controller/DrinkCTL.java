/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.Drink;
import models.Table;
import utils.Event;
import views.AddDrink;
import views.DrinksTable;
import views.EditDrink;

/**
 *
 * @author hoangkien
 */
public class DrinkCTL {
    DrinksTable drinksTableView;
    ArrayList<Drink> drinks;
    
    public DrinkCTL() {
        drinks = Drink.getAll();
        drinksTableView = new DrinksTable();
        drinksTableView.onSubmit((Event e) -> {
            switch (e.type()) {
                case "CLOSE":
                    drinksTableView.dispose();
                    break;
                case "ADD":
                    handleAddAction(e);
                    break;
                case "EDIT":
                    handleEditAction(e);
                    break;
                case "DELETE":
                    handleDeleteAction(e);
                    break;
                default:
                    System.out.println("Action not listened " + e.type());
            }
        });
        drinksTableView.setData(drinks);
        drinksTableView.setVisible(true);
    }
    
    private void handleAddAction(Event e) {
        AddDrink addDrinkView = new AddDrink();
        addDrinkView.onSubmit((Event event) -> {
            switch (event.type()) {
                case "CLOSE":
                    addDrinkView.dispose();
                    break;
                case "ADD":
                    addDrink(addDrinkView.getName(), addDrinkView.getPrice());
                    addDrinkView.dispose();
                    break;
                default:
                    System.out.println("Action not listened " + e.type());
            }
        });
        addDrinkView.setVisible(true);
    }
    
    private void handleEditAction(Event e) {
        int index = drinksTableView.getSelectedIndex();
        if (index != -1) {
            Drink drink = drinks.get(index);
            EditDrink editDrinkView = new EditDrink();
            editDrinkView.onSubmit((Event event) -> {
                switch (event.type()) {
                    case "CLOSE":
                        editDrinkView.dispose();
                        break;
                    case "EDIT":
                        drink.setName(editDrinkView.getName());
                        drink.setPrice(editDrinkView.getPrice());
                        drink.save();
                        drinksTableView.setData(drinks);
                        editDrinkView.dispose();
                        break;
                    default:
                        System.out.println("Action not listened " + e.type());
                }
            });
            editDrinkView.setData(drink.getName(), drink.getPrice());
            editDrinkView.setVisible(true);
        }
    }
    
    private void handleDeleteAction(Event e) {
        int index = drinksTableView.getSelectedIndex();
        if (index != -1) {
            if (JOptionPane.showConfirmDialog(drinksTableView, "Bạn có muốn xóa đồ uống này không?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Drink drink = drinks.get(index);
                drink.delete();
                drinks.remove(drink);
                drinksTableView.setData(drinks);
            }
        }
    }
    
    private boolean addDrink(String name, int price) {
        Drink drink = new Drink();
        drink.setName(name);
        drink.setPrice(price);
        drink.save();
        drinks.add(drink);
        drinksTableView.setData(drinks);
        return true;
    }
}
