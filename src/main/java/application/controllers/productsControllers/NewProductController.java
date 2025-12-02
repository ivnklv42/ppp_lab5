package application.controllers.productsControllers;

import application.Database;
import application.dao.ProductsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import onlinestore.InputCheck;
import onlinestore.Product;
import onlinestore.ProductType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewProductController extends GeneralProductsController {
    @FXML
    protected TextField titleField;

    @FXML
    protected TextField priceField;

    @FXML
    protected Label wrongData;

    @FXML
    protected GridPane gridPane;

    @FXML
    protected GridPane choiceGridPane;

    @FXML
    public void continuee(ActionEvent event) {
        newTitle = titleField.getText();
        newPrice = priceField.getText();
        changeMenu(gridPane, choiceGridPane, wrongData,
                InputCheck.productTitleCheck(newTitle) && InputCheck.productPriceCheck(newPrice));
    }

    @FXML
    public void onTypeButtonPressed(ActionEvent event) throws IOException, SQLException {
        ProductType productType = processType((Button) (event.getSource()));
        Product product = new Product(newTitle,
                Double.parseDouble(newPrice.replace(",", ".")), productType);
        ProductsDao.insert(product);
        toPreviousMenu(event);
    }
}
