package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import domain.Tema;

public class ManagerForo extends JFrame implements Tema.CambiarTema {

    private static final long serialVersionUID = 1L;
    private JTextArea messageArea;
    private JTextField messageInput;
    private static final String FILE_NAME = "resources/data/foro_mensajes.txt";
    private JPanel mainPanel;

    public ManagerForo() {
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
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
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

        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> enviarMensaje());
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Crear el ComboBox para seleccionar el tema
        JComboBox<String> themeComboBox = new JComboBox<>(new String[]{"Claro", "Oscuro"});
        themeComboBox.setSelectedItem(Tema.getTemaActual() == Tema.Theme.CLARO ? "Claro" : "Oscuro");
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
        topPanel.add(new JLabel("Seleccionar Tema:"));
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

    private void enviarMensaje() {
        String mensaje = messageInput.getText();
        if (!mensaje.trim().isEmpty()) {
            String fechaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            messageArea.append("[" + fechaHora + "] " + mensaje + "\n");
            messageInput.setText("");
            guardarMensaje("[" + fechaHora + "] " + mensaje);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, escribe un mensaje.", "Mensaje vacío", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarMensaje(String mensaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(mensaje);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void mostrarVentanaAnterior() {
        dispose();
        ManagerWelcome v = new ManagerWelcome();
        v.setVisible(true);
    }

    private void applyTheme() {
        if (Tema.getTemaActual() == Tema.Theme.OSCURO) {
            mainPanel.setBackground(Color.DARK_GRAY);
            messageArea.setBackground(Color.DARK_GRAY);
            messageArea.setForeground(Color.WHITE);
        } else {
            mainPanel.setBackground(Color.WHITE);
            messageArea.setBackground(Color.WHITE);
            messageArea.setForeground(Color.BLACK);
        }
    }

    @Override
    public void onThemeChanged(Tema.Theme theme) {
        applyTheme();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerForo());
    }
}
