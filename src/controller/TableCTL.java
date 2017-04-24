/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.Employee;
import models.Table;
import utils.Event;
import views.AddTable;
import views.EditTable;
import views.TablesTable;

/**
 *
 * @author hoangkien
 */
public class TableCTL {
    private TablesTable tableTableView;
    private ArrayList<Table> tables;
    
    public TableCTL() {
        tableTableView = new TablesTable();
        tables = Table.getAll();
        tableTableView.onSubmit((Event e) -> {
            switch (e.type()) {
                case "CLOSE":
                    tableTableView.dispose();
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
        tableTableView.setData(tables);
        tableTableView.setVisible(true);
    }
    
    private void handleAddAction(Event event) {
        AddTable addTableView = new AddTable();
        addTableView.onSubmit((Event e) -> {
            switch (e.type()) {
                case "CLOSE":
                    addTableView.dispose();
                    break;
                case "ADD":
                    if (addTable(addTableView.getName())) {
                        addTableView.dispose();
                        tableTableView.setData(tables);
                    } else {
                        JOptionPane.showMessageDialog(addTableView, 
                                "Tên bàn đã tồn tại", "Lỗi", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    System.out.println("Action not listened " + e.type());
            }
        });
        addTableView.setVisible(true);
    }
    
    private void handleEditAction(Event event) {
        int index = tableTableView.getSelectedIndex();
        if (index != -1) {
            Table table = tables.get(index);
            EditTable editTableView = new EditTable();
            editTableView.onSubmit((Event e) -> {
                switch (e.type()) {
                    case "CLOSE":
                        editTableView.dispose();
                        break;
                    case "EDIT":
                        if (editTable(table, editTableView.getName())) {
                            editTableView.dispose();
                            tableTableView.setData(tables);
                        } else {
                            JOptionPane.showMessageDialog(editTableView, 
                                    "Tên bàn đã tồn tại", "Lỗi", 
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    default:
                        System.out.println("Action not listened " + e.type());
                }
            });
            editTableView.setName(table.getName());
            editTableView.setVisible(true);
        }
    }
    
    private void handleDeleteAction(Event event) {
        int index = tableTableView.getSelectedIndex();
        if (index != -1) {
            if (JOptionPane.showConfirmDialog(tableTableView, "Bạn có muốn xóa bàn này không?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Table table = tables.get(index);
                table.delete();
                tables.remove(table);
                tableTableView.setData(tables);
            }
        }
    }
    
    private boolean addTable(String name) {
        Table table = Table.findByName(name);
        if (table != null) {
            return false;
        }
        table = new Table();
        table.setName(name);
        table.save();
        tables.add(table);
        return true;
    }
    
    private boolean editTable(Table table, String name) {
        if (table.getName().equals(name)) {
            return true;
        }
        Table t = Table.findByName(name);
        if (t != null) {
            return false;
        }
        table.setName(name);
        table.save();
        return true;
    }
}
