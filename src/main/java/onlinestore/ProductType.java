package onlinestore;

public enum ProductType {
    Electronics,
    Clothes,
    Shoes,
    Books,
    Beauty;

    @Override
    public String toString() {
        switch (this) {
            case Electronics -> {
                return "Электроника";
            }
            case Clothes -> {
                return "Одежда";
            }
            case Shoes -> {
                return "Обувь";
            }
            case Books -> {
                return "Книги";
            }
            case Beauty -> {
                return "Красота";
            }
            default -> {
                return null;
            }
        }
    }

    public static ProductType fromString(String line) {
        switch (line) {
            case "Электроника" -> {
                return Electronics;
            }
            case "Одежда" -> {
                return Clothes;
            }
            case "Обувь" -> {
                return Shoes;
            }
            case "Книги" -> {
                return Books;
            }
            case "Красота" -> {
                return Beauty;
            }
            default -> {
                return null;
            }
        }
    }
}
