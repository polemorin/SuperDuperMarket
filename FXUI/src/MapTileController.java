import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class MapTileController {

    @FXML
    private ImageView ImageTile;
    @FXML
    private Label coordinateLabel;
    @FXML
    private Rectangle borderRectangle;


    Tooltip tileToolTip;
    User userInTile;
    Store storeInTile;
    boolean isTileOccupied;
    public void setCoordinateLabel(String row){
        coordinateLabel.setText(row);
    }
    public void setIsTileOccupied(boolean tileOccupied){
        isTileOccupied = tileOccupied;
    }
    public boolean getIsTileOccupied(){
        return isTileOccupied;
    }
    public void setUserImage(User user) {
        userInTile = user;
        ImageTile.setImage(new Image("images/cartoon-shopper.jpg"));
        tileToolTip = new Tooltip();
        tileToolTip.setText("ID: "+ user.getID() +
                System.lineSeparator()+"Name: "+ user.getName()+
                System.lineSeparator()+"Order amount: "+user.getOrderHistory().size());
        Tooltip.install(ImageTile,tileToolTip);
    }
    public void setStoreImage(Store store) {
        storeInTile = store;
        ImageTile.setImage(new Image("images/store.jpg"));
        tileToolTip = new Tooltip();
        tileToolTip.setText("ID: "+ store.getID() +
                System.lineSeparator()+"Name: "+ store.getName()+
                System.lineSeparator()+"PPK: "+ store.getDeliveryPPK()+
                System.lineSeparator()+"Order amount: "+store.getStoreOrderHistory().size());
        Tooltip.install(ImageTile,tileToolTip);
    }
    public void setPrettyTile(){
     //   Random rand = new Random();
     //   int randNum = rand.nextInt(100);
     //   if(randNum<25){
     //       ImageTile.setImage(new Image("images/tree1.png"));
     //   }
     //   else if(randNum<50){
     //       ImageTile.setImage(new Image("images/tree2.jpg"));
     //   }
     //   else if(randNum<75){
     //       ImageTile.setImage(new Image("images/tree3.png"));
     //   }
     //   else {
     //       ImageTile.setImage(new Image("images/flower2.jpg"));
     //   }
    }
    @FXML
    void updateToolTipAction(MouseEvent event) {
        if(userInTile != null){
            tileToolTip.setText("ID: "+ userInTile.getID() +
                    System.lineSeparator()+"Name: "+ userInTile.getName()+
                    System.lineSeparator()+"Order amount: "+userInTile.getOrderHistory().size());
        }
        if(storeInTile != null){
            tileToolTip.setText("ID: "+ storeInTile.getID() +
                    System.lineSeparator()+"Name: "+ storeInTile.getName()+
                    System.lineSeparator()+"PPK: "+ storeInTile.getDeliveryPPK()+
                    System.lineSeparator()+"Order amount: "+storeInTile.getStoreOrderHistory().size());
        }
    }

    public void removeTile() {
        ImageTile.setVisible(false);
        borderRectangle.setVisible(false);
    }
}
