package org.example.recursos.persistencia;

import org.example.modelo.Actividad;
import org.example.modelo.GestorProcesos;
import org.example.modelo.Proceso;
import org.example.modelo.Tarea;
import org.example.recursos.listasEnlazadasGenericas.ListaDoble;

import java.io.IOException;
import java.util.ArrayList;


public class Persistencia {

    private static final String RUTA_ARCHIVO_MODELO_GESTORPROCESOS_XML = "src/main/resources/persistencia/modelo.xml";
    private static final String RUTA_ARCHIVO_MODELO_PROCESOS_XML = "src/main/resources/persistencia/procesos.xml";
    private static final String RUTA_ARCHIVO_PROCESOS_TXT = "src/main/resources/persistencia/procesos.txt";
    private static final String RUTA_ARCHIVO_ACTIVIDADES_TXT = "src/main/resources/persistencia/actividades.txt";
    private static final String RUTA_ARCHIVO_TAREAS_TXT = "src/main/resources/persistencia/tareas.txt";
    public static void guardarRecursoGestorArchivosXML(GestorProcesos p) {

        try {
            org.example.recursos.persistencia.ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_MODELO_GESTORPROCESOS_XML, p);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void guardarProcesosXML(ListaDoble<Proceso> listaProcesos) {
        try {
            org.example.recursos.persistencia.ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_MODELO_PROCESOS_XML, listaProcesos);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static ListaDoble<Proceso> cargarProcesosXML() {
        ListaDoble <Proceso> p = null;

        try {
            p = (ListaDoble<Proceso>) org.example.recursos.persistencia.ArchivoUtil.cargarRecursoSerializadoXML(RUTA_ARCHIVO_MODELO_PROCESOS_XML);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return p;
    }

    public static void guardarProcesosTXT(ListaDoble<Proceso> listaProcesos) throws IOException {

        String contenido = "";

        for (Proceso proceso:listaProcesos) {

            contenido += proceso.getId()+","+proceso.getNombre()+","+proceso.getDescripcion()+"\n";
        }

        org.example.recursos.persistencia.ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_PROCESOS_TXT, contenido, false);

    }

    public static void guardarActividadesTXT(ListaDoble<Actividad> listaActividades) throws IOException {

        String contenido = "";

        for (Actividad actividad:listaActividades) {

            contenido += actividad.getNombre()+","+actividad.getDescripcion()+","+actividad.isObligatorio()+"\n";
        }

        org.example.recursos.persistencia.ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_ACTIVIDADES_TXT, contenido, false);

    }

    public static void guardarTareasTXT(ArrayList<Tarea> listaTareas) throws IOException{

        String contenido = "";

        for (Tarea tarea:listaTareas) {

            contenido += tarea.getDescripcion()+","+ tarea.isObligatorio()+","+tarea.getTiempoMaximo()+","+tarea.getTiempoMinimo()+"\n";
        }

        org.example.recursos.persistencia.ArchivoUtil.guardarArchivo(RUTA_ARCHIVO_TAREAS_TXT, contenido, false);
    }
}
