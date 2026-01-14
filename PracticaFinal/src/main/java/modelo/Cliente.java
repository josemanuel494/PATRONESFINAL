/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 * Representa un cliente del cine
 * Implementa Serializable para persistencia
 */
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String dni;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String contrasena;
    private LocalDate fechaAlta;
    private TipoAbono tipoAbono;
    
    /**
     * Constructor de Cliente
     */
    public Cliente(String dni, String nombreCompleto, String email, String telefono,
                   String contrasena, TipoAbono tipoAbono) {
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.fechaAlta = LocalDate.now();
        this.tipoAbono = tipoAbono;
    }
    
    /**
     * Verifica si el cliente tiene acceso a salas VIP
     */
    public boolean tieneAccesoVIP() {
        return tipoAbono == TipoAbono.VIP;
    }
    
    /**
     * Obtiene el porcentaje de descuento según el abono
     */
    public double getPorcentajeDescuento() {
        return tipoAbono.getPorcentajeDescuento();
    }
    
    // Getters
    public String getDni() {
        return dni;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public LocalDate getFechaAlta() {
        return fechaAlta;
    }
    
    public TipoAbono getTipoAbono() {
        return tipoAbono;
    }
    
    // Setters
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean verificarContrasena(String contrasenaTextoPlano) {
        return contrasena.equals(contrasenaTextoPlano)
            || contrasena.equals(hashContrasena(contrasenaTextoPlano));
    }

    public static String hashContrasena(String contrasenaTextoPlano) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(contrasenaTextoPlano.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte value : encoded) {
                sb.append(String.format("%02x", value));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo inicializar el hash de contraseña", e);
        }
    }
    
    public void setTipoAbono(TipoAbono tipoAbono) {
        this.tipoAbono = tipoAbono;
    }
    
    @Override
    public String toString() {
        return nombreCompleto + " (" + email + ") - " + tipoAbono;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente cliente = (Cliente) obj;
        return dni.equals(cliente.dni);
    }
    
    @Override
    public int hashCode() {
        return dni.hashCode();
    }
}
