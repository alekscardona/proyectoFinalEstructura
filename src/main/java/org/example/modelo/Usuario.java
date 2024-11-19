package org.example.modelo;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.Aplicacion.Aplicacion;
import org.example.Ejecutable.Main;
import org.example.controlador.LoginController;


import java.util.HashMap;
import java.util.Set;

public class Usuario {
    Aplicacion aplicacion = new Aplicacion();
    Main main = new Main();

    private String user;
    private String password;
    private String rol;

    private HashMap<String, String> usuarios = new HashMap<>();




    public void usuarios() {
        usuarios.put("1", "1");
        usuarios.put("bra", "1");
    }


    public Usuario(String user, String password, String rol) {
        this.user = user;
        this.password = password;
        this.rol = rol;
    }
    public Usuario(String user, String password) {
        this.user = user;
        this.password = password;

        // Asignar roles específicos en el constructor
        if (user.equals("1")) {
            this.rol = "admin";
        } else if (user.equals("bra")) {
            this.rol = "usuario";
        }
    }




    public String getUser() {
        return this.user;
    }
    public Set<String> getUsuarios() {
        return usuarios.keySet();
    }


    public void aniadirUsuario(String user, String password) {
        usuarios.put(user, password);
    }


    public String getPassword() {
        return this.password;
    }

    public void validacionIngreso(LoginController controlador) {
        usuarios();
        // Recorre el HashMap y compara las credenciales
        for (String username : usuarios.keySet()) {
            String clave = usuarios.get(username);

            if (user.equals(username) && password.equals(clave)) {
                if (rol.equals("admin")) {
                    aplicacion.mostrarVentanaAdmin();
                } else if (rol.equals("usuario")) {
                    aplicacion.mostrarVentanaExplorer();

                } else {
                    aplicacion.mostrarVentanaExplorer();
                }

                return; // Sale del método si encuentra una coincidencia
            }

        }
        // Si no se encontró coincidencia
        controlador.mostrarAlerta("Credenciales incorrectas", Alert.AlertType.ERROR);
    }




    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getRol() {
        return this.rol;
    }
}

