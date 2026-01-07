/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import java.io.*;

/**
 * PATRÓN: TEMPLATE METHOD
 * 
 * Define el esqueleto del algoritmo para generar documentos.
 * Las subclases implementan pasos específicos sin cambiar la estructura.
 */
public abstract class GeneradorDocumento {
    
    /**
     * Template Method: Define la estructura del algoritmo
     * Este método no debe ser sobrescrito
     */
    public final boolean generarDocumento(String rutaArchivo) {
        try {
            // Paso 1: Crear el archivo
            File archivo = new File(rutaArchivo);
            archivo.getParentFile().mkdirs();
            
            // Paso 2: Escribir contenido
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                // Subpasos definidos por subclases
                writer.write(generarEncabezado());
                writer.write("\n\n");
                writer.write(generarCuerpo());
                writer.write("\n\n");
                writer.write(generarPie());
            }
            
            System.out.println("Documento generado: " + rutaArchivo);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al generar documento: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Métodos abstractos que deben implementar las subclases
     */
    protected abstract String generarEncabezado();
    protected abstract String generarCuerpo();
    
    /**
     * Hook method: método con implementación por defecto que puede sobrescribirse
     */
    protected String generarPie() {
        return "Gracias por confiar en Ocine\n" +
               "Centro Comercial Quadernillos";
    }
}