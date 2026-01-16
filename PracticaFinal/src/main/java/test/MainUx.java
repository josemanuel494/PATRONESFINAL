/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package test;


import javax.swing.SwingUtilities;
import controlador.ControladorCine;
import ui.Login;

/**
 *
 * @author Usuario
 */
public class MainUx {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorCine controlador = new ControladorCine();
            new Login(controlador).setVisible(true);
        });
    }
}
