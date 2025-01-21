import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class Dublicat_Renderer1 extends JLabel implements ListCellRenderer<String> {

    public Dublicat_Renderer1() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String dubl, int index, boolean isSelected, boolean cellHasFocus) {

        setText(dubl);

        if (index == 0) {
            setBackground(Color.WHITE);
            setForeground(Color.CYAN);
        } else {
            setBackground(Color.BLUE);
            setForeground(Color.ORANGE);
        }

        return this;
    }
}