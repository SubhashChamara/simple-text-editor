package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {


    public static SimpleStringProperty observableTitle = new SimpleStringProperty("untitled");

    public static Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage =primaryStage;
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/EditorView.fxml")).load()));
        primaryStage.titleProperty().bind(observableTitle);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
