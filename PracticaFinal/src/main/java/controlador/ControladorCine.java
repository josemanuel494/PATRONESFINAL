/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package controlador;

import modelo.*;
import patrones.*;
import excepciones.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador principal del sistema de gestión de cine
 * Maneja toda la lógica de negocio y coordina los patrones
 */
public class ControladorCine {
    
    private GestorPersistencia gestor;
    private DatosCine datos;
    private Cliente usuarioActual;
    
    /**
     * Constructor - Inicializa el sistema
     */
    public ControladorCine() {
        this.gestor = GestorPersistencia.getInstance();
        
        // Intentar cargar datos existentes
        if (!gestor.cargarDatos()) {
            // Primera ejecución: inicializar datos de prueba
            gestor.inicializarDatosPrueba();
            gestor.guardarDatos();
        }
        
        this.datos = gestor.getDatos();
    }
    
    // ==================== AUTENTICACIÓN ====================
    
    /**
     * Inicia sesión con email y contraseña
     */
    public Cliente login(String email, String contrasena) throws CredencialesInvalidasException {
        Cliente cliente = datos.getClientes().stream()
            .filter(c -> c.getEmail().equals(email) && c.verificarContrasena(contrasena))
            .findFirst()
            .orElseThrow(() -> new CredencialesInvalidasException());
        
        this.usuarioActual = cliente;
        return cliente;
    }
    
    /**
     * Cierra la sesión actual
     */
    public void logout() {
        this.usuarioActual = null;
    }
    
    /**
     * Verifica si el usuario actual es empleado (admin)
     */
    public boolean esEmpleado() {
        return usuarioActual != null && 
               usuarioActual.getEmail().equals("admin@ocine.com");
    }
    
    // ==================== GESTIÓN DE PELÍCULAS ====================
    
    /**
     * Crea una nueva película usando Factory Method
     */
    public void crearPelicula(String tipo, String codigo, String titulo, String director,
                             int anio, int duracion, Genero genero, String sinopsis) 
            throws PeliculaDuplicadaException {
        
        // Verificar que no exista el código
        if (buscarPeliculaPorCodigo(codigo) != null) {
            throw new PeliculaDuplicadaException(codigo);
        }
        
        // Usar Factory Method para crear la película
        Pelicula pelicula = PeliculaFactory.crearPelicula(
            tipo, codigo, titulo, director, anio, duracion, genero, sinopsis
        );
        
        datos.getPeliculas().add(pelicula);
        gestor.guardarDatos();
    }
    
    /**
     * Elimina una película
     */
    public void eliminarPelicula(String codigo) throws EntidadNoEncontradaException {
        Pelicula pelicula = buscarPeliculaPorCodigo(codigo);
        if (pelicula == null) {
            throw new EntidadNoEncontradaException("Película", codigo);
        }
        
        datos.getPeliculas().remove(pelicula);
        gestor.guardarDatos();
    }
    
    /**
     * Busca película por código
     */
    public Pelicula buscarPeliculaPorCodigo(String codigo) {
        return datos.getPeliculas().stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene todas las películas
     */
    public List<Pelicula> obtenerPeliculas() {
        return new ArrayList<>(datos.getPeliculas());
    }
    
    /**
     * Busca películas por género
     */
    public List<Pelicula> buscarPeliculasPorGenero(Genero genero) {
        return datos.getPeliculas().stream()
            .filter(p -> p.getGenero() == genero)
            .collect(Collectors.toList());
    }
    
    // ==================== GESTIÓN DE SESIONES ====================
    
    /**
     * Programa una nueva sesión
     */
    public void programarSesion(String codigoSesion, String codigoPelicula, 
                               LocalDate fecha, LocalTime hora, int numeroSala, 
                               double precioBase) 
            throws Exception {
        
        // Buscar película
        Pelicula pelicula = buscarPeliculaPorCodigo(codigoPelicula);
        if (pelicula == null) {
            throw new EntidadNoEncontradaException("Película", codigoPelicula);
        }
        
        // Buscar sala
        Sala sala = buscarSalaPorNumero(numeroSala);
        if (sala == null) {
            throw new EntidadNoEncontradaException("Sala", String.valueOf(numeroSala));
        }
        
        // Verificar compatibilidad IMAX
        if (!pelicula.esCompatibleConSala(sala)) {
            throw new SalaNoCompatibleIMAXException(numeroSala);
        }
        
        // Verificar que la sala no esté ocupada
        if (salaOcupada(sala, fecha, hora, pelicula.getDuracionMinutos())) {
            throw new SalaOcupadaException(
                "La sala " + numeroSala + " ya tiene una sesión programada en ese horario"
            );
        }
        
        // Crear sesión
        Sesion sesion = new Sesion(codigoSesion, pelicula, fecha, hora, sala, precioBase);
        datos.getSesiones().add(sesion);
        gestor.guardarDatos();
    }
    
    /**
     * Verifica si una sala está ocupada en un horario
     */
    private boolean salaOcupada(Sala sala, LocalDate fecha, LocalTime hora, int duracion) {
        LocalTime horaFin = hora.plusMinutes(duracion + 15); // +15 min limpieza
        
        return datos.getSesiones().stream()
            .filter(s -> s.getSala().equals(sala) && s.getFecha().equals(fecha))
            .filter(s -> s.getEstado() == EstadoSesion.PROGRAMADA)
            .anyMatch(s -> {
                LocalTime inicioExistente = s.getHoraInicio();
                LocalTime finExistente = inicioExistente.plusMinutes(
                    s.getPelicula().getDuracionMinutos() + 15
                );
                return !(horaFin.isBefore(inicioExistente) || hora.isAfter(finExistente));
            });
    }
    
    /**
     * Cancela una sesión
     */
    public void cancelarSesion(String codigoSesion) throws EntidadNoEncontradaException {
        Sesion sesion = buscarSesionPorCodigo(codigoSesion);
        if (sesion == null) {
            throw new EntidadNoEncontradaException("Sesión", codigoSesion);
        }
        
        sesion.cancelar();
        gestor.guardarDatos();
    }
    
    /**
     * Busca sesión por código
     */
    public Sesion buscarSesionPorCodigo(String codigo) {
        return datos.getSesiones().stream()
            .filter(s -> s.getCodigoSesion().equals(codigo))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene sesiones disponibles (para cartelera)
     */
    public List<Sesion> obtenerSesionesDisponibles() {
        LocalDate hoy = LocalDate.now();
        return datos.getSesiones().stream()
            .filter(s -> s.getFecha().isAfter(hoy) || s.getFecha().isEqual(hoy))
            .filter(s -> s.getEstado() == EstadoSesion.PROGRAMADA)
            .filter(s -> s.getPlazasDisponibles() > 0)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las sesiones
     */
    public List<Sesion> obtenerTodasSesiones() {
        return new ArrayList<>(datos.getSesiones());
    }
    
// ==================== GESTIÓN DE CLIENTES ====================
    
    /**
     * Registra un nuevo cliente
     */
    public void registrarCliente(String dni, String nombre, String email, 
                                String telefono, String contrasena, TipoAbono tipoAbono) 
            throws ClienteDuplicadoException {
        
        // Verificar DNI duplicado
        if (buscarClientePorDNI(dni) != null) {
            throw new ClienteDuplicadoException(dni);
        }
        
        String contrasenaHasheada = Cliente.hashContrasena(contrasena);
        Cliente cliente = new Cliente(dni, nombre, email, telefono, contrasenaHasheada, tipoAbono);
        datos.getClientes().add(cliente);
        gestor.guardarDatos();
    }
    
    /**
     * Busca cliente por DNI
     */
    public Cliente buscarClientePorDNI(String dni) {
        return datos.getClientes().stream()
            .filter(c -> c.getDni().equals(dni))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene todos los clientes
     */
    public List<Cliente> obtenerClientes() {
        return new ArrayList<>(datos.getClientes());
    }
    
    /**
     * Elimina un cliente
     */
    public void eliminarCliente(String dni) throws EntidadNoEncontradaException {
        Cliente cliente = buscarClientePorDNI(dni);
        if (cliente == null) {
            throw new EntidadNoEncontradaException("Cliente", dni);
        }
        
        datos.getClientes().remove(cliente);
        gestor.guardarDatos();
    }
    
    // ==================== GESTIÓN DE RESERVAS ====================
    
    /**
     * Crea una nueva reserva usando Builder y Proxy
     */
    public Reserva crearReserva(String codigoReserva, Cliente cliente, String codigoSesion,
                               int numEntradas, boolean palomitas, boolean bebida, boolean combo)
            throws Exception {
        
        // Buscar sesión
        Sesion sesion = buscarSesionPorCodigo(codigoSesion);
        if (sesion == null) {
            throw new EntidadNoEncontradaException("Sesión", codigoSesion);
        }
        
        // Verificar que la sesión esté disponible
        if (sesion.getEstado() != EstadoSesion.PROGRAMADA) {
            throw new SesionNoDisponibleException(
                "La sesión está " + sesion.getEstado()
            );
        }
        
        // Verificar plazas disponibles
        if (sesion.getPlazasDisponibles() < numEntradas) {
            throw new SinPlazasDisponiblesException(sesion.toString());
        }
        
        // Usar Proxy para verificar acceso a sala VIP
        ISala salaProxy = SalaFactory.crearSala(sesion.getSala());
        salaProxy.verificarAcceso(cliente);
        
        // Reservar plazas
        if (!sesion.reservarPlazas(numEntradas)) {
            throw new SinPlazasDisponiblesException(sesion.toString());
        }
        
        // Usar Builder con Decorator para crear la reserva
        ReservaBuilder builder = new ReservaBuilder(codigoReserva, cliente, sesion, numEntradas);
        
        if (palomitas) builder.agregarPalomitas();
        if (bebida) builder.agregarBebida();
        if (combo) builder.agregarCombo();
        
        Reserva reserva = builder.build();
        reserva.confirmar();
        
        datos.getReservas().add(reserva);
        gestor.guardarDatos();
        
        // Generar ticket
        GeneradorTicket generador = new GeneradorTicket(reserva);
        generador.generar();
        
        return reserva;
    }
    
    /**
     * Cancela una reserva
     */
    public void cancelarReserva(String codigoReserva) throws Exception {
        Reserva reserva = buscarReservaPorCodigo(codigoReserva);
        if (reserva == null) {
            throw new EntidadNoEncontradaException("Reserva", codigoReserva);
        }
        
        // Verificar plazo de 2 horas
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioSesion = LocalDateTime.of(
            reserva.getSesion().getFecha(),
            reserva.getSesion().getHoraInicio()
        );
        
        if (Duration.between(ahora, inicioSesion).toHours() < 2) {
            throw new CancelacionFueraDePlazaException();
        }
        
        reserva.cancelar();
        gestor.guardarDatos();
    }
    
    /**
     * Busca reserva por código
     */
    public Reserva buscarReservaPorCodigo(String codigo) {
        return datos.getReservas().stream()
            .filter(r -> r.getCodigoReserva().equals(codigo))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene reservas de un cliente
     */
    public List<Reserva> obtenerReservasCliente(Cliente cliente) {
        return datos.getReservas().stream()
            .filter(r -> r.getCliente().equals(cliente))
            .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las reservas
     */
    public List<Reserva> obtenerTodasReservas() {
        return new ArrayList<>(datos.getReservas());
    }
    
    // ==================== GENERACIÓN DE INFORMES ====================
    
    /**
     * Genera informe de una sesión
     */
    public boolean generarInformeSesion(String codigoSesion) throws EntidadNoEncontradaException {
        Sesion sesion = buscarSesionPorCodigo(codigoSesion);
        if (sesion == null) {
            throw new EntidadNoEncontradaException("Sesión", codigoSesion);
        }
        
        // Calcular ingresos totales
        double ingresos = datos.getReservas().stream()
            .filter(r -> r.getSesion().equals(sesion))
            .filter(r -> r.getEstado() == EstadoReserva.CONFIRMADA)
            .mapToDouble(Reserva::getPrecioFinal)
            .sum();
        
        GeneradorInforme generador = new GeneradorInforme(sesion, ingresos);
        return generador.generar();
    }
    
    // ==================== UTILIDADES ====================
    
    /**
     * Busca sala por número
     */
    private Sala buscarSalaPorNumero(int numero) {
        return datos.getSalas().stream()
            .filter(s -> s.getNumero() == numero)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene las salas del cine
     */
    public List<Sala> obtenerSalas() {
        return new ArrayList<>(datos.getSalas());
    }
    
    /**
     * Obtiene el usuario actual
     */
    public Cliente getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Guarda los datos manualmente
     */
    public void guardarDatos() {
        gestor.guardarDatos();
    }
}
