package guiComponents;

import guiComponents.guis.WebhookCreateConsole;
import other.WebhookGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * A class for adding several colors and methods to classes that extend it
 */
public class JFrameEssentials extends JFrame {
    public static final Color WHITE = new Color(0xFFFFFF);
    public static final Color NOT_QUITE_BLACK = new Color(0x23272A);
    public static final Color DARK_GRAY = new Color(0x2C2F33);
    public static final Color GRAY = new Color(0x99AAB5);
    public static final Color BLURPLE = new Color(0x5865F2);
    public static final Color GREEN = new Color(0x57F287);
    public static final Color RED = new Color(0xED4245);
    public static final Color MID_GRAY = new Color(0x36393F);
    public static final Color LIGHTER_MID_GRAY = new Color(0x40444B);

    /**
     * Creates a JPanel with a background color
     *
     * @param color The color to set the background to
     * @return A {@link JPanel}
     */
    public JPanel padding(Color color) {
        JPanel padding = new JPanel();
        padding.setBackground(color);
        return padding;
    }

    /**
     * Gives a {@link JComponent} a percent change in color once it is hovered over. For example, if a brightness change of .25f is assigned,
     * the component will have a brightness of 75% of its original brightness.
     *
     * @param component The {@link JComponent} to assign the brightness change to
     * @param percentBrightnessChange The percent change in brightness (ex. .25f, .5f)
     * @param <T> A generic that extends a {@link JComponent}
     */
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

    /**
     * Creates a {@link JPanel} housing a "Cancel Button" that will close the current {@link JFrame} and return the current GUI to {@link guiComponents.guis.MainConsole}
     *
     * @param frame The {@link JFrame} the button is assigned to
     * @return A {@link JPanel} housing the button
     */
    @SuppressWarnings("DuplicatedCode")
    public static JPanel cancel(JFrame frame) {
        // Create JPanel that houses the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DARK_GRAY);
        buttonPanel.setLayout(new GridBagLayout());

        // Create the JButton alongside formatting
        JButton cancelButton = new JButton();
        cancelButton.setBackground(RED);
        cancelButton.setForeground(WHITE);
        cancelButton.setBorder(new RoundedBorder(MID_GRAY,0,16));
        cancelButton.setFont(new Font("Calibri",Font.BOLD,40));
        addHoverBrightnessChange(cancelButton, .25f);
        cancelButton.setFocusable(false);

        // Create button's icon
        try {
            URL resource = WebhookCreateConsole.class.getResource("/xicon.png");
            if(resource == null)
                throw new NullPointerException("Could not get resource: xicon.png");
            Image img = ImageIO.read(resource);
            cancelButton.setIcon(new ImageIcon(img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add action listener for once the button is pressed
        cancelButton.addActionListener(event -> {
            WebhookGUI.GUI.MAIN_CONSOLE.setEnabled(true);
            frame.dispose();
        });

        // Created GBC for formatting
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Add button to panel
        buttonPanel.add(cancelButton, gbc);

        return buttonPanel;
    }
}
