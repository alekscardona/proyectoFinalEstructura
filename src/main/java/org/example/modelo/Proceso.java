package org.example.modelo;

import org.example.excepciones.CrudActividadException;

import org.example.recursos.listasEnlazadasGenericas.ListaDoble;

import java.io.Serializable;

public class Proceso implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nombre;
    private String descripcion;
    private ListaDoble<Actividad> listaActividades;
    private org.example.modelo.Actividad ultimaActividadCreada;
    public Proceso() {
    }
    public Proceso(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        listaActividades = new ListaDoble<>();
    }

    public boolean crearActividad(org.example.modelo.Actividad actividad, String opcion) throws CrudActividadException {
        if (actividad != null) {
            boolean creado = false;
            if (!compararNombre(actividad)) {
                if (opcion.equals("DUAC") || opcion.equals("FINAL")) {

                    switch (opcion) {
                        case "DUAC":
                            // Agregar después de la actividad precedente
                            creado = agregarDespuesDeActividad(actividad);
                            break;
                        case "FINAL":
                            listaActividades.agregarfinal(actividad);
                            creado = true;
                            break;
                        default:
                            throw new CrudActividadException("Opción inválida");

                    }
                }else {
                    creado = agregarDadoNombre(actividad, opcion);
                }
                if (creado) {
                    ultimaActividadCreada = actividad;
                    System.out.println("Actividad creada");
                    return true;
                }
            }
        } else {
            throw new CrudActividadException("La actividad es nula");
        }
        return false;
    }

    private boolean agregarDadoNombre(org.example.modelo.Actividad actividad, String opcion) {
        for (org.example.modelo.Actividad actividad1 : listaActividades) {
            if (actividad1.getNombre().equals(opcion)) {
                int posicion = listaActividades.obtenerPosicionNodo(actividad1) + 1 ;
                listaActividades.agregar(actividad, posicion);
                return true;
            }
        }
        return false;
    }

    private boolean agregarDespuesDeActividad(org.example.modelo.Actividad actividad) {
        if (ultimaActividadCreada == null) {
            listaActividades.agregarfinal(actividad);
            return true;
        }
        if (ultimaActividadCreada != null) {
            int posicion = listaActividades.obtenerPosicionNodo(ultimaActividadCreada) + 1;
            listaActividades.agregar(actividad, posicion);
            return true;
        }
        return false;
    }


    public boolean eliminarActividad(org.example.modelo.Actividad actividad) throws CrudActividadException {
        if (actividad != null) {
            if (compararNombre(actividad)) {
                listaActividades.eliminar(actividad);
                return true;
            } else {
                throw new CrudActividadException("La actividad no existe");
            }
        } else {
            throw new CrudActividadException("La actividad es nula");
        }
    }

    private boolean compararNombre(org.example.modelo.Actividad actividad) {
        for (org.example.modelo.Actividad actividad1 : listaActividades) {
            if (actividad.getNombre().equals(actividad1.getNombre())) {
                return true;
            }
        }
        return false;
    }
    public boolean actualizarActividad(String nombre, String descripcion, boolean obligatorio, org.example.modelo.Actividad actividadSeleccionada) {
        for (org.example.modelo.Actividad actividad : listaActividades) {
            if (actividad.getNombre().equals(actividadSeleccionada.getNombre())) {
                actividad.setNombre(nombre);
                actividad.setDescripcion(descripcion);
                actividad.setObligatorio(obligatorio);
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public ListaDoble<org.example.modelo.Actividad> getListaActividades() {

        return listaActividades;
    }

    public void setListaActividades(ListaDoble<org.example.modelo.Actividad> listaActividades) {
        this.listaActividades = listaActividades;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", nombre='" + nombre + '\'';
    }

    public org.example.modelo.Actividad ObtenerActividad(org.example.modelo.Actividad value) {
        for (org.example.modelo.Actividad actividad : listaActividades) {
            if (actividad.getNombre().equals(value.getNombre())) {
                return actividad;
            }
        }
        return null;
    }


}
