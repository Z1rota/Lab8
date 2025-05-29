package org.example.gui;

import org.example.builders.MusicBandsBuilder;
import org.example.commands.*;
import org.example.mainClasses.*;
import org.example.mainClasses.Label;
import org.example.network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class GuiCommandManager {
    private MainFrame mainFrame;
    private MainMenu mainMenu;
    private BandTablePanel tablePanel;
    private BandGraphPanel graphPanel;
    private JLabel commandsTitleLabel;

    public GuiCommandManager(MainFrame mainFrame, MainMenu mainMenu) {
        this.mainFrame = mainFrame;
        this.mainMenu = mainMenu;
    }

    public JPanel createCommandsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setPreferredSize(new Dimension(200, 0));

        // Сохраняем ссылку на заголовок
        commandsTitleLabel = new JLabel(mainFrame.getLocalizedString("commands.title"));
        commandsTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        commandsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(commandsTitleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        createCommandButton(panel, "Add", mainFrame.getLocalizedString("command.add"));
        createCommandButton(panel, "Clear", mainFrame.getLocalizedString("command.clear"));
        createCommandButton(panel, "ExecuteScript", mainFrame.getLocalizedString("command.execute_script"));
        createCommandButton(panel, "GroupCountingByLabel", mainFrame.getLocalizedString("command.group_counting"));
        createCommandButton(panel, "Help", mainFrame.getLocalizedString("command.help"));
        createCommandButton(panel, "Info", mainFrame.getLocalizedString("command.info"));
        createCommandButton(panel, "PrintDescending", mainFrame.getLocalizedString("command.print_descending"));
        createCommandButton(panel, "PrintFieldAscendingLabel", mainFrame.getLocalizedString("command.print_field_ascending"));
        createCommandButton(panel, "RemoveAt", mainFrame.getLocalizedString("command.remove_at"));
        createCommandButton(panel, "RemoveFirst", mainFrame.getLocalizedString("command.remove_first"));
        createCommandButton(panel, "Shuffle", mainFrame.getLocalizedString("command.shuffle"));
        createCommandButton(panel, "UpdateId", mainFrame.getLocalizedString("command.update_id"));

        return panel;
    }

    private void createCommandButton(JPanel panel, String commandName, String buttonText) {
        JButton btn = new JButton(buttonText);
        styleCommandButton(btn);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 30));
        btn.setActionCommand(commandName);

        btn.addActionListener(e -> handleCommand(commandName));

        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void styleCommandButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setBackground(new Color(220, 220, 220));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void handleCommand(String commandName) {
        switch (commandName) {
            case "Add": showAddDialog(null); break;
            case "RemoveById": showRemoveByIdDialog(); break;
            case "RemoveAt": showRemoveAtDialog(); break;
            case "UpdateId": showUpdateIdDialog(); break;
            case "ExecuteScript": executeScript(); break;
            default: executeSimpleCommand(commandName); break;
        }
    }

    public void showAddDialog(ActionEvent e) {
        JDialog dialog = new JDialog(mainFrame, mainFrame.getLocalizedString("dialog.add.title"), true);
        dialog.setLayout(new GridLayout(11, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JTextField participantsField = new JTextField();
        JTextField dateField = new JTextField();
        JComboBox<MusicGenre> genreCombo = new JComboBox<>(MusicGenre.values());
        JTextField labelNameField = new JTextField();
        JTextField labelBandsField = new JTextField();
        JTextField labelSalesField = new JTextField();

        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.name")));
        dialog.add(nameField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.x")));
        dialog.add(xField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.y")));
        dialog.add(yField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.participants")));
        dialog.add(participantsField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.date")));
        dialog.add(dateField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.genre")));
        dialog.add(genreCombo);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.label")));
        dialog.add(labelNameField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.label_bands")));
        dialog.add(labelBandsField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.label_sales")));
        dialog.add(labelSalesField);

        JButton okBtn = new JButton(mainFrame.getLocalizedString("button.add"));
        JButton cancelBtn = new JButton(mainFrame.getLocalizedString("button.cancel"));

        okBtn.addActionListener(ev -> {
            try {
                Coordinates coord = new Coordinates(
                        Float.parseFloat(xField.getText()),
                        Long.parseLong(yField.getText())
                );

                Label label = new Label(
                        labelNameField.getText(),
                        Integer.parseInt(labelBandsField.getText()),
                        Long.parseLong(labelSalesField.getText())
                );

                MusicBand newBand = new MusicBand(
                        nameField.getText(),
                        coord,
                        Integer.parseInt(participantsField.getText()),
                        LocalDateTime.now(),
                        (MusicGenre) genreCombo.getSelectedItem(),
                        label
                );
                newBand.setUserLogin(mainMenu.getCurrentUser() != null ? mainMenu.getCurrentUser().getLogin() : "");

                new Thread(() -> {
                    try {
                        Response response = mainFrame.getRequestManager().getClient().sendRequest(
                                new Request(new Add(), newBand, mainFrame.getUser())
                        );

                        SwingUtilities.invokeLater(() -> {
                            if (response.getLoginError() == null) {
                                showResponseDialog(response.getResult());
                                updateTableData();
                                dialog.dispose();
                            } else {
                                showErrorDialog(mainFrame.getLocalizedString("error.add") + response.getLoginError());
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                                showErrorDialog(mainFrame.getLocalizedString("error.add") + ex.getMessage()));
                    }
                }).start();
            } catch (Exception ex) {
                showErrorDialog(mainFrame.getLocalizedString("error.input") + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());

        dialog.add(okBtn);
        dialog.add(cancelBtn);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    public void showRemoveByIdDialog() {
        JDialog dialog = new JDialog(mainFrame, mainFrame.getLocalizedString("dialog.remove.title"), true);
        dialog.setLayout(new GridLayout(2, 2, 5, 5));

        JTextField idField = new JTextField();
        JButton okBtn = new JButton(mainFrame.getLocalizedString("button.remove"));
        JButton cancelBtn = new JButton(mainFrame.getLocalizedString("button.cancel"));

        okBtn.addActionListener(e -> {
            try {
                String input = idField.getText().trim();
                if (input.isEmpty()) {
                    showErrorDialog(mainFrame.getLocalizedString("error.empty_id"));
                    return;
                }

                long id = Long.parseLong(input);
                dialog.dispose();

                new Thread(() -> {
                    try {
                        Response response = mainFrame.getRequestManager().getClient().sendRequest(
                                new Request(new RemoveById(), id, mainFrame.getUser())
                        );

                        SwingUtilities.invokeLater(() -> {
                            if (response == null) {
                                showErrorDialog(mainFrame.getLocalizedString("error.no_response"));
                                return;
                            }

                            if (response.getOperationflag()) {
                                showResponseDialog(mainFrame.getLocalizedString("success.remove"));
                                updateTableData();
                            } else {
                                showErrorDialog(mainFrame.getLocalizedString("error.remove_failed"));
                                updateTableData();
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            showErrorDialog(mainFrame.getLocalizedString("error.connection") + ex.getMessage());
                            updateTableData();
                        });
                    }
                }).start();

            } catch (NumberFormatException ex) {
                showErrorDialog(mainFrame.getLocalizedString("error.invalid_id"));
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.remove.id")));
        dialog.add(idField);
        dialog.add(okBtn);
        dialog.add(cancelBtn);

        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    public void showRemoveAtDialog() {
        JDialog dialog = new JDialog(mainFrame, mainFrame.getLocalizedString("dialog.remove_at.title"), true);
        dialog.setLayout(new GridLayout(2, 2, 5, 5));

        JTextField indexField = new JTextField();
        JButton okBtn = new JButton(mainFrame.getLocalizedString("button.remove"));
        JButton cancelBtn = new JButton(mainFrame.getLocalizedString("button.cancel"));

        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.remove_at.index")));
        dialog.add(indexField);
        dialog.add(okBtn);
        dialog.add(cancelBtn);

        okBtn.addActionListener(e -> {
            try {
                long index = Long.parseLong(indexField.getText());

                new Thread(() -> {
                    try {
                        Response response = mainFrame.getRequestManager().getClient().sendRequest(
                                new Request(new RemoveAt(), index, mainFrame.getUser())
                        );

                        SwingUtilities.invokeLater(() -> {
                            if (response.getOperationflag()) {
                                showResponseDialog(response.getResult());
                                updateTableData();
                                dialog.dispose();
                            } else {
                                showErrorDialog(mainFrame.getLocalizedString("error.remove_failed"));
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                                showErrorDialog(mainFrame.getLocalizedString("error.remove_at") + ex.getMessage()));
                    }
                }).start();
            } catch (NumberFormatException ex) {
                showErrorDialog(mainFrame.getLocalizedString("error.invalid_index"));
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    public void showUpdateIdDialog() {
        JDialog dialog = new JDialog(mainFrame, mainFrame.getLocalizedString("dialog.update.title"), true);
        dialog.setLayout(new GridLayout(2, 1, 5, 5));

        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JTextField idField = new JTextField();
        inputPanel.add(new JLabel(mainFrame.getLocalizedString("dialog.update.id")));
        inputPanel.add(idField);

        JButton okBtn = new JButton(mainFrame.getLocalizedString("button.continue"));
        JButton cancelBtn = new JButton(mainFrame.getLocalizedString("button.cancel"));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(inputPanel);
        dialog.add(buttonPanel);

        okBtn.addActionListener(e -> {
            try {
                long id = Long.parseLong(idField.getText());
                dialog.dispose();
                showUpdateDialog(id);
            } catch (NumberFormatException ex) {
                showErrorDialog(mainFrame.getLocalizedString("error.invalid_id"));
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    public void showUpdateDialog(long id) {
        JDialog dialog = new JDialog(mainFrame, mainFrame.getLocalizedString("dialog.update_band.title"), true);
        dialog.setLayout(new GridLayout(11, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JTextField participantsField = new JTextField();
        JTextField dateField = new JTextField();
        JComboBox<MusicGenre> genreCombo = new JComboBox<>(MusicGenre.values());
        JTextField labelNameField = new JTextField();
        JTextField labelBandsField = new JTextField();
        JTextField labelSalesField = new JTextField();

        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.name")));
        dialog.add(nameField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.x")));
        dialog.add(xField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.y")));
        dialog.add(yField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.participants")));
        dialog.add(participantsField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.date")));
        dialog.add(dateField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.genre")));
        dialog.add(genreCombo);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.label")));
        dialog.add(labelNameField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.label_bands")));
        dialog.add(labelBandsField);
        dialog.add(new JLabel(mainFrame.getLocalizedString("dialog.add.label_sales")));
        dialog.add(labelSalesField);

        JButton okBtn = new JButton(mainFrame.getLocalizedString("button.update"));
        JButton cancelBtn = new JButton(mainFrame.getLocalizedString("button.cancel"));

        okBtn.addActionListener(ev -> {
            try {
                Coordinates coord = new Coordinates(
                        Float.parseFloat(xField.getText()),
                        Long.parseLong(yField.getText())
                );

                Label label = new Label(
                        labelNameField.getText(),
                        Integer.parseInt(labelBandsField.getText()),
                        Long.parseLong(labelSalesField.getText())
                );

                MusicBand updatedBand = new MusicBand(
                        nameField.getText(),
                        coord,
                        Integer.parseInt(participantsField.getText()),
                        LocalDateTime.now(),
                        (MusicGenre) genreCombo.getSelectedItem(),
                        label
                );
                updatedBand.setUserLogin(mainMenu.getCurrentUser() != null ? mainMenu.getCurrentUser().getLogin() : "");

                new Thread(() -> {
                    try {
                        Response response = mainFrame.getRequestManager().getClient().sendRequest(
                                new Request(new UpdateId(), updatedBand, id, mainFrame.getUser())
                        );

                        SwingUtilities.invokeLater(() -> {
                            if (response.getOperationflag()) {
                                showResponseDialog(mainFrame.getLocalizedString("success.update"));
                                updateTableData();
                                dialog.dispose();
                            } else {
                                showErrorDialog(mainFrame.getLocalizedString("error.update"));
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() ->
                                showErrorDialog(mainFrame.getLocalizedString("error.update") + ex.getMessage()));
                    }
                }).start();
            } catch (Exception ex) {
                showErrorDialog(mainFrame.getLocalizedString("error.input") + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());

        dialog.add(okBtn);
        dialog.add(cancelBtn);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    public void executeScript() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(mainFrame.getLocalizedString("dialog.script.title"));

        if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            File scriptFile = fileChooser.getSelectedFile();

            new Thread(() -> {
                try {
                    Set<String> visitedScripts = new HashSet<>();
                    if (hasRecursion(scriptFile.getAbsolutePath(), visitedScripts)) {
                        SwingUtilities.invokeLater(() ->
                                showErrorDialog(mainFrame.getLocalizedString("error.script_recursion")));
                        return;
                    }

                    User currentUser = mainFrame.getUser();
                    if (currentUser == null) {
                        SwingUtilities.invokeLater(() ->
                                showErrorDialog(mainFrame.getLocalizedString("error.script_auth")));
                        return;
                    }

                    try (Scanner scriptScanner = new Scanner(scriptFile)) {
                        while (scriptScanner.hasNextLine()) {
                            String line = scriptScanner.nextLine().trim();
                            if (line.isEmpty() || line.startsWith("#")) continue;

                            String[] parts = line.split(" ");
                            String commandName = parts[0];

                            if (commandName.equals("add")) {
                                MusicBand band = new MusicBandsBuilder(currentUser).create();
                                band.setUserLogin(currentUser.getLogin());
                                Response response = mainFrame.getRequestManager().getClient().sendRequest(
                                        new Request(new Add(), band, currentUser)
                                );
                            } else {
                                Response response = mainFrame.getRequestManager().getClient().sendRequest(
                                        new Request(getCommandByName(commandName), currentUser)
                                );
                            }
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        showResponseDialog(mainFrame.getLocalizedString("success.script"));
                        updateTableData();
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            showErrorDialog(mainFrame.getLocalizedString("error.script") + ex.getMessage()));
                }
            }).start();
        }
    }

    private boolean hasRecursion(String scriptName, Set<String> visitedScripts) {
        if (visitedScripts.contains(scriptName)) {
            return true;
        }

        visitedScripts.add(scriptName);

        try (Scanner fileScanner = new Scanner(new File(scriptName))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("execute_script")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        String nestedScript = parts[1];
                        if (hasRecursion(nestedScript, visitedScripts)) {
                            return true;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            SwingUtilities.invokeLater(() ->
                    showErrorDialog(mainFrame.getLocalizedString("error.script_not_found") + scriptName));
        }

        visitedScripts.remove(scriptName);
        return false;
    }

    public void executeSimpleCommand(String commandName) {
        new Thread(() -> {
            try {
                Command command = getCommandByName(commandName);
                Response response = mainFrame.getRequestManager().getClient().sendRequest(
                        new Request(command, mainFrame.getUser())
                );

                SwingUtilities.invokeLater(() -> {
                    if (response != null) {
                        if (response.getLoginError() == null) {
                            showResponseDialog(response.getResult());
                            if (commandName.equals("Clear") || commandName.equals("Shuffle") ||
                                    commandName.equals("RemoveFirst")) {
                                updateTableData();
                            }
                        } else {
                            showErrorDialog(mainFrame.getLocalizedString("error.command") + response.getLoginError());
                        }
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        showErrorDialog(mainFrame.getLocalizedString("error.command") + ex.getMessage()));
            }
        }).start();
    }

    private Command getCommandByName(String name) {
        switch (name) {
            case "Clear": return new Clear();
            case "GroupCountingByLabel": return new GroupCountingByLabel();
            case "Help": return new Help();
            case "Info": return new Info();
            case "PrintDescending": return new PrintDescending();
            case "PrintFieldAscendingLabel": return new PrintFieldAscendingLabel();
            case "RemoveFirst": return new RemoveFirst();
            case "Show": return new Show();
            case "Shuffle": return new Shuffle();
            default: return new Help();
        }
    }

    public void updateTableData() {
        new Thread(() -> {
            try {
                if(mainFrame.getUser() != null) {
                    Response response = mainFrame.getRequestManager().getClient().sendRequest(
                            new Request(new Show(), mainFrame.getUser())
                    );

                    if (response.getMusicBands() != null) {
                        List<MusicBand> bands = new ArrayList<>((Collection<MusicBand>) response.getMusicBands());

                        bands.forEach(b -> {
                            if (b.getUserLogin() == null) {
                                b.setUserLogin("unknown");
                            }
                        });

                        SwingUtilities.invokeLater(() -> {
                            if (mainMenu.getTablePanel() != null) {
                                mainMenu.getTablePanel().updateTable(bands);
                            }
                            if (mainMenu.getGraphPanel() != null) {
                                mainMenu.getGraphPanel().updateGraph(bands);
                            }
                        });
                    }
                } else {
                    updateTableData();
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        showErrorDialog(mainFrame.getLocalizedString("error.load_data") + ex.getMessage()));
            }
        }).start();
    }

    public void updateBandOnServer(MusicBand band) {
        if (band.getUserLogin() != null &&
                !band.getUserLogin().equals(mainFrame.getUser().getLogin())) {
            return;
        }

        new Thread(() -> {
            try {
                Response response = mainFrame.getRequestManager().getClient().sendRequest(
                        new Request(new UpdateId(), band, band.getId(), mainFrame.getUser())
                );

                SwingUtilities.invokeLater(() -> {
                    if (response.getLoginError() == null) {
                        showResponseDialog(mainFrame.getLocalizedString("success.update"));
                    } else {
                        showErrorDialog(mainFrame.getLocalizedString("error.update") + response.getLoginError());
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        showErrorDialog(mainFrame.getLocalizedString("error.connection") + ex.getMessage()));
            }
        }).start();
    }

    public void showBandInfo(MusicBand band) {
        JDialog infoDialog = new JDialog(mainFrame, mainFrame.getLocalizedString("dialog.band_info.title"), true);
        infoDialog.setLayout(new GridLayout(0, 2, 5, 5));

        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.id"), String.valueOf(band.getId()));
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.name"), band.getName());
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.x"), String.valueOf(band.getCoordinates().getX()));
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.y"), String.valueOf(band.getCoordinates().getY()));
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.participants"), String.valueOf(band.getNumberOfParticipants()));
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.date"), band.getCreationDate().toString());
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.genre"), band.getGenre().name());
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.label"), band.getLabel().getName());
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.label_bands"), String.valueOf(band.getLabel().getBands()));
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.label_sales"), String.valueOf(band.getLabel().getSales()));
        addInfoRow(infoDialog, mainFrame.getLocalizedString("dialog.band_info.owner"), band.getUserLogin());

        JButton closeBtn = new JButton(mainFrame.getLocalizedString("button.close"));
        closeBtn.addActionListener(e -> infoDialog.dispose());

        infoDialog.add(new JLabel());
        infoDialog.add(closeBtn);
        infoDialog.pack();
        infoDialog.setLocationRelativeTo(mainFrame);
        infoDialog.setVisible(true);
    }

    public void updateLocalization() {
        // Обновляем заголовок
        if (commandsTitleLabel != null) {
            commandsTitleLabel.setText(mainFrame.getLocalizedString("commands.title"));
        }

        // Обновляем кнопки команд
        Component[] components = mainMenu.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                updateButtonsLocalization(panel);
            }
        }
    }

    private void updateButtonsLocalization(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String command = button.getActionCommand();
                if (command != null) {
                    String key = "command." + command.toLowerCase();
                    button.setText(mainFrame.getLocalizedString(key));
                }
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().equals(mainFrame.getLocalizedString("commands.title"))) {
                    label.setText(mainFrame.getLocalizedString("commands.title"));
                }
            }
        }
    }

    private void addInfoRow(JDialog dialog, String label, String value) {
        dialog.add(new JLabel(label));
        dialog.add(new JLabel(value));
    }

    public void showResponseDialog(String message) {
        JOptionPane.showMessageDialog(mainFrame, message,
                mainFrame.getLocalizedString("dialog.response.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(mainFrame, message,
                mainFrame.getLocalizedString("dialog.error.title"),
                JOptionPane.ERROR_MESSAGE);
    }
}