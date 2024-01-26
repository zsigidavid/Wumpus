import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WumpusGameTest {

    @Test
    public void testTurnRight() {
        WumpusGame game = new WumpusGame(5, 2, 2, 'N', "Player");
        game.turnRight();
        assertEquals('E', game.getPlayerDirection());
    }

    @Test
    public void testTurnLeft() {
        WumpusGame game = new WumpusGame(5, 2, 2, 'N', "Player");
        game.turnLeft();
        assertEquals('W', game.getPlayerDirection());
    }
    @Test
    public void testPickupGold() {
        // Arrangement
        int size = 10;
        int playerRow = 0;
        int playerCol = 0;
        char playerDirection = 'N';
        String playerName = "Player";
        WumpusGame game = new WumpusGame(size, playerRow, playerCol, playerDirection, playerName);

        // Act
        game.initializeRandomElements(playerRow, playerCol);
        game.pickupGold(); // Hős felveszi az aranyat

        // Assert
        assertEquals(1, game.getGoldCount(), "A hősnek fel kell vennie az aranyat.");
    }

    @Test
    public void testGoldGeneration() {
        // Arrange
        int size = 10;
        int playerRow = 0;
        int playerCol = 0;
        char playerDirection = 'N';

        // Act
        WumpusGame game = new WumpusGame(size, playerRow, playerCol, playerDirection, "Player");
        game.initializeRandomElements(playerRow, playerCol);

        // Assert
        assertTrue(game.getGoldCount() > 0, "There is no gold generated on the board.");
    }
}
