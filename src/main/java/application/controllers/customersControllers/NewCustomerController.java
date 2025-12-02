package application.controllers.customersControllers;

import application.Database;
import application.dao.CustomersDao;
import application.dao.ProductsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import onlinestore.Customer;
import onlinestore.InputCheck;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewCustomerController extends GeneralCustomersController {
    @FXML
    protected TextField nameField;

    @FXML
    protected Label wrongData;

    @FXML
    public void continuee(ActionEvent event) throws IOException, SQLException {
        String name = nameField.getText();
        if (!InputCheck.customerCheck(name)) {
            wrongData.setVisible(true);
            return;
        }

        Customer customer = new Customer(name);
        CustomersDao.insert(customer);
        toPreviousMenu(event);
    }
}
