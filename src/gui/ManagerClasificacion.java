package gui;

import javax.imageio.ImageIO;
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
    	
    	//Configuracion inicial de la ventana con componentes basicos
        setSize(600, 600);
        setTitle("Clasificación");
        setResizable(false);
        setLocationRelativeTo(null);

        // Logica para cambiar el logo de la ventana
        try {
            File file = new File("resources/imagenes/logo.png");
            if (file.exists()) {
                Image icono = ImageIO.read(file);
                setIconImage(icono);
            } else {
                System.err.println("El archivo de logo no existe en la ruta especificada: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al intentar cargar el icono desde la ruta: resources/imagenes/logo.png");
            e.printStackTrace();
        }
        

        //Obtencion de los datos del usuario actual y ordenar la lista de usuarios
        String username = UserData.getUsername();
        List<Usuario> usuarios = obtenerUsuarios();
        ordenarUsuarios(usuarios, usuarios.size());

        //Configuracion del panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(196, 238, 255));

        //Configuracion de la etiqueta del titulo 
        JLabel labelTitulo = new JLabel("Clasificación", JLabel.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(196, 238, 255));
        labelTitulo.setForeground(Color.BLACK);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(labelTitulo, BorderLayout.NORTH);

        //Creacion de la tabla de clasificacion
        String[] columnNames = {"Foto", "Usuario", "Puntos"};
        Object[][] data = new Object[usuarios.size()][3];

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            // Cargar la imagen del perfil del usuario
            String imagenPath = "resources/imagenes/" + usuario.getUsername() + "_perfil.png"; // Ruta de la imagen del perfil
            ImageIcon fotoPerfil = cargarFotoPerfil(imagenPath);
            data[i][0] = fotoPerfil; 
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
        tablaClasificacion.setRowHeight(70);

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

        // Renderizado para la columna de imágenes de la tabla clasificacion
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
        

        //Añadir el scrollpane a la tabla de clasificacion y añadirlo al panel
        JScrollPane scrollPane = new JScrollPane(tablaClasificacion);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        //Creacion del panel inferior y el boton atras, asi como ciertas caracteristicas del boton
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelInferior.setBackground(new Color(196, 238, 255));
        JButton btnAtras = new JButton("Atrás");
        btnAtras.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnAtras.setToolTipText("Volver a la ventana principal");
        
        //Action listener del boton atras 
        btnAtras.addActionListener(e -> {
            dispose();
            new ManagerWelcome().setVisible(true);
        });

        //Creacion del boton foro, caracteristicas y action listener
        JButton btnForo = new JButton("Foro");
        btnForo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnForo.addActionListener(e -> {     	
            dispose();
            new ManagerForo().setVisible(true);
        });

        panelInferior.add(btnAtras);
        panelInferior.add(btnForo);
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
    

    //Metodo para escalar una imagen a un tamaño especifico
    private ImageIcon escalarImagen(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(ruta);
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    // Método para cargar la foto de perfil del usuario, si no existe se carga una por defecto
    private ImageIcon cargarFotoPerfil(String ruta) {
        File archivoImagen = new File(ruta);
        if (archivoImagen.exists()) {
            return escalarImagen(ruta, 50, 50);
        } else {
            // Foto por defecto si no existe la imagen del usuario
            return escalarImagen("resources/imagenes/usuario.png", 30, 30);
        }
    }
    

    //Metodo para obetner los usuarios
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
    
 // Método recursivo para ordenar la lista de usuarios por puntos 
    private void ordenarUsuarios(List<Usuario> usuarios, int n) {
      
        if (n <= 1) {
            return;
        }
        ordenarUsuarios(usuarios, n - 1);
        Usuario ultimo = usuarios.get(n - 1);
        int j = n - 2;
        while (j >= 0 && usuarios.get(j).getPuntos() < ultimo.getPuntos()) {
            usuarios.set(j + 1, usuarios.get(j));
            j--;
        }
        usuarios.set(j + 1, ultimo);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerClasificacion frame = new ManagerClasificacion();
            frame.setVisible(true);
        });
    }

    //Creacion de la clase usuario 
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
