package org.example.gui;

import org.example.commands.Register;
import org.example.exceptions.InvalidDataException;
import org.example.network.Request;
import org.example.network.RequestManager;
import org.example.network.Response;
import org.example.network.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class RegisterPanel extends JPanel {
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private MainFrame mainFrame;
    private JLabel messageLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    private JButton registerButton;
    private JLabel backLabel;
    private RequestManager requestManager;


    public RegisterPanel(MainFrame mainFrame, RequestManager requestManager) {
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
        Dimension labelSize = new Dimension(70, 25);
        Dimension fieldSize = new Dimension(200, 30);
        Dimension buttonSize = new Dimension(200, 30);

        loginLabel = new JLabel(mainFrame.getLocalizedString("register.login.label"));
        loginLabel.setPreferredSize(labelSize);

        passwordLabel = new JLabel(mainFrame.getLocalizedString("register.password.label"));
        passwordLabel.setPreferredSize(labelSize);

        confirmPasswordLabel = new JLabel(mainFrame.getLocalizedString("register.confirm.label"));
        confirmPasswordLabel.setPreferredSize(labelSize);

        loginField = new JTextField();
        loginField.setPreferredSize(fieldSize);
        loginField.setBorder(createFieldBorder());

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        passwordField.setBorder(loginField.getBorder());

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(fieldSize);
        confirmPasswordField.setBorder(loginField.getBorder());

        registerButton = new JButton(mainFrame.getLocalizedString("register.button"));
        registerButton.setPreferredSize(buttonSize);
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBorder(BorderFactory.createEmptyBorder());
        registerButton.setFocusPainted(false);

        backLabel = new JLabel(mainFrame.getLocalizedString("register.back.link"));
        backLabel.setForeground(Color.GRAY);
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        // Подтверждение пароля
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(confirmPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(confirmPasswordField, gbc);

        // Кнопка регистрации
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 0, 0);

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonContainer.setOpaque(false);
        buttonContainer.add(registerButton);
        add(buttonContainer, gbc);

        // Ссылка "Назад"
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 5, 0);

        JPanel textContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        textContainer.setOpaque(false);
        textContainer.add(backLabel);
        add(textContainer, gbc);

        // Сообщение об ошибке
        gbc.gridy = 5;
        add(messageLabel, gbc);
    }

    private void setupListeners() {
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showLoginPanel();
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

        registerButton.addActionListener(e -> {
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                messageLabel.setText(mainFrame.getLocalizedString("register.passwords_mismatch"));
                return;
            }
            if (password.isBlank()) {
                messageLabel.setText(mainFrame.getLocalizedString("register.empty_password"));
                return;
            }

            User user = new User(login, password);

            new Thread(() -> {
                try {
                    Response response = requestManager.getClient().sendRequest(
                            new Request(new Register(), user)
                    );

                    SwingUtilities.invokeLater(() -> {
                        if (response.getLoginError() == null) {
                            mainFrame.setUser(user);
                            mainFrame.showMainMenu();
                        } else {
                            messageLabel.setText(mainFrame.getLocalizedString("register.error") + " " + response.getLoginError());
                        }
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            messageLabel.setText(mainFrame.getLocalizedString("register.connection_error")));
                }
            }).start();
        });
    }

    public void updateLocalization() {
        loginLabel.setText(mainFrame.getLocalizedString("register.login.label"));
        passwordLabel.setText(mainFrame.getLocalizedString("register.password.label"));
        confirmPasswordLabel.setText(mainFrame.getLocalizedString("register.confirm.label"));
        registerButton.setText(mainFrame.getLocalizedString("register.button"));
        backLabel.setText(mainFrame.getLocalizedString("register.back.link"));
        messageLabel.setText("");
    }

    private Border createFieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }
}
