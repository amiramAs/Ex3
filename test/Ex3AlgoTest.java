import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import Ex3.Ex3Algo;

class Ex3AlgoTest {
    @Test
    void testGetInfo() {
        Ex3Algo algo = new Ex3Algo();
        assertNotNull(algo.getInfo());
        assertTrue(algo.getInfo().contains("Pac-Man"));
    }

}