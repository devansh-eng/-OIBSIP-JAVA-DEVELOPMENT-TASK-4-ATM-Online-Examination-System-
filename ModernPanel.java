package ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import ui.Theme;

public class ModernPanel extends JPanel {
    private int cornerRadius = 15;
    private java.awt.Color borderColor = null;
    private int borderThickness = 1;

    public ModernPanel() {
        setOpaque(false);
        setBackground(Theme.CARD_BG);
    }

    public ModernPanel(int cornerRadius) {
        this();
        this.cornerRadius = cornerRadius;
    }

    public ModernPanel(int cornerRadius, java.awt.Color bg) {
        setOpaque(false);
        setBackground(bg);
        this.cornerRadius = cornerRadius;
    }

    public void setBorderColor(java.awt.Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw border if specified
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new java.awt.BasicStroke(borderThickness));
            g2.drawRoundRect(
                borderThickness / 2, 
                borderThickness / 2, 
                getWidth() - borderThickness, 
                getHeight() - borderThickness, 
                cornerRadius, 
                cornerRadius
            );
        }

        g2.dispose();
    }
}
