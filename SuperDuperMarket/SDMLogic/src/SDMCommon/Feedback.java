package SDMCommon;

import java.time.LocalDate;
import java.util.Date;

public class Feedback {
    private String customerName;
    private String orderDate;
    private int rating;
    private String feedbackText;

    public Feedback(String customerName, String orderDate, int rating, String feedbackText) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.rating = rating;
        this.feedbackText = feedbackText;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedbackText() {
        return feedbackText;
    }
}
