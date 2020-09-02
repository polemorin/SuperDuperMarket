import java.util.Objects;

public class Product {
    private final int id;
    private final String name;
    private final ProductCategory category;

    public Product(int id, String name, ProductCategory cat){
        this.id = id;
        this.name = name;
        category = cat;
    }
    public Product(Product other){
        this.id = other.id;
        this.name = other.name;
        this.category = other.category;
    }
    public int getProductID()
    {
        return id;
    }
    public String getProductName()
    {
        return name;
    }
    public ProductCategory getProductCategory()
    {
        return category;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                name.equals(product.name) &&
                category == product.category;
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + id;
        res = 31 * res + name.hashCode();
        res = 31 * res + category.hashCode();
        return res;
    }
}
