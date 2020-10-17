package SDMCommon;

import java.time.LocalDate;
import java.util.Date;

public class Feedback {
    String customerName;
    String orderDate;
    int rating;
    String feedbackText;

    public Feedback(String customerName, String orderDate, int rating, String feedbackText) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.rating = rating;
        this.feedbackText = feedbackText;
    }
}
