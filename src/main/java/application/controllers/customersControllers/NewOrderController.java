package application.controllers.customersControllers;

import application.controllers.productsControllers.GeneralProductsController;
import application.dao.CustomersDao;
import application.dao.OrdersDao;
import application.dao.ProductsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import onlinestore.Customer;
import onlinestore.InputCheck;
import onlinestore.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class NewOrderController extends GeneralCustomersController {
    @FXML
    protected GridPane customerGridPane;

    @FXML
    protected TextField nameField;

    @FXML
    protected Label wrongData1;

    @FXML
    protected GridPane productGridPane;

    @FXML
    protected TextField titleField;

    @FXML
    protected TextField amountField;

    @FXML
    protected Label wrongData2;

    private Customer customer;

    @FXML
    public void continuee1(ActionEvent event) throws IOException, SQLException {
        String name = nameField.getText();
        customer = CustomersDao.findByName(name);
        changeMenu(customerGridPane, productGridPane, wrongData1, customer != null);
    }

    @FXML
    public void continuee2(ActionEvent event) throws IOException, SQLException {
        String title = titleField.getText();
        String amount = amountField.getText();
        Product product = ProductsDao.findByTitle(title);
        if (!(product != null && InputCheck.amountCheck(amount))) {
            wrongData2.setVisible(true);
        } else {
            OrdersDao.insert(customer, product, Integer.parseInt(amount));
            toPreviousMenu(event);
        }
    }
}
