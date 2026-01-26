package ui;

import model.Card;

import javax.swing.*;
import java.awt.*;

public class CardTitleRenderer extends JPanel implements ListCellRenderer<Card> {

    private final JLabel lblTitle = new JLabel();
    private final JLabel lblDue = new JLabel();

    public CardTitleRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD));
        lblDue.setFont(lblDue.getFont().deriveFont(Font.PLAIN, 11f));
        lblDue.setForeground(Color.DARK_GRAY);

        add(lblTitle, BorderLayout.NORTH);
        add(lblDue, BorderLayout.SOUTH);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Card> list,
            Card card,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        lblTitle.setText(card.getTitle());
        lblDue.setText("Due: " + card.getDueDate());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            lblTitle.setForeground(list.getSelectionForeground());
            lblDue.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            lblTitle.setForeground(list.getForeground());
            lblDue.setForeground(Color.DARK_GRAY);
        }
        
        setOpaque(true);
        return this;
    }
}
