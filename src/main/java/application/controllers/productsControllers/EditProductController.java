package application.controllers.productsControllers;

import application.dao.ProductsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import onlinestore.InputCheck;
import onlinestore.Product;
import onlinestore.ProductType;

import java.io.IOException;
import java.sql.SQLException;

public class EditProductController extends GeneralProductsController {
    @FXML
    protected Label productNotExistLabel;

    @FXML
    protected GridPane gridPane;

    @FXML
    protected TextField existingTextField;

    @FXML
    protected VBox inputVBox;

    @FXML
    protected TextField titleTextField;

    @FXML
    protected TextField priceTextField;

    @FXML
    protected Label wrongData;

    @FXML
    protected GridPane choiceGridPane;

    private Product product;

    @FXML
    public void continuee(ActionEvent event) throws IOException, SQLException {
        product = ProductsDao.findByTitle(existingTextField.getText());
        changeMenu(gridPane, inputVBox, productNotExistLabel, product != null);
    }

    @FXML
    public void continuee2(ActionEvent event) {
        newTitle = titleTextField.getText();
        newPrice = priceTextField.getText();
        changeMenu(inputVBox, choiceGridPane, wrongData,
                InputCheck.productTitleCheck(newTitle) && InputCheck.productPriceCheck(newPrice));
    }

    @FXML
    public void onTypeButtonPressed(ActionEvent event) throws IOException, SQLException {
        ProductType productType = processType((Button) (event.getSource()));
        ProductsDao.update(product, new Product(newTitle,
                Double.parseDouble(newPrice.replace(",", ".")), productType));
        toPreviousMenu(event);
    }
}
