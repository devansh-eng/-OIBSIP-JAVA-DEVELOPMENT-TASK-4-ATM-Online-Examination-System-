package ui.components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import ui.Theme;

public class ModernButton extends JButton {
    private boolean isHovered = false;
    private int cornerRadius = 10;
    private java.awt.Color borderColor = null;

    public ModernButton(String text) {
        super(text);
        initColors(Theme.ACCENT, Theme.TEXT_DARK);
    }

    public ModernButton(String text, java.awt.Color bg, java.awt.Color fg) {
        super(text);
        initColors(bg, fg);
    }

    private void initColors(java.awt.Color bg, java.awt.Color fg) {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(bg);
        setForeground(fg);
        setFont(Theme.BODY_BOLD);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setBorderColor(java.awt.Color color) {
        this.borderColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        java.awt.Color bg = getBackground();
        if (!isEnabled()) {
            bg = Theme.BORDER_COLOR;
        } else if (isHovered) {
            bg = bg.equals(Theme.ACCENT) ? Theme.ACCENT_HOVER : bg.brighter();
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw custom border if specified
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new java.awt.BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
