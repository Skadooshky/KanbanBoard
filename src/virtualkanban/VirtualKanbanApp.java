package virtualkanban;

import ui.MainFrame;

import javax.swing.SwingUtilities;

public class VirtualKanbanApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
