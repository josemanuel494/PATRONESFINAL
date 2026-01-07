/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;
import java.io.*;
import java.util.*;

/**
 * PATRÓN: SINGLETON
 * 
 * Garantiza una única instancia del gestor de datos del sistema.
 * Maneja la persistencia de todos los datos (películas, sesiones, clientes, reservas).
 */
public class GestorPersistencia {
    
    // Instancia única (Singleton)
    private static GestorPersistencia instancia;
    
    // Archivo de persistencia
    private static final String ARCHIVO_DATOS = "cine_datos.dat";
    
    // Datos del sistema
    private DatosCine datos;
    
    /**
     * Constructor privado (patrón Singleton)
     */
    private GestorPersistencia() {
        this.datos = new DatosCine();
    }
    
    /**
     * Obtiene la instancia única del gestor
     * @return Instancia del GestorPersistencia
     */
    public static synchronized GestorPersistencia getInstance() {
        if (instancia == null) {
            instancia = new GestorPersistencia();
        }
        return instancia;
    }
    
    /**
     * Carga los datos desde el archivo
     * @return true si se cargaron correctamente, false si no existe el archivo
     */
    public boolean cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            return false;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            datos = (DatosCine) ois.readObject();
            System.out.println("Datos cargados correctamente desde " + ARCHIVO_DATOS);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Guarda los datos en el archivo
     * @return true si se guardaron correctamente
     */
    public boolean guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(datos);
            System.out.println("Datos guardados correctamente en " + ARCHIVO_DATOS);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene los datos del cine
     */
    public DatosCine getDatos() {
        return datos;
    }
    
    /**
     * Inicializa datos de prueba (primera ejecución)
     */
    public void inicializarDatosPrueba() {
        datos.inicializarDatosPrueba();
    }
}