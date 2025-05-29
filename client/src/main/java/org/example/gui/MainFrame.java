package org.example.gui;

import org.example.network.RequestManager;
import org.example.network.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainFrame extends JFrame {
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JComboBox<String> languageComboBox;
    private Locale currentLocale = new Locale("ru", "RU");
    private MainMenu mainMenu;
    private RequestManager requestManager;
    private User user;



    public MainFrame(RequestManager requestManager) {
        this.requestManager = requestManager;
        setTitle(getLocalizedString("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        // Панель выбора языка
        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        languagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] languages = {"Русский", "Suomi (Финский)", "Shqip (Албанский)", "Español (Коста-Рика)"};
        languageComboBox = new JComboBox<>(languages);
        languageComboBox.setPreferredSize(new Dimension(180, 25));
        languageComboBox.addActionListener(this::changeLanguage);

        languagePanel.add(languageComboBox);

        // Основная панель
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, requestManager);
        registerPanel = new RegisterPanel(this, requestManager);
        mainMenu = new MainMenu(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(mainMenu, "mainMenu");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(languagePanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        showLoginPanel();
    }

    private void changeLanguage(ActionEvent e) {
        String selected = (String) languageComboBox.getSelectedItem();
        Locale newLocale = LocalizationManager.getLocale(selected);
        if (newLocale != null) {
            currentLocale = newLocale;
            updateLocalization();
        }
    }

    private void updateLocalization() {
        setTitle(getLocalizedString("title"));
        loginPanel.updateLocalization();
        registerPanel.updateLocalization();
        if (mainMenu != null) {
            mainMenu.updateLocalization();
        }
    }

    public String getLocalizedString(String key) {
        return LocalizationManager.getLocalizedString(currentLocale, key);
    }

    public void showMainMenu() {
        cardLayout.show(mainPanel, "mainMenu");
        setTitle(getLocalizedString("mainmenu.title"));
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "login");
        setTitle(getLocalizedString("login.title"));
    }

    public void showRegisterPanel() {
        cardLayout.show(mainPanel, "register");
        setTitle(getLocalizedString("register.title"));
    }

    public User getUser() {
        return user;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public void setUser(User user) {
        this.user = user;
        if (mainMenu != null) {
            mainMenu.setCurrentUser(this.user);
        }
    }
}