import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Image;

public class Logistica extends JFrame {
    // Componentes de la interfaz [cite: 23, 27, 29, 25, 28]
    private JTextField txtNumero, txtCliente, txtPeso, txtDistancia;
    private JComboBox<String> cbTipo;
    private JButton btnGuardar, btnEliminar;
    private JTable tabla;
    private DefaultTableModel modelo;

    // Lista para el polimorfismo
    private ArrayList<Envio> listaEnvios;

    public Logistica() {
        listaEnvios = new ArrayList<>();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Operador Logístico");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Layout nulo para posicionar manualmente como en clase
    }

  private void inicializarComponentes() {
    // 1. CREAR OBJETOS (Variables de instancia)
    txtNumero = new JTextField();
    txtCliente = new JTextField();
    txtPeso = new JTextField();
    txtDistancia = new JTextField();
    cbTipo = new JComboBox<>(new String[] { "Terrestre", "Aereo", "Maritimo" });
    btnGuardar = new JButton("Guardar"); 
    btnEliminar = new JButton(); // Este es para "Retirar" [cite: 17]
    
    // Botones locales (solo para diseño)
    JButton btnCancelar = new JButton("Cancelar"); 
    JButton btnIconoAgregar = new JButton(); 

    // 2. CONFIGURAR ICONOS SUPERIORES
    try {
        ImageIcon iconoA = new ImageIcon("img/guardar.png");
        Image imgA = iconoA.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        btnIconoAgregar.setIcon(new ImageIcon(imgA));

        ImageIcon iconoR = new ImageIcon("img/eliminar.png");
        Image imgR = iconoR.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        btnEliminar.setIcon(new ImageIcon(imgR));
    } catch (Exception e) {
        System.out.println("Error cargando iconos: " + e.getMessage());
    }

    // 3. POSICIONAMIENTO (Layout manual) [cite: 19]
    btnIconoAgregar.setBounds(20, 10, 80, 70);
    btnEliminar.setBounds(105, 10, 80, 70);
    add(btnIconoAgregar);
    add(btnEliminar);

    // Campos: Fila 1
    JLabel lblNumero = new JLabel("Número"); 
    lblNumero.setBounds(20, 100, 80, 25);
    add(lblNumero);
    txtNumero.setBounds(100, 100, 150, 25);
    add(txtNumero);

    JLabel lblTipo = new JLabel("Tipo"); 
    lblTipo.setBounds(270, 100, 80, 25);
    add(lblTipo);
    cbTipo.setBounds(350, 100, 150, 25);
    add(cbTipo);

    // Campos: Fila 2
    JLabel lblCliente = new JLabel("Cliente"); 
    lblCliente.setBounds(20, 140, 80, 25);
    add(lblCliente);
    txtCliente.setBounds(100, 140, 150, 25);
    add(txtCliente);

    JLabel lblDistancia = new JLabel("Distancia en Km"); 
    lblDistancia.setBounds(270, 140, 120, 25);
    add(lblDistancia);
    txtDistancia.setBounds(380, 140, 120, 25);
    add(txtDistancia);

    // Campos: Fila 3
    JLabel lblPeso = new JLabel("Peso"); 
    lblPeso.setBounds(20, 180, 80, 25);
    add(lblPeso);
    txtPeso.setBounds(100, 180, 150, 25);
    add(txtPeso);

    // Botones Centrales
    btnGuardar.setBounds(270, 180, 110, 30);
    add(btnGuardar);
    btnCancelar.setBounds(390, 180, 110, 30);
    add(btnCancelar);

    // Tabla [cite: 33, 38]
    String[] columnas = { "Tipo", "Código", "Cliente", "Peso", "Distancia", "Costo" };
    modelo = new DefaultTableModel(columnas, 0);
    tabla = new JTable(modelo);
    JScrollPane scroll = new JScrollPane(tabla);
    scroll.setBounds(20, 230, 540, 180);
    add(scroll);

    // 4. EVENTOS (ActionListeners)
    btnGuardar.addActionListener(e -> agregarEnvio());
    btnIconoAgregar.addActionListener(e -> agregarEnvio());
    
    btnEliminar.addActionListener(e -> {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            listaEnvios.remove(fila);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un envío para retirar");
        }
    });

    btnCancelar.addActionListener(e -> {
        txtNumero.setText("");
        txtCliente.setText("");
        txtPeso.setText("");
        txtDistancia.setText("");
    });
}
    
  private void agregarEnvio() {
    // 1. VALIDACIÓN: Revisar si algún campo está vacío
    if (txtNumero.getText().isEmpty() || txtCliente.getText().isEmpty() || 
        txtPeso.getText().isEmpty() || txtDistancia.getText().isEmpty()) {
        
        // Mostrar ventana emergente de advertencia 
        JOptionPane.showMessageDialog(this, 
            "Todos los campos son obligatorios. Por favor, llénelos para continuar.", 
            "Campos Incompletos", 
            JOptionPane.WARNING_MESSAGE);
        return; // Detiene el método aquí para que no intente guardar
    }

    try {
        // 2. CAPTURA DE DATOS (Solo si pasó la validación)
        String cod = txtNumero.getText();
        String cli = txtCliente.getText();
        double p = Double.parseDouble(txtPeso.getText());
        double d = Double.parseDouble(txtDistancia.getText());
        String tipo = cbTipo.getSelectedItem().toString();

        Envio nuevo = null;

        // Polimorfismo en acción [cite: 11, 13]
        if (tipo.equals("Terrestre")) {
            nuevo = new Terrestre(cod, cli, p, d);
        } else if (tipo.equals("Aereo")) {
            nuevo = new Aereo(cod, cli, p, d);
        } else {
            nuevo = new Maritimo(cod, cli, p, d);
        }

        listaEnvios.add(nuevo);
        actualizarTabla();
        
    } catch (NumberFormatException e) {
        // Por si escriben letras en lugar de números en Peso o Distancia
        JOptionPane.showMessageDialog(this, 
            "Error: En Peso y Distancia solo se permiten números.", 
            "Error de Formato", 
            JOptionPane.ERROR_MESSAGE);
    }
}

    private void actualizarTabla() {
        modelo.setRowCount(0); // Limpiar tabla
        for (Envio env : listaEnvios) {
            Object[] fila = {
                    env.getClass().getSimpleName(),
                    env.getCodigo(),
                    env.getCliente(),
                    env.getPeso(),
                    env.getDistancia(),
                    env.calcularTarifa() // Aquí ocurre la magia del polimorfismo [cite: 13]
            };
            modelo.addRow(fila);
        }
    }

  public static void main() {
    Logistica ventana = new Logistica();
    ventana.setVisible(true);
}
}
