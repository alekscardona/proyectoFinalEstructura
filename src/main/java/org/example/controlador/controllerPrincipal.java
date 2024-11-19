package org.example.controlador;

import org.example.excepciones.CrudActividadException;
import org.example.excepciones.CrudProcesoException;
import org.example.excepciones.CrudTareasException;
import org.example.modelo.Actividad;
import org.example.modelo.GestorProcesos;
import org.example.modelo.Proceso;
import org.example.modelo.Tarea;
import org.example.recursos.listasEnlazadasGenericas.Cola;
import org.example.recursos.listasEnlazadasGenericas.ListaDoble;
import org.example.recursos.persistencia.Persistencia;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class controllerPrincipal implements Runnable {
    org.example.controlador.BoundedSemaphore semaphore = new org.example.controlador.BoundedSemaphore(1);
    Thread hiloServicio1_GuardarResourceXml;
    Thread hiloServicio2_GuardarProcesosXml;
    Thread hiloServicio3_GuardarProcesosTxt;
    Thread hiloServicio4_GuardarActividadesTxt;
    Thread hiloServicio5_GuardarTareasTxt;
    private GestorProcesos gestorProcesos;
    @Override
    public void run() {
        Thread hiloActual = Thread.currentThread();

        try {
            semaphore.ocupar();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (hiloActual == hiloServicio1_GuardarResourceXml) {
            Persistencia.guardarRecursoGestorArchivosXML(gestorProcesos);
            liberarSemaforo();
        }if (hiloActual == hiloServicio2_GuardarProcesosXml) {
            Persistencia.guardarProcesosXML(gestorProcesos.getListaProcesos());
            liberarSemaforo();
        }if (hiloActual == hiloServicio3_GuardarProcesosTxt) {

            try {
                Persistencia.guardarProcesosTXT(gestorProcesos.getListaProcesos());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if (hiloActual == hiloServicio4_GuardarActividadesTxt) {
            ListaDoble<Actividad> listaActividades = new ListaDoble<>();
            for (Proceso proceso:gestorProcesos.getListaProcesos()) {
                for (Actividad actividad:proceso.getListaActividades()) {
                    listaActividades.agregarfinal(actividad);
                }
            }try {
                Persistencia.guardarActividadesTXT(listaActividades);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if (hiloActual == hiloServicio5_GuardarTareasTxt) {
            ArrayList <Tarea> listaTareas = new ArrayList<>();
            for (Proceso proceso:gestorProcesos.getListaProcesos()) {
                for (Actividad actividad:proceso.getListaActividades()) {
                    listaTareas.addAll( obtenerTareas(proceso,actividad));
                }
            }try {
                Persistencia.guardarTareasTXT(listaTareas);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        liberarSemaforo();

    }

    public controllerPrincipal() {
        try {
            gestorProcesos = GestorProcesos.obtenerInstancia();
            //guardarRecursoXML();
            //guardarProcesosXml();
        } catch (CrudActividadException e) {
            e.printStackTrace();
        } catch (CrudProcesoException e) {
            e.printStackTrace();
        }
        cargarProcesosXml();

    }


    private void guardarProcesosXml() {
        hiloServicio2_GuardarProcesosXml = new Thread(this);
        hiloServicio2_GuardarProcesosXml.start();
    }
    private void cargarProcesosXml() {
        ListaDoble<Proceso> listaProcesos = Persistencia.cargarProcesosXML();
        gestorProcesos.setListaProcesos(listaProcesos);
    }

    private void guardarRecursoXML() {
        hiloServicio1_GuardarResourceXml = new Thread(this);
        hiloServicio1_GuardarResourceXml.start();
    }
    private void guardarProcesosTXT() {
        hiloServicio3_GuardarProcesosTxt = new Thread(this);
        hiloServicio3_GuardarProcesosTxt.start();
    }
    private void guardarActividadesTXT() {
        hiloServicio4_GuardarActividadesTxt = new Thread(this);
        hiloServicio4_GuardarActividadesTxt.start();
    }

    public boolean crearProceso(Proceso p)  {
        try {
            boolean creado = gestorProcesos.crearProceso(p);
            if (creado) {
                guardarProcesosXml();
                guardarProcesosTXT();
                return true;
            }
        } catch (CrudProcesoException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean eliminarProceso(Proceso procesoSeleccionado) {
        try {
            boolean eliminado = gestorProcesos.eliminarProceso(procesoSeleccionado);
            if (eliminado) {
                guardarProcesosXml();
                guardarProcesosTXT();
                return true;
            }
        } catch (CrudProcesoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Proceso> obtenerProcesos() {
        ListaDoble<Proceso> listaProcesos = gestorProcesos.getListaProcesos();
        ArrayList<Proceso> lista = new ArrayList<>();
        for (Proceso proceso : listaProcesos) {

            lista.add(proceso);
        }
        return lista;
    }

    public boolean actualizarProceso(Proceso procesoSeleccionado, Proceso p) {
        boolean actualizado = gestorProcesos.actualizarProceso(procesoSeleccionado, p);
        if (actualizado) {
            guardarProcesosXml();
            guardarProcesosTXT();
            return true;
        }
        return false;
    }

    public boolean crearActividad(Actividad a, String posicion, Proceso p) {
        try {
            boolean creado = gestorProcesos.ObtenerProceso(p).crearActividad(a, posicion);
            if (creado) {
                guardarProcesosXml();
                guardarActividadesTXT();
                return true;
            }
        } catch (CrudActividadException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean eliminarActividad(Actividad actividadSeleccionada, Proceso p) {
        try {
            boolean eliminado = gestorProcesos.ObtenerProceso(p).eliminarActividad(actividadSeleccionada);
            if (eliminado) {
                guardarProcesosXml();
                guardarActividadesTXT();
                return true;
            }
        } catch (CrudActividadException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public boolean actualizarActividad(String nombre, String descripcion, boolean obligatorio, Actividad actividadSeleccionada, Proceso p) {
        boolean actualizado = gestorProcesos.ObtenerProceso(p).actualizarActividad(nombre, descripcion, obligatorio, actividadSeleccionada);
        if (actualizado) {
            guardarProcesosXml();
            guardarActividadesTXT();
            return true;
        }
        return false;
    }
    private void liberarSemaforo() {
        try {
            semaphore.liberar();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public boolean crearTarea(Tarea nuevaTarea, Actividad value, Proceso value1, int posicion) {
        try {
            boolean creado = gestorProcesos.ObtenerProceso(value1).ObtenerActividad(value).crearTarea(nuevaTarea, posicion);
            if (creado) {
                guardarProcesosXml();
                guardarTareasTXT();
                return true;
            }
        } catch (CrudTareasException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private void guardarTareasTXT() {
        hiloServicio5_GuardarTareasTxt = new Thread(this);
        hiloServicio5_GuardarTareasTxt.start();
    }

    public boolean eliminarTarea(Tarea tareaSeleccionada, Actividad value, Proceso value1) {
        try {
            boolean eliminado = gestorProcesos.ObtenerProceso(value1).ObtenerActividad(value).eliminarTarea(tareaSeleccionada);
            if (eliminado) {
                guardarProcesosXml();
                guardarTareasTXT();
                return true;
            }
        } catch (CrudTareasException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public ArrayList<Actividad> obtenerActividades(Proceso p) {
        ListaDoble<Actividad> listaActividades = gestorProcesos.ObtenerProceso(p).getListaActividades();
        ArrayList<Actividad> lista = new ArrayList<>();
        for (Actividad actividad : listaActividades) {
            lista.add(actividad);
        }
        return lista;
    }

    public ArrayList<Tarea> obtenerTareas(Proceso p, Actividad value) {
        Cola<Tarea> tareas = gestorProcesos.ObtenerProceso(p).ObtenerActividad(value).getTareas().clone();
        ArrayList<Tarea> lista = new ArrayList<>();
        for (int i = 0; i < tareas.getTamano(); i++) {
            lista.add(tareas.desencolar());
        }

        return lista;
    }

    public List<Actividad> buscarActividades(Proceso proceso, String textoBusqueda) {
        List<Actividad> actividadesEncontradas = new ArrayList<>();
        ListaDoble<Actividad> listaActividades = gestorProcesos.ObtenerProceso(proceso).getListaActividades();

        // Iterar sobre las actividades del proceso y agregar las coincidencias al resultado
        for (Actividad actividad : listaActividades) {
            if (actividad.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                actividadesEncontradas.add(actividad);
            }
        }

        return actividadesEncontradas;
    }

    // Método para buscar tareas por descripción dentro de un proceso y una actividad
    public List<Tarea> buscarTareas(Proceso proceso, String textoBusqueda) {
        List<Tarea> tareasEncontradas = new ArrayList<>();

        // Iterar sobre las actividades del proceso
        for (Actividad actividad : proceso.getListaActividades()) {
            // Obtener las tareas asociadas a la actividad
            List<Tarea> tareas = obtenerTareas(proceso, actividad);

            // Iterar sobre las tareas y agregar las coincidencias al resultado
            for (Tarea tarea : tareas) {
                if (tarea.getDescripcion().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                    tareasEncontradas.add(tarea);
                }
            }
        }

        return tareasEncontradas;
    }




}



