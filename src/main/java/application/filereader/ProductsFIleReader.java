package application.filereader;

import onlinestore.InputCheck;
import onlinestore.Product;
import onlinestore.ProductType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static application.controllers.productsControllers.GeneralProductsController.productTypes;

public class ProductsFIleReader {
    public static ArrayList<Product> read(File file) {
        ArrayList<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches(".+? - .+? - .+?")) {
                    throw new IOException("Товар не соответствует формату");
                }

                String[] elements = line.split(" - ");
                if (!(InputCheck.productTitleCheck(elements[0]) &&
                        InputCheck.productPriceCheck(elements[1]) &&
                        productTypes.contains(elements[2]))) {
                    throw new IOException("Товар не соответствует формату");
                }

                products.add(new Product(elements[0],
                        Double.parseDouble(elements[1].replace(",", ".")),
                        ProductType.fromString(elements[2])));
            }
        } catch (Exception e) {
            return null;
        }

        return products;
    }
}
