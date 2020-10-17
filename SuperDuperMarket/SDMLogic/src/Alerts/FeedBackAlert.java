package Alerts;

public class FeedBackAlert implements IUserAlert {
    int userRating;
    String storeName;
    String userFeedback;
    String alertText;

    public FeedBackAlert(int userRating, String storeName, String userFeedback) {
        this.userRating = userRating;
        this.storeName = storeName;
        this.userFeedback = userFeedback;
        alertText = this.toString();
    }

    public int getUserRating() {
        return userRating;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    @Override
    public String toString() {
        String text;
        text = "Store "+storeName+" was rated: "+userRating;
        if(!userFeedback.equals("")){
            text += " with this feedback: "+userFeedback;
        }
        return text;
    }
}
