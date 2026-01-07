/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Película en formato IMAX
 */
public class PeliculaIMAX extends Pelicula {
    private static final long serialVersionUID = 1L;
    
    public PeliculaIMAX(String codigo, String titulo, String director, int anioEstreno,
                        int duracionMinutos, Genero genero, String sinopsis) {
        super(codigo, titulo, director, anioEstreno, duracionMinutos, genero, sinopsis);
    }
    
    @Override
    public double getRecargoProyeccion() {
        return 4.0; // Recargo de 4€
    }
    
    @Override
    public String getTipoProyeccion() {
        return "IMAX";
    }
    
    @Override
    public boolean esCompatibleConSala(Sala sala) {
        return sala.isSoportaIMAX(); // Solo salas con soporte IMAX
    }
}

