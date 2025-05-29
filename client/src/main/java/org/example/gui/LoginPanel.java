package org.example.gui;

import org.example.commands.Login;
import org.example.network.Request;
import org.example.network.RequestManager;
import org.example.network.Response;
import org.example.network.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class LoginPanel extends JPanel {
    private JTextField loginField;
    private JPasswordField passwordField;
    private MainFrame mainFrame;
    private JLabel messageLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JLabel registerLabel;
    private RequestManager requestManager;
    private HashMap zalupa;

    public LoginPanel(MainFrame mainFrame, RequestManager requestManager) {
        this.mainFrame = mainFrame;
        this.requestManager = requestManager;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        setBackground(Color.WHITE);

        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        // Размеры компонентов
        Dimension labelSize = new Dimension(70, 25);
        Dimension fieldSize = new Dimension(200, 30);
        Dimension buttonSize = new Dimension(100, 30);

        loginLabel = new JLabel(mainFrame.getLocalizedString("login.username"));
        loginLabel.setPreferredSize(labelSize);

        passwordLabel = new JLabel(mainFrame.getLocalizedString("login.password"));
        passwordLabel.setPreferredSize(labelSize);

        loginField = new JTextField();
        loginField.setPreferredSize(fieldSize);
        loginField.setBorder(createFieldBorder());

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        passwordField.setBorder(loginField.getBorder());

        loginButton = new JButton(mainFrame.getLocalizedString("login.button"));
        loginButton.setPreferredSize(buttonSize);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setFocusPainted(false);

        registerLabel = new JLabel(mainFrame.getLocalizedString("login.no_account"));
        registerLabel.setForeground(Color.GRAY);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(new Color(223, 33, 33));
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Логин
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(loginLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(loginField, gbc);

        // Пароль
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordField, gbc);

        // Кнопка входа
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 0, 0);

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonContainer.setOpaque(false);
        buttonContainer.add(loginButton);
        add(buttonContainer, gbc);

        // Ссылка на регистрацию
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 0);

        JPanel textContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        textContainer.setOpaque(false);
        textContainer.add(registerLabel);
        add(textContainer, gbc);

        // Сообщение об ошибке
        gbc.gridy = 4;
        add(messageLabel, gbc);
    }

    private void setupListeners() {
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showRegisterPanel();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((JLabel) e.getSource()).setForeground(new Color(128, 0, 128));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JLabel) e.getSource()).setForeground(Color.GRAY);
            }
        });

        loginButton.addActionListener(e -> {
            try {
                char[] passwd = passwordField.getPassword();
                User user = new User(loginField.getText(), new String(passwd));

                new Thread(() -> {
                    try {
                        Response response = requestManager.getClient().sendRequest(
                                new Request(new Login(), user)
                        );

                        SwingUtilities.invokeLater(() -> {
                            if (response.getLoginError() == null) {
                                mainFrame.setUser(user);
                                mainFrame.showMainMenu();
                            } else {
                                messageLabel.setText(mainFrame.getLocalizedString("login.error"));
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                                messageLabel.setText(mainFrame.getLocalizedString("login.connection_error")));
                    }
                }).start();
            } catch (Exception ex) {
                messageLabel.setText(mainFrame.getLocalizedString("login.input_error"));
            }
        });
    }

    public void updateLocalization() {
        loginLabel.setText(mainFrame.getLocalizedString("login.username"));
        passwordLabel.setText(mainFrame.getLocalizedString("login.password"));
        loginButton.setText(mainFrame.getLocalizedString("login.button"));
        registerLabel.setText(mainFrame.getLocalizedString("login.no_account"));
    }

    private Border createFieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }
}