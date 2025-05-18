package com.bibliotheque.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    public static final int TYPE_PRIMARY = 1;
    public static final int TYPE_SECONDARY = 2;
    public static final int TYPE_DANGER = 3;

    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final int BORDER_RADIUS = 10;
    private Color defaultColor;
    private Color hoverColor;

    public CustomButton(String text) {
        this(text, TYPE_PRIMARY);
    }

    public CustomButton(String text, int type) {
        super(text);
        setFont(BUTTON_FONT);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        applyTypeStyle(type);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultColor);
            }
        });
    }

    private void applyTypeStyle(int type) {
        switch (type) {
            case TYPE_DANGER -> {
                defaultColor = new Color(231, 76, 60);
                hoverColor = new Color(192, 57, 43);
            }
            case TYPE_SECONDARY -> {
                defaultColor = new Color(149, 165, 166);
                hoverColor = new Color(127, 140, 141);
            }
            case TYPE_PRIMARY -> {
                defaultColor = new Color(52, 152, 219);
                hoverColor = new Color(41, 128, 185);
            }
            default -> {
                defaultColor = new Color(52, 152, 219);
                hoverColor = new Color(41, 128, 185);
            }
        }
        setBackground(defaultColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
        super.paintComponent(g2);
        g2.dispose();
    }
}
