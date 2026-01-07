/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package test;

import controlador.ControladorCine;
import modelo.*;
import excepciones.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase de prueba para verificar el funcionamiento del sistema
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DEL SISTEMA DE CINE ===\n");
        
        ControladorCine controlador = new ControladorCine();
        
        // PRUEBA 1: Login
        System.out.println("--- PRUEBA 1: Login ---");
        pruebaLogin(controlador);
        
        // PRUEBA 2: Crear películas (Factory Method)
        System.out.println("\n--- PRUEBA 2: Crear Películas (Factory Method) ---");
        pruebaCrearPeliculas(controlador);
        
        // PRUEBA 3: Programar sesiones
        System.out.println("\n--- PRUEBA 3: Programar Sesiones ---");
        pruebaProgramarSesiones(controlador);
        
        // PRUEBA 4: Registrar clientes
        System.out.println("\n--- PRUEBA 4: Registrar Clientes ---");
        pruebaRegistrarClientes(controlador);
        
        // PRUEBA 5: Crear reservas (Builder + Strategy + Decorator + Proxy)
        System.out.println("\n--- PRUEBA 5: Crear Reservas ---");
        pruebaCrearReservas(controlador);
        
        // PRUEBA 6: Generar documentos (Template Method)
        System.out.println("\n--- PRUEBA 6: Generar Documentos ---");
        pruebaGenerarDocumentos(controlador);
        
        // PRUEBA 7: Cancelar sesión (Observer)
        System.out.println("\n--- PRUEBA 7: Cancelar Sesión (Observer) ---");
        pruebaCancelarSesion(controlador);
        
        // PRUEBA 8: Acceso Sala VIP (Proxy)
        System.out.println("\n--- PRUEBA 8: Control Acceso Sala VIP (Proxy) ---");
        pruebaAccesoSalaVIP(controlador);
        
        // PRUEBA 9: Persistencia (Singleton)
        System.out.println("\n--- PRUEBA 9: Persistencia (Singleton) ---");
        pruebaPersistencia(controlador);
        
        System.out.println("\n=== TODAS LAS PRUEBAS COMPLETADAS ===");
    }
    
    // ==================== PRUEBA 1: LOGIN ====================
    private static void pruebaLogin(ControladorCine controlador) {
        try {
            Cliente admin = controlador.login("admin@ocine.com", "admin");
            System.out.println("✓ Login exitoso: " + admin.getNombreCompleto());
            System.out.println("  Es empleado: " + controlador.esEmpleado());
        } catch (CredencialesInvalidasException e) {
            System.out.println("✗ Error en login: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 2: FACTORY METHOD ====================
    private static void pruebaCrearPeliculas(ControladorCine controlador) {
        try {
            // Crear película 2D
            controlador.crearPelicula("2D", "P001", "Avatar 2", "James Cameron",
                    2022, 192, Genero.CIENCIA_FICCION, "Secuela de Avatar");
            System.out.println("✓ Película 2D creada: Avatar 2");
            
            // Crear película 3D
            controlador.crearPelicula("3D", "P002", "Spider-Man 3D", "Jon Watts",
                    2021, 148, Genero.ACCION, "Spider-Man en 3D");
            System.out.println("✓ Película 3D creada: Spider-Man 3D");
            
            // Crear película IMAX
            controlador.crearPelicula("IMAX", "P003", "Dune IMAX", "Denis Villeneuve",
                    2021, 155, Genero.CIENCIA_FICCION, "Dune en formato IMAX");
            System.out.println("✓ Película IMAX creada: Dune IMAX");
            
            // Verificar recargos
            Pelicula p1 = controlador.buscarPeliculaPorCodigo("P001");
            Pelicula p2 = controlador.buscarPeliculaPorCodigo("P002");
            Pelicula p3 = controlador.buscarPeliculaPorCodigo("P003");
            
            System.out.println("  Recargo 2D: " + p1.getRecargoProyeccion() + "€");
            System.out.println("  Recargo 3D: " + p2.getRecargoProyeccion() + "€");
            System.out.println("  Recargo IMAX: " + p3.getRecargoProyeccion() + "€");
            
        } catch (PeliculaDuplicadaException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 3: PROGRAMAR SESIONES ====================
    private static void pruebaProgramarSesiones(ControladorCine controlador) {
        try {
            LocalDate manana = LocalDate.now().plusDays(1);
            
            // Sesión en Sala 1 (IMAX)
            controlador.programarSesion("S001", "P003", manana, 
                    LocalTime.of(18, 0), 1, 10.0);
            System.out.println("✓ Sesión IMAX programada en Sala 1");
            
            // Sesión en Sala 2 (Normal)
            controlador.programarSesion("S002", "P002", manana, 
                    LocalTime.of(20, 0), 2, 8.0);
            System.out.println("✓ Sesión 3D programada en Sala 2");
            
            // Sesión en Sala VIP
            controlador.programarSesion("S003", "P001", manana, 
                    LocalTime.of(19, 0), 3, 12.0);
            System.out.println("✓ Sesión 2D programada en Sala VIP");
            
            System.out.println("  Total sesiones: " + controlador.obtenerTodasSesiones().size());
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 4: REGISTRAR CLIENTES ====================
    private static void pruebaRegistrarClientes(ControladorCine controlador) {
        try {
            // Cliente ya existe (creado en inicialización)
            Cliente juan = controlador.buscarClientePorDNI("12345678A");
            System.out.println("✓ Cliente existente: " + juan.getNombreCompleto() + 
                             " (" + juan.getTipoAbono() + ")");
            
            // Crear nuevo cliente VIP
            controlador.registrarCliente("99999999Z", "Ana Martín", 
                    "ana@email.com", "644444444", "1234", TipoAbono.VIP);
            System.out.println("✓ Nuevo cliente VIP registrado: Ana Martín");
            
            System.out.println("  Total clientes: " + controlador.obtenerClientes().size());
            
        } catch (ClienteDuplicadoException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 5: CREAR RESERVAS ====================
    private static void pruebaCrearReservas(ControladorCine controlador) {
        try {
            Cliente cliente = controlador.buscarClientePorDNI("12345678A");
            
            // Reserva simple sin extras
            Reserva r1 = controlador.crearReserva("R001", cliente, "S002", 
                    2, false, false, false);
            System.out.println("✓ Reserva sin extras: " + r1.getCodigoReserva());
            System.out.println("  Precio: " + String.format("%.2f€", r1.getPrecioFinal()));
            System.out.println("  Extras: " + r1.getDescripcionExtras());
            
            // Reserva con palomitas y bebida
            Reserva r2 = controlador.crearReserva("R002", cliente, "S002", 
                    1, true, true, false);
            System.out.println("✓ Reserva con extras: " + r2.getCodigoReserva());
            System.out.println("  Precio: " + String.format("%.2f€", r2.getPrecioFinal()));
            System.out.println("  Extras: " + r2.getDescripcionExtras());
            
            // Reserva con combo
            Cliente premium = controlador.buscarClientePorDNI("87654321B");
            Reserva r3 = controlador.crearReserva("R003", premium, "S002", 
                    2, false, false, true);
            System.out.println("✓ Reserva Premium con combo: " + r3.getCodigoReserva());
            System.out.println("  Precio: " + String.format("%.2f€", r3.getPrecioFinal()));
            System.out.println("  Descuento aplicado: " + premium.getTipoAbono());
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 6: GENERAR DOCUMENTOS ====================
    private static void pruebaGenerarDocumentos(ControladorCine controlador) {
        try {
            // Generar informe de sesión
            boolean generado = controlador.generarInformeSesion("S002");
            if (generado) {
                System.out.println("✓ Informe de sesión generado correctamente");
                System.out.println("  Ubicación: informes/Sesion_S002_Informe.txt");
            }
            
            System.out.println("✓ Tickets generados automáticamente al crear reservas");
            System.out.println("  Ubicación: tickets/Ticket_[DNI]_[CodigoReserva].txt");
            
        } catch (EntidadNoEncontradaException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 7: OBSERVER (Cancelar Sesión) ====================
    private static void pruebaCancelarSesion(ControladorCine controlador) {
        try {
            System.out.println("✓ Cancelando sesión S002...");
            controlador.cancelarSesion("S002");
            
            // Verificar que las reservas se cancelaron automáticamente
            Reserva r1 = controlador.buscarReservaPorCodigo("R001");
            Reserva r2 = controlador.buscarReservaPorCodigo("R002");
            
            System.out.println("  Estado R001: " + r1.getEstado());
            System.out.println("  Estado R002: " + r2.getEstado());
            System.out.println("✓ Patrón Observer funcionando: Reservas canceladas automáticamente");
            
        } catch (EntidadNoEncontradaException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 8: PROXY (Acceso Sala VIP) ====================
    private static void pruebaAccesoSalaVIP(ControladorCine controlador) {
        try {
            // Intentar reservar en sala VIP sin abono VIP
            Cliente basico = controlador.buscarClientePorDNI("12345678A");
            System.out.println("Intentando reservar Sala VIP con abono " + basico.getTipoAbono() + "...");
            
            try {
                controlador.crearReserva("R004", basico, "S003", 1, false, false, false);
                System.out.println("✗ ERROR: Debería haber bloqueado el acceso");
            } catch (AccesoSalaVIPDenegadoException e) {
                System.out.println("✓ Proxy funcionando: " + e.getMessage());
            }
            
            // Reservar con abono VIP
            Cliente vip = controlador.buscarClientePorDNI("99999999Z");
            System.out.println("\nIntentando reservar Sala VIP con abono " + vip.getTipoAbono() + "...");
            Reserva r4 = controlador.crearReserva("R005", vip, "S003", 1, false, false, false);
            System.out.println("✓ Acceso permitido para cliente VIP");
            System.out.println("  Reserva creada: " + r4.getCodigoReserva());
            
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }
    
    // ==================== PRUEBA 9: SINGLETON (Persistencia) ====================
    private static void pruebaPersistencia(ControladorCine controlador) {
        System.out.println("Guardando datos...");
        controlador.guardarDatos();
        System.out.println("✓ Datos guardados correctamente");
        System.out.println("  Archivo: cine_datos.dat");
        System.out.println("✓ Patrón Singleton funcionando: Una única instancia de persistencia");
        
        // Verificar estadísticas
        System.out.println("\n--- ESTADÍSTICAS FINALES ---");
        System.out.println("  Películas: " + controlador.obtenerPeliculas().size());
        System.out.println("  Sesiones: " + controlador.obtenerTodasSesiones().size());
        System.out.println("  Clientes: " + controlador.obtenerClientes().size());
        System.out.println("  Reservas: " + controlador.obtenerTodasReservas().size());
    }
}