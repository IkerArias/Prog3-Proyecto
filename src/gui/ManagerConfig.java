package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import domain.Tema;
import domain.Tema.CambiarTema;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerConfig extends JFrame implements CambiarTema {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;
    private JLabel headerLabel;
    private JComboBox<String> themeComboBox;
    private List<JLabel> labels; // Lista para almacenar los JLabel

    public ManagerConfig(String username) {
        setTitle("Configuración del Gestor de Plantillas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Tema.addListener(this);

        labels = new ArrayList<>(); // Inicializar la lista de JLabel

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                applyTheme();
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(196, 238, 255)); // Establecer el color de fondo claro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        headerLabel = new JLabel("Configuración del Gestor", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headerLabel, gbc);
        labels.add(headerLabel); // Añadir a la lista

        JLabel themeLabel = new JLabel("Seleccionar Tema:");
        themeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(themeLabel, gbc);
        labels.add(themeLabel); // Añadir a la lista

        themeComboBox = new JComboBox<>(new String[]{"Claro", "Oscuro"});
        themeComboBox.setFont(new Font("Arial", Font.BOLD, 16));
        themeComboBox.setBackground(Color.white);
        themeComboBox.setForeground(Color.BLACK);
        themeComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        themeComboBox.setSelectedItem(Tema.getTemaActual() == Tema.Theme.CLARO ? "Claro" : "Oscuro");
        themeComboBox.addActionListener(e -> {
            String selectedTheme = (String) themeComboBox.getSelectedItem();
            Tema.setTema("Claro".equals(selectedTheme) ? Tema.Theme.CLARO : Tema.Theme.OSCURO);
        });
        gbc.gridx = 1;
        mainPanel.add(themeComboBox, gbc);

        // Botones principales
        addLabelWithButton("Cambiar Contraseña:", "Cambiar Contraseña", gbc, 0, 2, e -> cambiarContraseña(username));
        addLabelWithButton("Cerrar Sesión:", "Cerrar Sesión", gbc, 0, 3, e -> {
            dispose();
            new ManagerLogin().setVisible(true);
        });

        // Botón Atrás con tooltip
        addBackButtonWithTooltip("Atrás", gbc, 0, 4, new Color(0, 51, 102), e -> {
            dispose();
            ManagerPerfil perfil = new ManagerPerfil();
            perfil.setVisible(true);
        });

        addBottomButton("Guardar Cambios", gbc, 1, 4, new Color(0, 51, 102), e -> {
            JOptionPane.showMessageDialog(mainPanel, "Configuración guardada correctamente.");
        });

        setWindowIcon("resources/imagenes/logo.png");

        add(mainPanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ManagerPerfil perfil = new ManagerPerfil();
                perfil.setVisible(true);
            }
        });
    }

    private void addLabelWithButton(String labelText, String buttonText, GridBagConstraints gbc, int x, int y, ActionListener action) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = x;
        gbc.gridy = y;
        mainPanel.add(label, gbc);
        labels.add(label); // Añadir a la lista

        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(153, 204, 255));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.addActionListener(action);
        gbc.gridx = x + 1;
        mainPanel.add(button, gbc);
    }

    private void addBottomButton(String text, GridBagConstraints gbc, int x, int y, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.addActionListener(action);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(button, gbc);
    }

    private void addBackButtonWithTooltip(String text, GridBagConstraints gbc, int x, int y, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.addActionListener(action);

        // Crear y configurar el JToolTip
        JToolTip tooltip = new JToolTip();
        tooltip.setTipText("Volver"); // Texto por defecto
        tooltip.setBackground(Color.GRAY); // Fondo gris
        tooltip.setForeground(Color.WHITE); // Texto en blanco

        // Añadir MouseListener para cambiar el tooltip y el color cuando el ratón pasa por encima
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Cambiar texto y color del tooltip cuando el ratón entra
                tooltip.setTipText("Volver");
                tooltip.setBackground(Color.LIGHT_GRAY);  // Fondo más claro
                tooltip.setForeground(Color.BLACK); // Cambiar texto a negro
                tooltip.setLocation(button.getLocationOnScreen().x, button.getLocationOnScreen().y - tooltip.getPreferredSize().height);
                tooltip.setVisible(true);  // Mostrar el tooltip
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tooltip.setVisible(false);  // Ocultar el tooltip cuando el ratón sale
            }
        });

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(button, gbc);
    }

    private void setWindowIcon(String iconPath) {
        try {
            Image icon = ImageIO.read(new File(iconPath));
            setIconImage(icon);
        } catch (IOException e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }

    private void cambiarContraseña(String username) {
        String nuevaContraseña = JOptionPane.showInputDialog(this, "Introduce la nueva contraseña:");
        if (nuevaContraseña != null && !nuevaContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Contraseña para el usuario " + username + " ha sido actualizada.");
        }
    }

    private void applyTheme() {
        if (Tema.getTemaActual() == Tema.Theme.OSCURO) {
            mainPanel.setBackground(Color.DARK_GRAY);
            headerLabel.setForeground(Color.WHITE);
            for (JLabel label : labels) {
                label.setForeground(Color.WHITE); // Cambiar texto a blanco
            }
        } else {
            mainPanel.setBackground(new Color(196, 238, 255));
            headerLabel.setForeground(Color.BLACK);
            for (JLabel label : labels) {
                label.setForeground(Color.BLACK); // Cambiar texto a negro
            }
        }
    }

    @Override
    public void onThemeChanged(Tema.Theme theme) {
        applyTheme();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerConfig managerConfig = new ManagerConfig("usuario");
            managerConfig.setVisible(true);
        });
    }
}
