package application.controllers.productsControllers;

import application.OnlineStoreApplication;
import application.dao.ProductsDao;
import application.filereader.ProductsFIleReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import onlinestore.InputCheck;
import onlinestore.Product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

public class ProductsMenuController extends GeneralProductsController {
    @FXML
    protected TextArea readProductsTextArea;

    @FXML
    protected Label wrongFileLabel;

    @FXML
    public void addProduct(ActionEvent event) throws IOException {
        changeScene(event, "/application/product/newproduct.fxml");
    }

    @FXML
    public void deleteProduct(ActionEvent event) throws IOException {
        changeScene(event, "/application/product/deleteproduct.fxml");
    }

    @FXML
    public void showAllProducts(ActionEvent event) throws IOException, SQLException {
        String content = ProductsDao.findAllAsStrings();

        FXMLLoader loader = new FXMLLoader(
                OnlineStoreApplication.class.getResource("/application/product/readproductsfile.fxml"));
        Pane root = loader.load();

        ProductsMenuController ctrl = loader.getController();
        ctrl.readProductsTextArea.setText(content);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene old = stage.getScene();
        stage.setScene(new Scene(root, old.getWidth(), old.getHeight()));
        stage.show();
        changeScene(event, "/application/product/readproductsfile.fxml");
    }

    @FXML
    public void editProduct(ActionEvent event) throws IOException {
        changeScene(event, "/application/product/editproduct.fxml");
    }

    @FXML
    public void replaceProducts(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        File newfile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        ArrayList<Product> products = ProductsFIleReader.read(newfile);
        if (products != null) {
            ProductsDao.replaceFromList(products);
        } else {
            wrongFileLabel.setVisible(true);
        }
    }

    @FXML
    public void updateProducts(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        File newfile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        ArrayList<Product> products = ProductsFIleReader.read(newfile);
        if (products != null) {
            ProductsDao.updateFromList(products);
        } else {
            wrongFileLabel.setVisible(true);
        }
    }

    @FXML
    public void saveProducts(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Текстовые файлы","*.txt", "*.csv"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file == null) {
            return;
        }

        ProductsDao.saveToFile(file);
    }
}
