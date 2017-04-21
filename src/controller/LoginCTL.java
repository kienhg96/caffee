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
import utils.Handler;
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
                    JOptionPane.showMessageDialog(loginView, 
                        "Đăng nhập thành công", "Thông báo", 
                            JOptionPane.INFORMATION_MESSAGE);
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
                    JOptionPane.showMessageDialog(loginView, 
                        "Đăng nhập thành công", "Thông báo", 
                            JOptionPane.INFORMATION_MESSAGE);
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
