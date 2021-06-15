/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.
        
        final String INSTRUCTIONS = (
                "How to play: \n"
                + "Press an arrow key to slide all of the tiles in one direction. \n"
                + "Tiles of the same value will merge. \n"
                + "Create a tile of value 2048 to win! You can Undo as many moves as you want. \n"
                + "You can save your progress on one game by hitting the Save button, "
                + "and continue it later by hitting the Import button. \n"
                + "Note, however, that your move history won't be saved; "
                + "you'll just be starting out with the same score and tile arrangement "
                + "that you last saved if you import. \n"
                + "Otherwise, you can start from scratch!"
                );

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("2048");
        frame.setLocation(500, 500);
        
        // Pop-up containing game instructions
        JOptionPane.showMessageDialog(frame, INSTRUCTIONS, "Instructions", 
                JOptionPane.PLAIN_MESSAGE);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Game in Play --- Score: 0");
        status_panel.add(status);

        // Tile grid
        final Board2048 gameboard = new Board2048(status);
        frame.add(gameboard, BorderLayout.CENTER);

        // Control Panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameboard.reset();
            }
        });
        control_panel.add(reset);
        
        // Undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameboard.undo();
            }
        });
        control_panel.add(undo);
        
        // Save button
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameboard.save();
            }
        });
        control_panel.add(save);
        
        // Import button
        final JButton continueSaved = new JButton("Import");
        continueSaved.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameboard.continueSaved();
                gameboard.updateStatus();
            }
        });
        control_panel.add(continueSaved);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        gameboard.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}