package org.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.modelo.Usuario;

public class RegistroController {
    @FXML
    private AnchorPane visual;

    @FXML
    private TextField newUser;

    @FXML
    private TextField newPassword;

    private Usuario usuario;
    @FXML
    private TextField txtRol;

    @FXML
    private void registrar() {
        String rol = txtRol.getText();  // Obtiene el rol desde el campo correspondiente
        usuario = new Usuario(newUser.getText(), newPassword.getText(), rol);
        usuario.usuarios();
        if (true) {
            Stage stage = (Stage) visual.getScene().getWindow();
            stage.close();
        } else {
            // Puedes manejar el flujo si el registro no es exitoso
        }
    }


    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
