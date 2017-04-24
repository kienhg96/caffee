/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import utils.Event;
import views.MainManager;

/**
 *
 * @author hoangkien
 */
public class MainManagerCTL {
    private MainManager mainManagerView;
    
    public MainManagerCTL() {
        mainManagerView = new MainManager();
        mainManagerView.onSubmit((Event e) -> {
            switch (e.type()) {
                case "SALE":
                    new SaleCTL();
                    break;
                case "MENU":
                    new DrinkCTL();
                    break;
                case "TABLE":
                    new TableCTL();
                    break;
                case "EMPLOYEE":
                    new EmployeeCTL();
                    break;
                case "EXIT":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Action not listened " + e.type());
            }
        });
        mainManagerView.setVisible(true);
    }
}
