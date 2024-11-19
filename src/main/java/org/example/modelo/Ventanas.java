package org.example.modelo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class Ventanas {
    public <T> T abrirVentana(Stage ventanaActual, String direccion, String titulo) {
        try {
            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(direccion));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Crear una nueva ventana
            Stage stage = new Stage();

            // Título de la ventana
            stage.setTitle(titulo);

            // Configurar la nueva escena en la nueva ventana
            stage.setScene(scene);

            // Mostrar la nueva ventana
            stage.show();

            // Cerrar la ventana actual
            ((Stage) ventanaActual).close();

            // Obtener el controlador de la vista cargada y devolverlo
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar errores de carga de la vista
        }
        return null;
    }









}

