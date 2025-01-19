package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import domain.Tema;
import domain.UserData;
import gui.ManagerClasificacion.Usuario;

public class ManagerForo extends JFrame implements Tema.CambiarTema {

    private static final long serialVersionUID = 1L;
    private JTextArea messageArea;
    private JTextField messageInput;
    private static final String FILE_NAME = "resources/data/foro_mensajes.txt";
    private JPanel mainPanel;

    public ManagerForo() {
        // Configuración inicial de la ventana con componentes básicos
        setTitle("Foro/Chat");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Registrar la clase como listener para cambios en el tema
        Tema.addListener(this);

        // Configuración del panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Área de texto para mostrar mensajes
        messageArea = new JTextArea();
        messageArea.setEditable(false);  // Desactivar la edición
        messageArea.setLineWrap(true);
        messageArea.setCaretColor(new Color(0, 0, 0, 0)); // Establece un color transparente para el caret (cursor)
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12)); // Establecer fuente para los mensajes
        JScrollPane scrollPane = new JScrollPane(messageArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel para la entrada de mensajes
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        // Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("resources/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        messageInput = new JTextField();
        inputPanel.add(messageInput, BorderLayout.CENTER);

        Color ycolor = new Color(255, 239, 108);

        JButton sendButton = new JButton("Enviar");
        sendButton.setFont(new Font("Arial", Font.BOLD, 13));
        sendButton.setForeground(Color.black);
        sendButton.setPreferredSize(new Dimension(80, 28));
        sendButton.setBackground(ycolor);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sendButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        sendButton.addActionListener(e -> enviarMensaje());

        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendButton.setBackground(ycolor.brighter()); // Hacer el color más claro
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sendButton.setBackground(ycolor); // Volver al color original
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Crear el ComboBox para seleccionar el tema
        JComboBox<String> themeComboBox = new JComboBox<>(new String[]{"Claro", "Oscuro"});
        themeComboBox.setSelectedItem(Tema.getTemaActual() == Tema.Theme.CLARO ? "Claro" : "Oscuro");
        themeComboBox.setFont(new Font("Arial", Font.BOLD, 13));
        themeComboBox.setForeground(Color.black);
        themeComboBox.setPreferredSize(new Dimension(95, 25));
        themeComboBox.setBackground(Color.white);
        themeComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        themeComboBox.addActionListener(e -> {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            if ("Claro".equals(selectedTheme)) {
                Tema.setTema(Tema.Theme.CLARO);
            } else {
                Tema.setTema(Tema.Theme.OSCURO);
            }
        });

        // Agregar el ComboBox en la parte superior
        JPanel topPanel = new JPanel();
        JLabel lbltitulo = new JLabel("Seleccionar Tema :");
        lbltitulo.setFont(new Font("Arial", Font.BOLD, 13));

        topPanel.add(lbltitulo);
        topPanel.add(themeComboBox);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        add(mainPanel);

        // Cargar mensajes al iniciar
        cargarMensajes();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mostrarVentanaAnterior();
            }
        });

        // Aplicar el tema al iniciar la ventana
        applyTheme();
    }

    // Método para enviar mensajes
    private void enviarMensaje() {
        String mensaje = messageInput.getText();
        if (!mensaje.trim().isEmpty()) {
            String fechaHora = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            messageArea.append("[" + fechaHora + "] " + UserData.getName() + " " + mensaje + "\n");
            messageInput.setText("");
            guardarMensaje("[" + fechaHora + "] " + mensaje);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, escribe un mensaje.", "Mensaje vacío", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para guardar mensajes
    private void guardarMensaje(String mensaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(mensaje);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para cargar mensajes
    private void cargarMensajes() {
        File archivo = new File(FILE_NAME);
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    messageArea.append(linea + "\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar los mensajes.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para mostrar la ventana anterior
    private void mostrarVentanaAnterior() {
        dispose();
        ManagerClasificacion v = new ManagerClasificacion();
        v.setVisible(true);
    }

    // Método para aplicar el tema seleccionado
    private void applyTheme() {
        // Colores personalizados para cada tema
        Color claroColor = new Color(196, 238, 255); // Modo claro
        Color oscuroColor = Color.DARK_GRAY; // Modo oscuro

        if (Tema.getTemaActual() == Tema.Theme.OSCURO) {
            // Modo oscuro
            mainPanel.setBackground(oscuroColor);
            messageArea.setBackground(oscuroColor);
            messageArea.setForeground(Color.WHITE);
        } else {
            // Modo claro
            mainPanel.setBackground(claroColor);
            messageArea.setBackground(claroColor);
            messageArea.setForeground(Color.BLACK);
        }
    }

    // Método para obtener los usuarios
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

    @Override
    public void onThemeChanged(Tema.Theme theme) {
        applyTheme();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerForo managerForo = new ManagerForo();
            managerForo.setVisible(true); // Esto muestra la ventana
        });
    }
}
