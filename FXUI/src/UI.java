
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;

public class UI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("MainWindow.fxml");
        loader.setLocation(mainFXML);

        Parent root = loader.load();

        mainWindowController controller = loader.getController();
        SuperDuperMarket SDM = new SuperDuperMarket();
        controller.setPrimaryStage(primaryStage);
        controller.setSDM(SDM);


        primaryStage.setTitle("Super Duper Market");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
      //primaryStage.setTitle("Super Duper Market");
      //Parent load = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
      //
      //Scene scene = new Scene(load, 600,400);
      // // mainWindowController controller = loader.getController();
      //primaryStage.setScene(scene);
      //primaryStage.show();

    }
   //public void start(Stage primaryStage) throws Exception {

   //    CSSFX.start();

   //    FXMLLoader loader = new FXMLLoader();

   //    // load main fxml
   //    URL mainFXML = getClass().getResource(HistogramResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
   //    loader.setLocation(mainFXML);
   //    BorderPane root = loader.load();

   //    // wire up controller
   //    HistogramController histogramController = loader.getController();
   //    BusinessLogic businessLogic = new BusinessLogic(histogramController);
   //    histogramController.setPrimaryStage(primaryStage);
   //    histogramController.setBusinessLogic(businessLogic);

   //    // set stage
   //    primaryStage.setTitle("HistogramS");
   //    Scene scene = new Scene(root, 1050, 600);
   //    primaryStage.setScene(scene);
   //    primaryStage.show();


   //}

    public static void main(String[] args) {
        launch(args);
    }
}


