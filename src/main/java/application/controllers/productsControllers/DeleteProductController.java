package application.controllers.productsControllers;

import application.dao.ProductsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import onlinestore.Product;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteProductController extends GeneralProductsController {
    @FXML
    protected Label productNotFoundLabel;

    @FXML
    protected TextField deleteTextField;

    @FXML
    public void continuee(ActionEvent event) throws IOException, SQLException {
        Product product = ProductsDao.findByTitle(deleteTextField.getText());
        if (product != null) {
            ProductsDao.delete(product);
            toPreviousMenu(event);
        } else {
            productNotFoundLabel.setVisible(true);
        }
    }
}
