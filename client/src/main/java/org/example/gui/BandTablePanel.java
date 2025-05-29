package org.example.gui;

import org.example.mainClasses.Label;
import org.example.network.*;
import org.example.mainClasses.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class BandTablePanel extends JPanel {
    private MainFrame mainFrame;
    private GuiCommandManager commandManager;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<MusicBand> musicBands = new ArrayList<>();
    private JLabel filterLabel;
    private JComboBox<String> filterCombo;
    private JTextField searchField;
    private JButton addBtn;
    private JButton removeBtn;
    private JLabel messageLabel;
    private boolean isUpdatingLocalization = false;

    public BandTablePanel(MainFrame mainFrame, GuiCommandManager commandManager) {
        this.mainFrame = mainFrame;
        this.commandManager = commandManager;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        filterLabel = new JLabel(mainFrame.getLocalizedString("table.filter_label"));
        filterCombo = new JComboBox<>();
        updateFilterComboItems();

        filterCombo.addActionListener(e -> {
            if (!isUpdatingLocalization && filterCombo.getSelectedIndex() >= 0) {
                int colIndex = filterCombo.getSelectedIndex();
                if (colIndex < tableModel.getColumnCount()) {
                    sorter.setSortable(colIndex, true);
                    sorter.setSortKeys(List.of(new RowSorter.SortKey(colIndex, SortOrder.ASCENDING)));
                }
            }
        });

        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filter(); }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        addBtn = new JButton(mainFrame.getLocalizedString("table.button.add"));
        removeBtn = new JButton(mainFrame.getLocalizedString("table.button.remove"));

        addBtn.addActionListener(e -> commandManager.showAddDialog(null));
        removeBtn.addActionListener(e -> commandManager.showRemoveByIdDialog());

        styleSmallButton(addBtn);
        styleSmallButton(removeBtn);

        searchPanel.add(filterLabel);
        searchPanel.add(filterCombo);
        searchPanel.add(searchField);
        searchPanel.add(addBtn);
        searchPanel.add(removeBtn);

        String[] columns = {
                mainFrame.getLocalizedString("table.column.id"),
                mainFrame.getLocalizedString("table.column.name"),
                mainFrame.getLocalizedString("table.column.x"),
                mainFrame.getLocalizedString("table.column.y"),
                mainFrame.getLocalizedString("table.column.participants"),
                mainFrame.getLocalizedString("table.column.date"),
                mainFrame.getLocalizedString("table.column.genre"),
                mainFrame.getLocalizedString("table.column.label"),
                mainFrame.getLocalizedString("table.column.label_bands"),
                mainFrame.getLocalizedString("table.column.sales")
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (row >= musicBands.size()) return false;
                MusicBand band = musicBands.get(row);
                String bandUserLogin = band.getUserLogin();
                String currentUserLogin = mainFrame.getUser() != null ? mainFrame.getUser().getLogin() : null;
                return currentUserLogin != null && currentUserLogin.equals(bandUserLogin);
            }
        };

        dataTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                int modelRow = convertRowIndexToModel(row);
                if (modelRow >= musicBands.size()) return c;

                MusicBand band = musicBands.get(modelRow);
                String bandUserLogin = band.getUserLogin();
                String currentUserLogin = mainFrame.getUser() != null ? mainFrame.getUser().getLogin() : null;

                if (bandUserLogin == null || !bandUserLogin.equals(currentUserLogin)) {
                    c.setBackground(new Color(255, 230, 230));
                } else {
                    c.setBackground(getBackground());
                }
                return c;
            }
        };

        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (row >= 0 && column >= 0 && row < musicBands.size()) {
                    MusicBand band = musicBands.get(row);
                    Object value = tableModel.getValueAt(row, column);
                    commandManager.updateBandOnServer(createUpdatedBand(band, column, value));
                }
            }
        });

        sorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(700, 400));

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateFilterComboItems() {
        isUpdatingLocalization = true;
        try {
            String selected = filterCombo.getSelectedItem() != null ?
                    filterCombo.getSelectedItem().toString() : null;

            filterCombo.removeAllItems();
            String[] columns = {
                    mainFrame.getLocalizedString("table.column.id"),
                    mainFrame.getLocalizedString("table.column.name"),
                    mainFrame.getLocalizedString("table.column.x"),
                    mainFrame.getLocalizedString("table.column.y"),
                    mainFrame.getLocalizedString("table.column.participants"),
                    mainFrame.getLocalizedString("table.column.date"),
                    mainFrame.getLocalizedString("table.column.genre"),
                    mainFrame.getLocalizedString("table.column.label"),
                    mainFrame.getLocalizedString("table.column.label_bands"),
                    mainFrame.getLocalizedString("table.column.sales")
            };

            for (String column : columns) {
                filterCombo.addItem(column);
            }

            if (selected != null) {
                for (int i = 0; i < filterCombo.getItemCount(); i++) {
                    if (filterCombo.getItemAt(i).equals(selected)) {
                        filterCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } finally {
            isUpdatingLocalization = false;
        }
    }

    public void updateLocalization() {
        if (filterLabel != null) {
            filterLabel.setText(mainFrame.getLocalizedString("table.filter_label"));
        }

        updateFilterComboItems();

        if (addBtn != null) {
            addBtn.setText(mainFrame.getLocalizedString("table.button.add"));
        }

        if (removeBtn != null) {
            removeBtn.setText(mainFrame.getLocalizedString("table.button.remove"));
        }

        if (tableModel != null) {
            String[] columns = {
                    mainFrame.getLocalizedString("table.column.id"),
                    mainFrame.getLocalizedString("table.column.name"),
                    mainFrame.getLocalizedString("table.column.x"),
                    mainFrame.getLocalizedString("table.column.y"),
                    mainFrame.getLocalizedString("table.column.participants"),
                    mainFrame.getLocalizedString("table.column.date"),
                    mainFrame.getLocalizedString("table.column.genre"),
                    mainFrame.getLocalizedString("table.column.label"),
                    mainFrame.getLocalizedString("table.column.label_bands"),
                    mainFrame.getLocalizedString("table.column.sales")
            };
            tableModel.setColumnIdentifiers(columns);
        }
    }

    private MusicBand createUpdatedBand(MusicBand original, int column, Object value) {
        try {
            MusicBand updated = new MusicBand(original);
            updated.setUserLogin(original.getUserLogin());

            switch (column) {
                case 1: updated.setName((String) value); break;
                case 2: updated.getCoordinates().setX(Float.parseFloat(value.toString())); break;
                case 3: updated.getCoordinates().setY(Long.parseLong(value.toString())); break;
                case 4: updated.setNumberOfParticipants(Integer.parseInt(value.toString())); break;
                case 5:
                    try {
                        LocalDateTime newDate = LocalDateTime.parse(value.toString());
                        updated.setCreationDate(newDate);
                    } catch (DateTimeParseException ex) {
                        SwingUtilities.invokeLater(() ->
                                commandManager.showErrorDialog(mainFrame.getLocalizedString("table.error.date_format")));
                    }
                    break;
                case 6: updated.setGenre(MusicGenre.valueOf(value.toString())); break;
                case 7:
                    if (updated.getLabel() == null) {
                        updated.setLabel(new Label(value.toString(), 0, 0));
                    } else {
                        Label oldLabel = updated.getLabel();
                        updated.setLabel(new Label(value.toString(), oldLabel.getBands(), oldLabel.getSales()));
                    }
                    break;
                case 8:
                    if (updated.getLabel() == null) {
                        updated.setLabel(new Label("", Integer.parseInt(value.toString()), 0));
                    } else {
                        Label oldLabel = updated.getLabel();
                        updated.setLabel(new Label(oldLabel.getName(), Integer.parseInt(value.toString()), oldLabel.getSales()));
                    }
                    break;
                case 9:
                    if (updated.getLabel() == null) {
                        updated.setLabel(new Label("", 0, Long.parseLong(value.toString())));
                    } else {
                        Label oldLabel = updated.getLabel();
                        updated.setLabel(new Label(oldLabel.getName(), oldLabel.getBands(), Long.parseLong(value.toString())));
                    }
                    break;
            }
            return updated;
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() ->
                    commandManager.showErrorDialog(mainFrame.getLocalizedString("table.error.update") + ex.getMessage()));
            return original;
        }
    }

    public void updateTable(List<MusicBand> bands) {
        this.musicBands = bands;
        tableModel.setRowCount(0);
        for (MusicBand band : bands) {
            Object[] rowData = {
                    band.getId(),
                    band.getName(),
                    band.getCoordinates().getX(),
                    band.getCoordinates().getY(),
                    band.getNumberOfParticipants(),
                    band.getCreationDate(),
                    band.getGenre(),
                    band.getLabel() != null ? band.getLabel().getName() : "",
                    band.getLabel() != null ? band.getLabel().getBands() : 0,
                    band.getLabel() != null ? band.getLabel().getSales() : 0
            };
            tableModel.addRow(rowData);
        }

        String currentUserLogin = mainFrame.getUser() != null ? mainFrame.getUser().getLogin() : null;
        for (int i = 0; i < bands.size(); i++) {
            MusicBand band = bands.get(i);
            if (!band.getUserLogin().equals(currentUserLogin)) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    tableModel.setValueAt(tableModel.getValueAt(i, j), i, j);
                }
            }
        }
    }

    public void updateTable() {
        dataTable.repaint();
    }

    private void styleSmallButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}