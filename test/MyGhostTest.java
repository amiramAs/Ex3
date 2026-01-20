import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Ex3.Index2D;
import Ex3.myGame.MyGhost;

class MyGhostTest {
    private MyGhost ghost;
    private Index2D startPos;

    @BeforeEach
    void setUp() {
        startPos = new Index2D(10, 7);
        ghost = new MyGhost(0, startPos, 1);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(0, ghost.getType());
        assertEquals("10,7", ghost.getPos(0));
        assertEquals(1, ghost.getStatus());
    }

    @Test
    void testSetStatus() {
        ghost.setStatus(2);
        assertEquals(2, ghost.getStatus());
    }

    @Test
    void testGreenTime() {
        ghost.setGreenTime(500.5);
        assertEquals(500.5, ghost.remainTimeAsEatable(0));
    }

    @Test
    void testSetPos() {
        Index2D newPos = new Index2D(5, 5);
        ghost.setPos(newPos);
        assertEquals(5, ghost.getPos2D().getX());
        assertEquals(5, ghost.getPos2D().getY());
    }
}