import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Board2048 extends JPanel {
    
    private Model model;
    private JLabel status;
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    public Board2048(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        model = new Model();
        status = statusInit;
        
        // updates game state using slideTiles function
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    model.slideTiles("left");
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    model.slideTiles("right");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    model.slideTiles("down");
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    model.slideTiles("up");
                }
                
                updateStatus();
                repaint();
            }
        });
    }
    
    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        model.reset();
        status.setText("Game in Play --- Score: " + model.getScore());
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    
    // undo the latest move
    public void undo() {
        model.undo();
        status.setText("Game in Play --- Score: " + model.getScore());
        repaint();
        requestFocusInWindow();
    }
    
    // save the current game state (tile arrangement and score)
    public void save() {
        model.exportGame();
        requestFocusInWindow();
    }
    
    // import the last saved game
    public void continueSaved() {
        model.importGame();
        repaint();
        requestFocusInWindow();
    }
    
    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    public void updateStatus() {
        status.setText(model.getStatus());
        repaint();
    }
    
    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int x = (BOARD_WIDTH / 4) * col;
                int y = (BOARD_HEIGHT / 4) * row;
                Tile2048 tile = new Tile2048(gc, model.getTile(row, col));
                
                gc.setColor(tile.getTileColor());
                gc.fillRect(x, y, tile.getDimension(), tile.getDimension());
                
                gc.setColor(Color.WHITE);
                gc.drawRect(x, y, tile.getDimension(), tile.getDimension());
                
                gc.setColor(tile.getFontColor());
                gc.setFont(tile.getFont());
                gc.drawString(tile.getTileValue(), x + 20, y + 80);
            }
        }
    }
    
    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}







