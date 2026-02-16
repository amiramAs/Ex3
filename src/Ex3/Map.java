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
	@Override
	/////// add your code below ///////
	public int getWidth() {return _map.length;}
	@Override
	/////// add your code below ///////
	public int getHeight() {
        if (_map.length == 0) {
            throw new RuntimeException("Map width 0");
        }
        return _map[0].length;
    }
	@Override
	/////// add your code below ///////
	public int getPixel(int x, int y) {
        return _map[x][y];
    }
	@Override
	/////// add your code below ///////
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(),p.getY());
	}
	@Override
	/////// add your code below ///////
	public void setPixel(int x, int y, int v) {_map[x][y] = v;}
	@Override
	/////// add your code below ///////
	public void setPixel(Pixel2D p, int v) {
		this.setPixel(p.getX(),p.getY(),v);
	}
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
        if(!isInside(xy) ){ throw new RuntimeException("The point is not in map");}

        int ans=0;

        int old_v = getPixel(xy);
        if(this.isInside(xy) && old_v != new_v){
            int w = getWidth();
            int h = getHeight();

            boolean[][] visited = new boolean[w][h];
            ArrayDeque<Pixel2D> q = new ArrayDeque<>();
            q.add(new Index2D(xy));
            visited[xy.getX()][xy.getY()] = true;

            while (!q.isEmpty()){
                Pixel2D p = q.removeFirst();

                setPixel(p,new_v);
                ans++;
                visited[p.getX()][p.getY()] = true;

                Pixel2D[] directions = directions(p);
                for(Pixel2D d : directions){
                    if (!visited[d.getX()][d.getY()] && getPixel(d)==old_v){
                        q.addLast(d);
                    }
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
		Pixel2D[] ans = null;
        if(!isInside(p1) || !isInside(p2)){ throw new RuntimeException("One of points is not ia map");}
        Map2D dis = allDistance(p1,obsColor);
        if(dis.getPixel(p2)!=-1) {
            Pixel2D temp = p2;
            ArrayDeque<Pixel2D> path = new ArrayDeque<>();
            path.addLast(p2);
            while (!temp.equals(p1)) {
                Pixel2D[] directions = directions(temp);
                for (Pixel2D d : directions) {
                    if (dis.getPixel(d) < dis.getPixel(temp) && dis.getPixel(d) != -1) {
                        path.addFirst(d);
                        temp = d;
                    }
                }
            }
            ans = path.toArray(Pixel2D[]::new);
        }

		return ans;
	}
	@Override
	/////// add your code below ///////
	public boolean isInside(Pixel2D p) {
        boolean ans = false;
        int x = p.getX();
        int y = p.getY();
        if(x>=0 && x<getWidth() && y>=0 && y<getHeight()) {
            ans = true;
        }
        return ans;
	}

	@Override
	/////// add your code below ///////
	public boolean isCyclic() {
		return _cyclicFlag;
	}
	@Override
	/////// add your code below ///////
	public void setCyclic(boolean cy) {_cyclicFlag = cy;}
	@Override
	/////// add your code below ///////
	public Map2D allDistance(Pixel2D start, int obsColor) {
		Map2D ans = null;
        if(!isInside(start) ){ throw new RuntimeException("The point is not ia map");}

        if(isInside(start)){
            ans = new Map(getWidth(),getHeight(), -1);
            ans.setCyclic(isCyclic());
            ans.setPixel(start,0);

            if (getPixel(start) != obsColor) {
                ArrayDeque<Pixel2D> q = new ArrayDeque<>();
                q.add(new Index2D(start));
                while (!q.isEmpty()) {
                    Pixel2D p = q.removeFirst();

                    Pixel2D[] directions = directions(p);
                    for (Pixel2D d : directions) {
                        int x = d.getX(), y = d.getY();
                        if (ans.getPixel(d)==-1 && getPixel(d)!=obsColor) {
                            ans.setPixel(x, y,ans.getPixel(p) + 1 );
                            q.addLast(new Index2D(x,y));
                        }
                    }
                }
            }
        }

		return ans;
	}

    private Pixel2D[] directions(Pixel2D p) {
        int x = p.getX(), y = p.getY();
        int w = getWidth()-1, h = getHeight()-1;

        Index2D[] ans = new Index2D[] {
                new Index2D(x+1, y),
                new Index2D(x-1, y),
                new Index2D(x, y+1),
                new Index2D(x, y-1)
        };

        if (x==w) {
            if (isCyclic()){
                ans[0].setX(0);
            }else{
                ans[0].setX(w);
            }
        }
        if (x==0) {
            if (isCyclic()){
                ans[1].setX(w);
            }else{
                ans[1].setX(0);
            }
        }
        if (y==h) {
            if (isCyclic()){
                ans[2].setY(0);
            }else{
                ans[2].setY(h);
            }
        }
        if (y==0) {
            if (isCyclic()){
                ans[3].setY(h);
            }else{
                ans[3].setY(0);
            }
        }

        return ans;
    }
}
