package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import domain.UserData;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class ManagerClasificacion extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerClasificacion() {
        setSize(400, 400);
        setTitle("Clasificación");
        setResizable(false);
        setLocationRelativeTo(null);

        String username = UserData.getUsername();
        List<Usuario> usuarios = obtenerUsuarios();

        Collections.sort(usuarios, (u1, u2) -> Integer.compare(u2.getPuntos(), u1.getPuntos()));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(196, 238, 255));

        JLabel labelTitulo = new JLabel("Clasificación", JLabel.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(196, 238, 255));
        labelTitulo.setForeground(Color.BLACK);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(labelTitulo, BorderLayout.NORTH);
        
        

        String[] columnNames = {"Foto", "Usuario", "Puntos"};
        Object[][] data = new Object[usuarios.size()][3];

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            data[i][0] = escalarImagen("resources/imagenes/usuario.png", 30, 30); 
            data[i][1] = usuario.getUsername();
            data[i][2] = usuario.getPuntos();
        }

        JTable tablaClasificacion = new JTable(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Icon.class : Object.class;
            }
        };

        tablaClasificacion.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tablaClasificacion.setRowHeight(50);

        tablaClasificacion.setDefaultRenderer(Object.class, (table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value.toString(), JLabel.CENTER);
            if (column == 1 && value.equals(username)) {
                label.setFont(new Font("SansSerif", Font.BOLD, 16));
                label.setForeground(new Color(33, 150, 243));
                label.setText("<html><u>" + value + "</u></html>");
            } else {
                label.setFont(new Font("SansSerif", Font.PLAIN, 16));
                label.setForeground(Color.BLACK);
            }
            return label;
        });

        // Renderizador para la columna de imágenes
        tablaClasificacion.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Icon) {
                    JLabel label = new JLabel();
                    label.setIcon((Icon) value);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaClasificacion);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelInferior.setBackground(new Color(196, 238, 255));
        JButton btnAtras = new JButton("Atrás");
        btnAtras.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnAtras.setToolTipText("Volver a la ventana principal");
        btnAtras.addActionListener(e -> {
            dispose();
            new ManagerWelcome().setVisible(true);
        });
        panelInferior.add(btnAtras);
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                new ManagerWelcome().setVisible(true);
            }
        });
    }

    private ImageIcon escalarImagen(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(ruta);
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    private List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String filePath = "resources/data/usuarios.csv";

        Random random = new Random();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                if (datos.length >= 2) {
                    String username = datos[0];
                    int puntos = random.nextInt(50) + 1;

                    try {
                        puntos = Integer.parseInt(datos[1]);
                    } catch (NumberFormatException e) {
                        // Mantener valor aleatorio
                    }
                    usuarios.add(new Usuario(username, puntos));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de usuarios", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return usuarios;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerClasificacion frame = new ManagerClasificacion();
            frame.setVisible(true);
        });
    }

    static class Usuario {
        private String username;
        private int puntos;

        public Usuario(String username, int puntos) {
            this.username = username;
            this.puntos = puntos;
        }

        public String getUsername() {
            return username;
        }

        public int getPuntos() {
            return puntos;
        }
    }
}
