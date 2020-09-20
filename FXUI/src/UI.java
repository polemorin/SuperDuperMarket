import SDMCommon.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.net.URL;

public class UI extends Application {

    public Stage s;


    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("MainWindow.fxml");
        loader.setLocation(mainFXML);

        ScrollPane root = loader.load();

        mainWindowController controller = loader.getController();
        SuperDuperMarket SDM = new SuperDuperMarket();
        controller.setPrimaryStage(primaryStage);
        controller.setSDM(SDM);
        controller.setStyle("Lime.css");
        primaryStage.setTitle("Super Duper Market");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Lime.css");
        primaryStage.setScene(scene);

         s=primaryStage;
        primaryStage.show();
        new Thread(()-> System.out.println("hey") ).start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}


