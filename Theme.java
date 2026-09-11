package ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class Theme {
    // Colors (Catppuccin Mocha inspired)
    public static final Color BG_DARK = new Color(0x1e, 0x1e, 0x2e);      // Outer background
    public static final Color CARD_BG = new Color(0x25, 0x25, 0x38);      // Panel background
    public static final Color INPUT_BG = new Color(0x18, 0x18, 0x25);     // Text fields & sidebars
    
    public static final Color ACCENT = new Color(0xcb, 0xa6, 0xf7);       // Lavender primary
    public static final Color ACCENT_HOVER = new Color(0xb4, 0xbe, 0xfe); // Hover state
    public static final Color SECONDARY = new Color(0x89, 0xb4, 0xfa);    // Soft Blue
    
    public static final Color TEXT_MAIN = new Color(0xcd, 0xd6, 0xf4);    // Light text
    public static final Color TEXT_MUTED = new Color(0xa6, 0xad, 0xc8);   // Gray text
    public static final Color TEXT_DARK = new Color(0x11, 0x11, 0x1b);    // Dark text (for high contrast buttons if needed)
    
    public static final Color SUCCESS = new Color(0xa6, 0xe3, 0xa1);      // Green
    public static final Color DANGER = new Color(0xf3, 0x8b, 0xa8);       // Red
    public static final Color WARNING = new Color(0xf9, 0xe2, 0xaf);      // Yellow
    
    public static final Color BORDER_COLOR = new Color(0x31, 0x32, 0x44);  // Subtle borders

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TIMER_FONT = new Font("Consolas", Font.BOLD, 24);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Borders
    public static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    );
    
    public static final Border FIELD_FOCUS_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(ACCENT, 1, true),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    );
}
