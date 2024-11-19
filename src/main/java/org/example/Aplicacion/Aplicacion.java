package org.example.Aplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.controlador.ControllerAdmin;
import org.example.controlador.ExplorarController;


import java.io.IOException;



public class Aplicacion extends Application {
    private Stage primaryStage;

    public Aplicacion() {
        primaryStage = new Stage();
        primaryStage.setTitle("Gestor de Procesos");
    }

    @Override
    public void start(Stage primaryStage)  throws Exception{
        this.primaryStage = primaryStage;
        mostrarVentanaAdmin();

    }

    public void mostrarVentanaAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("/vista/VistaAdmin.fxml"));
            TabPane pane = loader.load();
            ControllerAdmin controllerAdmin = loader.getController();
            controllerAdmin.setAplicacion(this);
            Scene scene = new Scene(pane);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void mostrarVentanaExplorer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("/vista/Explora.fxml"));
            TabPane pane = loader.load();
            ExplorarController controllerExplorer = loader.getController();
            controllerExplorer.setAplicacion(this);
            Scene scene = new Scene(pane);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
