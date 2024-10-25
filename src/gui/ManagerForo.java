package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerForo foroFrame = new ManagerForo();
            foroFrame.setVisible(true);
        });
    }
}
