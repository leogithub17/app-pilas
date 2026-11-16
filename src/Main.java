package bodega;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación: abre la ventana de control de la pila de cajas.
 */
public class Main {

    public static void main(String[] args) {
        // Usa la apariencia del sistema operativo (Windows, macOS o Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa la apariencia por defecto de Java
        }

        // Las interfaces Swing se deben crear en el hilo de eventos (EDT)
        SwingUtilities.invokeLater(() -> {
            Interfaz ventana = new Interfaz();
            ventana.setVisible(true);
        });
    }
}
