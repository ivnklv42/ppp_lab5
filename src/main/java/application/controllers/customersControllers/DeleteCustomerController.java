package application.controllers.customersControllers;

import application.dao.CustomersDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import onlinestore.Customer;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteCustomerController extends GeneralCustomersController {
    @FXML
    protected Label customerNotFoundLabel;

    @FXML
    protected TextField deleteTextField;

    @FXML
    public void continuee(ActionEvent event) throws IOException, SQLException {
        Customer customer = CustomersDao.findByName(deleteTextField.getText());
        if (customer != null) {
            CustomersDao.delete(customer);
            toPreviousMenu(event);
        } else {
            customerNotFoundLabel.setVisible(true);
        }
    }
}
