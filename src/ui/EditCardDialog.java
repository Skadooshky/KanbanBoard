package ui;

import model.Card;
import model.KanbanStatus;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditCardDialog extends JDialog {

    private static final DateTimeFormatter DATE_ONLY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JTextField txtTitle = new JTextField(25);
    private final JTextField txtDueDate = new JTextField(25);
    private final JTextField txtOwner = new JTextField(25);
    private final JTextField txtAssignee = new JTextField(25);
    private final JTextArea txtDescription = new JTextArea(5, 25);

    private final JComboBox<KanbanStatus> cboStatus = new JComboBox<>(KanbanStatus.values());
    private final JCheckBox chkExpedite = new JCheckBox("Expedite (fast track)");

    private boolean saved = false;

    public EditCardDialog(Window owner, Card card) {
        super(owner, "Edit Card", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        // Pre-fill fields
        txtTitle.setText(card.getTitle());
        txtDueDate.setText(card.getDueDate());
        txtOwner.setText(card.getOwner());
        txtAssignee.setText(card.getAssignee());
        txtDescription.setText(card.getDescription());
        cboStatus.setSelectedItem(card.getStatus());
        chkExpedite.setSelected(card.isExpedite());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        int row = 0;
        addRow(form, c, row++, "Title *", txtTitle);
        addRow(form, c, row++, "Due Date * (yyyy-MM-dd OR yyyy-MM-dd HH:mm:ss)", txtDueDate);
        addRow(form, c, row++, "Owner *", txtOwner);
        addRow(form, c, row++, "Assignee *", txtAssignee);
        addRow(form, c, row++, "Status *", cboStatus);

        c.gridx = 1; c.gridy = row;
        form.add(chkExpedite, c);
        row++;

        c.gridx = 0; c.gridy = row;
        form.add(new JLabel("Description"), c);
        c.gridx = 1;
        form.add(new JScrollPane(txtDescription), c);

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnCancel);
        buttons.add(btnSave);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addRow(JPanel form, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row;
        form.add(new JLabel(label), c);
        c.gridx = 1;
        form.add(field, c);
    }

    private void onSave() {
        String title = txtTitle.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task name/title cannot be blank.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String due = txtDueDate.getText().trim();
        if (!isValidSQLiteDate(due)) {
            JOptionPane.showMessageDialog(this,
                    "Due date must be in format:\n- yyyy-MM-dd\nOR\n- yyyy-MM-dd HH:mm:ss",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txtOwner.getText().trim().isEmpty() || txtAssignee.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Owner and Assignee are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        saved = true;
        dispose();
    }

    private boolean isValidSQLiteDate(String value) {
        try {
            LocalDate.parse(value, DATE_ONLY);
            return true;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDateTime.parse(value, DATE_TIME);
            return true;
        } catch (DateTimeParseException ignored) {}

        return false;
    }

    public boolean isSaved() { return saved; }

    public String getTitleValue() { return txtTitle.getText().trim(); }
    public String getDueDateValue() { return txtDueDate.getText().trim(); }
    public String getOwnerValue() { return txtOwner.getText().trim(); }
    public String getAssigneeValue() { return txtAssignee.getText().trim(); }
    public String getDescriptionValue() { return txtDescription.getText().trim(); }
    public KanbanStatus getStatusValue() { return (KanbanStatus) cboStatus.getSelectedItem(); }
    public boolean getExpediteValue() { return chkExpedite.isSelected(); }
}
