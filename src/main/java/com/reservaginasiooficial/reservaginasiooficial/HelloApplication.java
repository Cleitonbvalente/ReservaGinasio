package com.reservaginasiooficial.reservaginasiooficial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sistema de Reservas");
        stage.setScene(scene);
        stage.show();
    }

    public static void criarTela(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(fxml)
        );
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
