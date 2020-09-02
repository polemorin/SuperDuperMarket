
public enum ProductCategory {
    Quantity("Quantity"),
    Weight("Weight");

    private final String name;

    ProductCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
