/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;
import java.time.format.DateTimeFormatter;

/**
 * Generador concreto: Ticket de reserva
 */
public class GeneradorTicket extends GeneradorDocumento {
    private Reserva reserva;
    private static final DateTimeFormatter FORMATO_FECHA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = 
        DateTimeFormatter.ofPattern("HH:mm");
    
    public GeneradorTicket(Reserva reserva) {
        this.reserva = reserva;
    }
    
    @Override
    protected String generarEncabezado() {
        return "==========================================\n" +
               "           TICKET DE RESERVA              \n" +
               "              CINE OCINE                  \n" +
               "==========================================";
    }
    
    @Override
    protected String generarCuerpo() {
        Cliente cliente = reserva.getCliente();
        Sesion sesion = reserva.getSesion();
        Pelicula pelicula = sesion.getPelicula();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Código de Reserva: ").append(reserva.getCodigoReserva()).append("\n");
        sb.append("Fecha de Reserva: ").append(
            reserva.getFechaHoraReserva().format(FORMATO_FECHA)).append("\n\n");
        
        sb.append("DATOS DEL CLIENTE:\n");
        sb.append("  Nombre: ").append(cliente.getNombreCompleto()).append("\n");
        sb.append("  DNI: ").append(cliente.getDni()).append("\n");
        sb.append("  Tipo de Abono: ").append(cliente.getTipoAbono()).append("\n\n");
        
        sb.append("DETALLES DE LA SESIÓN:\n");
        sb.append("  Película: ").append(pelicula.getTitulo()).append("\n");
        sb.append("  Tipo: ").append(pelicula.getTipoProyeccion()).append("\n");
        sb.append("  Fecha: ").append(sesion.getFecha().format(FORMATO_FECHA)).append("\n");
        sb.append("  Hora: ").append(sesion.getHoraInicio().format(FORMATO_HORA)).append("\n");
        sb.append("  Sala: ").append(sesion.getSala().getNumero()).append("\n");
        sb.append("  Número de Entradas: ").append(reserva.getNumEntradas()).append("\n\n");
        
        sb.append("EXTRAS:\n");
        sb.append("  ").append(reserva.getDescripcionExtras()).append("\n\n");
        
        sb.append("PRECIO TOTAL: ").append(String.format("%.2f€", reserva.getPrecioFinal()));
        sb.append("\n  (Descuento ").append(cliente.getTipoAbono()).append(" aplicado)");
        
        return sb.toString();
    }
    
    /**
     * Genera el archivo con nombre específico
     */
    public boolean generar() {
        String nombreArchivo = "Ticket_" + reserva.getCliente().getDni() + 
                              "_" + reserva.getCodigoReserva() + ".txt";
        return generarDocumento("tickets/" + nombreArchivo);
    }
}
