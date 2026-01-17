/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.empleado;

import controlador.ControladorCine;
import javax.swing.table.DefaultTableModel;
import modelo.Pelicula;
import excepciones.EntidadNoEncontradaException;
import javax.swing.JOptionPane;
import modelo.Sesion;

public class EmpleadoFrame extends javax.swing.JFrame {
    private final ControladorCine controlador;
    private static final java.time.format.DateTimeFormatter F_ES =
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-uuuu");
    private static final java.time.format.DateTimeFormatter H_ES =
        java.time.format.DateTimeFormatter.ofPattern("HH:mm");
    private static final java.time.format.DateTimeFormatter DT_ES =
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm");
    private javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorterPeliculas;
    private javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorterSesiones;
    private javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorterClientes;
    private javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorterReservas;
    
    
    public EmpleadoFrame(ControladorCine controlador) {
        this.controlador = controlador;
        initComponents();
        getContentPane().setPreferredSize(new java.awt.Dimension(849, 564));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        setTitle("OCine - Empleado");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                volverALogin();
            }
        });
        
        configurarFiltrosPeliculas(); 
        configurarFiltrosSesiones();
        
        cargarTablaPeliculas();
        cargarTablaSesiones();
        
        configurarFiltrosClientes();
        cargarTablaClientes();
        
        configurarFiltrosReservas();
        cargarTablaReservas();
        
        configurarInformes();

    }
    
    private void volverALogin() {
        int resp = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "¿Cerrar sesión y volver al login?",
                "Salir",
                javax.swing.JOptionPane.YES_NO_OPTION
        );
        if (resp != javax.swing.JOptionPane.YES_OPTION) return;

        controlador.logout();

        ui.Login login = new ui.Login(controlador);
        login.setVisible(true);

        dispose();
    }

    
    private void cargarTablaPeliculas() {
        String[] cols = {"Código", "Título", "Director", "Año", "Duración", "Género", "Tipo"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // tabla solo lectura
            }
        };

        for (Pelicula p : controlador.obtenerPeliculas()) {
            Object[] row = {
                p.getCodigo(),
                p.getTitulo(),
                p.getDirector(),
                p.getAnioEstreno(),
                p.getDuracionMinutos(),
                p.getGenero(),
                p.getClass().getSimpleName() // Pelicula2D / Pelicula3D / PeliculaIMAX
            };
            model.addRow(row);
        }

        tblPeliculas.setModel(model);

        sorterPeliculas = new javax.swing.table.TableRowSorter<>(model);
        tblPeliculas.setRowSorter(sorterPeliculas);

        sorterPeliculas.setComparator(3, java.util.Comparator.comparingInt(a -> (Integer) a)); // Año
        sorterPeliculas.setComparator(4, java.util.Comparator.comparingInt(a -> (Integer) a)); // Duración

        aplicarFiltrosPeliculas();
    }
    
    private void configurarFiltrosPeliculas() {
        cmbCampoBuscar.removeAllItems();
        cmbCampoBuscar.addItem("Título");
        cmbCampoBuscar.addItem("Director");
        cmbCampoBuscar.addItem("Ambos");

        cmbGeneroFiltro.removeAllItems();
        cmbGeneroFiltro.addItem("TODOS");
        for (modelo.Genero g : modelo.Genero.values()) {
            cmbGeneroFiltro.addItem(g.toString());
        }

        // Listener para filtrar al escribir
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosPeliculas(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosPeliculas(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosPeliculas(); }
        });
    }
    
    private void aplicarFiltrosPeliculas() {
        if (sorterPeliculas == null) return;

        java.util.List<javax.swing.RowFilter<Object, Object>> filtros = new java.util.ArrayList<>();

        // Filtro por género
        String genSel = (String) cmbGeneroFiltro.getSelectedItem();
        if (genSel != null && !genSel.equals("TODOS")) {
            int colGenero = 5;
            filtros.add(new javax.swing.RowFilter<Object, Object>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    String valor = entry.getStringValue(colGenero);
                    return genSel.equalsIgnoreCase(valor);
                }
            });
        }

        // Filtro por texto
        String q = txtBuscar.getText().trim();
        if (!q.isEmpty()) {
            String campo = (String) cmbCampoBuscar.getSelectedItem();
            String patron = "(?i)" + java.util.regex.Pattern.quote(q); // contains, case-insensitive

            int colTitulo = 1;   // <- AJUSTA si cambia
            int colDirector = 2; // <- AJUSTA si cambia

            if ("Título".equals(campo)) {
                filtros.add(javax.swing.RowFilter.regexFilter(patron, colTitulo));
            } else if ("Director".equals(campo)) {
                filtros.add(javax.swing.RowFilter.regexFilter(patron, colDirector));
            } else { // "Título o Director"
                java.util.List<javax.swing.RowFilter<Object, Object>> or = new java.util.ArrayList<>();
                or.add(javax.swing.RowFilter.regexFilter(patron, colTitulo));
                or.add(javax.swing.RowFilter.regexFilter(patron, colDirector));
                filtros.add(javax.swing.RowFilter.orFilter(or));
            }
        }

        // Aplicar (AND) o limpiar
        if (filtros.isEmpty()) {
            sorterPeliculas.setRowFilter(null);
        } else {
            sorterPeliculas.setRowFilter(javax.swing.RowFilter.andFilter(filtros));
        }
    }
    //-------------------------------------
    private void cargarTablaSesiones() {
        String[] cols = {"Código", "Película", "Fecha", "Hora", "Sala", "Precio base", "Plazas disp.", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        for (Sesion s : controlador.obtenerTodasSesiones()) {
            Object[] row = {
                s.getCodigoSesion(),
                s.getPelicula().getTitulo(),
                s.getFecha(),            
                s.getHoraInicio(),       
                s.getSala().getNumero(), 
                s.getPrecioBase(),       
                s.getPlazasDisponibles(),
                s.getEstado()
            };
            model.addRow(row);
        }

        tblSesiones.setModel(model);
        
        sorterSesiones = new javax.swing.table.TableRowSorter<>(model);
        tblSesiones.setRowSorter(sorterSesiones);
        
        // Formatear Fecha a dd-mm-aaaa
        tblSesiones.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof java.time.LocalDate ld) {
                    setText(ld.format(F_ES));
                } else {
                    super.setValue(value);
                }
            }
        });

        aplicarFiltrosSesiones();
    }
    
    private void configurarFiltrosSesiones() {
        cmbSalaFiltroSesiones.removeAllItems();
        cmbSalaFiltroSesiones.addItem("TODAS");
        cmbSalaFiltroSesiones.addItem("1");
        cmbSalaFiltroSesiones.addItem("2");
        cmbSalaFiltroSesiones.addItem("3");

        txtFechaFiltroSesiones.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosSesiones(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosSesiones(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosSesiones(); }
        });
    }
    
    private void aplicarFiltrosSesiones() {
        if (sorterSesiones == null) return;

        java.util.List<javax.swing.RowFilter<Object, Object>> filtros = new java.util.ArrayList<>();

        int colFecha = 2; 
        int colSala  = 4; 
        int colEstado= 7; 

        String fechaTxt = txtFechaFiltroSesiones.getText().trim();
        if (!fechaTxt.isEmpty()) {
            try {
                java.time.LocalDate f = java.time.LocalDate.parse(fechaTxt, F_ES);

                filtros.add(new javax.swing.RowFilter<Object, Object>() {
                    @Override
                    public boolean include(Entry<?, ?> entry) {
                        Object v = entry.getValue(colFecha);
                        return (v instanceof java.time.LocalDate ld) && ld.equals(f);
                    }
                });
            } catch (Exception ex) {
            }
        }


        // Sala
        String salaSel = (String) cmbSalaFiltroSesiones.getSelectedItem();
        if (salaSel != null && !salaSel.equals("TODAS")) {
            filtros.add(javax.swing.RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(salaSel) + "$", colSala));
        }

        // Solo programadas
        if (chkSoloProgramadas.isSelected()) {
            filtros.add(new javax.swing.RowFilter<Object, Object>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    Object v = entry.getValue(colEstado);

                    if (v instanceof modelo.EstadoSesion es) {
                        return es == modelo.EstadoSesion.PROGRAMADA;
                    }

                    return "PROGRAMADA".equalsIgnoreCase(String.valueOf(v).trim());
                }
            });
        }


        sorterSesiones.setRowFilter(filtros.isEmpty() ? null : javax.swing.RowFilter.andFilter(filtros));
    }
    //-------------------------------------
    private void cargarTablaClientes() {
        String[] cols = {"DNI", "Nombre", "Email", "Teléfono", "Abono"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        for (modelo.Cliente c : controlador.obtenerClientes()) {
            Object[] row = {
                c.getDni(),
                c.getNombreCompleto(),
                c.getEmail(),
                c.getTelefono(),
                c.getTipoAbono()
            };
            model.addRow(row);
        }

        tblClientes.setModel(model);
        sorterClientes = new javax.swing.table.TableRowSorter<>(model);
        tblClientes.setRowSorter(sorterClientes);

        aplicarFiltrosClientes();
    }
    
    private void configurarFiltrosClientes() {
        txtBuscarCliente.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosClientes(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosClientes(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosClientes(); }
        });
    }

    private void aplicarFiltrosClientes() {
        if (sorterClientes == null) return;

        String q = txtBuscarCliente.getText().trim();
        if (q.isEmpty()) {
            sorterClientes.setRowFilter(null);
            return;
        }

        String campo = (String) cmbCampoCliente.getSelectedItem();
        int col;

        if ("DNI".equalsIgnoreCase(campo)) col = 0;
        else if ("Nombre".equalsIgnoreCase(campo)) col = 1;
        else col = 2; // Email

        String patron = "(?i)" + java.util.regex.Pattern.quote(q); // contiene, sin distinguir mayúsculas

        sorterClientes.setRowFilter(javax.swing.RowFilter.regexFilter(patron, col));
    }
    //-------------------------------------
    private void cargarTablaReservas() {
        String[] cols = {"Código", "Cliente", "DNI", "Sesión", "Fecha/Hora", "Entradas", "Extras", "Precio", "Estado"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        for (modelo.Reserva r : controlador.obtenerTodasReservas()) {
            Object[] row = {
                r.getCodigoReserva(),
                r.getCliente().getNombreCompleto(),
                r.getCliente().getDni(),
                r.getSesion().getCodigoSesion(),
                r.getFechaHoraReserva(),
                r.getNumEntradas(),
                r.getDescripcionExtras(),
                r.getPrecioFinal(),
                r.getEstado()
            };
            model.addRow(row);
        }

        tblReservas.setModel(model);

        sorterReservas = new javax.swing.table.TableRowSorter<>(model);
        
        // Fecha/Hora en formato ES
        tblReservas.setRowSorter(sorterReservas);
        tblReservas.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof java.time.LocalDateTime ldt) setText(ldt.format(DT_ES));
                else super.setValue(value);
            }
        });
        
        // Precio con formato
        java.text.NumberFormat eur = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "ES"));
        tblReservas.getColumnModel().getColumn(7).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number n) {
                    setText(eur.format(n.doubleValue()));
                } else {
                    super.setValue(value);
                }
            }
        });

        aplicarFiltrosReservas();
    }
    
    private void configurarFiltrosReservas() {
        txtBuscarReserva.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosReservas(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosReservas(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltrosReservas(); }
        });
    }
    
    private void aplicarFiltrosReservas() {
        if (sorterReservas == null) return;

        java.util.List<javax.swing.RowFilter<Object,Object>> filtros = new java.util.ArrayList<>();

        String q = txtBuscarReserva.getText().trim();
        if (!q.isEmpty()) {
            String campo = (String) cmbCampoReserva.getSelectedItem();

            int col;
            if ("Código".equalsIgnoreCase(campo)) col = 0;
            else if ("DNI Cliente".equalsIgnoreCase(campo)) col = 2;
            else if ("Email Cliente".equalsIgnoreCase(campo)) {
                col = 1;
            }
            else col = 3; // Código Sesión

            String patron = "(?i)" + java.util.regex.Pattern.quote(q);
            filtros.add(javax.swing.RowFilter.regexFilter(patron, col));
        }

        // Solo confirmadas
        if (chkSoloConfirmadas.isSelected()) {
            int colEstado = 8;
            filtros.add(new javax.swing.RowFilter<Object, Object>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    Object v = entry.getValue(colEstado);

                    if (v instanceof modelo.EstadoReserva er) {
                        return er == modelo.EstadoReserva.CONFIRMADA;
                    }

                    return "CONFIRMADA".equalsIgnoreCase(String.valueOf(v).trim());
                }
            });
        }

        sorterReservas.setRowFilter(filtros.isEmpty() ? null : javax.swing.RowFilter.andFilter(filtros));
    }
    //-------------------------------------
    private void configurarInformes() {
        recargarComboSesionesInforme();

        // combo muestra fecha/hora en formato ES
        cmbSesionInforme.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof modelo.Sesion s) {
                    setText(s.getCodigoSesion() + " - " + s.getPelicula().getTitulo()
                            + " - " + s.getFecha().format(F_ES) + " " + s.getHoraInicio().format(H_ES)
                            + " (Sala " + s.getSala().getNumero() + ")");
                }
                return this;
            }
        });
        
        actualizarVistaPreviaInforme();
        cmbSesionInforme.addActionListener(e -> actualizarVistaPreviaInforme());
    }
    
    private void recargarComboSesionesInforme() {
        javax.swing.DefaultComboBoxModel m = new javax.swing.DefaultComboBoxModel();
        for (modelo.Sesion s : controlador.obtenerTodasSesiones()) {
            m.addElement(s);
        }
        cmbSesionInforme.setModel(m);
    }

    private void actualizarVistaPreviaInforme() {
        Object sel = cmbSesionInforme.getSelectedItem();
        if (!(sel instanceof modelo.Sesion)) {
            txtVistaInforme.setText("Selecciona una sesión para ver el informe.");
            return;
        }
        modelo.Sesion s = (modelo.Sesion) sel;


        int vendidas = controlador.obtenerTodasReservas().stream()
                .filter(r -> r.getEstado() == modelo.EstadoReserva.CONFIRMADA)
                .filter(r -> r.getSesion().equals(s))
                .mapToInt(modelo.Reserva::getNumEntradas)
                .sum();

        double ingresos = controlador.obtenerTodasReservas().stream()
                .filter(r -> r.getEstado() == modelo.EstadoReserva.CONFIRMADA)
                .filter(r -> r.getSesion().equals(s))
                .mapToDouble(modelo.Reserva::getPrecioFinal)
                .sum();

        int aforo = s.getSala().getCapacidad();
        double ocupacion = (aforo == 0) ? 0 : (vendidas * 100.0 / aforo);

        java.text.NumberFormat eur = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "ES"));

        String texto = ""
                + "INFORME DE SESIÓN\n"
                + "=================\n"
                + "Código sesión: " + s.getCodigoSesion() + "\n"
                + "Película: " + s.getPelicula().getTitulo() + "\n"
                + "Fecha y hora: " + s.getFecha().format(F_ES) + " " + s.getHoraInicio().format(H_ES) + "\n"
                + "Sala: " + s.getSala().toString() + "\n\n"
                + "Aforo total: " + aforo + "\n"
                + "Entradas vendidas: " + vendidas + "\n"
                + "Porcentaje ocupación: " + String.format(java.util.Locale.US, "%.1f", ocupacion) + "%\n"
                + "Ingresos totales: " + eur.format(ingresos) + "\n";

        txtVistaInforme.setText(texto);
        txtVistaInforme.setCaretPosition(0);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabsEmpleado = new javax.swing.JTabbedPane();
        panelPeliculas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPeliculas = new javax.swing.JTable();
        panelFiltrosPeliculas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        cmbCampoBuscar = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbGeneroFiltro = new javax.swing.JComboBox<>();
        btnLimpiarFiltros = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnActualizarPeliculas = new javax.swing.JButton();
        btnAlta = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificarPelicula = new javax.swing.JButton();
        panelSesiones = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtFechaFiltroSesiones = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbSalaFiltroSesiones = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        chkSoloProgramadas = new javax.swing.JCheckBox();
        btnLimpiarFiltrosSesiones = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnActualizarSesiones = new javax.swing.JButton();
        btnProgramarSesion = new javax.swing.JButton();
        btnCancelarSesion = new javax.swing.JButton();
        btnOcupacionSesion = new javax.swing.JButton();
        JScrollPane2 = new javax.swing.JScrollPane();
        tblSesiones = new javax.swing.JTable();
        panelClientes = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtBuscarCliente = new javax.swing.JTextField();
        cmbCampoCliente = new javax.swing.JComboBox<>();
        btnLimpiarFiltroClientes = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnActualizarClientes = new javax.swing.JButton();
        btnAltaCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnModificarCliente = new javax.swing.JButton();
        pane1Clientes = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        panelReservas = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtBuscarReserva = new javax.swing.JTextField();
        cmbCampoReserva = new javax.swing.JComboBox<>();
        chkSoloConfirmadas = new javax.swing.JCheckBox();
        btnLimpiarFiltrosReservas = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnActualizarReservas = new javax.swing.JButton();
        btnCancelarReserva = new javax.swing.JButton();
        btnVerDetalleReserva = new javax.swing.JButton();
        paneReservas1 = new javax.swing.JScrollPane();
        tblReservas = new javax.swing.JTable();
        panelInformes = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cmbSesionInforme = new javax.swing.JComboBox();
        btnActualizarInformes = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        btnGenerarInforme = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtVistaInforme = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(1000, 650));

        tblPeliculas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Título", "Director", "Año", "Duración", "Género", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPeliculas);

        jLabel1.setText("Buscar");

        cmbCampoBuscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Título", "Director", "Ambos" }));
        cmbCampoBuscar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCampoBuscarItemStateChanged(evt);
            }
        });

        jLabel2.setText("Género");

        cmbGeneroFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbGeneroFiltro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbGeneroFiltroItemStateChanged(evt);
            }
        });

        btnLimpiarFiltros.setText("Limpiar");
        btnLimpiarFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarFiltrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFiltrosPeliculasLayout = new javax.swing.GroupLayout(panelFiltrosPeliculas);
        panelFiltrosPeliculas.setLayout(panelFiltrosPeliculasLayout);
        panelFiltrosPeliculasLayout.setHorizontalGroup(
            panelFiltrosPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosPeliculasLayout.createSequentialGroup()
                .addGroup(panelFiltrosPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFiltrosPeliculasLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbCampoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFiltrosPeliculasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(panelFiltrosPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(panelFiltrosPeliculasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cmbGeneroFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(btnLimpiarFiltros)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFiltrosPeliculasLayout.setVerticalGroup(
            panelFiltrosPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosPeliculasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltrosPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltrosPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCampoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbGeneroFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarFiltros))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setPreferredSize(new java.awt.Dimension(837, 61));

        btnActualizarPeliculas.setText("Actualizar");
        btnActualizarPeliculas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarPeliculasActionPerformed(evt);
            }
        });
        jPanel3.add(btnActualizarPeliculas);

        btnAlta.setText("Alta");
        btnAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaActionPerformed(evt);
            }
        });
        jPanel3.add(btnAlta);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel3.add(btnEliminar);

        btnModificarPelicula.setText("Modificar");
        btnModificarPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarPeliculaActionPerformed(evt);
            }
        });
        jPanel3.add(btnModificarPelicula);

        javax.swing.GroupLayout panelPeliculasLayout = new javax.swing.GroupLayout(panelPeliculas);
        panelPeliculas.setLayout(panelPeliculasLayout);
        panelPeliculasLayout.setHorizontalGroup(
            panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPeliculasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFiltrosPeliculas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPeliculasLayout.createSequentialGroup()
                        .addGroup(panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        panelPeliculasLayout.setVerticalGroup(
            panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPeliculasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFiltrosPeliculas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );

        tabsEmpleado.addTab("Películas", panelPeliculas);

        jPanel1.setPreferredSize(new java.awt.Dimension(694, 66));

        jLabel3.setText("Fecha (dd-mm-aaaa)");

        jLabel4.setText("Sala");

        cmbSalaFiltroSesiones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSalaFiltroSesiones.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSalaFiltroSesionesItemStateChanged(evt);
            }
        });

        chkSoloProgramadas.setText("Solo programadas");
        chkSoloProgramadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSoloProgramadasActionPerformed(evt);
            }
        });

        btnLimpiarFiltrosSesiones.setText("Limpiar");
        btnLimpiarFiltrosSesiones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarFiltrosSesionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtFechaFiltroSesiones, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cmbSalaFiltroSesiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkSoloProgramadas)
                        .addGap(57, 57, 57)
                        .addComponent(btnLimpiarFiltrosSesiones))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(317, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaFiltroSesiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSalaFiltroSesiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkSoloProgramadas)
                    .addComponent(btnLimpiarFiltrosSesiones))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btnActualizarSesiones.setText("Actualizar");
        btnActualizarSesiones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarSesionesActionPerformed(evt);
            }
        });
        jPanel2.add(btnActualizarSesiones);

        btnProgramarSesion.setText("Programar");
        btnProgramarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProgramarSesionActionPerformed(evt);
            }
        });
        jPanel2.add(btnProgramarSesion);

        btnCancelarSesion.setText("Cancelar");
        btnCancelarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarSesionActionPerformed(evt);
            }
        });
        jPanel2.add(btnCancelarSesion);

        btnOcupacionSesion.setText("Ocupación");
        btnOcupacionSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOcupacionSesionActionPerformed(evt);
            }
        });
        jPanel2.add(btnOcupacionSesion);

        tblSesiones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Película", "Fecha", "Hora", "Sala", "Precio", "Plazas Disp.", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        JScrollPane2.setViewportView(tblSesiones);

        javax.swing.GroupLayout panelSesionesLayout = new javax.swing.GroupLayout(panelSesiones);
        panelSesiones.setLayout(panelSesionesLayout);
        panelSesionesLayout.setHorizontalGroup(
            panelSesionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSesionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSesionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
                    .addComponent(JScrollPane2)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSesionesLayout.setVerticalGroup(
            panelSesionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSesionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabsEmpleado.addTab("Sesiones", panelSesiones);

        jLabel6.setText("Buscar");

        cmbCampoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "Nombre", "Email" }));
        cmbCampoCliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCampoClienteItemStateChanged(evt);
            }
        });

        btnLimpiarFiltroClientes.setText("Limpiar");
        btnLimpiarFiltroClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarFiltroClientesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbCampoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(btnLimpiarFiltroClientes))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCampoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarFiltroClientes))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        btnActualizarClientes.setText("Actualizar");
        btnActualizarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarClientesActionPerformed(evt);
            }
        });
        jPanel5.add(btnActualizarClientes);

        btnAltaCliente.setText("Alta");
        btnAltaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaClienteActionPerformed(evt);
            }
        });
        jPanel5.add(btnAltaCliente);

        btnEliminarCliente.setText("Eliminar");
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });
        jPanel5.add(btnEliminarCliente);

        btnModificarCliente.setText("Modificar");
        btnModificarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarClienteActionPerformed(evt);
            }
        });
        jPanel5.add(btnModificarCliente);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "DNI", "Nombre", "Email", "Teléfono", "Abono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pane1Clientes.setViewportView(tblClientes);

        javax.swing.GroupLayout panelClientesLayout = new javax.swing.GroupLayout(panelClientes);
        panelClientes.setLayout(panelClientesLayout);
        panelClientesLayout.setHorizontalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pane1Clientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelClientesLayout.setVerticalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pane1Clientes, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabsEmpleado.addTab("Clientes", panelClientes);

        jLabel7.setText("Buscar");

        cmbCampoReserva.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "DNI", "Email", "Código Sesión" }));

        chkSoloConfirmadas.setText("Solo confirmadas");
        chkSoloConfirmadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSoloConfirmadasActionPerformed(evt);
            }
        });

        btnLimpiarFiltrosReservas.setText("Limpiar");
        btnLimpiarFiltrosReservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarFiltrosReservasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtBuscarReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbCampoReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkSoloConfirmadas)
                        .addGap(68, 68, 68)
                        .addComponent(btnLimpiarFiltrosReservas))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(306, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCampoReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkSoloConfirmadas)
                    .addComponent(btnLimpiarFiltrosReservas))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        btnActualizarReservas.setText("Actualizar");
        btnActualizarReservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarReservasActionPerformed(evt);
            }
        });
        jPanel7.add(btnActualizarReservas);

        btnCancelarReserva.setText("Cancelar Reserva");
        btnCancelarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarReservaActionPerformed(evt);
            }
        });
        jPanel7.add(btnCancelarReserva);

        btnVerDetalleReserva.setText("Detalle");
        btnVerDetalleReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerDetalleReservaActionPerformed(evt);
            }
        });
        jPanel7.add(btnVerDetalleReserva);

        tblReservas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Cliente", "DNI", "Sesión", "Fecha/Hora", "Entradas", "Extras", "Precio", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paneReservas1.setViewportView(tblReservas);

        javax.swing.GroupLayout panelReservasLayout = new javax.swing.GroupLayout(panelReservas);
        panelReservas.setLayout(panelReservasLayout);
        panelReservasLayout.setHorizontalGroup(
            panelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReservasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paneReservas1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelReservasLayout.setVerticalGroup(
            panelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReservasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paneReservas1, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabsEmpleado.addTab("Reservas", panelReservas);

        panelInformes.setToolTipText("");
        panelInformes.setLayout(new java.awt.BorderLayout());

        jLabel8.setText("Sesión");

        cmbSesionInforme.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSesionInforme.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSesionInformeItemStateChanged(evt);
            }
        });

        btnActualizarInformes.setText("Actualizar");
        btnActualizarInformes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarInformesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cmbSesionInforme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizarInformes))
                    .addComponent(jLabel8))
                .addContainerGap(656, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSesionInforme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizarInformes))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        panelInformes.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        btnGenerarInforme.setText("Generar Informe");
        btnGenerarInforme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarInformeActionPerformed(evt);
            }
        });
        jPanel9.add(btnGenerarInforme);

        panelInformes.add(jPanel9, java.awt.BorderLayout.PAGE_END);

        txtVistaInforme.setEditable(false);
        txtVistaInforme.setColumns(20);
        txtVistaInforme.setRows(5);
        jScrollPane2.setViewportView(txtVistaInforme);

        panelInformes.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tabsEmpleado.addTab("Informes", panelInformes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabsEmpleado)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabsEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 453, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabsEmpleado.getAccessibleContext().setAccessibleName("tab1");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarPeliculasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarPeliculasActionPerformed
        cargarTablaPeliculas();
    }//GEN-LAST:event_btnActualizarPeliculasActionPerformed

    private void btnAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaActionPerformed
        AltaPeliculaDialog dlg = new AltaPeliculaDialog(this, true, controlador);
        dlg.setVisible(true);

        if (dlg.isCreada()) {
            cargarTablaPeliculas();
        }
    }//GEN-LAST:event_btnAltaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int viewRow = tblPeliculas.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una película en la tabla.", "Eliminar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblPeliculas.convertRowIndexToModel(viewRow);
        String codigo = tblPeliculas.getModel().getValueAt(modelRow, 0).toString(); // col 0 = Código

        int resp = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar la película con código: " + codigo + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (resp != JOptionPane.YES_OPTION) return;

        try {
            controlador.eliminarPelicula(codigo);
            cargarTablaPeliculas();
            JOptionPane.showMessageDialog(this, "Película eliminada.");
        } catch (EntidadNoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "No se encontró la película.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cmbCampoBuscarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCampoBuscarItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            aplicarFiltrosPeliculas();
        }
    }//GEN-LAST:event_cmbCampoBuscarItemStateChanged

    private void cmbGeneroFiltroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGeneroFiltroItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            aplicarFiltrosPeliculas();
        }
    }//GEN-LAST:event_cmbGeneroFiltroItemStateChanged

    private void btnLimpiarFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarFiltrosActionPerformed
        txtBuscar.setText("");
        cmbGeneroFiltro.setSelectedItem("TODOS");
        cmbCampoBuscar.setSelectedItem("Título o Director");
        aplicarFiltrosPeliculas();
    }//GEN-LAST:event_btnLimpiarFiltrosActionPerformed
//-------------------------------------
    private void cmbSalaFiltroSesionesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSalaFiltroSesionesItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) aplicarFiltrosSesiones();
    }//GEN-LAST:event_cmbSalaFiltroSesionesItemStateChanged

    private void chkSoloProgramadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSoloProgramadasActionPerformed
        aplicarFiltrosSesiones();
    }//GEN-LAST:event_chkSoloProgramadasActionPerformed

    private void btnLimpiarFiltrosSesionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarFiltrosSesionesActionPerformed
        txtFechaFiltroSesiones.setText("");
        cmbSalaFiltroSesiones.setSelectedItem("TODAS");
        chkSoloProgramadas.setSelected(false);
        aplicarFiltrosSesiones();
    }//GEN-LAST:event_btnLimpiarFiltrosSesionesActionPerformed

    private void btnActualizarSesionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarSesionesActionPerformed
        cargarTablaSesiones();
    }//GEN-LAST:event_btnActualizarSesionesActionPerformed

    private void btnCancelarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarSesionActionPerformed
        int viewRow = tblSesiones.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión en la tabla.", "Cancelar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblSesiones.convertRowIndexToModel(viewRow);
        String codigoSesion = tblSesiones.getModel().getValueAt(modelRow, 0).toString();

        int resp = JOptionPane.showConfirmDialog(
                this,
                "¿Cancelar la sesión " + codigoSesion + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );
        if (resp != JOptionPane.YES_OPTION) return;

        try {
            controlador.cancelarSesion(codigoSesion);
            cargarTablaSesiones();
            JOptionPane.showMessageDialog(this, "Sesión cancelada.");
        } catch (EntidadNoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "No se encontró la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCancelarSesionActionPerformed

    private void btnProgramarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProgramarSesionActionPerformed
        ProgramarSesionDialog dlg = new ProgramarSesionDialog(this, true, controlador);
        dlg.setVisible(true);

        if (dlg.isCreada()) {
            cargarTablaSesiones();
        }
    }//GEN-LAST:event_btnProgramarSesionActionPerformed

    private void btnOcupacionSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOcupacionSesionActionPerformed
        int viewRow = tblSesiones.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una sesión en la tabla.", "Ocupación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblSesiones.convertRowIndexToModel(viewRow);
        String codigoSesion = tblSesiones.getModel().getValueAt(modelRow, 0).toString(); // col 0 = Código

        Sesion s = controlador.buscarSesionPorCodigo(codigoSesion);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int aforo = s.getSala().getCapacidad();
        int ocupadas = s.getPlazasOcupadas();
        int disponibles = s.getPlazasDisponibles();
        double porcentaje = (aforo == 0) ? 0 : (ocupadas * 100.0 / aforo);
        
        String fechaES = s.getFecha().format(F_ES);

        String msg = ""
            + "Sesión: " + s.getCodigoSesion() + "\n"
            + "Película: " + s.getPelicula().getTitulo() + "\n"
            + "Fecha/Hora: " + fechaES + " " + s.getHoraInicio() + "\n"
            + "Sala: " + s.getSala().getNumero() + "\n"
            + "Estado: " + s.getEstado() + "\n\n"
            + "Aforo: " + aforo + "\n"
            + "Ocupadas: " + ocupadas + "\n"
            + "Disponibles: " + disponibles + "\n"
            + "Ocupación: " + String.format(java.util.Locale.US, "%.1f", porcentaje) + "%";

        JOptionPane.showMessageDialog(this, msg, "Ocupación de la sesión", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnOcupacionSesionActionPerformed
//-------------------------------------
    private void btnActualizarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarClientesActionPerformed
        cargarTablaClientes();
    }//GEN-LAST:event_btnActualizarClientesActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        int viewRow = tblClientes.getSelectedRow();
        if (viewRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un cliente.", "Eliminar", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblClientes.convertRowIndexToModel(viewRow);
        String dni = tblClientes.getModel().getValueAt(modelRow, 0).toString();

        int resp = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Eliminar el cliente con DNI " + dni + "?",
                "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);

        if (resp != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            controlador.eliminarCliente(dni);
            cargarTablaClientes();
            javax.swing.JOptionPane.showMessageDialog(this, "Cliente eliminado.");
        } catch (excepciones.EntidadNoEncontradaException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnAltaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaClienteActionPerformed
        AltaClienteDialog dlg = new AltaClienteDialog(this, true, controlador);
        dlg.setVisible(true);

        if (dlg.isCreado()) {
            cargarTablaClientes();
        }
    }//GEN-LAST:event_btnAltaClienteActionPerformed

    private void cmbCampoClienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCampoClienteItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            aplicarFiltrosClientes();
        }
    }//GEN-LAST:event_cmbCampoClienteItemStateChanged

    private void btnLimpiarFiltroClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarFiltroClientesActionPerformed
        txtBuscarCliente.setText("");
        cmbCampoCliente.setSelectedItem("DNI");
        aplicarFiltrosClientes();
    }//GEN-LAST:event_btnLimpiarFiltroClientesActionPerformed
//-------------------------------------
    private void btnLimpiarFiltrosReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarFiltrosReservasActionPerformed
        txtBuscarReserva.setText("");
        chkSoloConfirmadas.setSelected(false);
        aplicarFiltrosReservas();
    }//GEN-LAST:event_btnLimpiarFiltrosReservasActionPerformed

    private void btnActualizarReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarReservasActionPerformed
        cargarTablaReservas();
    }//GEN-LAST:event_btnActualizarReservasActionPerformed

    private void btnCancelarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarReservaActionPerformed
        int viewRow = tblReservas.getSelectedRow();
        if (viewRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una reserva.", "Cancelar", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblReservas.convertRowIndexToModel(viewRow);
        String codigoReserva = tblReservas.getModel().getValueAt(modelRow, 0).toString();

        int resp = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Cancelar la reserva " + codigoReserva + "?",
                "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        if (resp != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            controlador.cancelarReserva(codigoReserva);
            cargarTablaReservas();
            javax.swing.JOptionPane.showMessageDialog(this, "Reserva cancelada.");
        } catch (excepciones.CancelacionFueraDePlazaException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se puede cancelar: quedan menos de 2 horas.", "Plazo excedido", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCancelarReservaActionPerformed

    private void chkSoloConfirmadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSoloConfirmadasActionPerformed
        aplicarFiltrosReservas();
    }//GEN-LAST:event_chkSoloConfirmadasActionPerformed

    private void btnVerDetalleReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerDetalleReservaActionPerformed
        int viewRow = tblReservas.getSelectedRow();
        if (viewRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una reserva.", "Detalle", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblReservas.convertRowIndexToModel(viewRow);
        String codigoReserva = tblReservas.getModel().getValueAt(modelRow, 0).toString(); // col 0 = Código

        modelo.Reserva r = controlador.buscarReservaPorCodigo(codigoReserva);
        if (r == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontró la reserva.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Formato ES
        java.time.format.DateTimeFormatter dt = java.time.format.DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm");
        java.text.NumberFormat eur = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "ES"));

        String msg = ""
            + "Código: " + r.getCodigoReserva() + "\n"
            + "Estado: " + r.getEstado() + "\n"
            + "Fecha reserva: " + r.getFechaHoraReserva().format(dt) + "\n\n"
            + "Cliente:\n"
            + "  DNI: " + r.getCliente().getDni() + "\n"
            + "  Nombre: " + r.getCliente().getNombreCompleto() + "\n"
            + "  Email: " + r.getCliente().getEmail() + "\n"
            + "  Teléfono: " + r.getCliente().getTelefono() + "\n"
            + "  Abono: " + r.getCliente().getTipoAbono() + "\n\n"
            + "Sesión:\n"
            + "  Código: " + r.getSesion().getCodigoSesion() + "\n"
            + "  Película: " + r.getSesion().getPelicula().getTitulo() + "\n"
            + "  Fecha/Hora: " + r.getSesion().getFecha().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-uuuu"))
            + " " + r.getSesion().getHoraInicio().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) + "\n"
            + "  Sala: " + r.getSesion().getSala().getNumero() + "\n\n"
            + "Entradas: " + r.getNumEntradas() + "\n"
            + "Extras: " + r.getDescripcionExtras() + "\n"
            + "Precio final: " + eur.format(r.getPrecioFinal());

        javax.swing.JOptionPane.showMessageDialog(this, msg, "Detalle de reserva", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnVerDetalleReservaActionPerformed
//-------------------------------------
    private void cmbSesionInformeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSesionInformeItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            actualizarVistaPreviaInforme();
        }
    }//GEN-LAST:event_cmbSesionInformeItemStateChanged

    private void btnActualizarInformesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarInformesActionPerformed
        recargarComboSesionesInforme();
        actualizarVistaPreviaInforme();
    }//GEN-LAST:event_btnActualizarInformesActionPerformed

    private void btnGenerarInformeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarInformeActionPerformed
        Object sel = cmbSesionInforme.getSelectedItem();
        if (!(sel instanceof modelo.Sesion s)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una sesión.", "Informe", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean ok = controlador.generarInformeSesion(s.getCodigoSesion());
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Informe generado: Sesion_" + s.getCodigoSesion() + "_Informe.txt",
                        "Informe", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se pudo generar el informe.",
                        "Informe", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (excepciones.EntidadNoEncontradaException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGenerarInformeActionPerformed

    private void btnModificarPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarPeliculaActionPerformed
        int viewRow = tblPeliculas.getSelectedRow();
        if (viewRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una película.", "Modificar", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblPeliculas.convertRowIndexToModel(viewRow);
        String codigo = tblPeliculas.getModel().getValueAt(modelRow, 0).toString(); // col 0 Código

        modelo.Pelicula p = controlador.buscarPeliculaPorCodigo(codigo);
        if (p == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontró la película.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        AltaPeliculaDialog dlg = new AltaPeliculaDialog(this, true, controlador);
        dlg.setModoEdicion(p);
        dlg.setVisible(true);

        if (dlg.isCreada()) {
            cargarTablaPeliculas();
        }
    }//GEN-LAST:event_btnModificarPeliculaActionPerformed

    private void btnModificarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarClienteActionPerformed
        int viewRow = tblClientes.getSelectedRow();
        if (viewRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un cliente.", "Modificar", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblClientes.convertRowIndexToModel(viewRow);
        String dni = tblClientes.getModel().getValueAt(modelRow, 0).toString(); // col 0 = DNI

        modelo.Cliente c = controlador.buscarClientePorDNI(dni);
        if (c == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontró el cliente.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        AltaClienteDialog dlg = new AltaClienteDialog(this, true, controlador);
        dlg.setModoEdicion(c);
        dlg.setVisible(true);

        if (dlg.isCreado()) {
            cargarTablaClientes();
        }
    }//GEN-LAST:event_btnModificarClienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JScrollPane2;
    private javax.swing.JButton btnActualizarClientes;
    private javax.swing.JButton btnActualizarInformes;
    private javax.swing.JButton btnActualizarPeliculas;
    private javax.swing.JButton btnActualizarReservas;
    private javax.swing.JButton btnActualizarSesiones;
    private javax.swing.JButton btnAlta;
    private javax.swing.JButton btnAltaCliente;
    private javax.swing.JButton btnCancelarReserva;
    private javax.swing.JButton btnCancelarSesion;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnGenerarInforme;
    private javax.swing.JButton btnLimpiarFiltroClientes;
    private javax.swing.JButton btnLimpiarFiltros;
    private javax.swing.JButton btnLimpiarFiltrosReservas;
    private javax.swing.JButton btnLimpiarFiltrosSesiones;
    private javax.swing.JButton btnModificarCliente;
    private javax.swing.JButton btnModificarPelicula;
    private javax.swing.JButton btnOcupacionSesion;
    private javax.swing.JButton btnProgramarSesion;
    private javax.swing.JButton btnVerDetalleReserva;
    private javax.swing.JCheckBox chkSoloConfirmadas;
    private javax.swing.JCheckBox chkSoloProgramadas;
    private javax.swing.JComboBox<String> cmbCampoBuscar;
    private javax.swing.JComboBox<String> cmbCampoCliente;
    private javax.swing.JComboBox<String> cmbCampoReserva;
    private javax.swing.JComboBox<String> cmbGeneroFiltro;
    private javax.swing.JComboBox<String> cmbSalaFiltroSesiones;
    private javax.swing.JComboBox cmbSesionInforme;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane pane1Clientes;
    private javax.swing.JScrollPane paneReservas1;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelFiltrosPeliculas;
    private javax.swing.JPanel panelInformes;
    private javax.swing.JPanel panelPeliculas;
    private javax.swing.JPanel panelReservas;
    private javax.swing.JPanel panelSesiones;
    private javax.swing.JTabbedPane tabsEmpleado;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblPeliculas;
    private javax.swing.JTable tblReservas;
    private javax.swing.JTable tblSesiones;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtBuscarCliente;
    private javax.swing.JTextField txtBuscarReserva;
    private javax.swing.JTextField txtFechaFiltroSesiones;
    private javax.swing.JTextArea txtVistaInforme;
    // End of variables declaration//GEN-END:variables
}
