package application.filereader;

import application.controllers.productsControllers.GeneralProductsController;
import onlinestore.InputCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CustomersFileReader {
    public static HashMap<String, String> read(File file) {
        HashMap<String, String> fileContent = new HashMap<>();
        int step = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content = "";
            String name = "";
            String line;
            while ((line = reader.readLine()) != null) {
                if (InputCheck.customerCheck(line)) {
                    if (step != 0) {
                        throw new IOException("Файл не соответствует формату");
                    }

                    step++;
                    if (!name.isEmpty() && !content.isEmpty()) {
                        fileContent.put(name, content.substring(1));
                    }

                    content = "";
                    name = line;
                } else if (line.equals("Заказы:")) {
                    if (step != 1) {
                        throw new IOException("Файл не соответствует формату");
                    }

                    step++;
                } else if (line.matches(".+? - .+? - .+? - .+? - .+?")) {
                    if (step != 2) {
                        throw new IOException("Заказы не соответствуют формату");
                    }

                    String[] elements = line.split(" - ");
                    if (!(InputCheck.productTitleCheck(elements[0]) &&
                            InputCheck.productPriceCheck(elements[1]) &&
                            GeneralProductsController.productTypes.contains(elements[2]) &&
                            InputCheck.amountCheck(elements[3]) &&
                            InputCheck.orderPriceCheck(elements[4]) &&
                            String.format("%.2f",
                                            Double.parseDouble(elements[3].replace(",", "."))
                                                    * Double.parseDouble(elements[1].replace(",", ".")))
                                    .equals(elements[4]))) {
                        throw new IOException("Заказы не соответствуют формату");
                    }

                    content += "\n" + line;
                } else if (line.isEmpty()) {
                    if (step != 2 && step != 1) {
                        throw new IOException("Заказы не соответствуют формату");
                    }

                    step = 0;
                } else {
                    throw new IOException("Заказы не соответствуют формату");
                }
            }

            if (!content.isEmpty()) {
                fileContent.put(name, content.substring(1));
            }
        } catch (Exception e) {
            return null;
        }

        return fileContent;
    }
}
