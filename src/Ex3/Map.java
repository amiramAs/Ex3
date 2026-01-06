package Ex3;

import java.util.ArrayDeque;
import java.util.Arrays;

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

	@Override
	public void init(int w, int h, int v) {
		_map=new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                _map[i][j]=v;
            }
        }
	}

	@Override
	public void init(int[][] arr) {
        if(arr==null||arr.length==0) {
            throw new RuntimeException("Err: arr is null");
        }
        _map = new int[arr.length][];

        for (int i = 0; i < arr.length; i++) {
            if(i!=0) {
                if(arr[i].length!=arr[0].length){
                    throw new RuntimeException("Err: arr is ragged");
                }
            }
            _map[i] = Arrays.copyOf(arr[i], arr[i].length);
        }
	}
	@Override
	public int[][] getMap() {
		int[][] ans = new int[_map.length][];

        for (int i = 0; i < _map.length; i++) {
            if(i!=0) {
                if(_map[i].length!=_map[0].length){
                    throw new RuntimeException("Err: arr is ragged");
                }
            }
            ans[i] = Arrays.copyOf(_map[i], _map[i].length);
        }

		return ans;
	}

	@Override
	public int getWidth() {
        return _map.length;
    }
	@Override
	public int getHeight() {
        return _map[0].length;
    }
	@Override
	public int getPixel(int x, int y) {
        return _map[x][y];
    }

	@Override
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(),p.getY());
	}

	@Override
	public void setPixel(int x, int y, int v) {
        _map[x][y] = v;
    }

	@Override
	public void setPixel(Pixel2D p, int v) {
		_map[p.getX()][p.getY()] = v;
	}
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
        int ans = 0;
        final int H = _map.length;
        final int W = _map[0].length;
        final int fx = xy.getX();
        final int fy = xy.getY();
        if (fy < 0 || fy >= _map[0].length || fx < 0 || fx >= _map.length) {
            return ans;
        }
        final int old = _map[fy][fx];
        if (old == new_v) {
            return ans;
        }
        final boolean[][] visited = new boolean[H][W];
        final ArrayDeque<int[]> q = new ArrayDeque<>();
        visited[fy][fx] = true;
        q.add(new int[]{fx, fy});
        final int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int x = cur[0];
            int y = cur[1];
            if (_map[y][x] == old) {
                _map[y][x] = new_v;
                ans++;
            }
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (_cyclicFlag) {
                    nx = ((nx % W) + W) % W;
                    ny = ((ny % H) + H) % H;
                } else {
                    if (nx < 0 || nx >= W || ny < 0 || ny >= H)
                        continue;
                }
                if (!visited[ny][nx] && _map[ny][nx] == old) {
                    visited[ny][nx] = true;
                    q.add(new int[]{nx, ny});
                }
            }
        }
        return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        Pixel2D[] ans = null;  // the result.
        if (p1 == null || p2 == null) return null;
        final int H = _map.length;
        final int W = _map[0].length;
        final int sx = p1.getX(), sy = p1.getY();
        final int ex = p2.getX(), ey = p2.getY();
        if (sx < 0 || sx >= W || sy < 0 || sy >= H) return null;
        if (ex < 0 || ex >= W || ey < 0 || ey >= H) return null;
        if (_map[sy][sx] == obsColor || _map[ey][ex] == obsColor) return null;
        if (sx == ex && sy == ey) return new Pixel2D[]{p1};
        final boolean[][] visited = new boolean[H][W];
        final int[][] parentX = new int[H][W];
        final int[][] parentY = new int[H][W];
        for (int y = 0; y < H; y++) {
            java.util.Arrays.fill(parentX[y], -1);
            java.util.Arrays.fill(parentY[y], -1);
        }
        final int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        final java.util.ArrayDeque<int[]> q = new java.util.ArrayDeque<>();
        visited[sy][sx] = true;
        q.addLast(new int[]{sx, sy});
        boolean found = false;
        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int x = cur[0], y = cur[1];

            if (x == ex && y == ey) {
                found = true;
                break;
            }
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (_cyclicFlag) {
                    nx = (nx % W + W) % W;
                    ny = (ny % H + H) % H;
                } else {
                    if (nx < 0 || nx >= W || ny < 0 || ny >= H) continue;
                }
                if (!visited[ny][nx] && _map[ny][nx] != obsColor) {
                    visited[ny][nx] = true;
                    parentX[ny][nx] = x;
                    parentY[ny][nx] = y;
                    q.addLast(new int[]{nx, ny});
                }
            }
        }
        if (!found) return null;
        java.util.ArrayList<Pixel2D> path = new java.util.ArrayList<>();
        int cx = ex, cy = ey;
        while (true) {
            path.add(new Index2D(cx, cy));
            if (cx == sx && cy == sy) break; // reached start
            int px = parentX[cy][cx];
            int py = parentY[cy][cx];
            if (px == -1 && py == -1) return null; // safety
            cx = px;
            cy = py;
        }
        java.util.Collections.reverse(path);
        ans = path.toArray(new Pixel2D[0]);
        return ans;
    }

	@Override
	/////// add your code below ///////
	public boolean isInside(Pixel2D p) {
		return false;
	}

	@Override
	/////// add your code below ///////
	public boolean isCyclic() {
		return false;
	}

	@Override
	/////// add your code below ///////
	public void setCyclic(boolean cy) {;}

	@Override
	public Map2D allDistance(Pixel2D start, int obsColor) {
        Map2D ans = null;  // the result.
        final int H = _map.length;
        final int W = _map[0].length;
        final int sx = start.getX(), sy = start.getY();
        int[][] dist = new int[H][W];
        for (int y = 0; y < H; y++) java.util.Arrays.fill(dist[y], -1);
        if (sx < 0 || sx >= W || sy < 0 || sy >= H) {
            return new Map(dist);
        }
        if (_map[sy][sx] == obsColor) {
            for (int y = 0; y < H; y++)
                for (int x = 0; x < W; x++)
                    if (_map[y][x] == obsColor) dist[y][x] = obsColor;
            return new Map(dist);
        }
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++)
                if (_map[y][x] == obsColor) dist[y][x] = obsColor;
        final boolean[][] visited = new boolean[H][W];
        final ArrayDeque<int[]> q = new ArrayDeque<>();
        final int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        visited[sy][sx] = true;
        dist[sy][sx] = 0;
        q.add(new int[]{sx, sy});
        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int x = cur[0], y = cur[1];

            for (int[] d : directions) {
                int nx = x + d[0], ny = y + d[1];

                if (_cyclicFlag) {
                    nx = ((nx % W) + W) % W;
                    ny = ((ny % H) + H) % H;
                } else {
                    if (nx < 0 || nx >= W || ny < 0 || ny >= H) continue;
                }
                if (visited[ny][nx]) continue;
                if (_map[ny][nx] == obsColor) continue;
                visited[ny][nx] = true;
                dist[ny][nx] = dist[y][x] + 1;
                q.add(new int[]{nx, ny});
            }
        }
        ans = new Map(dist);
        return ans;
	}
}
