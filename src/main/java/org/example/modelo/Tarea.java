package org.example.modelo;

import java.io.Serializable;

public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;
    String descripcion;
    boolean obligatorio;
    int tiempoMaximo;
    int tiempoMinimo;
    public Tarea() {
    }
    public Tarea(String descripcion, boolean obligatorio, int tiempoMaximo, int tiempoMinimo) {
        this.descripcion = descripcion;
        this.obligatorio = obligatorio;
        this.tiempoMaximo = tiempoMaximo;
        this.tiempoMinimo = tiempoMinimo;
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

    public int getTiempoMaximo() {
        return tiempoMaximo;
    }

    public void setTiempoMaximo(int tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    public int getTiempoMinimo() {
        return tiempoMinimo;
    }

    public void setTiempoMinimo(int tiempoMinimo) {
        this.tiempoMinimo = tiempoMinimo;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "descripcion='" + descripcion + '\'' +
                ", obligatorio=" + obligatorio +
                ", tiempoMaximo=" + tiempoMaximo +
                ", tiempoMinimo=" + tiempoMinimo ;
    }
}
