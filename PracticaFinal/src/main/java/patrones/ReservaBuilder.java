/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;

/**
 * PATRÓN: BUILDER
 * 
 * Construye objetos Reserva complejos paso a paso.
 * Maneja la lógica de cálculo de precios con descuentos y extras.
 */
public class ReservaBuilder {
    
    // Componentes obligatorios
    private String codigoReserva;
    private Cliente cliente;
    private Sesion sesion;
    private int numEntradas;
    
    // Componentes opcionales (extras)
    private boolean conPalomitas = false;
    private boolean conBebida = false;
    private boolean conCombo = false;
    
    /**
     * Constructor del builder con parámetros obligatorios
     */
    public ReservaBuilder(String codigoReserva, Cliente cliente, Sesion sesion, int numEntradas) {
        this.codigoReserva = codigoReserva;
        this.cliente = cliente;
        this.sesion = sesion;
        this.numEntradas = numEntradas;
    }
    
    /**
     * Añade palomitas a la reserva
     */
    public ReservaBuilder agregarPalomitas() {
        this.conPalomitas = true;
        return this;
    }
    
    /**
     * Añade bebida a la reserva
     */
    public ReservaBuilder agregarBebida() {
        this.conBebida = true;
        return this;
    }
    
    /**
     * Añade combo a la reserva
     */
    public ReservaBuilder agregarCombo() {
        this.conCombo = true;
        return this;
    }
    
    /**
     * Construye la reserva aplicando decoradores y calculando el precio final
     * @return Reserva construida con todos los extras y precio calculado
     */
    public Reserva build() {
        // Crear reserva base
        Reserva reserva = new Reserva(codigoReserva, cliente, sesion, numEntradas);
        
        // Aplicar decoradores según los extras seleccionados
        if (conCombo) {
            reserva = new ComboDecorator(reserva);
        } else {
            if (conPalomitas) {
                reserva = new PalomitasDecorator(reserva);
            }
            if (conBebida) {
                reserva = new BebidaDecorator(reserva);
            }
        }
        
        // Calcular precio final usando Strategy
        double precioEntradas = reserva.calcularPrecioEntradas();
        
        // Obtener estrategia de descuento según el tipo de abono
        EstrategiaDescuento estrategia = EstrategiaDescuentoFactory.getEstrategia(
            cliente.getTipoAbono()
        );
        
        // Calcular descuento solo sobre las entradas (no sobre extras)
        double descuento = estrategia.calcularDescuento(precioEntradas);
        
        // Establecer precio final
        reserva.calcularPrecioFinal(descuento);
        
        return reserva;
    }
    
    /**
     * Método estático para crear un builder rápidamente
     */
    public static ReservaBuilder crear(String codigoReserva, Cliente cliente, 
                                       Sesion sesion, int numEntradas) {
        return new ReservaBuilder(codigoReserva, cliente, sesion, numEntradas);
    }
}
