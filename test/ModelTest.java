import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    
    public static final int[][] GRIDLOCKED = new int[][] {
        {2, 4, 8, 2},
        {4, 8, 2, 4},
        {8, 2, 4, 8},
        {2, 4, 8, 2}
    };
    
    public static final int[][] BOARD1 = new int[][] {
        {2, 4, 8, 2},
        {2, 2, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    public static final int[][] ALMOSTWON = new int[][] {
        {2, 4, 8, 2},
        {4, 8, 1024, 4},
        {8, 2, 1024, 8},
        {2, 4, 8, 2}
    };
    
    public static final int[][] ALMOSTGL = new int[][] {
        {16, 4, 8, 0},
        {4, 8, 2, 4},
        {8, 2, 4, 8},
        {2, 4, 8, 2}
    };
    
    public static final int[][] BOARD2 = new int[][] {
        {2, 0, 8, 2},
        {2, 2, 0, 2},
        {0, 0, 0, 4},
        {0, 2, 0, 0}
    };

    @Test
    public void testSlideUpGridlocked() {
        Model model = new Model();
        model.setBoard(Model.clone(GRIDLOCKED));
        model.setScore(0);
        model.slideTiles("up");
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], GRIDLOCKED[r][c]);
            }
        }
        assertEquals(model.getScore(), 0);
        assertEquals(model.getStatus(), "Game in Play --- Score: 0");
    }
    
    @Test
    public void testSlideDownGridlocked() {
        Model model = new Model();
        model.setBoard(Model.clone(GRIDLOCKED));
        model.setScore(0);
        model.slideTiles("down");
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], GRIDLOCKED[r][c]);
            }
        }
        assertEquals(model.getScore(), 0);
        assertEquals(model.getStatus(), "Game in Play --- Score: 0");
    }
    
    @Test
    public void testSlideLeftGridlocked() {
        Model model = new Model();
        model.setBoard(Model.clone(GRIDLOCKED));
        model.setScore(0);
        model.slideTiles("left");
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], GRIDLOCKED[r][c]);
            }
        }
        assertEquals(model.getScore(), 0);
        assertEquals(model.getStatus(), "Game in Play --- Score: 0");
    }
    
    @Test
    public void testSlideRightGridlocked() {
        Model model = new Model();
        model.setBoard(Model.clone(GRIDLOCKED));
        model.setScore(0);
        model.slideTiles("right");
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], GRIDLOCKED[r][c]);
            }
        }
        assertEquals(model.getScore(), 0);
        assertEquals(model.getStatus(), "Game in Play --- Score: 0");
    }
    
    @Test
    public void testSlideTwoMerges() {
        Model model = new Model();
        model.setBoard(Model.clone(BOARD1));
        model.setScore(0);
        model.slideTiles("up");
        assertEquals(model.getBoard()[0][0], 4);
        assertEquals(model.getBoard()[0][1], 4);
        assertEquals(model.getBoard()[0][2], 8);
        assertEquals(model.getBoard()[0][3], 2);
        assertEquals(model.getBoard()[1][1], 2);
        assertEquals(model.getScore(), 4);
        model.slideTiles("right");
        assertFalse(model.getBoard()[0][0] == 4);
        assertFalse(model.getBoard()[0][1] == 8);
        assertEquals(model.getBoard()[0][2], 16);
        assertEquals(model.getBoard()[0][3], 2);
        assertFalse(model.getBoard()[1][3] == 0);
        assertTrue(model.getScore() >= 28);
        model.undo();
        assertEquals(model.getBoard()[0][0], 4);
        assertEquals(model.getBoard()[0][1], 4);
        assertEquals(model.getBoard()[0][2], 8);
        assertEquals(model.getBoard()[0][3], 2);
        assertEquals(model.getBoard()[1][1], 2);
        assertEquals(model.getScore(), 4);
        model.undo();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], BOARD1[r][c]);
            }
        }
        assertEquals(model.getScore(), 0);
    }
    
    @Test
    public void testSlideOtherDirections() {
        Model model = new Model();
        model.setBoard(BOARD2);
        model.setScore(0);
        model.slideTiles("down");
        model.slideTiles("left");
        assertEquals(model.getBoard()[3][0], 16);
        assertEquals(model.getBoard()[3][1], 8);
        assertTrue(model.getScore() >= 20);
    }
    
    @Test
    public void testTooManyUndos() {
        Model model = new Model();
        model.setBoard(Model.clone(BOARD1));
        model.setScore(0);
        model.slideTiles("down");
        model.slideTiles("right");
        model.slideTiles("up");
        model.slideTiles("left");
        model.slideTiles("right");
        model.slideTiles("down");
        model.slideTiles("up");
        model.undo();
        model.undo();
        model.undo();
        model.undo();
        model.undo();
        model.undo();
        model.undo();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], BOARD1[r][c]);
            }
        }
        assertEquals(model.getScore(), 0);
        model.undo();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], BOARD1[r][c]);
            }
        }
        assertEquals(model.getScore(), 0);
    }
    
    @Test
    public void testUndoAfterIneffectiveMoves() {
        Model model = new Model();
        model.setBoard(Model.clone(ALMOSTGL));
        model.setScore(0);
        model.slideTiles("right");
        int[][] gl = new int[][] {
            {2, 16, 4, 8},
            {4, 8, 2, 4},
            {8, 2, 4, 8},
            {2, 4, 8, 2}
        };
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], gl[r][c]);
            }
        }
        model.slideTiles("left");
        model.slideTiles("down");
        model.slideTiles("right");
        model.slideTiles("up");
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], gl[r][c]);
            }
        }
        model.undo();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(model.getBoard()[r][c], ALMOSTGL[r][c]);
            }
        }
    }
    
    @Test
    public void testGameWon() {
        Model model = new Model();
        model.setBoard(Model.clone(ALMOSTWON));
        model.setScore(0);
        model.slideTiles("down");
        assertEquals(model.getStatus(), "Game Won! --- Score: 2048");
    }
    
    @Test
    public void testEncapsulation() {
        Model model = new Model();
        model.setBoard(BOARD1);
        model.setScore(0);
        int[][] b = model.getBoard();
        b[0][0] = 2048;
        assertFalse(model.getBoard()[0][0] == b[0][0]);
        assertEquals(model.getBoard()[0][0], 2);
        int s = model.getScore();
        s = 1000;
        assertFalse(model.getScore() == s);
        assertEquals(model.getScore(), 0);
    }

}










