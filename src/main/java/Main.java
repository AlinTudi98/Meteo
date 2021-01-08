import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mta.facc.selab.tema2.controller.MeteoController;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        FXMLLoader loader = new FXMLLoader();
        MeteoController controller = new MeteoController();
        try{
            loader.setLocation(this.getClass().getResource("view/MeteoView.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
            controller.initialize("src/main/resources/inputFile.txt");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
