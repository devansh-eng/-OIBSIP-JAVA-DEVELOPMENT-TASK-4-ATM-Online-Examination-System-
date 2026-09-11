package ui.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import app.MainFrame;
import model.User;
import ui.Theme;
import ui.components.ModernButton;
import ui.components.ModernPanel;

public class LoginScreen extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_DARK);

        // Main Login Card
        ModernPanel card = new ModernPanel(20, Theme.CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(420, 480));
        card.setMinimumSize(new Dimension(420, 480));
        card.setMaximumSize(new Dimension(420, 480));

        // Title
        JLabel titleLabel = new JLabel("Exam Portal", SwingConstants.CENTER);
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.ACCENT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign in to start your examination", SwingConstants.CENTER);
        subtitleLabel.setFont(Theme.BODY_FONT);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Input Fields Panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Username Field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(Theme.BODY_BOLD);
        usernameLabel.setForeground(Theme.TEXT_MAIN);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);

        usernameField = new JTextField();
        styleTextField(usernameField);
        usernameField.setAlignmentX(LEFT_ALIGNMENT);

        // Password Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(Theme.BODY_BOLD);
        passwordLabel.setForeground(Theme.TEXT_MAIN);
        passwordLabel.setAlignmentX(LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordField.setAlignmentX(LEFT_ALIGNMENT);

        // Buttons
        ModernButton loginBtn = new ModernButton("Login", Theme.ACCENT, Theme.TEXT_DARK);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());

        ModernButton registerBtn = new ModernButton("Register New Account", Theme.INPUT_BG, Theme.TEXT_MUTED);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        registerBtn.setAlignmentX(CENTER_ALIGNMENT);
        registerBtn.setCornerRadius(10);
        registerBtn.addActionListener(e -> handleRegister());

        // Assemble card
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(subtitleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        card.add(usernameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(usernameField);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        card.add(passwordLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(registerBtn);

        // Center card on screen
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(card, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(Theme.INPUT_BG);
        field.setForeground(Theme.TEXT_MAIN);
        field.setCaretColor(Theme.ACCENT);
        field.setFont(Theme.BODY_FONT);
        field.setBorder(Theme.FIELD_BORDER);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(340, 42));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(Theme.FIELD_FOCUS_BORDER);
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(Theme.FIELD_BORDER);
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty!");
            return;
        }

        User user = mainFrame.authenticate(username, password);
        if (user != null) {
            usernameField.setText("");
            passwordField.setText("");
            mainFrame.showProfile(user);
        } else {
            showError("Invalid username or password!");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty for registration!");
            return;
        }

        if (mainFrame.userExists(username)) {
            showError("Username is already taken!");
            return;
        }

        User newUser = new User(username, password, username); // Default display name same as username
        mainFrame.registerUser(newUser);
        JOptionPane.showMessageDialog(this, 
            "Registration successful! You can now log in.", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Authentication Error", JOptionPane.ERROR_MESSAGE);
    }
}
