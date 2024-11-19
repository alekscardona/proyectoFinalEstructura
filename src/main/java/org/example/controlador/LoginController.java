package org.example.controlador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.modelo.Usuario;
import org.example.modelo.Ventanas;


import java.util.Optional;

public class LoginController {
    @FXML
    private AnchorPane visual;

    private Ventanas ventanas ;
    @FXML
    private TextField password;

    @FXML
    private TextField user;


    private Usuario usuario;

    @FXML
    private void ingresar() {
        usuario = new Usuario(user.getText(), password.getText());
        usuario.usuarios();
        if(true) {
            usuario.validacionIngreso(this);

            Stage stage = (Stage) visual.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    public void Registrar(){
        // Abrir la ventana de registro
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/vista/Registro.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
