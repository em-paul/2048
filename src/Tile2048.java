
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Tile2048 {
    
    private static final int TILE_DIMENSION = Board2048.BOARD_WIDTH / 4; 
    private int value;
    private Color tilecolor, fontcolor;
    private Font font = new Font("Verdana", Font.BOLD, 24);

    public Tile2048(Graphics gc, int val) {
        value = val;
        
        if (val == 0) {
            tilecolor = Color.WHITE;
            fontcolor = Color.WHITE;
        } else if (val == 2) {
            // purple
            tilecolor = new Color(103, 57, 229);
            fontcolor = Color.BLACK;
        } else if (val == 4) {
            // blue
            tilecolor = new Color(12, 88, 252);
            fontcolor = Color.BLACK;
        } else if (val == 8) {
            // blue-green
            tilecolor = new Color(12, 244, 252);
            fontcolor = Color.BLACK;
        } else if (val == 16) {
            // sea green
            tilecolor = new Color(12, 252, 160);
            fontcolor = Color.BLACK;
        } else if (val == 32) {
            // lime green
            tilecolor = new Color(92, 252, 12);
            fontcolor = Color.BLACK;
        } else if (val == 64) {
            // yellow
            tilecolor = new Color(252, 252, 12);
            fontcolor = Color.BLACK;
        } else if (val == 128) {
            // yellow-orange
            tilecolor = new Color(252, 156, 12);
            fontcolor = Color.BLACK;
        } else if (val == 256) {
            // red-orange
            tilecolor = new Color(252, 84, 12);
            fontcolor = Color.BLACK;
        } else if (val == 512) {
            // red
            tilecolor = new Color(252, 12, 12);
            fontcolor = Color.BLACK;
        } else if (val == 1024) {
            // fuschia
            tilecolor = new Color(252, 12, 96);
            fontcolor = Color.WHITE;
        } else if (val == 2048) {
            // raspberry
            tilecolor = new Color(127, 6, 48);
            fontcolor = Color.WHITE;
        } else {
            tilecolor = Color.BLACK;
            fontcolor = Color.RED;
        }
    }
    
    public Color getTileColor() {
        return tilecolor;
    }
    
    public Color getFontColor() {
        return fontcolor;
    }
    
    public Font getFont() {
        return font;
    }
    
    public int getDimension() {
        return TILE_DIMENSION;
    }
    
    public String getTileValue() {
        return String.valueOf(value);
    }

}











