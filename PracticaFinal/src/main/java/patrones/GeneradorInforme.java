/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;
import java.time.format.DateTimeFormatter;

/**
 * Generador concreto: Informe de sesión
 */
public class GeneradorInforme extends GeneradorDocumento {
    private Sesion sesion;
    private double ingresosTotales;
    private static final DateTimeFormatter FORMATO_FECHA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = 
        DateTimeFormatter.ofPattern("HH:mm");
    
    public GeneradorInforme(Sesion sesion, double ingresosTotales) {
        this.sesion = sesion;
        this.ingresosTotales = ingresosTotales;
    }
    
    @Override
    protected String generarEncabezado() {
        return "==========================================\n" +
               "          INFORME DE SESIÓN               \n" +
               "              CINE OCINE                  \n" +
               "==========================================";
    }
    
    @Override
    protected String generarCuerpo() {
        Pelicula pelicula = sesion.getPelicula();
        int aforoTotal = sesion.getSala().getCapacidad();
        int entradasVendidas = sesion.getPlazasOcupadas();
        double porcentajeOcupacion = (entradasVendidas * 100.0) / aforoTotal;
        
        StringBuilder sb = new StringBuilder();
        sb.append("Código de Sesión: ").append(sesion.getCodigoSesion()).append("\n\n");
        
        sb.append("PELÍCULA PROYECTADA:\n");
        sb.append("  Título: ").append(pelicula.getTitulo()).append("\n");
        sb.append("  Director: ").append(pelicula.getDirector()).append("\n");
        sb.append("  Tipo: ").append(pelicula.getTipoProyeccion()).append("\n");
        sb.append("  Duración: ").append(pelicula.getDuracionMinutos()).append(" min\n\n");
        
        sb.append("INFORMACIÓN DE LA SESIÓN:\n");
        sb.append("  Fecha: ").append(sesion.getFecha().format(FORMATO_FECHA)).append("\n");
        sb.append("  Hora: ").append(sesion.getHoraInicio().format(FORMATO_HORA)).append("\n");
        sb.append("  Sala: ").append(sesion.getSala().getNumero()).append("\n");
        sb.append("  Estado: ").append(sesion.getEstado()).append("\n\n");
        
        sb.append("ESTADÍSTICAS:\n");
        sb.append("  Aforo Total: ").append(aforoTotal).append(" butacas\n");
        sb.append("  Entradas Vendidas: ").append(entradasVendidas).append("\n");
        sb.append("  Plazas Disponibles: ").append(sesion.getPlazasDisponibles()).append("\n");
        sb.append("  Porcentaje de Ocupación: ").append(
            String.format("%.2f%%", porcentajeOcupacion)).append("\n\n");
        
        sb.append("INGRESOS:\n");
        sb.append("  Precio Base: ").append(String.format("%.2f€", sesion.getPrecioBase())).append("\n");
        sb.append("  Recargo Proyección: ").append(
            String.format("%.2f€", pelicula.getRecargoProyeccion())).append("\n");
        sb.append("  Ingresos Totales: ").append(String.format("%.2f€", ingresosTotales));
        
        return sb.toString();
    }
    
    /**
     * Genera el archivo con nombre específico
     */
    public boolean generar() {
        String nombreArchivo = "Sesion_" + sesion.getCodigoSesion() + "_Informe.txt";
        return generarDocumento("informes/" + nombreArchivo);
    }
}
