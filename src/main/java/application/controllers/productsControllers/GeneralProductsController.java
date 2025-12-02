package application.controllers.productsControllers;

import application.Database;
import application.controllers.GeneralController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import onlinestore.Product;
import onlinestore.ProductType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GeneralProductsController extends GeneralController {
    protected String newTitle;
    protected String newPrice;

    public static final ArrayList<String> productTypes = new ArrayList<>(
            List.of(new String[]{"Электроника", "Одежда", "Обувь", "Книги", "Красота"}));

    protected ProductType processType(Button btn) {
        String text = btn.getText();
        switch (text) {
            case "Электроника" -> {
                return ProductType.Electronics;
            }
            case "Одежда" -> {
                return ProductType.Clothes;
            }
            case "Обувь" -> {
                return ProductType.Shoes;
            }
            case "Книги" -> {
                return ProductType.Books;
            }
            case "Красота" -> {
                return ProductType.Beauty;
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void toPreviousMenu(ActionEvent event) throws IOException {
        changeScene(event, "/application/product/productsmenu.fxml");
    }
}
