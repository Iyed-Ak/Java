package com.bibliotheque.ui.components;

import javax.swing.*;
import java.awt.*;

public class CustomPanel extends JPanel {
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_CARD = 1;
    public static final int TYPE_FORM = 2;

    public CustomPanel() {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public CustomPanel(LayoutManager layout) {
        super(layout);
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    public static JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(label, BorderLayout.WEST);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return panel;
    }

    public static JPanel createFormPanel(String[] labels, JComponent[] components) {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            form.add(new JLabel(labels[i] + ":"), gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            form.add(components[i], gbc);
        }

        return form;
    }

    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (JButton btn : buttons) {
            panel.add(btn);
        }
        return panel;
    }
}
