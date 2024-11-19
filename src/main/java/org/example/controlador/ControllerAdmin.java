package org.example.controlador;


import java.util.LinkedList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Aplicacion.Aplicacion;
import org.example.modelo.Actividad;
import org.example.modelo.EmailSender;
import org.example.modelo.Proceso;
import org.example.modelo.Tarea;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import org.example.recursos.listasEnlazadasGenericas.Cola;

import javax.swing.*;
import java.util.*;

import java.util.Optional;
import java.util.stream.Collectors;

public class ControllerAdmin {
    Aplicacion aplicacion;
    controllerPrincipal controllerPrincipal = new controllerPrincipal();
    ObservableList<Proceso> listaProcesoData = FXCollections.observableArrayList();
    ObservableList<Actividad> listaActividadesData = FXCollections.observableArrayList();
    ObservableList<Tarea> listaTareasData = FXCollections.observableArrayList();
    Proceso procesoSeleccionado;

    @FXML
    private TableView<Actividad> tblActividades;
    @FXML
    private TableColumn<Actividad, String> colDescripcionActividad;

    @FXML
    private TableColumn<Actividad, String> colNombreActividad;

    @FXML
    private TableColumn<Actividad, Boolean> colObligatorioActividad;

    @FXML
    private CheckBox checkCrearDUAC;

    @FXML
    private CheckBox checkCrearalfinal;

    @FXML
    private CheckBox checkObligatorio;
    @FXML
    private ComboBox<Proceso> comboBoxProcesos;

    @FXML
    private ComboBox<Proceso> comboBoxProcesos1;

    @FXML
    private TableView<Proceso> tblProcesos;
    @FXML
    private TableColumn<Proceso, String> colDescripcion;

    @FXML
    private TableColumn<Proceso, String> colID;

    @FXML
    private TableColumn<Proceso, String> colNombreProceso;
    @FXML
    private TableView<Proceso> tblProcesos1;
    @FXML
    private TableColumn<Proceso, String> colDescripcion1;

    @FXML
    private TableColumn<Proceso, String> colID1;

    @FXML
    private TableColumn<Proceso, String> colNombreProceso1;

    @FXML
    private TextArea txtDescripcionActividad;

    @FXML
    private TextField txtNombreActividad;
    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtIdProcesos;

    @FXML
    private TextField txtNombreProceso;
    @FXML
    private TextField txtPosicionActividad;
    @FXML
    private TreeView<String> arbol;
    @FXML
    private TreeView<String> arbol1;
    @FXML
    private TreeView<String> arbol2;
    private ObservableList<Actividad> listaActividadesData1 = FXCollections.observableArrayList();
    @FXML
    private Button buscarAct;


    @FXML
    void initialize() {
        //inicializacion para la tabla de procesos informativa
        actualizarArbol();
        this.colDescripcion1.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colNombreProceso1.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colID1.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblProcesos1.getItems().clear();
        tblProcesos1.setItems(getListaProcesoData());
        tblProcesos1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            procesoSeleccionado = newValue;
            if (procesoSeleccionado != null) {
                txtDescripcion.setText(procesoSeleccionado.getDescripcion());
                txtIdProcesos.setText(procesoSeleccionado.getId());
                txtNombreProceso.setText(procesoSeleccionado.getNombre());
            }
        });
        // Inicialización para la tabla de procesos
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colNombreProceso.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colID.setCellValueFactory(new PropertyValueFactory<>("id"));

        tblProcesos.getItems().clear();
        tblProcesos.setItems(getListaProcesoData());
        comboBoxProcesos.getItems().clear();
        comboBoxProcesos.setItems(getListaProcesoData());


        tblProcesos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            procesoSeleccionado = newValue;
            if (procesoSeleccionado != null) {
                txtDescripcion.setText(procesoSeleccionado.getDescripcion());
                txtIdProcesos.setText(procesoSeleccionado.getId());
                txtNombreProceso.setText(procesoSeleccionado.getNombre());
            }
        });
        this.colNombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colDescripcionActividad.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colObligatorioActividad.setCellValueFactory(new PropertyValueFactory<>("obligatorio"));
        tblActividades.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Actividad actividadSeleccionada = newValue;
            if (actividadSeleccionada != null) {
                txtNombreActividad.setText(actividadSeleccionada.getNombre());
                txtDescripcionActividad.setText(actividadSeleccionada.getDescripcion());
                checkObligatorio.setSelected(actividadSeleccionada.isObligatorio());
            }
        });
        comboBoxProcesos.setOnAction(event -> cargarActividades(comboBoxProcesos.getValue()));
        tblProcesos1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Seleccionar un proceso en tblProcesos1 habilitará la búsqueda de actividades y procesos
            if (newValue != null) {
                habilitarBusqueda(true);
            } else {
                habilitarBusqueda(false);
            }
        });


        // Inicialización para la tabla de tareas
        colDescripcionTarea.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTiempoMaximoTarea.setCellValueFactory(new PropertyValueFactory<>("tiempoMaximo"));
        colTiempoMinimoTarea.setCellValueFactory(new PropertyValueFactory<>("tiempoMinimo"));
        colObligatorioTarea.setCellValueFactory(new PropertyValueFactory<>("obligatorio"));
        colObligatorioTarea.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean obligatorio, boolean empty) {
                super.updateItem(obligatorio, empty);

                if (empty || obligatorio == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(obligatorio ? "Si" : "No");
                }
            }
        });
        comboBoxProcesos1.getItems().clear();
        comboBoxProcesos1.setItems(getListaProcesoData());
        comboBoxProcesos1.setOnAction(event -> cargarActividadesTareas(comboBoxProcesos1.getValue()));
        comboBoxActividades.setOnAction(event -> cargarTareas(comboBoxProcesos1.getValue(), comboBoxActividades.getValue()));
        tblTareas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Tarea tareaSeleccionada = newValue;
            if (tareaSeleccionada != null) {
                txtDescripcionTarea.setText(tareaSeleccionada.getDescripcion());
                checkObligatoriaTarea.setSelected(tareaSeleccionada.isObligatorio());
                txtTiempoMaximoTarea.setText(String.valueOf(tareaSeleccionada.getTiempoMaximo()));
                txtTiempoMinimoTarea.setText(String.valueOf(tareaSeleccionada.getTiempoMinimo()));
            }
        });

    }

    private void habilitarBusqueda(boolean habilitar) {
        buscarActividad.setDisable(!habilitar);
        buscarTarea.setDisable(!habilitar);
    }


    @FXML
    void calcularTiempoProcesoAction(ActionEvent event) {
        calcularTiempoProceso();
    }

    private void calcularTiempoProceso() {
        int tiempoMax = 0;
        int tiempoMin = 0;
        for (Proceso proceso : listaProcesoData) {
            for (Actividad actividad : proceso.getListaActividades()) {
                ArrayList<Tarea> tareas = controllerPrincipal.obtenerTareas(proceso, actividad);
                for (Tarea tarea : tareas) {
                    tiempoMax += tarea.getTiempoMaximo();
                    tiempoMin += tarea.getTiempoMinimo();
                }

            }

        }
        mostrarMensaje("Notificacion tiempo", "Tiempo del proceso", "El tiempo maximo del proceso es: " + tiempoMax + " y el tiempo minimo es: " + tiempoMin, Alert.AlertType.INFORMATION);
    }

    private void cargarActividades(Proceso p) {
        if (p != null) {
            tblActividades.getItems().clear();
            tblActividades.setItems(getListaActividadesData(p));
        }
    }

    @FXML
    public void crearProcesoAction(ActionEvent event) {
        crearProceso();
    }


    @FXML
    void eliminarProcesoAction(ActionEvent event) {
        eliminarProceso();
    }

    private void eliminarProceso() {
        if (procesoSeleccionado == null) {
            mostrarMensaje("Notification proceso", "Proceso no seleccionado", "Seleccione un proceso de la lista", Alert.AlertType.WARNING);
            return;
        }

        if (!mostrarMensajeConfirmacion("¿Está seguro de eliminar el proceso?")) {
            return;
        }

        boolean procesoEliminado = controllerPrincipal.eliminarProceso(procesoSeleccionado);

        if (procesoEliminado) {
            // Eliminar de la lista observable
            listaProcesoData.remove(procesoSeleccionado);

            // Solo necesitas hacer refresh
            tblProcesos.refresh();

            // Actualizar ComboBoxes
            actualizarComboBoxes();

            // Limpiar selección y campos
            procesoSeleccionado = null;
            tblProcesos.getSelectionModel().clearSelection();
            limpiarCamposProceso();
            actualizarArbol();

            mostrarMensaje("Notification proceso", "Proceso eliminado",
                    "El proceso se ha eliminado con éxito", Alert.AlertType.INFORMATION);
        } else {
            mostrarMensaje("Notification proceso", "Proceso no eliminado",
                    "El proceso no se puede eliminar", Alert.AlertType.ERROR);
        }
    }

    private void actualizarComboBoxes() {
        comboBoxProcesos.getItems().clear();
        comboBoxProcesos.getItems().addAll(listaProcesoData);
        comboBoxProcesos1.getItems().clear();
        comboBoxProcesos1.getItems().addAll(listaProcesoData);
    }

    @FXML
    void actualizarProcesoAction(ActionEvent event) {
        actualizarProceso();
    }

    private void actualizarProceso() {
        boolean procesoActualizado = false;

        if (procesoSeleccionado != null) {

            String id = txtIdProcesos.getText();
            String nombre = txtNombreProceso.getText();
            String descripcion = txtDescripcion.getText();

            if (datosValidosProducto(id, nombre, descripcion)) {

                Proceso p = new Proceso(id, nombre, descripcion);

                if (mostrarMensajeConfirmacion("¿Estas seguro de actualizar el proceso?") == true) {

                    procesoActualizado = controllerPrincipal.actualizarProceso(procesoSeleccionado, p);

                    if (procesoActualizado == true) {
                        tblProcesos.getItems().clear();
                        tblProcesos.setItems(getListaProcesoData());
                        mostrarMensaje("Notification proceso", "Proceso actualizado", "El proceso se ha actualizado con éxito", Alert.AlertType.INFORMATION);
                    } else {
                        mostrarMensaje("Notification proceso", "Proceso no actualizado", "El proceso no se puede actualizar", Alert.AlertType.ERROR);
                    }
                }
            } else {
                mostrarMensaje("Notification proceso", "Proceso no actualizado", "Los datos ingresados son invalidos", Alert.AlertType.ERROR);
            }
        } else {
            mostrarMensaje("Notification proceso", "Proceso no seleccionado", "Seleccionado un proceso de la lista", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void crearActividadAction(ActionEvent event) {
        crearActividad();
    }
    @FXML
    void eliminarActividadAction(ActionEvent event) {
        eliminarActividad();
    }

    private void eliminarActividad() {
        boolean actividadEliminada = false;
        Proceso p = comboBoxProcesos.getValue();
        Actividad actividadSeleccionada = tblActividades.getSelectionModel().getSelectedItem();
        if (actividadSeleccionada != null) {
            if (mostrarMensajeConfirmacion("¿Estas seguro de elimininar la actividad?") == true) {
                actividadEliminada = controllerPrincipal.eliminarActividad(actividadSeleccionada, p);
                if (actividadEliminada == true) {
                    tblActividades.getItems().clear();
                    tblActividades.setItems(getListaActividadesData(p));
                    txtNombreActividad.setText("");
                    txtDescripcionActividad.setText("");
                    checkObligatorio.setSelected(false);
                    checkCrearDUAC.setSelected(false);
                    checkCrearalfinal.setSelected(false);
                    txtPosicionActividad.setText("");
                    actualizarArbol();
                    mostrarMensaje("Notification actividad", "Actividad eliminada", "La actividad se ha eliminado con éxito", Alert.AlertType.INFORMATION);
                } else {
                    mostrarMensaje("Notification actividad", "Actividad no eliminada", "La actividad no se puede eliminar", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarMensaje("Notification actividad", "Actividad no seleccionada", "Seleccionado una actividad de la lista", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void actualizarActividadAction(ActionEvent event) {
        actualizarActividad();
    }

    private void actualizarActividad() {
        boolean actividadActualizada = false;
        Proceso p = comboBoxProcesos.getValue();
        Actividad actividadSeleccionada = tblActividades.getSelectionModel().getSelectedItem();
        if (actividadSeleccionada != null) {
            String nombre = txtNombreActividad.getText();
            String descripcion = txtDescripcionActividad.getText();
            boolean obligatorio = checkObligatorio.isSelected();
            if (datosValidosProducto("1", nombre, descripcion)) {
                if (mostrarMensajeConfirmacion("¿Estas seguro de actualizar la actividad?") == true) {
                    actividadActualizada = controllerPrincipal.actualizarActividad(nombre, descripcion, obligatorio, actividadSeleccionada, p);
                    if (actividadActualizada == true) {
                        txtNombreActividad.setText("");
                        txtDescripcionActividad.setText("");
                        checkObligatorio.setSelected(false);
                        checkCrearDUAC.setSelected(false);
                        checkCrearalfinal.setSelected(false);
                        txtPosicionActividad.setText("");
                        tblActividades.getItems().clear();
                        tblActividades.setItems(getListaActividadesData(p));
                        mostrarMensaje("Notification actividad", "Actividad actualizada", "La actividad se ha actualizado con éxito", Alert.AlertType.INFORMATION);
                    } else {
                        mostrarMensaje("Notification actividad", "Actividad no actualizada", "La actividad no se puede actualizar", Alert.AlertType.ERROR);
                    }
                }
            } else {
                mostrarMensaje("Notification actividad", "Actividad no actualizada", "Los datos ingresados son invalidos", Alert.AlertType.ERROR);
            }
        } else {
            mostrarMensaje("Notification actividad", "Actividad no seleccionada", "Seleccionado una actividad de la lista", Alert.AlertType.WARNING);
        }
    }

    private String DeterminarPosicionActividad() {
        String posicion = "";
        if (checkCrearDUAC.isSelected() && checkCrearalfinal.isSelected()) {
            mostrarMensaje("Notificación Actividad", "Actividad no creada", "Solo puede seleccionar una unica posición", Alert.AlertType.ERROR);
        }
        if (checkCrearDUAC.isSelected()) {
            posicion = "DUAC";
        }
        if (checkCrearalfinal.isSelected()) {
            posicion = "FINAL";
        }
        if (!checkCrearDUAC.isSelected() && !checkCrearalfinal.isSelected()) {
            posicion = txtPosicionActividad.getText();
        }
        return posicion;
    }


    private boolean datosValidosProducto(String id, String nombre, String descripcion) {

        String mensaje = "";

        if (id == null || id.equals("") || !id.matches("[0-9]+"))
            mensaje += "El id es invalido \n";

        if (nombre == null || nombre.equals(""))
            mensaje += "El nombre del producto es invalido \n";

        if (descripcion == null || descripcion.equals(""))
            mensaje += "La descripcion es invalida \n";


        if (mensaje.equals("")) {
            return true;
        } else {
            mostrarMensaje("Notification proceso", "Datos invalidos", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }

    private void limpiarCamposProceso() {
        txtDescripcion.setText("");
        txtIdProcesos.setText("");
        txtNombreProceso.setText("");
    }

    private void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {

        Alert aler = new Alert(alertType);
        aler.setTitle(titulo);
        aler.setHeaderText(header);
        aler.setContentText(contenido);
        aler.showAndWait();
    }

    private boolean mostrarMensajeConfirmacion(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmation");
        alert.setContentText(mensaje);
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }


    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    public ObservableList<Proceso> getListaProcesoData() {
        listaProcesoData = FXCollections.observableArrayList(controllerPrincipal.obtenerProcesos());
        return listaProcesoData;
    }

    private ObservableList<Actividad> getListaActividadesData(Proceso p) {
        listaActividadesData = FXCollections.observableArrayList(controllerPrincipal.obtenerActividades(p));
        return listaActividadesData;
    }

    private ObservableList<Tarea> getListaTareasData(Proceso p, Actividad value) {
        listaTareasData = FXCollections.observableArrayList(controllerPrincipal.obtenerTareas(p, value));
        return listaTareasData;
    }

    /////////////////////////////////// tareas////////////////////////////////////////////////////////////

    private Queue<Tarea> colaTareas = new LinkedList<>();

    @FXML
    private TableView<Tarea> tblTareas;
    @FXML
    private TableColumn<Tarea, String> colDescripcionTarea;
    @FXML
    private TableColumn<Tarea, Boolean> colObligatorioTarea;
    @FXML
    private TableColumn<Tarea, Integer> colTiempoMaximoTarea;
    @FXML
    private TableColumn<Tarea, Integer> colTiempoMinimoTarea;

    @FXML
    private ComboBox<Actividad> comboBoxActividades;
    @FXML
    private TextArea txtDescripcionTarea;
    @FXML
    private CheckBox checkObligatoriaTarea;
    @FXML
    private TextField txtTiempoMaximoTarea;
    @FXML
    private TextField txtTiempoMinimoTarea;
    @FXML
    private TextField txtPosicionTarea;
    private int determinarPosicionTarea() {
        if (txtPosicionTarea.getText().equals("")) {
            return 0;
        } else {
            return Integer.parseInt(txtPosicionTarea.getText());
        }

    }

    @FXML
    void eliminarTareaAction(ActionEvent event) {
        eliminarTarea();
    }

    private void eliminarTarea() {
        Tarea tareaSeleccionada = tblTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada == null) {
            mostrarMensaje("Notificación tarea", "Tarea no creada", "No se ha seleccionado una tarea", Alert.AlertType.ERROR);
            return;
        }
        boolean tareaEliminada = controllerPrincipal.eliminarTarea(tareaSeleccionada, comboBoxActividades.getValue(), comboBoxProcesos1.getValue());
        if (tareaEliminada) {
            tblTareas.getItems().clear();
            tblTareas.setItems(getListaTareasData(comboBoxProcesos1.getValue(), comboBoxActividades.getValue()));
            tblTareas.refresh();
            txtDescripcionTarea.setText("");
            txtTiempoMaximoTarea.setText("");
            txtTiempoMinimoTarea.setText("");
            checkObligatoriaTarea.setSelected(false);

            mostrarMensaje("Notification tarea", "Tarea eliminada", "La tarea se ha eliminado con éxito", Alert.AlertType.INFORMATION);
        } else {
            mostrarMensaje("Notification tarea", "Tarea no eliminada", "La tarea no se puede eliminar", Alert.AlertType.ERROR);
        }
    }

    private void cargarActividadesTareas(Proceso p) {
        if (p != null) {
            comboBoxActividades.getItems().clear();
            comboBoxActividades.setItems(getListaActividadesData(p));
        }
    }

    private void cargarTareas(Proceso p, Actividad value) {
        if (value != null) {
            tblTareas.getItems().clear();
            tblTareas.setItems(getListaTareasData(p, value));
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private TextField buscarActividad;
    @FXML
    private TextField buscarTarea;


    private void mostrarEnAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /////////////////////////////////
    @FXML
    private void buscarAct() {
        Proceso procesoSeleccionado = tblProcesos1.getSelectionModel().getSelectedItem();
        String textoBusqueda = buscarActividad.getText().trim();

        if (procesoSeleccionado == null) {
            mostrarMensaje("Error de búsqueda", "Proceso no seleccionado",
                    "Por favor, seleccione un proceso antes de buscar actividades.", Alert.AlertType.WARNING);
            return;
        }

        if (textoBusqueda.isEmpty()) {
            mostrarMensaje("Error de búsqueda", "Texto vacío",
                    "Por favor, ingrese un texto para buscar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Obtener las actividades del proceso seleccionado
            List<Actividad> todasLasActividades = controllerPrincipal.obtenerActividades(procesoSeleccionado);

            // Filtrar las actividades que coincidan con el texto de búsqueda
            List<Actividad> actividadesEncontradas = todasLasActividades.stream()
                    .filter(actividad ->
                            actividad.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase()) ||
                                    actividad.getDescripcion().toLowerCase().contains(textoBusqueda.toLowerCase()))
                    .collect(Collectors.toList());

            // Actualizar la tabla con los resultados
            tblActividades.getItems().clear();

            if (!actividadesEncontradas.isEmpty()) {
                // Crear una nueva ObservableList con los resultados
                ObservableList<Actividad> actividadesData = FXCollections.observableArrayList(actividadesEncontradas);
                tblActividades.setItems(actividadesData);

                // Mostrar mensaje de éxito
                mostrarMensaje("Resultado de búsqueda",
                        "Actividades encontradas",
                        "Se encontraron " + actividadesEncontradas.size() + " actividades.",
                        Alert.AlertType.INFORMATION);
            } else {
                mostrarMensaje("Resultado de búsqueda",
                        "Sin resultados",
                        "No se encontraron actividades que coincidan con: " + textoBusqueda,
                        Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            mostrarMensaje("Error",
                    "Error en la búsqueda",
                    "Ocurrió un error al buscar las actividades: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }







    ////////////////////////////////////////////////
    @FXML
    public void buscarTar(ActionEvent event) {
        Proceso procesoSeleccionado = tblProcesos1.getSelectionModel().getSelectedItem();
        String textoBusqueda = buscarTarea.getText();
        try {
            if (procesoSeleccionado != null && !textoBusqueda.isEmpty()) {
                List<Tarea> tareasEncontradas = controllerPrincipal.buscarTareas(procesoSeleccionado, textoBusqueda);

                actualizarTablaTareas(tareasEncontradas);

                if (!tareasEncontradas.isEmpty()) {
                    mostrarEnAlerta("Detalles de la Tarea:\n" +
                            "ID: " + tareasEncontradas.get(0).getDescripcion() + "\n" +
                            "Descripción: " + tareasEncontradas.get(0).getDescripcion());
                } else {
                    mostrarEnAlerta("No se encontraron tareas.");
                }
            }
        } catch (Exception e) {
            mostrarEnAlerta("No se encontraron tareas.");
        }
    }


    private void actualizarTablaActividades(List<Actividad> actividades) {
        // Limpia la tabla actual
        tblActividades.getItems().clear();

        // Agrega las actividades encontradas a la tabla
        if (actividades != null && !actividades.isEmpty()) {
            listaActividadesData1.addAll(actividades);
            tblActividades.setItems(listaActividadesData1);
        }
    }

    private void actualizarTablaTareas(List<Tarea> tareas) {
        // Limpia la tabla actual
        tblTareas.getItems().clear();

        // Agrega las tareas encontradas a la tabla
        if (tareas != null && !tareas.isEmpty()) {
            listaTareasData.addAll(tareas);
            tblTareas.setItems(listaTareasData);
            tblTareas.refresh();
        }
    }

    // Método mejorado para actualizar las tablas
    private void actualizarTablas() {
        // Actualizar tabla de procesos
        actualizarArbol();
        listaProcesoData.clear();
        listaProcesoData.addAll(controllerPrincipal.obtenerProcesos());

        // Actualizar ComboBoxes de procesos
        comboBoxProcesos.getItems().clear();
        comboBoxProcesos.getItems().addAll(listaProcesoData);
        comboBoxProcesos1.getItems().clear();
        comboBoxProcesos1.getItems().addAll(listaProcesoData);

        // Si hay un proceso seleccionado, actualizar sus actividades
        Proceso procesoSeleccionado = comboBoxProcesos.getValue();
        if (procesoSeleccionado != null) {
            actualizarTablaActividades(procesoSeleccionado);
        }

        // Si hay una actividad seleccionada, actualizar sus tareas
        Actividad actividadSeleccionada = comboBoxActividades.getValue();
        if (actividadSeleccionada != null && procesoSeleccionado != null) {
            actualizarTablaTareas(procesoSeleccionado, actividadSeleccionada);
        }
    }

    private void actualizarTablaActividades(Proceso proceso) {
        listaActividadesData.clear();
        listaActividadesData.addAll(controllerPrincipal.obtenerActividades(proceso));
        tblActividades.setItems(listaActividadesData);

        // Actualizar ComboBox de actividades
        comboBoxActividades.getItems().clear();
        comboBoxActividades.getItems().addAll(listaActividadesData);
    }

    private void actualizarTablaTareas(Proceso proceso, Actividad actividad) {
        listaTareasData.clear();
        listaTareasData.addAll(controllerPrincipal.obtenerTareas(proceso, actividad));

        // Forzar refresco limpiando y volviendo a asignar los items
        tblTareas.setItems(null);
        tblTareas.setItems(listaTareasData);
        tblTareas.refresh(); // Refresca visualmente la tabla de tareas
    }


    // Modificar el método crearProceso
    private void crearProceso() {
        String id = txtIdProcesos.getText();
        String nombre = txtNombreProceso.getText();
        String descripcion = txtDescripcion.getText();


        if (datosValidosProducto(id, nombre, descripcion)) {
            Proceso p = new Proceso(id, nombre, descripcion);
            boolean creado = controllerPrincipal.crearProceso(p);
            if (creado) {
                mostrarMensaje("Notificación proceso", "Proceso creado",
                        "El proceso se ha creado correctamente", Alert.AlertType.INFORMATION);
                limpiarCamposProceso();
                actualizarTablas();
                tblProcesos.getItems().clear();
                tblProcesos.setItems(getListaProcesoData());
                actualizarArbol();

                EmailSender.sendEmail("pruebaestructa@gmail.com", "Recordatorio proceso nuevo", "el nuevo proceso se a creado y este es el recordatorio : " + nombre);
            } else {
                mostrarMensaje("Notificación proceso", "Proceso no creado",
                        "El proceso no se ha creado correctamente", Alert.AlertType.ERROR);
                limpiarCamposProceso();
            }
        } else {
            mostrarMensaje("Notificación proceso", "Proceso no creado",
                    "Los datos ingresados son inválidos", Alert.AlertType.ERROR);
        }
    }

    // Modificar el método crearActividad
    private void crearActividad() {
        String nombre = txtNombreActividad.getText();
        String descripcion = txtDescripcionActividad.getText();
        boolean obligatorio = checkObligatorio.isSelected();
        Proceso p = comboBoxProcesos.getValue();

        if (p == null) {
            mostrarMensaje("Notificación actividad", "Actividad no creada",
                    "No se ha seleccionado un proceso", Alert.AlertType.ERROR);
            return;
        }

        if (datosValidosProducto("1", nombre, descripcion)) {
            Actividad a = new Actividad(nombre, descripcion, obligatorio);
            String posicion = DeterminarPosicionActividad();
            boolean creado = controllerPrincipal.crearActividad(a, posicion, p);

            if (creado) {
                mostrarMensaje("Notificación actividad", "Actividad creada",
                        "La actividad se ha creado correctamente", Alert.AlertType.INFORMATION);
                limpiarCamposActividad();
                p = comboBoxProcesos.getValue();
                actualizarTablaActividades(p);
                actualizarArbol();

            } else {
                mostrarMensaje("Notificación actividad", "Actividad no creada",
                        "La actividad no se ha creado ya existe una con ese nombre", Alert.AlertType.ERROR);
                limpiarCamposActividad();
            }
        }
    }

    // Agregar método para limpiar campos de actividad
    private void limpiarCamposActividad() {
        txtNombreActividad.setText("");
        txtDescripcionActividad.setText("");
        checkObligatorio.setSelected(false);
        checkCrearDUAC.setSelected(false);
        checkCrearalfinal.setSelected(false);
        txtPosicionActividad.setText("");
    }

    // Modificar el método tarea
    @FXML
    private void tarea() {
        try {
            String descripcion = txtDescripcionTarea.getText();
            boolean obligatoria = checkObligatoriaTarea.isSelected();
            int tiempoMaximo = Integer.parseInt(txtTiempoMaximoTarea.getText());
            int tiempoMinimo = Integer.parseInt(txtTiempoMinimoTarea.getText());
            int posicion = determinarPosicionTarea();

            Proceso procesoSeleccionado = comboBoxProcesos1.getValue();
            Actividad actividadSeleccionada = comboBoxActividades.getValue();

            if (procesoSeleccionado == null || actividadSeleccionada == null) {
                mostrarMensaje("Error", "Selección incompleta",
                        "Debe seleccionar un proceso y una actividad", Alert.AlertType.ERROR);
                return;
            }

            Tarea nuevaTarea = new Tarea(descripcion, obligatoria, tiempoMaximo, tiempoMinimo);
            boolean creada = controllerPrincipal.crearTarea(nuevaTarea, actividadSeleccionada,
                    procesoSeleccionado, posicion);

            if (creada) {
                mostrarMensaje("Notificación tarea", "Tarea creada",
                        "La tarea se ha creado correctamente", Alert.AlertType.INFORMATION);
                limpiarCamposTarea();
                actualizarTablaTareas(procesoSeleccionado, actividadSeleccionada);
                tblTareas.refresh();
                actualizarArbol();
            } else {
                mostrarMensaje("Notificación tarea", "Tarea no creada",
                        "La tarea no se ha creado correctamente", Alert.AlertType.ERROR);
                limpiarCamposTarea();
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error", "Entrada inválida",
                    "Ingrese valores numéricos válidos para tiempo máximo y mínimo.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarMensaje("Error", "Error en la operación",
                    "Se ha producido un error al realizar la tarea.", Alert.AlertType.ERROR);
        }
    }

    // Agregar método para limpiar campos de tarea
    private void limpiarCamposTarea() {
        txtDescripcionTarea.setText("");
        checkObligatoriaTarea.setSelected(false);
        txtTiempoMaximoTarea.setText("");
        txtTiempoMinimoTarea.setText("");
        txtPosicionTarea.setText("");
    }




    private void actualizarArbol() {
        // Crear el nodo raíz

        TreeItem<String> rootItem = new TreeItem<>("Procesos");
        rootItem.setExpanded(true);  // El nodo raíz sí puede estar expandido

        // Obtener todos los procesos actualizados
        List<Proceso> procesos = controllerPrincipal.obtenerProcesos();

        // Para cada proceso
        for (Proceso proceso : procesos) {
            TreeItem<String> procesoItem = new TreeItem<>(proceso.getNombre());
            procesoItem.setExpanded(false);  // Proceso colapsado inicialmente

            // Obtener actividades actualizadas del proceso
            List<Actividad> actividades = controllerPrincipal.obtenerActividades(proceso);

            // Para cada actividad
            for (Actividad actividad : actividades) {
                TreeItem<String> actividadItem = new TreeItem<>(actividad.getNombre());
                actividadItem.setExpanded(false);  // Actividad colapsada inicialmente

                // Obtener tareas actualizadas de la actividad
                ArrayList<Tarea> tareas = controllerPrincipal.obtenerTareas(proceso, actividad);

                // Para cada tarea
                for (Tarea tarea : tareas) {
                    TreeItem<String> tareaItem = new TreeItem<>(tarea.getDescripcion());
                    actividadItem.getChildren().add(tareaItem);
                }

                procesoItem.getChildren().add(actividadItem);
            }

            rootItem.getChildren().add(procesoItem);
        }

        // Actualizar los tres TreeView
        Platform.runLater(() -> {
            arbol.setRoot(rootItem);
            arbol1.setRoot(rootItem);
            arbol2.setRoot(rootItem);
        });
    }



}

