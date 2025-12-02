package application.controllers.customersControllers;

import application.Database;
import application.controllers.GeneralController;
import javafx.event.ActionEvent;
import onlinestore.Customer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeneralCustomersController extends GeneralController {
    @Override
    public void toPreviousMenu(ActionEvent event) throws IOException {
        changeScene(event, "/application/customer/customersmenu.fxml");
    }
}
