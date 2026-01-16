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

public class EmpleadoFrame extends javax.swing.JFrame {
    private final ControladorCine controlador;
    private javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorterPeliculas;

    public EmpleadoFrame(ControladorCine controlador) {
        this.controlador = controlador;
        initComponents();
        setSize(611, 515);          // el tamaño que quieras
        setLocationRelativeTo(null);
        setResizable(false);
        setMinimumSize(getSize());
        setMaximumSize(getSize());

        setTitle("OCine - Empleado");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        configurarFiltrosPeliculas(); 
        cargarTablaPeliculas();
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

        // (Opcional recomendado) ordenar bien Año/Duración si son Integer en el modelo
        sorterPeliculas.setComparator(3, java.util.Comparator.comparingInt(a -> (Integer) a)); // Año
        sorterPeliculas.setComparator(4, java.util.Comparator.comparingInt(a -> (Integer) a)); // Duración

        aplicarFiltrosPeliculas(); // importante para mantener filtros al refrescar
    }
    
    private void configurarFiltrosPeliculas() {
        // Campo buscar
        cmbCampoBuscar.removeAllItems();
        cmbCampoBuscar.addItem("Título");
        cmbCampoBuscar.addItem("Director");
        cmbCampoBuscar.addItem("Ambos");

        // Género
        cmbGeneroFiltro.removeAllItems();
        cmbGeneroFiltro.addItem("TODOS");
        for (modelo.Genero g : modelo.Genero.values()) {
            cmbGeneroFiltro.addItem(g.name());
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

        // === Filtro por género ===
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

        // === Filtro por texto (título/director) ===
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
        btnActualizarPeliculas = new javax.swing.JButton();
        btnAlta = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        panelFiltrosPeliculas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        cmbCampoBuscar = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbGeneroFiltro = new javax.swing.JComboBox<>();
        btnLimpiarFiltros = new javax.swing.JButton();
        panelSesiones = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelClientes = new javax.swing.JPanel();
        panelReservas = new javax.swing.JPanel();
        panelInformes = new javax.swing.JPanel();

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

        btnActualizarPeliculas.setText("Actualizar");
        btnActualizarPeliculas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarPeliculasActionPerformed(evt);
            }
        });

        btnAlta.setText("Alta");
        btnAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(panelFiltrosPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFiltrosPeliculasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cmbGeneroFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(btnLimpiarFiltros))
                    .addGroup(panelFiltrosPeliculasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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

        javax.swing.GroupLayout panelPeliculasLayout = new javax.swing.GroupLayout(panelPeliculas);
        panelPeliculas.setLayout(panelPeliculasLayout);
        panelPeliculasLayout.setHorizontalGroup(
            panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPeliculasLayout.createSequentialGroup()
                .addGroup(panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPeliculasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelFiltrosPeliculas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelPeliculasLayout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(btnActualizarPeliculas)
                        .addGap(33, 33, 33)
                        .addComponent(btnAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPeliculasLayout.setVerticalGroup(
            panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPeliculasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFiltrosPeliculas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelPeliculasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAlta)
                    .addComponent(btnActualizarPeliculas)
                    .addComponent(btnEliminar))
                .addGap(70, 70, 70))
        );

        tabsEmpleado.addTab("Películas", panelPeliculas);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 66, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 82, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout panelSesionesLayout = new javax.swing.GroupLayout(panelSesiones);
        panelSesiones.setLayout(panelSesionesLayout);
        panelSesionesLayout.setHorizontalGroup(
            panelSesionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSesionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSesionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSesionesLayout.setVerticalGroup(
            panelSesionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSesionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabsEmpleado.addTab("Sesiones", panelSesiones);

        javax.swing.GroupLayout panelClientesLayout = new javax.swing.GroupLayout(panelClientes);
        panelClientes.setLayout(panelClientesLayout);
        panelClientesLayout.setHorizontalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        panelClientesLayout.setVerticalGroup(
            panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        tabsEmpleado.addTab("tab3", panelClientes);

        javax.swing.GroupLayout panelReservasLayout = new javax.swing.GroupLayout(panelReservas);
        panelReservas.setLayout(panelReservasLayout);
        panelReservasLayout.setHorizontalGroup(
            panelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        panelReservasLayout.setVerticalGroup(
            panelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        tabsEmpleado.addTab("tab4", panelReservas);

        javax.swing.GroupLayout panelInformesLayout = new javax.swing.GroupLayout(panelInformes);
        panelInformes.setLayout(panelInformesLayout);
        panelInformesLayout.setHorizontalGroup(
            panelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        panelInformesLayout.setVerticalGroup(
            panelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        tabsEmpleado.addTab("tab5", panelInformes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabsEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 459, Short.MAX_VALUE)
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarPeliculas;
    private javax.swing.JButton btnAlta;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiarFiltros;
    private javax.swing.JComboBox<String> cmbCampoBuscar;
    private javax.swing.JComboBox<String> cmbGeneroFiltro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelFiltrosPeliculas;
    private javax.swing.JPanel panelInformes;
    private javax.swing.JPanel panelPeliculas;
    private javax.swing.JPanel panelReservas;
    private javax.swing.JPanel panelSesiones;
    private javax.swing.JTabbedPane tabsEmpleado;
    private javax.swing.JTable tblPeliculas;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
