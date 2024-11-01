package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagerForo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextArea messageArea;
    private JTextField messageInput;
    private static final String FILE_NAME = "foro_mensajes.txt"; // Nombre del archivo para almacenar mensajes

    public ManagerForo() {
        setTitle("Foro/Chat");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // No cerrar directamente
        setLocationRelativeTo(null);
        
        // Configuración del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        // Área de texto para mostrar los mensajes
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel para la entrada de mensajes
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        
        
      //Cambiar foto de la ventana
        try {
            Image icono = ImageIO.read(new File("src/imagenes/logo.png"));
            setIconImage(icono);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }
        
        messageInput = new JTextField();
        inputPanel.add(messageInput, BorderLayout.CENTER);
        
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
        
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);
        
        add(panel);
        
        // Cargar mensajes al iniciar
        cargarMensajes();

        // Listener para manejar el cierre de la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mostrarVentanaAnterior();
            }
        });
    }

    private void enviarMensaje() {
        String mensaje = messageInput.getText();
        if (!mensaje.trim().isEmpty()) {
            // Obtener la fecha y la hora actual
            String fechaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            // Añadir el mensaje al área de texto
            messageArea.append("[" + fechaHora + "] " + mensaje + "\n");
            messageInput.setText(""); // Limpiar el campo de entrada
            
            // Guardar mensaje en el archivo
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
        // Cierra la ventana actual
        dispose();
        // Crea y muestra la ventana anterior
        ManagerWelcome v = new ManagerWelcome(); 
        v.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerForo foroFrame = new ManagerForo();
            foroFrame.setVisible(true);
        });
    }
}
