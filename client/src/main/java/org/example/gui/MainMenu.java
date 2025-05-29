package org.example.gui;

import org.example.network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainMenu extends JPanel {
    private MainFrame mainFrame;
    private JPanel contentPanel;
    private User currentUser;
    private JLabel userLabel;
    private BandTablePanel tablePanel;
    private BandGraphPanel graphPanel;
    private GuiCommandManager commandManager;
    private JButton tableBtn;
    private JButton graphBtn;
    private JLabel commandsTitleLabel;

    public MainMenu(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.commandManager = new GuiCommandManager(mainFrame, this);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });

        JPanel leftPanel = commandManager.createCommandsPanel();
        add(leftPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(createUserInfoPanel(), BorderLayout.NORTH);
        contentPanel.add(createTopPanel(), BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        commandManager.updateTableData();
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        this.userLabel = new JLabel(mainFrame.getLocalizedString("mainmenu.user") +
                (currentUser != null ? currentUser.getLogin() :
                        mainFrame.getLocalizedString("mainmenu.unknown_user")));
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(userLabel);

        return panel;
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        tableBtn = new JButton(mainFrame.getLocalizedString("mainmenu.table_btn"));
        graphBtn = new JButton(mainFrame.getLocalizedString("mainmenu.graph_btn"));

        styleButton(tableBtn, true);
        styleButton(graphBtn, false);

        tablePanel = new BandTablePanel(mainFrame, commandManager);
        graphPanel = new BandGraphPanel(mainFrame, commandManager);

        CardLayout cardLayout = new CardLayout();
        JPanel tableGraphPanel = new JPanel(cardLayout);
        tableGraphPanel.add(tablePanel, "table");
        tableGraphPanel.add(graphPanel, "graph");

        tableBtn.addActionListener(e -> {
            cardLayout.show(tableGraphPanel, "table");
            styleButton(tableBtn, true);
            styleButton(graphBtn, false);
        });

        graphBtn.addActionListener(e -> {
            cardLayout.show(tableGraphPanel, "graph");
            styleButton(tableBtn, false);
            styleButton(graphBtn, true);
        });

        switchPanel.add(tableBtn);
        switchPanel.add(graphBtn);
        panel.add(switchPanel, BorderLayout.NORTH);
        panel.add(tableGraphPanel, BorderLayout.CENTER);

        return panel;
    }

    private void styleButton(JButton btn, boolean active) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(active ? new Color(70, 130, 180) : new Color(200, 200, 200));
        btn.setForeground(active ? Color.WHITE : Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public void updateLocalization() {
        userLabel.setText(mainFrame.getLocalizedString("mainmenu.user") +
                (currentUser != null ? currentUser.getLogin() :
                        mainFrame.getLocalizedString("mainmenu.unknown_user")));

        if (tableBtn != null) tableBtn.setText(mainFrame.getLocalizedString("mainmenu.table_btn"));
        if (graphBtn != null) graphBtn.setText(mainFrame.getLocalizedString("mainmenu.graph_btn"));

        // Обновляем локализацию кнопок команд
        commandManager.updateLocalization();

        if (tablePanel != null) tablePanel.updateLocalization();
        if (graphPanel != null) graphPanel.updateLocalization();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (userLabel != null) {
            userLabel.setText(mainFrame.getLocalizedString("mainmenu.user") +
                    (user != null ? user.getLogin() :
                            mainFrame.getLocalizedString("mainmenu.unknown_user")));
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public BandTablePanel getTablePanel() {
        return tablePanel;
    }

    public BandGraphPanel getGraphPanel() {
        return graphPanel;
    }
}