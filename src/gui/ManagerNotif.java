package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ManagerNotif extends JFrame {

    private static final long serialVersionUID = 1L;

    public ManagerNotif() {
        setSize(600, 600);
        setTitle("Notificaciones");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal para la estructura
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título de la ventana
        JLabel titleLabel = new JLabel("NOTIFICACIONES", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 63, 65));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de notificaciones
        JTextArea notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        notificationArea.setLineWrap(true);
        notificationArea.setWrapStyleWord(true);
        notificationArea.setText("Aquí aparecerán tus notificaciones...\n");

        JScrollPane scrollPane = new JScrollPane(notificationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones en la parte inferior
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton markAsReadButton = new JButton("Marcar como leídas");
        JButton deleteAllButton = new JButton("Eliminar todas");

        styleButton(markAsReadButton);
        styleButton(deleteAllButton);

        // Acción para "Marcar como leídas"
        markAsReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notificationArea.setText("No tienes notificaciones nuevas.\n");
            }
        });

        // Acción para "Eliminar todas"
        deleteAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notificationArea.setText("");
                JOptionPane.showMessageDialog(null, "Todas las notificaciones han sido eliminadas.");
            }
        });

        buttonPanel.add(markAsReadButton);
        buttonPanel.add(deleteAllButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Acción al cerrar ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerPerfil wel = new ManagerPerfil();
                wel.setVisible(true);
            }
        });
    }

    // Método para estilizar botones
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerNotif frame = new ManagerNotif();
            frame.setVisible(true);
        });
    }
}

