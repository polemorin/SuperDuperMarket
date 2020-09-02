public class XmlProductCategoryNotRecognizedException extends Exception {
    String category;

    public XmlProductCategoryNotRecognizedException(String category){
        this.category = category;
    }
    public String getInvalidCategoryString() {
        return category;
    }
}
