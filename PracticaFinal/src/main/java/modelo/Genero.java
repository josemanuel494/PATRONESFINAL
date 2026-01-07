/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Enumeración para los géneros de películas
 */
public enum Genero {
    DRAMA("Drama"),
    COMEDIA("Comedia"),
    TERROR("Terror"),
    CIENCIA_FICCION("Ciencia Ficción"),
    ACCION("Acción"),
    ANIMACION("Animación"),
    DOCUMENTAL("Documental");
    
    private final String nombre;
    
    Genero(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
