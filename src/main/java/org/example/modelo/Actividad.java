package org.example.modelo;

import org.example.excepciones.CrudTareasException;
import org.example.recursos.listasEnlazadasGenericas.Cola;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

public class Actividad implements Serializable {
    private String id; // O usar un int si prefieres
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String descripcion;
    private boolean obligatorio;
    private Cola <org.example.modelo.Tarea> tareas;


    public Actividad() {
    }
    public Actividad(String nombre, String descripcion, boolean obligatorio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.obligatorio = obligatorio;
        tareas = new Cola<>();
    }
    public Actividad(String id, String nombre, String descripcion, boolean obligatorio) {
        this.id = id; // o this.id = String.valueOf(generarIdUnico()); si deseas generarlo
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.obligatorio = obligatorio;
        tareas = new Cola<>();
    }
    public boolean crearTarea(Tarea nuevaTarea, int posicion) throws CrudTareasException{
        if (nuevaTarea != null) {
            boolean creado = false;
            if (posicion == 0) {
                tareas.encolar(nuevaTarea);
                creado = true;
            }else {
                Cola <Tarea> colaAux = new Cola<>();
                int contador = 0;
                while (!tareas.estaVacia()){
                    Tarea tarea = tareas.desencolar();
                    if (contador == posicion){
                        colaAux.encolar(nuevaTarea);
                        colaAux.encolar(tarea);
                    }else {
                        colaAux.encolar(tarea);
                    }
                    contador++;
                }
                tareas = colaAux;
                creado = true;
            }
            if (creado) {
                System.out.println("Tarea creada");
                return true;
            }
        }else {
            throw new CrudTareasException("La tarea es nula");
        }
        return false;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    @Override
    public String toString() {
        return "nombre='" + nombre + '\'' +
                ", obligatorio=" + obligatorio ;
    }
    public Cola getTareas() {
        return tareas;
    }


    public void setTareas(Cola tareas) {
        this.tareas = tareas;
    }

    public String getId() {
        return id; // O devolver un int si es el caso
    }

    public boolean eliminarTarea(Tarea tareaSeleccionada) throws CrudTareasException {
        if (tareaSeleccionada != null) {
            Cola <Tarea> colaAux = new Cola<>();
            while (!tareas.estaVacia()){
                Tarea tarea = tareas.desencolar();
                if (!tarea.getDescripcion().equals(tareaSeleccionada.getDescripcion())){
                    colaAux.encolar(tarea);
                }
            }
            tareas = colaAux;
            return true;
        }else {
            throw new CrudTareasException("La tarea es nula");
        }
    }


}

