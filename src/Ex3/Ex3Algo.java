package Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.util.ArrayList;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
    private int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;

    public Ex3Algo() {_count=0;}

	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
        int state=1;
        int[][] board = game.getGame(0);
        int code = 0;

        int blue = Game.getIntColor(Color.BLUE, code);
        int pink = Game.getIntColor(Color.PINK, code);
        int black = Game.getIntColor(Color.BLACK, code);
        int green = Game.getIntColor(Color.GREEN, code);
        String pos = game.getPos(code).toString();
        Index2D pos2D= posInt(pos);
        GhostCL[] ghosts = game.getGhosts(code);

        if(_count==0 || _count==300) {
            printBoard(board);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			System.out.println("Pacman coordinate: "+pos);
			printGhosts(ghosts);
		}

        _count++;

        int dir=1;
        Map map = new Map(board);

        state=findState(map,ghosts,pos2D,blue);
        if (state == 1||state == 3) {
            Pixel2D[] path = closestColor(map,pos2D,pink,blue);
            dir = goPath(path);
        }
        if (state == 2) {
            Pixel2D[] path = runWay(map, ghosts, pos2D, pink, blue);
            dir = goPath(path);
        }

		return dir;
	}

    private static Pixel2D[] closestColor(Map map, Index2D pos,int color, int colorAbs){
        Pixel2D[] ans=null;

        Index2D target = pixelOfColor(map,color,colorAbs,pos);
        ans =map.shortestPath(pos,target,colorAbs);

        return ans;
    }

    private int goPath(Pixel2D[] path){
        int ans=0;

        if(path[1].getX()>path[0].getX()){
            ans=right;
        }
        if(path[1].getX()<path[0].getX()){
            ans=left;
        }
        if(path[1].getY()>path[0].getY()){
            ans=up;
        }
        if(path[1].getY()<path[0].getY()){
            ans=down;
        }

        if(path[0].getX()==0 && path[1].getX()!=1){
            ans=left;
        }
        if(path[0].getX()==22 && path[1].getX()!=21){
            ans=right;
        }

        return ans;
    }

    private int findState(Map map,GhostCL[] ghosts,Index2D pos, int colorAbs){
        int ans = 1;

        Map2D allDis = map.allDistance(pos,colorAbs);

        for (int i=0; i<ghosts.length; i++){
            Index2D ghostPos = posInt(ghosts[i].getPos(0));
            int dis = allDis.getPixel(ghostPos);

            if(ghosts[i].getStatus()==1 && dis<6 && ghosts[i].remainTimeAsEatable(0)<1.6) {
                ans=2;
            }

            if(ghosts[i].getStatus()==1 && dis<6  && ghosts[i].remainTimeAsEatable(0)>1.6){
                ans=3;
            }
        }
        return ans;
    }

    private Pixel2D[] eatGhost(Map map,GhostCL[] ghosts,Index2D pos, int colorAbs){
        Pixel2D[] ans=null;
        Map2D allDis = map.allDistance(pos,colorAbs);

        Index2D nearPos = null;
        int nearDis=15;

        for (int i=0; i<ghosts.length; i++) {
            Index2D ghostPos = posInt(ghosts[i].getPos(0));
            int dis = allDis.getPixel(ghostPos);

            if (ghosts[i].getStatus() == 1 && dis!=-1 && dis < nearDis ) {
                nearDis = dis;
                nearPos = ghostPos;
            }
        }

        ans = map.shortestPath(pos, nearPos, colorAbs);

        return ans;
    }

    private Pixel2D[] runWay(Map map,GhostCL[] ghosts,Index2D pos,int color, int colorAbs){
        Pixel2D[] ans = null;

        for (int i=0; i<ghosts.length; i++) {
            Index2D ghostPos = posInt(ghosts[i].getPos(0));
            map.setPixel(ghostPos, colorAbs);
        }

        ans = closestColor(map,pos,color,colorAbs);

        if(ans==null){
            ans =getEmergencyMove(map, pos, colorAbs);
        }

        return ans;
    }

    private Pixel2D[] getEmergencyMove(Map map, Index2D pos, int colorAbs) {
        Pixel2D[] ans =null;
        int x = pos.getX();
        int y = pos.getY();

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int i=0; i<directions.length; i++) {
            int nextX = x + directions[i][0];
            int nextY = y + directions[i][1];

            if (nextX < 0) nextX = 22;
            if (nextX > 22) nextX = 0;

            Index2D target = new Index2D(nextX, nextY);

            if (map.isInside(target) && map.getPixel(target) != colorAbs) {
                ans = new Pixel2D[] { pos, target };
            }
        }

        return ans;
    }


    private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}

	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}

	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}

    private static Index2D posInt(String pos) {
        String[] parts = pos.replace("(", "")
                .replace(")", "")
                .split(",");
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        return new Index2D (x,y);
    }

    private static Index2D pixelOfColor(Map map , int color, int colorAbs, Index2D pos) {
        Index2D ans=null;

        Map2D all = map.allDistance(pos,colorAbs);
        int[][] board= map.getMap();
        int[][] allMatrix = all.getMap();

        for(int x=0;x<allMatrix.length;x++){
            for(int y=0;y<allMatrix[0].length;y++){
                if(board[x][y]==color) {
                    if (ans == null) {
                        ans = new Index2D(x, y);
                    }
                    if (allMatrix[x][y] -1 < allMatrix[ans.getX()][ans.getY()]) {
                        ans = new Index2D(x, y);
                    }
                }
            }
        }

        return ans;
    }
}