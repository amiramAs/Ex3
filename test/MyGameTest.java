import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Ex3.Index2D;
import Ex3.Map;
import Ex3.myGame.MyGame;
import Ex3.myGame.MyGhost;

class MyGameTest {
    private MyGame game;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map(5, 5, 1);
        map.setPixel(1, 1, 0);

        Index2D pacmanPos = new Index2D(2, 2);
        MyGhost[] ghosts = { new MyGhost(0, new Index2D(0,0), 1) };

        game = new MyGame(pacmanPos, map, ghosts);
    }

    @Test
    void testInitialStatus() {
        assertEquals(0, game.getStatus());
    }

    @Test
    void testMovement() {
        game.setStatus(1);
        game.move(4);
        assertEquals(3, game.getPos2D(0).getX());
        assertEquals(2, game.getPos2D(0).getY());
    }

    @Test
    void testWallCollision() {
        game.setStatus(1);
        game.move(2);
        game.move(3);

        assertEquals(1, game.getPos2D(0).getX());
        assertEquals(2, game.getPos2D(0).getY());
    }

    @Test
    void testEatPinkDot() {
        game.setStatus(1);
        Index2D dotPos = new Index2D(3, 2);
        map.setPixel(dotPos, 3); // 3 זה PINK

        game.move(4);

        assertEquals(1, map.getPixel(dotPos));
    }

    @Test
    void testGhostCollision() {
        map.setPixel(0, 4, 3);
        game.setStatus(1);

        game.getGhosts(0)[0].setPos(new Index2D(3, 2));

        game.move(4);

        assertEquals(4, game.getStatus(), "הסטטוס אמור להיות LOSS (4) בעקבות התנגשות ברוח");
    }
}