/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.Employee;
import utils.Event;
import views.AddEmployee;
import views.EditEmployee;
import views.EmployeesTable;

/**
 *
 * @author hoangkien
 */
public class EmployeeCTL {
    private EmployeesTable employeeTableView;
    private ArrayList<Employee> employees;
            
    public EmployeeCTL() {
        employeeTableView = new EmployeesTable();
        employees = Employee.getAll();
        employeeTableView.setData(employees);
        employeeTableView.onSubmit((Event e) -> {
            switch (e.type()) {
                case "CLOSE":
                    employeeTableView.dispose();
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
        employeeTableView.setVisible(true);
    }
    
    private void handleAddAction(Event event) {
        AddEmployee eAddView = new AddEmployee();
        eAddView.onSubmit((Event e) -> {
            switch (e.type()) {
                case "CLOSE":
                    eAddView.dispose();
                    break;
                case "ADD":
                    if (addEmployee(eAddView.getName(), eAddView.getPhone(),
                            eAddView.getAddress(), eAddView.getUsername(),
                            eAddView.getPassword())) {
                        eAddView.dispose();
                        employeeTableView.setData(employees);
                    } else {
                        JOptionPane.showMessageDialog(eAddView, 
                                "Tài khoản đã tồn tại", "Lỗi", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    System.out.println("Action not listened " + e.type());
            }
        });
        eAddView.setVisible(true);
    }
    
    private void handleEditAction(Event event) {
        int index = employeeTableView.getSelectedIndex();
        if (index != -1) {
            Employee employee = employees.get(index);
            EditEmployee eEditView = new EditEmployee(employee.getName(), 
                    employee.getPhone(), employee.getAddress(), 
                    employee.getUsername(), employee.getPassword());
            eEditView.onSubmit((Event e) -> {
                switch (e.type()) {
                    case "CLOSE":
                        eEditView.dispose();
                        break;
                    case "EDIT":
                        employee.setName(eEditView.getName());
                        employee.setAddress(eEditView.getAddress());
                        employee.setPhone(eEditView.getPhone());
                        employee.setPassword(eEditView.getPassword());
                        employee.save();
                        employeeTableView.setData(employees);
                        eEditView.dispose();
                        break;
                    default:
                        System.out.println("Action not listened " + e.type());
                }
            });
            eEditView.setVisible(true);
        }
    }
    
    private void handleDeleteAction(Event event) {
        int index = employeeTableView.getSelectedIndex();
        if (index != -1) {
            if (JOptionPane.showConfirmDialog(employeeTableView, "Bạn có muốn xóa tài khoản này không?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Employee employee = employees.get(index);
                employee.delete();
                employees.remove(employee);
                employeeTableView.setData(employees);
            }
        }
    }
    
    private boolean addEmployee(String name, String phone, String address, 
            String username, String password) {
        Employee employee = Employee.findByUsername(username);
        if (employee != null) {
            return false;
        }
        employee = new Employee(name, phone, address, username, password);
        if (employee.save()) {
            employees.add(employee);
            return true;
        } else {
            return false;
        }
    }
}
