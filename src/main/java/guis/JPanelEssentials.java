package guis;

import other.WebhookGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class JPanelEssentials extends JFrame {
    public static final Color WHITE = new Color(0xFFFFFF);
    public static final Color NOT_QUITE_BLACK = new Color(0x23272A);
    public static final Color DARK_GRAY = new Color(0x2C2F33);
    public static final Color GRAY = new Color(0x99AAB5);
    public static final Color BLURPLE = new Color(0x5865F2);
    public static final Color GREEN = new Color(0x57F287);
    public static final Color RED = new Color(0xED4245);
    public static final Color MID_GRAY = new Color(0x36393F);

    public JPanel padding(Color color) {
        JPanel padding = new JPanel();
        padding.setBackground(color);
        return padding;
    }

    public enum DimensionType {
        HEIGHT,
        WIDTH
    }


    public static <T extends JComponent> void addHoverBrightnessChange(T component, float percentBrightnessChange) {
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Color originalColor = component.getBackground();

                float[] hsb = Color.RGBtoHSB(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), null);
                hsb[2] *= (1 - percentBrightnessChange);
                Color colorNew = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                component.setBackground(colorNew);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Color originalColor = component.getBackground();

                float[] hsb = Color.RGBtoHSB(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), null);
                hsb[2] /= (1 - percentBrightnessChange);
                Color colorNew = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                component.setBackground(colorNew);
            }
        });
    }

    @SuppressWarnings("DuplicatedCode")
    public static JPanel cancel(JFrame frame) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DARK_GRAY);
        buttonPanel.setLayout(new GridBagLayout());

        JButton cancelButton = new JButton();
        cancelButton.setBackground(RED);
        cancelButton.setForeground(WHITE);
        cancelButton.setBorder(new RoundedBorder(MID_GRAY,0,16, 0));
        cancelButton.setFont(new Font("Calibri",Font.BOLD,40));
        addHoverBrightnessChange(cancelButton, .25f);
        cancelButton.setFocusable(false);

        try {
            URL resource = WebhookCreateConsole.class.getResource("/xicon.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: xicon.png");
            Image img = ImageIO.read(resource);
            cancelButton.setIcon(new ImageIcon(img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        cancelButton.addActionListener(event -> {
            WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(true);
            frame.dispose();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        buttonPanel.add(cancelButton, gbc);

        return buttonPanel;
    }
}
