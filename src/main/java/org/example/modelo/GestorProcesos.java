package org.example.modelo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.excepciones.CrudActividadException;
import org.example.excepciones.CrudProcesoException;
import org.example.recursos.listasEnlazadasGenericas.ListaDoble;

public class GestorProcesos{
    private static GestorProcesos instancia = null;
    private ListaDoble<org.example.modelo.Proceso> listaProcesos;
    private GestorProcesos() {
        listaProcesos = new ListaDoble<>();
    }

    public static GestorProcesos obtenerInstancia() throws CrudActividadException, CrudProcesoException {
        if (instancia == null) {
            instancia = new GestorProcesos();
            //inicializarDatos();
        }
        return instancia;
    }
    private static void inicializarDatos() throws CrudProcesoException, CrudActividadException {
        instancia = new GestorProcesos();
        Proceso proceso1 = new Proceso("123", "Desayunos", "Desayunos bien balanceados");
        Proceso proceso2 = new Proceso("456", "Almuerzos", "Almuerzos bien balanceados");
        Proceso proceso3 = new Proceso("789", "Cenas", "Cenas bien balanceados");
        instancia.crearProceso(proceso1);
        instancia.crearProceso(proceso2);
        instancia.crearProceso(proceso3);
        org.example.modelo.Actividad actividad1 = new org.example.modelo.Actividad("Cafe", "Bebida para acompañar desayunos", true);
        org.example.modelo.Actividad actividad2 = new org.example.modelo.Actividad("Huevos", "Huevos para acompañar desayunos", true);
        org.example.modelo.Actividad actividad3 = new org.example.modelo.Actividad("Pan", "Pan para acompañar desayunos", false);
        org.example.modelo.Actividad actividad4 = new org.example.modelo.Actividad("Arroz", "Arroz para acompañar almuerzos", true);
        org.example.modelo.Actividad actividad5 = new org.example.modelo.Actividad("Carne", "Carne para acompañar almuerzos", false);
        proceso1.crearActividad(actividad1, "FINAL");
        proceso1.crearActividad(actividad2, "FINAL");
        proceso1.crearActividad(actividad3, "FINAL");
        proceso2.crearActividad(actividad4, "FINAL");
        proceso2.crearActividad(actividad5, "FINAL");

    }


    public boolean crearProceso(Proceso proceso) throws CrudProcesoException {
        if (proceso != null) {
            if (!compararId(proceso)) {
                listaProcesos.agregarfinal(proceso);
                System.out.println("Proceso creado");
                return true;
            } else {
                throw new CrudProcesoException("El proceso ya existe");
            }
        } else {
            throw new CrudProcesoException("El proceso es nulo");
        }
    }

    private boolean compararId(Proceso proceso) {
        for (Proceso proceso1 : listaProcesos) {
            if (proceso.getId().equals(proceso1.getId())) {
                return true;
            }
        }
        return false;
    }
    public boolean eliminarProceso (Proceso proceso) throws CrudProcesoException {
        if (proceso != null) {
            if (compararId(proceso)) {
                listaProcesos.eliminar(proceso);
                System.out.println("Proceso eliminado");
                return true;
            } else {
                throw new CrudProcesoException("El proceso no existe");
            }
        } else {
            throw new CrudProcesoException("El proceso es nulo");
        }
    }

    public ListaDoble<Proceso> getListaProcesos() {
        return listaProcesos;
    }

    public void setListaProcesos(ListaDoble<Proceso> listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public boolean actualizarProceso(Proceso procesoSeleccionado, Proceso p) {
        int posicion = listaProcesos.obtenerPosicionNodo(procesoSeleccionado);
        if (posicion != -1) {
            listaProcesos.modificarNodo(posicion, p);
            return true;
        }
        return false;
    }

    public Proceso ObtenerProceso(Proceso p) {
        for (Proceso proceso : listaProcesos) {
            if (proceso.getId().equals(p.getId())) {
                return proceso;
            }
        }
        return null;
    }



}
