package ui;

import dao.CardDao;
import model.Card;
import model.KanbanStatus;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private final CardDao cardDao = new CardDao();

    // Expedite swimlane models
    private final DefaultListModel<Card> expToDo = new DefaultListModel<>();
    private final DefaultListModel<Card> expInProg = new DefaultListModel<>();
    private final DefaultListModel<Card> expDone = new DefaultListModel<>();

    // Regular swimlane models
    private final DefaultListModel<Card> regToDo = new DefaultListModel<>();
    private final DefaultListModel<Card> regInProg = new DefaultListModel<>();
    private final DefaultListModel<Card> regDone = new DefaultListModel<>();

    // Lists
    private final JList<Card> expToDoList = new JList<>(expToDo);
    private final JList<Card> expInProgList = new JList<>(expInProg);
    private final JList<Card> expDoneList = new JList<>(expDone);

    private final JList<Card> regToDoList = new JList<>(regToDo);
    private final JList<Card> regInProgList = new JList<>(regInProg);
    private final JList<Card> regDoneList = new JList<>(regDone);

    public MainFrame() {
        super("Virtual Kanban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildToolbar(), BorderLayout.NORTH);
        add(buildBoard(), BorderLayout.CENTER);

        loadCards();
    }

    private JComponent buildToolbar() {
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnView = new JButton("View Details");
        JButton btnDelete = new JButton("Delete");
        JButton btnToggleExpedite = new JButton("Toggle Expedite");
        JButton btnMoveLeft = new JButton("< Move");
        JButton btnMoveRight = new JButton("Move >");
        JButton btnRefresh = new JButton("Refresh");

        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnView.addActionListener(e -> onView());
        btnDelete.addActionListener(e -> onDelete());
        btnToggleExpedite.addActionListener(e -> onToggleExpedite());
        btnMoveLeft.addActionListener(e -> onMove(-1));
        btnMoveRight.addActionListener(e -> onMove(+1));
        btnRefresh.addActionListener(e -> loadCards());

        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        tb.add(btnAdd);
        tb.add(btnEdit);
        tb.add(btnView);
        tb.add(btnDelete);
        tb.addSeparator();
        tb.add(btnToggleExpedite);
        tb.addSeparator();
        tb.add(btnMoveLeft);
        tb.add(btnMoveRight);
        tb.addSeparator();
        tb.add(btnRefresh);

        return tb;
    }

    private JComponent buildBoard() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(6, 6, 6, 6);

        // Header row
        addHeader(grid, c, 0, 0, "SWIMLANE");
        addHeader(grid, c, 1, 0, "TO DO");
        addHeader(grid, c, 2, 0, "IN PROGRESS");
        addHeader(grid, c, 3, 0, "DONE");

        // Expedite row
        addRowLabel(grid, c, 0, 1, "EXPEDITE");
        addCell(grid, c, 1, 1, expToDoList);
        addCell(grid, c, 2, 1, expInProgList);
        addCell(grid, c, 3, 1, expDoneList);

        // Regular row
        addRowLabel(grid, c, 0, 2, "REGULAR");
        addCell(grid, c, 1, 2, regToDoList);
        addCell(grid, c, 2, 2, regInProgList);
        addCell(grid, c, 3, 2, regDoneList);

        configureLists();

        return grid;
    }

    private void configureLists() {
        configure(expToDoList); configure(expInProgList); configure(expDoneList);
        configure(regToDoList); configure(regInProgList); configure(regDoneList);
    }

    private void configure(JList<Card> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
                enforceSingleSelection(list);
            }
        });
    }

    private void addHeader(JPanel grid, GridBagConstraints c, int x, int y, String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        c.gridx = x;
        c.gridy = y;
        c.weighty = 0;
        grid.add(label, c);
        c.weighty = 1;
    }

    private void addRowLabel(JPanel grid, GridBagConstraints c, int x, int y, String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        c.gridx = x;
        c.gridy = y;
        grid.add(label, c);
    }

    private void addCell(JPanel grid, GridBagConstraints c, int x, int y, JList<Card> list) {
        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        c.gridx = x;
        c.gridy = y;
        grid.add(sp, c);
    }

    private void loadCards() {
    Integer selectedId = getSelectedCardId();

    expToDo.clear(); expInProg.clear(); expDone.clear();
    regToDo.clear(); regInProg.clear(); regDone.clear();

    try {
        List<Card> cards = cardDao.getAllCards();

        for (Card card : cards) {
            boolean exp = card.isExpedite();

            if (card.getStatus() == KanbanStatus.TO_DO) {
                (exp ? expToDo : regToDo).addElement(card);
            } else if (card.getStatus() == KanbanStatus.IN_PROGRESS) {
                (exp ? expInProg : regInProg).addElement(card);
            } else if (card.getStatus() == KanbanStatus.DONE) {
                (exp ? expDone : regDone).addElement(card);
            }
        }

        reselectCardById(selectedId);

        } catch (Exception ex) {
            showError("Failed to load cards", ex);
        }
    }


    private void onAdd() {
        AddCardDialog dlg = new AddCardDialog(this);
        dlg.setVisible(true);

        if (!dlg.isSaved()) return;

        try {
            cardDao.insertCard(
                    dlg.getTitleValue(),
                    dlg.getDescriptionValue(),
                    dlg.getStatusValue(),
                    dlg.getDueDateValue(),
                    dlg.getOwnerValue(),
                    dlg.getAssigneeValue(),
                    dlg.getExpediteValue()
            );
            loadCards();
        } catch (Exception ex) {
            showError("Failed to add card", ex);
        }
    }

    private void onView() {
        Card selected = getSelectedCard();
        if (selected == null) {
            info("Select a card", "Please select a card first.");
            return;
        }

        String msg = """
                Title: %s
                Description: %s
                Status: %s
                Expedite: %s
                Due Date: %s
                Created At: %s
                Owner: %s
                Assignee: %s
                """.formatted(
                selected.getTitle(),
                selected.getDescription(),
                selected.getStatus().name(),
                selected.isExpedite() ? "YES" : "NO",
                selected.getDueDate(),
                selected.getCreatedAt(),
                selected.getOwner(),
                selected.getAssignee()
        );

        JOptionPane.showMessageDialog(this, msg, "Card Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onDelete() {
        Card selected = getSelectedCard();
        if (selected == null) {
            info("Select a card", "Please select a card first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this card?\n\n" + selected.getTitle(),
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            cardDao.deleteCard(selected.getId());
            loadCards();
        } catch (Exception ex) {
            showError("Failed to delete card", ex);
        }
    }

    private void onMove(int direction) {
        Card selected = getSelectedCard();
        if (selected == null) {
            info("Select a card", "Please select a card first.");
            return;
        }

        KanbanStatus current = selected.getStatus();
        KanbanStatus next = nextStatus(current, direction);

        if (next == null) {
            info("Cannot move", "This card is already at the edge.");
            return;
        }

        try {
            cardDao.updateStatus(selected.getId(), next);
            loadCards();
        } catch (Exception ex) {
            showError("Failed to move card", ex);
        }
    }
    private void onEdit() {
        Card selected = getSelectedCard();
        if (selected == null) {
            info("Select a card", "Please select a card first.");
            return;
        }

        EditCardDialog dlg = new EditCardDialog(this, selected);
        dlg.setVisible(true);

        if (!dlg.isSaved()) return;

        try {
            cardDao.updateCard(
                    selected.getId(),
                    dlg.getTitleValue(),
                    dlg.getDescriptionValue(),
                    dlg.getStatusValue(),
                    dlg.getDueDateValue(),
                    dlg.getOwnerValue(),
                    dlg.getAssigneeValue(),
                    dlg.getExpediteValue()
            );
            loadCards();
        } catch (Exception ex) {
            showError("Failed to edit card", ex);
        }
    }

    private void onToggleExpedite() {
        Card selected = getSelectedCard();
        if (selected == null) {
            info("Select a card", "Please select a card first.");
            return;
        }

        boolean newValue = !selected.isExpedite();

        try {
            cardDao.updateExpedite(selected.getId(), newValue);
            loadCards();
        } catch (Exception ex) {
            showError("Failed to toggle expedite", ex);
        }
    }


    private KanbanStatus nextStatus(KanbanStatus current, int direction) {
        return switch (current) {
            case TO_DO -> (direction > 0) ? KanbanStatus.IN_PROGRESS : null;
            case IN_PROGRESS -> (direction > 0) ? KanbanStatus.DONE : KanbanStatus.TO_DO;
            case DONE -> (direction > 0) ? null : KanbanStatus.IN_PROGRESS;
        };
    }

    private Card getSelectedCard() {
        Card c;

        c = expToDoList.getSelectedValue(); if (c != null) return c;
        c = expInProgList.getSelectedValue(); if (c != null) return c;
        c = expDoneList.getSelectedValue(); if (c != null) return c;

        c = regToDoList.getSelectedValue(); if (c != null) return c;
        c = regInProgList.getSelectedValue(); if (c != null) return c;
        c = regDoneList.getSelectedValue(); if (c != null) return c;

        return null;
    }
    
    private void enforceSingleSelection(JList<Card> active) {
        if (active != expToDoList) expToDoList.clearSelection();
        if (active != expInProgList) expInProgList.clearSelection();
        if (active != expDoneList) expDoneList.clearSelection();

        if (active != regToDoList) regToDoList.clearSelection();
        if (active != regInProgList) regInProgList.clearSelection();
        if (active != regDoneList) regDoneList.clearSelection();
    }
    private Integer getSelectedCardId() {
        Card c = getSelectedCard();
        return (c == null) ? null : c.getId();
    }

    private void reselectCardById(Integer id) {
        if (id == null) return;

        if (selectInList(expToDoList, expToDo, id)) return;
        if (selectInList(expInProgList, expInProg, id)) return;
        if (selectInList(expDoneList, expDone, id)) return;

        if (selectInList(regToDoList, regToDo, id)) return;
        if (selectInList(regInProgList, regInProg, id)) return;
        selectInList(regDoneList, regDone, id);
    }

    private boolean selectInList(JList<Card> list, DefaultListModel<Card> model, int id) {
        for (int i = 0; i < model.size(); i++) {
            Card c = model.get(i);
            if (c.getId() == id) {
                list.setSelectedIndex(i);
                list.ensureIndexIsVisible(i);

                enforceSingleSelection(list);
                return true;
            }
        }
        return false;
    }

    private void info(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(this,
                ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                title, JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
