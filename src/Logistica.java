import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.awt.Image;

public class Logistica extends JFrame {
  
    private JTextField txtNumero, txtCliente, txtPeso, txtDistancia;
    private JComboBox<String> cbTipo;
    private JButton btnGuardar, btnEliminar;
    private JTable tabla;
    private DefaultTableModel modelo;

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
        setLayout(null); 
    }

  private void inicializarComponentes() {

    txtNumero = new JTextField();
    txtCliente = new JTextField();
    txtPeso = new JTextField();
    txtDistancia = new JTextField();
    cbTipo = new JComboBox<>(new String[] { "Terrestre", "Aereo", "Maritimo" });
    btnGuardar = new JButton("Guardar"); 
    btnEliminar = new JButton(); 
    
   
    JButton btnCancelar = new JButton("Cancelar"); 
    JButton btnIconoAgregar = new JButton(); 

   
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

  
    btnIconoAgregar.setBounds(20, 10, 80, 70);
    btnEliminar.setBounds(105, 10, 80, 70);
    add(btnIconoAgregar);
    add(btnEliminar);

   
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

  
    JLabel lblPeso = new JLabel("Peso"); 
    lblPeso.setBounds(20, 180, 80, 25);
    add(lblPeso);
    txtPeso.setBounds(100, 180, 150, 25);
    add(txtPeso);

  
    btnGuardar.setBounds(270, 180, 110, 30);
    add(btnGuardar);
    btnCancelar.setBounds(390, 180, 110, 30);
    add(btnCancelar);

  
    String[] columnas = { "Tipo", "Código", "Cliente", "Peso", "Distancia", "Costo" };
    modelo = new DefaultTableModel(columnas, 0);
    tabla = new JTable(modelo);
    JScrollPane scroll = new JScrollPane(tabla);
    scroll.setBounds(20, 230, 540, 180);
    add(scroll);

   
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
   
    if (txtNumero.getText().isEmpty() || txtCliente.getText().isEmpty() || 
        txtPeso.getText().isEmpty() || txtDistancia.getText().isEmpty()) {
        
        
        JOptionPane.showMessageDialog(this, 
            "Todos los campos son obligatorios. Por favor, llénelos para continuar.", 
            "Campos Incompletos", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
       
        String cod = txtNumero.getText();
        String cli = txtCliente.getText();
        double p = Double.parseDouble(txtPeso.getText());
        double d = Double.parseDouble(txtDistancia.getText());
        String tipo = cbTipo.getSelectedItem().toString();

        Envio nuevo = null;

       
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
        JOptionPane.showMessageDialog(this, 
            "Error: En Peso y Distancia solo se permiten números.", 
            "Error de Formato", 
            JOptionPane.ERROR_MESSAGE);
    }
}

    private void actualizarTabla() {
        modelo.setRowCount(0); 
        for (Envio env : listaEnvios) {
            Object[] fila = {
                    env.getClass().getSimpleName(),
                    env.getCodigo(),
                    env.getCliente(),
                    env.getPeso(),
                    env.getDistancia(),
                    env.calcularTarifa() 
            };
            modelo.addRow(fila);
        }
    }

  public static void main() {
    Logistica ventana = new Logistica();
    ventana.setVisible(true);
}
}
