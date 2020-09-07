
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PlaceOrderProductsController {


    private SuperDuperMarket SDM;
    private User customer;
    private Store store;
    private LocalDate deliveryDate;


    @FXML
    private FlowPane ProductsFlowPane;

    @FXML
    private Button ContinueButton;

    @FXML
    void ContinueButtonAction(ActionEvent event) {

    }

    public void setData(SuperDuperMarket sdm, User customer, Store store, LocalDate date) {
        SDM = sdm;
        this.customer = customer;
        this.store = store;
        deliveryDate = date;
    }


    private void setProducts(){

        if(store == null){

        }
    }
}
