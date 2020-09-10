
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.net.URL;

public class UI extends Application {

    public Stage s;


    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("MainWindow.fxml");
        loader.setLocation(mainFXML);

        GridPane root = loader.load();

        mainWindowController controller = loader.getController();
        SuperDuperMarket SDM = new SuperDuperMarket();
        controller.setPrimaryStage(primaryStage);
        controller.setSDM(SDM);

        primaryStage.minHeightProperty().setValue(300);
        primaryStage.minWidthProperty().setValue(600);

        primaryStage.setTitle("Super Duper Market");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);

         s=primaryStage;
        primaryStage.show();



      //primaryStage.setTitle("Super Duper Market");
      //Parent load = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
      //
      //Scene scene = new Scene(load, 600,400);
      // // mainWindowController controller = loader.getController();
      //primaryStage.setScene(scene);
      //primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}


