package onlinestore;

import java.util.Locale;

public class Product {
    private long id;
    private String title;
    private double price;
    private ProductType productType;

    public Product(long id, String title, double price, ProductType productType) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.productType = productType;
    }

    public Product(String title, double price, ProductType productType) {
        this.title = title;
        this.price = price;
        this.productType = productType;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public ProductType getProductType() {
        return productType;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s - %.2f - %s", this.title, this.price, this.productType.toString());
    }
}
