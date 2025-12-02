package application.controllers.customersControllers;

import application.OnlineStoreApplication;
import application.controllers.productsControllers.GeneralProductsController;
import application.dao.CustomersDao;
import application.dao.ProductsDao;
import application.filereader.CustomersFileReader;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


public class CustomersMenuController extends GeneralCustomersController {
    @FXML
    protected TextArea readCustomersTextArea;

    @FXML
    protected Label wrongFileLabel;

    @FXML
    public void addCustomer(ActionEvent event) throws IOException {
        changeScene(event, "/application/customer/newcustomer.fxml");
    }

    @FXML
    public void addOrder(ActionEvent event) throws IOException {
        changeScene(event, "/application/customer/neworder.fxml");
    }

    @FXML
    public void deleteCustomer(ActionEvent event) throws IOException {
        changeScene(event, "/application/customer/deletecustomer.fxml");
    }

    @FXML
    public void showAllCustomers(ActionEvent event) throws IOException, SQLException {
        String content = CustomersDao.findAllAsStrings();
        FXMLLoader loader = new FXMLLoader(
                OnlineStoreApplication.class.getResource("/application/customer/readcustomersfile.fxml"));
        Pane root = loader.load();

        CustomersMenuController ctrl = loader.getController();
        ctrl.readCustomersTextArea.setText(content);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene old = stage.getScene();
        stage.setScene(new Scene(root, old.getWidth(), old.getHeight()));
        stage.show();
    }

    @FXML
    public void replaceCustomers(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        File newfile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        HashMap<String, String> fileContent = CustomersFileReader.read(newfile);

        if (fileContent != null) {
            boolean flag = CustomersDao.replaceFromMap(fileContent);
            if (flag) {
                toPreviousMenu(event);
                return;
            }
        }

        wrongFileLabel.setVisible(true);
    }

    @FXML
    public void updateCustomers(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        File newfile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        HashMap<String, String> fileContent = CustomersFileReader.read(newfile);

        if (fileContent != null) {
            boolean flag = CustomersDao.updateFromMap(fileContent);
            if (flag) {
                toPreviousMenu(event);
                return;
            }
        }

        wrongFileLabel.setVisible(true);
    }

    @FXML
    public void saveCustomers(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Текстовые файлы","*.txt", "*.csv"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file == null) {
            return;
        }

        CustomersDao.saveToFile(file);
    }
}

