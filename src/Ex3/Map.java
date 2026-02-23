package Ex3;

import java.util.ArrayDeque;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;
	
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w,h, v);}
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

    /**
     * Constructs a map from a Map object.
     * @param map
     */
    public Map(Map map) {
        init(map.getMap());
    }

    /**
     * Reinitializes the map to be w*h filled with v.
     *
     * @param w width (x dimension), must be positive
     * @param h height (y dimension), must be positive
     * @param v fill value
     * @throws RuntimeException if w<=0 or h<=0
     */
	@Override
	public void init(int w, int h, int v) {
        if (w <= 0 || h <= 0) throw new RuntimeException("Width and height must be positive");
        _map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] = v;
            }
        }
	}


    /**
     * Reinitializes the map from a given matrix (deep copy).
     * The input must be rectangular (all rows same length).
     *
     * @param arr source matrix
     * @throws RuntimeException if arr is null/empty/not rectangular
     */
    @Override
    public void init(int[][] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null || arr[0].length == 0) {
            throw new RuntimeException("Array must be non-null and non-empty");
        }
        int w = arr.length;
        int h = arr[0].length;
        for (int x = 0; x < w; x++) {
            if (arr[x] == null || arr[x].length != h) {
                throw new RuntimeException("Array must be rectangular (same length for all rows)");
            }
        }
        _map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _map[x][y] = arr[x][y];
            }
        }
    }

    /**
     * Returns a deep copy of the game map.
     * Validates that the map is initialized before copying each pixel into a new 2D array.
     * @return A 2D integer array representing the map.
     * @throws RuntimeException if the map is not initialized.
     */
    @Override
    public int[][] getMap() {
        if (_map == null) throw new RuntimeException("Map is not initialized");
        int[][] ans = null;
        int w = getWidth(), h = getHeight();
        ans = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                ans[x][y] = _map[x][y];
            }
        }
        return ans;
    }

    /**
     * Returns the width (number of columns) of the map.
     * @return The length of the first dimension of the map array.
     */
    @Override
    public int getWidth() {
        return _map.length;
    }

    /**
     * Returns the height (number of rows) of the map.
     * Validates that the map has at least one column before accessing its height.
     * @return The length of the second dimension of the map array.
     * @throws RuntimeException if the map width is 0.
     */
    @Override
    public int getHeight() {
        if (_map.length == 0) {
            throw new RuntimeException("Map width 0");
        }
        return _map[0].length;
    }

    /**
     * Retrieves the integer value (color/type) of a pixel at specific coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The value at the given (x, y) position.
     */
    @Override
    public int getPixel(int x, int y) {
        return _map[x][y];
    }

    /**
     * Retrieves the integer value of a pixel using a Pixel2D location object.
     * @param p The Pixel2D object containing coordinates.
     * @return The value at the pixel's position.
     */
    @Override
    public int getPixel(Pixel2D p) {
        return this.getPixel(p.getX(), p.getY());
    }

    /**
     * Sets the value of a specific pixel at the given coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param v The new value to set.
     */
    @Override
    public void setPixel(int x, int y, int v) {
        _map[x][y] = v;
    }

    /**
     * Sets the value of a specific pixel using a Pixel2D location object.
     * @param p The Pixel2D object containing coordinates.
     * @param v The new value to set.
     */
    @Override
    public void setPixel(Pixel2D p, int v) {
        this.setPixel(p.getX(), p.getY(), v);
    }

    /**
     * Fills a connected area of pixels with a new value (Flood Fill algorithm).
     * This function uses a Breadth-First Search (BFS) approach starting from the given coordinates.
     * It identifies the original color at the starting point and traverses all adjacent pixels
     * (Up, Down, Left, Right) that share this color, replacing them with new_v.
     * A 2D boolean array ('visited') and a queue are used to manage the traversal and prevent
     * infinite loops or redundant processing.
     * * @param xy The starting Pixel2D coordinates.
     * @param new_v The new value/color to apply to the area.
     * @return The total count of pixels that were successfully repainted.
     * @throws RuntimeException if the starting point is out of the map boundaries.
     */
    @Override
    public int fill(Pixel2D xy, int new_v) {
        if (!isInside(xy)) { throw new RuntimeException("The point is not in map"); }

        int ans = 0;
        int old_v = getPixel(xy);
        if (this.isInside(xy) && old_v != new_v) {
            int w = getWidth();
            int h = getHeight();

            boolean[][] visited = new boolean[w][h];
            ArrayDeque<Pixel2D> q = new ArrayDeque<>();
            q.add(new Index2D(xy));
            visited[xy.getX()][xy.getY()] = true;

            while (!q.isEmpty()) {
                Pixel2D p = q.removeFirst();

                setPixel(p, new_v);
                ans++;

                Pixel2D[] directions = directions(p);
                for (Pixel2D d : directions) {
                    if (!visited[d.getX()][d.getY()] && getPixel(d) == old_v) {
                        visited[d.getX()][d.getY()] = true;
                        q.addLast(d);
                    }
                }
            }
        }
        return ans;
    }



    /**
     * Calculates the shortest path between two points while avoiding specified obstacles.
     * The function first generates a distance map using the allDistance() BFS method.
     * If the destination (p2) is reachable, it performs a "backtracking" procedure:
     * starting from p2, it repeatedly identifies a neighboring pixel whose distance value
     * is exactly 1 less than the current pixel's distance, moving backward until it reaches p1.
     * * @param p1 The source Pixel2D.
     * @param p2 The destination Pixel2D.
     * @param obsColor The value representing an impassable obstacle (wall).
     * @return An ordered array of Pixel2D objects from p1 to p2, or null if no path exists.
     * @throws RuntimeException if p1 or p2 are outside map boundaries.
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        Pixel2D[] ans = null;
        if (!isInside(p1) || !isInside(p2)) { throw new RuntimeException("One of points is not in map"); }

        // Step 1: Generate distance map from source p1
        Map2D dis = allDistance(p1, obsColor);

        // Step 2: If p2 was reached in the distance map, backtrack to find the path
        if (dis.getPixel(p2) != -1) {
            Pixel2D temp = p2;
            ArrayDeque<Pixel2D> path = new ArrayDeque<>();
            path.addLast(p2);

            while (!temp.equals(p1)) {
                Pixel2D[] directions = directions(temp);
                for (Pixel2D d : directions) {
                    // Find a neighbor that is one step closer to the source
                    if (dis.getPixel(d) < dis.getPixel(temp) && dis.getPixel(d) != -1) {
                        path.addFirst(d);
                        temp = d;
                        break;
                    }
                }
            }
            ans = path.toArray(Pixel2D[]::new);
        }
        return ans;
    }



    /**
     * Checks if a given pixel is within the legal bounds of the map.
     * @param p The Pixel2D to validate.
     * @return true if the pixel's X and Y coordinates are within [0, width) and [0, height).
     */
    @Override
    public boolean isInside(Pixel2D p) {
        int x = p.getX(), y = p.getY();
        return (x >= 0 && x < getWidth() && y >= 0 && y < getHeight());
    }

    /**
     * @return true if map edges wrap around (Cyclic), false otherwise.
     */
    @Override
    public boolean isCyclic() {
        return _cyclicFlag;
    }

    /**
     * Enables or disables the cyclic (wrap-around) behavior of the map edges.
     * @param cy The desired cyclic state.
     */
    @Override
    public void setCyclic(boolean cy) {
        _cyclicFlag = cy;
    }

    /**
     * Generates a distance map where each pixel contains its shortest distance from the start point.
     * Using Breadth-First Search (BFS), this method marks the starting point with 0 and expands
     * outwards to all reachable pixels that are NOT of the specified obsColor.
     * Unreachable pixels and obstacles are marked with -1. This map is essential for
     * pathfinding and AI movement logic.
     * * @param start The source point to measure distance from.
     * @param obsColor The pixel value that acts as a wall/obstacle.
     * @return A Map2D object representing the grid of distances.
     * @throws RuntimeException if the start point is outside the map.
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        if (!isInside(start)) { throw new RuntimeException("The point is not in map"); }

        // Initialize a new map filled with -1 (representing unvisited/blocked)
        Map2D ans = new Map(getWidth(), getHeight(), -1);
        ans.setCyclic(isCyclic());

        // If start is an obstacle, no paths can be calculated
        if (getPixel(start) == obsColor) return ans;

        ans.setPixel(start, 0);
        ArrayDeque<Pixel2D> q = new ArrayDeque<>();
        q.add(new Index2D(start));

        while (!q.isEmpty()) {
            Pixel2D p = q.removeFirst();
            int currentDist = ans.getPixel(p);

            Pixel2D[] neighbors = directions(p);
            for (Pixel2D d : neighbors) {
                // If the neighbor is unvisited and is not an obstacle
                if (ans.getPixel(d) == -1 && getPixel(d) != obsColor) {
                    ans.setPixel(d, currentDist + 1);
                    q.addLast(d);
                }
            }
        }
        return ans;
    }



    /**
     * Calculates the neighbors of a pixel (Up, Down, Left, Right).
     * This function handles boundary logic:
     * - If isCyclic is true: coordinates wrap around (e.g., x=width-1 moves to x=0).
     * - If isCyclic is false: coordinates are clamped to the edge.
     * * @param p The source pixel.
     * @return An array of exactly 4 Pixel2D objects representing neighbors.
     */
    private Pixel2D[] directions(Pixel2D p) {
        int x = p.getX(), y = p.getY();
        int w = getWidth() - 1, h = getHeight() - 1;

        Index2D[] ans = new Index2D[] {
                new Index2D(x + 1, y),
                new Index2D(x - 1, y),
                new Index2D(x, y + 1),
                new Index2D(x, y - 1)
        };

        if (x == w) {
            ans[0].setX(isCyclic() ? 0 : w);
        }
        if (x == 0) {
            ans[1].setX(isCyclic() ? w : 0);
        }
        if (y == h) {
            ans[2].setY(isCyclic() ? 0 : h);
        }
        if (y == 0) {
            ans[3].setY(isCyclic() ? h : 0);
        }

        return ans;
    }
}
