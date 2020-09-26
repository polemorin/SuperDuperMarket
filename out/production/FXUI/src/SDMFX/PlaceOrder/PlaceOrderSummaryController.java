package SDMFX.PlaceOrder;
import SDMCommon.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PlaceOrderSummaryController {

    MarketArea SDM;
    CustomerLevelOrder order;
    @FXML
    private VBox StoreLevelOrderVbox;

    @FXML
    private Label ProductsPriceLabel;

    @FXML
    private Label DeliveryPriceLabel;

    @FXML
    private Label TotalPriceLabel;

    @FXML
    private Button CancelOrderButton;

    @FXML
    private Button ConfirmOrderButton;
    @FXML
    private Label DeliveryDateLabel;

    @FXML
    private Label CustomerNameLabel;

    @FXML
    private Label CustomerIDLabel;
    private String currentStyle;
    private boolean doesUserWantAnimation;

    @FXML
    void CancelOrderButtonAction(ActionEvent event) {
        Stage s = (Stage)CancelOrderButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void ConfirmOrderButtonAction(ActionEvent event) throws InterruptedException {
        SDM.placeOrderInSDM(order,order.getOrders().get(0).getCustomerID());
        Stage s = (Stage)CancelOrderButton.getScene().getWindow();
        if(doesUserWantAnimation) {
            animation();
        }
        s.close();
    }

    private void animation() throws InterruptedException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Timeline");
        stage.setResizable(false);

        ImageView imgView = new ImageView(new Image("images/delivery.png",200,200,true,true));

        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(50, 50, 25, 50));

        Timeline timeline = new Timeline();

        vbox.getChildren().addAll(imgView);

        Scene scene = new Scene(vbox, 500, 400);
        stage.setScene(scene);
        stage.show();

        Duration time = new Duration(1000);
        KeyValue keyValue = new KeyValue(imgView.translateXProperty(), 300);
        KeyFrame keyFrame = new KeyFrame(time, keyValue);
        timeline.getKeyFrames().add(keyFrame);

        keyValue = new KeyValue(imgView.translateYProperty(), 0);
        keyFrame = new KeyFrame(time, keyValue);
        timeline.getKeyFrames().add(keyFrame);

        timeline.setCycleCount(1);
        timeline.play();
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished( event -> stage.close() );
        delay.play();
    }

    public void setData(MarketArea sdm, CustomerLevelOrder customerLevelOrder) {
        SDM = sdm;
        order = customerLevelOrder;
        initStoreLevelOrders();
        initLabels();
    }

    private void initLabels() {
        DeliveryPriceLabel.setText(String.format("%.2f",order.getDeliveryPrice()));
        ProductsPriceLabel.setText(String.format("%.2f",order.getTotalProductPrice()));
        TotalPriceLabel.setText(String.format("%.2f",order.getDeliveryPrice()+order.getTotalProductPrice()));
        DeliveryDateLabel.setText(order.getDate().toString());
        CustomerIDLabel.setText(Integer.toString(order.getOrders().get(0).getCustomerID()));
        CustomerNameLabel.setText(SDM.getUsers().get(order.getOrders().get(0).getCustomerID()).getName());
    }

    public void initStoreLevelOrders(){
        FXMLLoader fxmlLoader;
        Node storeOrderTile;
        StoreLevelOrderController storeLevelOrderController;
        for (StoreLevelOrder storeLevelOrder:order.getOrders()) {
            try{
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(StoreLevelOrderController.class.getResource("StoreLevelOrder.fxml"));
                storeOrderTile = fxmlLoader.load();
                storeLevelOrderController = fxmlLoader.getController();
                storeLevelOrderController.setData(storeLevelOrder,SDM);
                StoreLevelOrderVbox.getChildren().add(storeOrderTile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void setStyle(String currentStyle) {
        this.currentStyle = currentStyle;
    }

    public void doAnimation(boolean doesUserWantAnimation) {
        this.doesUserWantAnimation = doesUserWantAnimation;
    }
}
