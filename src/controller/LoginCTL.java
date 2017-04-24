/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.swing.JOptionPane;
import models.Employee;
import models.Manager;
import utils.Event;
import views.LoginView;

/**
 *
 * @author hoangkien
 */
public class LoginCTL {
    private LoginView loginView;
    
    public LoginCTL() {
        loginView = new LoginView();
        loginView.onSubmit((Event e) -> {
            this.login(loginView.getAccountType(), loginView.getUsername(),
                    loginView.getPassword());
        });
        loginView.setVisible(true);
    }
    
    private boolean login(String acc, String username, String password) {
        if (acc.equals("Manager")) {
            Manager manager = Manager.findByUsername(username);
            if (manager != null) {
                if (manager.getPassword().equals(password)) {
                    // Change here
                    this.loginView.dispose();
                    new MainManagerCTL();
                } else {
                    JOptionPane.showMessageDialog(loginView, 
                        "Mật khẩu không đúng", "lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(loginView, 
                        "Tài khoản không tồn tại", "lỗi", 
                            JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Employee employee = Employee.findByUsername(username);
            if (employee != null) {
                if (employee.getPassword().equals(password)) {
                    // Change here
                    this.loginView.dispose();
                    new MainEmployeeCTL();
                } else {
                    JOptionPane.showMessageDialog(loginView, 
                        "Mật khẩu không đúng", "lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(loginView, 
                        "Tài khoản không tồn tại", "lỗi", 
                            JOptionPane.ERROR_MESSAGE);
            }
        }
        return true;
    }
}
