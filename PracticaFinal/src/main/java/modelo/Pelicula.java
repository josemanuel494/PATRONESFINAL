/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 * Clase abstracta que representa una película
 * Patrón: Template Method (define estructura común)
 */
public abstract class Pelicula implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String codigo;
    protected String titulo;
    protected String director;
    protected int anioEstreno;
    protected int duracionMinutos;
    protected Genero genero;
    protected String sinopsis;
    
    /**
     * Constructor de Película
     */
    public Pelicula(String codigo, String titulo, String director, int anioEstreno, 
                    int duracionMinutos, Genero genero, String sinopsis) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.director = director;
        this.anioEstreno = anioEstreno;
        this.duracionMinutos = duracionMinutos;
        this.genero = genero;
        this.sinopsis = sinopsis;
    }
    
    /**
     * Método abstracto que cada tipo de película implementa
     * @return Recargo específico del tipo de proyección
     */
    public abstract double getRecargoProyeccion();
    
    /**
     * Método abstracto que indica el tipo de proyección
     * @return Nombre del tipo de proyección
     */
    public abstract String getTipoProyeccion();
    
    /**
     * Verifica si la película puede proyectarse en una sala
     * @param sala Sala a verificar
     * @return true si es compatible, false en caso contrario
     */
    public abstract boolean esCompatibleConSala(Sala sala);
    
    // Getters
    public String getCodigo() {
        return codigo;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getDirector() {
        return director;
    }
    
    public int getAnioEstreno() {
        return anioEstreno;
    }
    
    public int getDuracionMinutos() {
        return duracionMinutos;
    }
    
    public Genero getGenero() {
        return genero;
    }
    
    public String getSinopsis() {
        return sinopsis;
    }
    
    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public void setAnioEstreno(int anioEstreno) {
        this.anioEstreno = anioEstreno;
    }
    
    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
    
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
    
    @Override
    public String toString() {
        return titulo + " (" + getTipoProyeccion() + ") - " + director;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pelicula pelicula = (Pelicula) obj;
        return codigo.equals(pelicula.codigo);
    }
    
    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}