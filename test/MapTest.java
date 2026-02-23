package Ex3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MapTest {

    @Test
    public void testConstructors() {
        Map m1 = new Map(10, 5, 7);
        assertEquals(10, m1.getWidth());
        assertEquals(5, m1.getHeight());
        assertEquals(7, m1.getPixel(0, 0));

        Map m2 = new Map(4);
        assertEquals(4, m2.getWidth());
        assertEquals(4, m2.getHeight());
        assertEquals(0, m2.getPixel(2, 2));

        int[][] data = {
                {1, 2},
                {3, 4}
        };
        Map m3 = new Map(data);
        assertEquals(2, m3.getWidth());
        assertEquals(2, m3.getHeight());
        assertEquals(3, m3.getPixel(1, 0));
    }


    @Test
    public void testGetSetPixel() {
        Map m = new Map(5, 5, 0);
        Pixel2D p = new Index2D(2, 3);

        m.setPixel(2, 3, 9);
        assertEquals(9, m.getPixel(2, 3));
        assertEquals(9, m.getPixel(p));

        m.setPixel(p, 5);
        assertEquals(5, m.getPixel(2, 3));
    }


    @Test
    public void testIsInside() {
        Map m = new Map(10, 10, 0);

        assertTrue(m.isInside(new Index2D(0, 0)));
        assertTrue(m.isInside(new Index2D(9, 9)));
        assertFalse(m.isInside(new Index2D(10, 5)));
        assertFalse(m.isInside(new Index2D(5, -1)));
    }


    @Test
    public void testCyclicFlag() {
        Map m = new Map(5);
        assertTrue(m.isCyclic());

        m.setCyclic(false);
        assertFalse(m.isCyclic());
    }


    @Test
    public void testFill() {
        int[][] data = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        Map m = new Map(data);
        m.setCyclic(false);

        int filledCount1 = m.fill(new Index2D(0, 0), 5);

        assertEquals(9, filledCount1);
        assertEquals(5, m.getPixel(0, 0));
        assertEquals(5, m.getPixel(2, 2));
        assertEquals(1, m.getPixel(1, 1));

        int filledCount2 = m.fill(new Index2D(1, 1), 4);
        assertEquals(1, filledCount2);
        assertEquals(5, m.getPixel(0, 0));
        assertEquals(5, m.getPixel(2, 2));
        assertEquals(4, m.getPixel(1, 1));
    }


    @Test
    public void testAllDistance() {
        Map m = new Map(3, 3, 0);
        m.setCyclic(false);

        int obs = 1;
        m.setPixel(1, 0, obs);
        m.setPixel(1, 1, obs);

        Map2D distMap = m.allDistance(new Index2D(0, 0), obs);

        assertEquals(0, distMap.getPixel(0, 0));
        assertEquals(1, distMap.getPixel(0, 1));
        assertEquals(-1, distMap.getPixel(1, 0));

        assertTrue(distMap.getPixel(2, 0) > 0);
    }


    @Test
    public void testShortestPath() {
        Map m = new Map(5, 5, 0);
        m.setCyclic(false);
        int obs = 1;

        m.setPixel(1, 0, obs);
        m.setPixel(1, 1, obs);
        m.setPixel(1, 2, obs);

        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(2, 0);

        Pixel2D[] path = m.shortestPath(start, end, obs);

        assertNotNull(path);
        assertEquals(start, path[0]);
        assertEquals(end, path[path.length - 1]);

        for (Pixel2D p : path) {
            assertNotEquals(obs, m.getPixel(p));
        }
    }


    @Test
    public void testDeepCopy() {
        int[][] data = {{1, 1}, {1, 1}};
        Map m1 = new Map(data);
        Map m2 = new Map(m1);

        m1.setPixel(0, 0, 5);

        assertNotEquals(m1.getPixel(0, 0), m2.getPixel(0, 0));
        assertEquals(1, m2.getPixel(0, 0));
    }
}