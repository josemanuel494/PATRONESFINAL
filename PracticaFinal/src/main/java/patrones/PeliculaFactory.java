/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;

/**
 * PATRÓN: FACTORY METHOD
 * 
 * Factory para crear diferentes tipos de películas según el tipo de proyección.
 * Encapsula la lógica de creación y devuelve la instancia correcta.
 */
public class PeliculaFactory {
    
    /**
     * Tipo de proyección disponibles
     */
    public enum TipoProyeccion {
        DOS_D("2D"),
        TRES_D("3D"),
        IMAX("IMAX");
        
        private final String nombre;
        
        TipoProyeccion(String nombre) {
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
    
    /**
     * Factory Method: Crea una película según el tipo especificado
     * 
     * @param tipo Tipo de proyección
     * @param codigo Código único de la película
     * @param titulo Título de la película
     * @param director Director de la película
     * @param anioEstreno Año de estreno
     * @param duracionMinutos Duración en minutos
     * @param genero Género de la película
     * @param sinopsis Sinopsis breve
     * @return Instancia de Pelicula según el tipo
     */
    public static Pelicula crearPelicula(TipoProyeccion tipo, String codigo, String titulo,
                                         String director, int anioEstreno, int duracionMinutos,
                                         Genero genero, String sinopsis) {
        switch (tipo) {
            case DOS_D:
                return new Pelicula2D(codigo, titulo, director, anioEstreno, 
                                     duracionMinutos, genero, sinopsis);
            case TRES_D:
                return new Pelicula3D(codigo, titulo, director, anioEstreno,
                                     duracionMinutos, genero, sinopsis);
            case IMAX:
                return new PeliculaIMAX(codigo, titulo, director, anioEstreno,
                                       duracionMinutos, genero, sinopsis);
            default:
                throw new IllegalArgumentException("Tipo de proyección no válido: " + tipo);
        }
    }
    
    /**
     * Crea una película a partir del nombre del tipo como String
     * 
     * @param tipoStr Nombre del tipo ("2D", "3D", "IMAX")
     * @param codigo Código único de la película
     * @param titulo Título de la película
     * @param director Director de la película
     * @param anioEstreno Año de estreno
     * @param duracionMinutos Duración en minutos
     * @param genero Género de la película
     * @param sinopsis Sinopsis breve
     * @return Instancia de Pelicula según el tipo
     */
    public static Pelicula crearPelicula(String tipoStr, String codigo, String titulo,
                                         String director, int anioEstreno, int duracionMinutos,
                                         Genero genero, String sinopsis) {
        TipoProyeccion tipo = parseTipo(tipoStr);
        return crearPelicula(tipo, codigo, titulo, director, anioEstreno, 
                           duracionMinutos, genero, sinopsis);
    }
    
    /**
     * Convierte un String a TipoProyeccion
     */
    private static TipoProyeccion parseTipo(String tipoStr) {
        if (tipoStr.equals("2D")) return TipoProyeccion.DOS_D;
        if (tipoStr.equals("3D")) return TipoProyeccion.TRES_D;
        if (tipoStr.equalsIgnoreCase("IMAX")) return TipoProyeccion.IMAX;
        throw new IllegalArgumentException("Tipo de proyección no válido: " + tipoStr);
    }
}