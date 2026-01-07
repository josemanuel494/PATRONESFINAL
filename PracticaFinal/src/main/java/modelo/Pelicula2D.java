/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Película en formato 2D estándar
 */
public class Pelicula2D extends Pelicula {
    private static final long serialVersionUID = 1L;
    
    public Pelicula2D(String codigo, String titulo, String director, int anioEstreno,
                      int duracionMinutos, Genero genero, String sinopsis) {
        super(codigo, titulo, director, anioEstreno, duracionMinutos, genero, sinopsis);
    }
    
    @Override
    public double getRecargoProyeccion() {
        return 0.0; // Sin recargo
    }
    
    @Override
    public String getTipoProyeccion() {
        return "2D";
    }
    
    @Override
    public boolean esCompatibleConSala(Sala sala) {
        return true; // Compatible con todas las salas
    }
}