/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import utils.Event;
import views.MainEmployee;
/**
 *
 * @author hoangkien
 */
public class MainEmployeeCTL {
    private MainEmployee mainEmployeeView;
    
    public MainEmployeeCTL() {
        mainEmployeeView = new MainEmployee();
        mainEmployeeView.onSubmit((Event e) -> {
            switch (e.type()) {
                case "SALE":
                    new SaleCTL();
                    break;
                case "EXIT":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Action not listened " + e.type());
            }
        });
        mainEmployeeView.setVisible(true);
    }
}
