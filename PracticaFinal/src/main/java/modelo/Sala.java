/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 * Representa una sala del cine
 */
public class Sala implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int numero;
    private int capacidad;
    private boolean esVIP;
    private boolean soportaIMAX;
    
    /**
     * Constructor de Sala
     * @param numero Número de la sala
     * @param capacidad Capacidad total de butacas
     * @param esVIP Si es sala VIP
     * @param soportaIMAX Si soporta proyección IMAX
     */
    public Sala(int numero, int capacidad, boolean esVIP, boolean soportaIMAX) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.esVIP = esVIP;
        this.soportaIMAX = soportaIMAX;
    }
    
    // Getters
    public int getNumero() {
        return numero;
    }
    
    public int getCapacidad() {
        return capacidad;
    }
    
    public boolean isEsVIP() {
        return esVIP;
    }
    
    public boolean isSoportaIMAX() {
        return soportaIMAX;
    }
    
    @Override
    public String toString() {
        String tipo = "";
        if (esVIP) {
            tipo = " (VIP)";
        } else if (soportaIMAX) {
            tipo = " (IMAX)";
        }
        return "Sala " + numero + tipo + " - " + capacidad + " butacas";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sala sala = (Sala) obj;
        return numero == sala.numero;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }
}