package ui.screens;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

public class ProfileScreen extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;

    private JTextField displayNameField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ProfileScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_DARK);
    }

    public void setUser(User user) {
        this.currentUser = user;
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private void initUI() {
        // Profile Card
        ModernPanel card = new ModernPanel(20, Theme.CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(35, 40, 35, 40));
        card.setPreferredSize(new Dimension(460, 560));
        card.setMinimumSize(new Dimension(460, 560));
        card.setMaximumSize(new Dimension(460, 560));

        // Header Title
        JLabel titleLabel = new JLabel("Welcome, " + currentUser.getDisplayName() + "!", SwingConstants.CENTER);
        titleLabel.setFont(Theme.SUBTITLE_FONT);
        titleLabel.setForeground(Theme.ACCENT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Update profile details before starting the exam", SwingConstants.CENTER);
        subtitleLabel.setFont(Theme.BODY_FONT);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Fields
        JLabel nameLabel = new JLabel("Display Name");
        nameLabel.setFont(Theme.BODY_BOLD);
        nameLabel.setForeground(Theme.TEXT_MAIN);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        displayNameField = new JTextField(currentUser.getDisplayName());
        styleTextField(displayNameField);
        displayNameField.setAlignmentX(LEFT_ALIGNMENT);

        JLabel currPassLabel = new JLabel("Current Password (to save changes)");
        currPassLabel.setFont(Theme.BODY_BOLD);
        currPassLabel.setForeground(Theme.TEXT_MAIN);
        currPassLabel.setAlignmentX(LEFT_ALIGNMENT);

        currentPasswordField = new JPasswordField();
        styleTextField(currentPasswordField);
        currentPasswordField.setAlignmentX(LEFT_ALIGNMENT);

        JLabel newPassLabel = new JLabel("New Password (optional)");
        newPassLabel.setFont(Theme.BODY_BOLD);
        newPassLabel.setForeground(Theme.TEXT_MAIN);
        newPassLabel.setAlignmentX(LEFT_ALIGNMENT);

        newPasswordField = new JPasswordField();
        styleTextField(newPasswordField);
        newPasswordField.setAlignmentX(LEFT_ALIGNMENT);

        JLabel confirmPassLabel = new JLabel("Confirm New Password");
        confirmPassLabel.setFont(Theme.BODY_BOLD);
        confirmPassLabel.setForeground(Theme.TEXT_MAIN);
        confirmPassLabel.setAlignmentX(LEFT_ALIGNMENT);

        confirmPasswordField = new JPasswordField();
        styleTextField(confirmPasswordField);
        confirmPasswordField.setAlignmentX(LEFT_ALIGNMENT);

        // Buttons Panel
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setAlignmentX(CENTER_ALIGNMENT);

        ModernButton saveBtn = new ModernButton("Save Changes", Theme.SECONDARY, Theme.TEXT_DARK);
        saveBtn.setPreferredSize(new Dimension(175, 45));
        saveBtn.setMaximumSize(new Dimension(175, 45));
        saveBtn.addActionListener(e -> handleSaveChanges());

        ModernButton startBtn = new ModernButton("Start Exam", Theme.SUCCESS, Theme.TEXT_DARK);
        startBtn.setPreferredSize(new Dimension(175, 45));
        startBtn.setMaximumSize(new Dimension(175, 45));
        startBtn.addActionListener(e -> mainFrame.startExam(currentUser));

        actionPanel.add(saveBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        actionPanel.add(startBtn);

        ModernButton logoutBtn = new ModernButton("Logout", Theme.INPUT_BG, Theme.DANGER);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.setAlignmentX(CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> mainFrame.logout());

        // Assembly
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtitleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(displayNameField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        card.add(currPassLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(currentPasswordField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        card.add(newPassLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(newPasswordField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        card.add(confirmPassLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(confirmPasswordField);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        card.add(actionPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(logoutBtn);

        // Center card
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
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(380, 40));

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

    private void handleSaveChanges() {
        String newName = displayNameField.getText().trim();
        String currentPass = new String(currentPasswordField.getPassword()).trim();
        String newPass = new String(newPasswordField.getPassword()).trim();
        String confirmPass = new String(confirmPasswordField.getPassword()).trim();

        if (newName.isEmpty()) {
            showError("Display name cannot be empty!");
            return;
        }

        if (currentPass.isEmpty()) {
            showError("Please enter your current password to save changes!");
            return;
        }

        if (!currentPass.equals(currentUser.getPassword())) {
            showError("Incorrect current password!");
            return;
        }

        // Handle password change if specified
        if (!newPass.isEmpty()) {
            if (!newPass.equals(confirmPass)) {
                showError("New password and confirm password do not match!");
                return;
            }
            currentUser.setPassword(newPass);
        }

        currentUser.setDisplayName(newName);
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");

        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        setUser(currentUser); // Refresh welcome heading
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Update Error", JOptionPane.ERROR_MESSAGE);
    }
}
