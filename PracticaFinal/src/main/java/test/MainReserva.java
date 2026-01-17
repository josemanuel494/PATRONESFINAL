/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package test;

import modelo.Reserva;
import controlador.ControladorCine;
import modelo.Cliente;

/**
 *
 * @author Usuario
 */
public class MainReserva {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ControladorCine controlador = new ControladorCine();
        
        try {
                    Cliente cliente = controlador.buscarClientePorDNI("12345678A");

                    // Reserva con palomitas y bebida
                    Reserva r = controlador.crearReserva("R007", cliente, "S001", 
                            1, true, true, false);
                    System.out.println("✓ Reserva con extras: " + r.getCodigoReserva());
                    System.out.println("  Precio: " + String.format("%.2f€", r.getPrecioFinal()));
                    System.out.println("  Extras: " + r.getDescripcionExtras());


                } catch (Exception e) {
                    System.out.println("✗ Error: " + e.getMessage());
                }
    }
    
}
