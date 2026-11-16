package bodega;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Interfaz gráfica para apilar y desapilar cajas.
 * Cada caja de la pila se muestra como un JLabel; la caja del tope aparece arriba.
 */
public class Interfaz extends JFrame {

    // Instancia de la pila que maneja la lógica
    private PilaCajas pila = new PilaCajas();

    // Campos del formulario
    private JTextField txtReferencia = new JTextField(12);
    private JTextField txtDescripcion = new JTextField(12);
    private JTextField txtPeso = new JTextField(12);

    // Botones de comando
    private JButton btnApilar = new JButton("Apilar caja");
    private JButton btnDesapilar = new JButton("Desapilar caja");

    // Consultas (RF3 y RF4)
    private JTextField txtBuscar = new JTextField(12);
    private JButton btnConsultarTope = new JButton("Consultar caja del tope");
    private JButton btnBuscar = new JButton("Buscar caja por referencia");

    // Referencia de la caja encontrada en la última búsqueda (se resalta en la pila)
    private String referenciaResaltada = null;

    // Etiquetas de resumen
    private JLabel lblNumCajas = new JLabel("Número de cajas: 0");
    private JLabel lblPesoTotal = new JLabel("Peso total: 0.0 kg");

    // Panel donde se dibujan las cajas
    private JPanel panelPila = new JPanel();

    public Interfaz() {
        super("Control de pila de cajas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelPila(), BorderLayout.CENTER);
        add(crearPanelResumen(), BorderLayout.SOUTH);

        // Eventos de los botones
        btnApilar.addActionListener(e -> apilar());
        btnDesapilar.addActionListener(e -> desapilar());
        btnConsultarTope.addActionListener(e -> consultarTope());
        btnBuscar.addActionListener(e -> buscarPorReferencia());
        txtBuscar.addActionListener(e -> buscarPorReferencia()); // Enter también busca

        setSize(700, 560);
        setLocationRelativeTo(null);
    }

    // ---------- Construcción de la interfaz ----------

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Operaciones"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Referencia:"), c);
        c.gridx = 1;              panel.add(txtReferencia, c);

        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Descripción:"), c);
        c.gridx = 1;              panel.add(txtDescripcion, c);

        c.gridx = 0; c.gridy = 2; panel.add(new JLabel("Peso (kg):"), c);
        c.gridx = 1;              panel.add(txtPeso, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnApilar, c);

        c.gridy = 4;
        panel.add(btnDesapilar, c);

        // ----- Sección de consultas -----
        c.gridy = 5;
        panel.add(new JSeparator(), c);

        c.gridy = 6;
        JLabel lblConsultas = new JLabel("Consultas");
        lblConsultas.setFont(lblConsultas.getFont().deriveFont(Font.BOLD));
        panel.add(lblConsultas, c);

        c.gridy = 7;
        panel.add(btnConsultarTope, c);

        c.gridy = 8; c.gridwidth = 1; c.fill = GridBagConstraints.NONE;
        c.gridx = 0; panel.add(new JLabel("Ref. a buscar:"), c);
        c.gridx = 1; panel.add(txtBuscar, c);

        c.gridx = 0; c.gridy = 9; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnBuscar, c);

        // Empuja el formulario hacia arriba
        c.gridy = 10; c.weighty = 1;
        panel.add(Box.createVerticalGlue(), c);

        return panel;
    }

    private JScrollPane crearPanelPila() {
        panelPila.setLayout(new BoxLayout(panelPila, BoxLayout.Y_AXIS));
        panelPila.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(panelPila);
        scroll.setBorder(new TitledBorder("Pila de cajas (tope arriba)"));
        return scroll;
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 8));
        lblNumCajas.setFont(lblNumCajas.getFont().deriveFont(Font.BOLD, 14f));
        lblPesoTotal.setFont(lblPesoTotal.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(lblNumCajas);
        panel.add(lblPesoTotal);
        return panel;
    }

    // ---------- Acciones ----------

    private void apilar() {
        String referencia = txtReferencia.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String textoPeso = txtPeso.getText().trim();

        if (referencia.isEmpty() || descripcion.isEmpty() || textoPeso.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(textoPeso.replace(',', '.'));
        } catch (NumberFormatException ex) {
            mostrarError("El peso debe ser un número.");
            txtPeso.requestFocus();
            return;
        }
        if (peso <= 0) {
            mostrarError("El peso debe ser mayor que cero.");
            txtPeso.requestFocus();
            return;
        }

        if (pila.consultarCaja(referencia) != null) {
            mostrarError("Ya existe una caja con la referencia " + referencia + ".");
            txtReferencia.requestFocus();
            return;
        }

        pila.apilarCaja(new Caja(referencia, descripcion, peso));
        referenciaResaltada = null;
        limpiarFormulario();
        actualizarVista();
    }

    private void desapilar() {
        Caja tope = pila.darTope(); // se guarda antes de retirarla para informar cuál salió
        if (pila.desapilarCaja()) {
            referenciaResaltada = null;
            actualizarVista();
            JOptionPane.showMessageDialog(this, "Se retiró la caja:\n" + tope,
                    "Caja desapilada", JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrarError("La pila está vacía, no hay cajas para retirar.");
        }
    }

    /** RF3 — Muestra los datos de la caja del tope sin retirarla. */
    private void consultarTope() {
        Caja tope = pila.darTope(); // darTope() no modifica la pila
        if (tope == null) {
            JOptionPane.showMessageDialog(this, "La pila está vacía, no hay caja en el tope.",
                    "Caja del tope", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Caja del tope:\n\n"
                + "Referencia: " + tope.referencia + "\n"
                + "Contenido: " + tope.descripcion + "\n"
                + "Peso: " + tope.peso + " kg",
                "Caja del tope", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * RF4 — Busca una caja por referencia e informa su posición desde el tope.
     * Solo recorre los nodos con una variable auxiliar, así que la pila
     * queda exactamente en el mismo orden.
     */
    private void buscarPorReferencia() {
        String referencia = txtBuscar.getText().trim();
        if (referencia.isEmpty()) {
            mostrarError("Escriba la referencia que desea buscar.");
            txtBuscar.requestFocus();
            return;
        }
        if (pila.esVacia()) {
            JOptionPane.showMessageDialog(this, "La pila está vacía.",
                    "Buscar caja", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int posicion = posicionDesdeTope(referencia);

        if (posicion == -1) {
            referenciaResaltada = null;
            actualizarVista();
            JOptionPane.showMessageDialog(this,
                    "La caja con referencia " + referencia + " no se encuentra en la pila.",
                    "Buscar caja", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Caja caja = pila.consultarCaja(referencia);
        referenciaResaltada = caja.referencia;
        actualizarVista(); // resalta la caja encontrada

        int cajasEncima = posicion - 1;
        String instruccion = (cajasEncima == 0)
                ? "Está en el tope: se puede retirar directamente."
                : "Para retirarla hay que bajar " + cajasEncima
                  + (cajasEncima == 1 ? " caja" : " cajas") + " que tiene encima.";

        JOptionPane.showMessageDialog(this,
                "Caja encontrada en la posición " + posicion + " desde el tope.\n\n"
                + "Referencia: " + caja.referencia + "\n"
                + "Contenido: " + caja.descripcion + "\n"
                + "Peso: " + caja.peso + " kg\n\n"
                + instruccion,
                "Buscar caja", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Recorre la pila desde el tope (posición 1) sin modificarla.
     * @return la posición de la caja, o -1 si no está en la pila.
     */
    private int posicionDesdeTope(String referencia) {
        Nodo actual = pila.tope;   // variable auxiliar: tope no cambia
        int posicion = 1;
        while (actual != null) {
            if (actual.contenido.referencia.equalsIgnoreCase(referencia)) {
                return posicion;
            }
            actual = actual.sig;
            posicion++;
        }
        return -1;
    }

    /** Vuelve a dibujar las cajas recorriendo la pila desde el tope y actualiza el resumen. */
    private void actualizarVista() {
        panelPila.removeAll();
        panelPila.add(Box.createVerticalGlue()); // hace que las cajas se apoyen en la base

        Nodo actual = pila.tope;
        boolean esTope = true;
        while (actual != null) {
            boolean resaltada = actual.contenido.referencia.equals(referenciaResaltada);
            panelPila.add(crearLabelCaja(actual.contenido, esTope, resaltada));
            panelPila.add(Box.createVerticalStrut(4));
            esTope = false;
            actual = actual.sig;
        }

        lblNumCajas.setText("Número de cajas: " + pila.numCajas);
        lblPesoTotal.setText(String.format("Peso total: %.2f kg", pila.darPesoTotal()));

        panelPila.revalidate();
        panelPila.repaint();
    }

    /** Crea el JLabel que representa visualmente una caja. */
    private JLabel crearLabelCaja(Caja caja, boolean esTope, boolean resaltada) {
        JLabel lbl = new JLabel("<html><b>" + caja.referencia + "</b> — " + caja.descripcion
                + "<br>Peso: " + caja.peso + " kg</html>", SwingConstants.CENTER);
        lbl.setOpaque(true);
        Color fondo = esTope ? new Color(255, 204, 128) : new Color(222, 184, 135);
        Color borde = new Color(139, 90, 43);
        if (resaltada) {               // caja encontrada en la última búsqueda
            fondo = new Color(173, 216, 255);
            borde = new Color(30, 90, 200);
        }
        lbl.setBackground(fondo);
        lbl.setBorder(new CompoundBorder(
                new LineBorder(borde, resaltada ? 3 : 2),
                new EmptyBorder(6, 10, 6, 10)));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(300, 55));
        lbl.setPreferredSize(new Dimension(300, 55));
        return lbl;
    }

    private void limpiarFormulario() {
        txtReferencia.setText("");
        txtDescripcion.setText("");
        txtPeso.setText("");
        txtReferencia.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}