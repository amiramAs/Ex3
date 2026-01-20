package Ex3.myGame;

import Ex3.Index2D;
import Ex3.Map;
import exe.ex3.game.GhostCL;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class myMain {
    public static void main(String[] args) {

        int[][] mapData = {
                {0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0},
                {0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0},
                {0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0},
                {0, 3, 0, 2, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 3, 0},
                {0, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 3, 0, 3, 0},
                {0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 3, 0},
                {0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0},
                {3, 3, 0, 3, 3, 0, 0, 0, 0, 0, 1, 0, 0, 0, 3, 3, 3, 0, 3, 3},
                {0, 3, 0, 0, 3, 3, 3, 0, 1, 1, 1, 1, 1, 0, 0, 0, 3, 0, 3, 0},
                {3, 3, 0, 0, 0, 0, 3, 0, 1, 1, 1, 1, 1, 0, 3, 3, 3, 0, 3, 3},
                {0, 3, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0},
                {0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0},
                {0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0},
                {0, 3, 0, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 3, 0},
                {0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0},
                {0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0}
        };

        int rows = mapData.length;
        int cols = mapData[0].length;

        Map map = new Map(cols, rows,  MyGame.BLACK);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                map.setPixel(x, rows - 1 - y, mapData[y][x]);
            }
        }

        Index2D ghostStartPos= new Index2D(10,7);
        MyGhost[] ghosts = {new MyGhost(0, ghostStartPos,1)
                ,new MyGhost(0, ghostStartPos,0)
                ,new MyGhost(1, ghostStartPos,0)
                ,new MyGhost(2, ghostStartPos,0)
                ,new MyGhost(3, ghostStartPos,0)};
        Index2D startPos = new Index2D(10, 9);
        MyGame game = new MyGame(startPos, map, ghosts);

        int scale = 30;
        StdDraw.setCanvasSize(cols * scale, rows * scale);
        StdDraw.setXscale(-0.5, cols - 0.5);
        StdDraw.setYscale(-0.5, rows - 0.5);
        StdDraw.enableDoubleBuffering();

        int count=0;

        while (game.getStatus()== MyGame.PLAY||game.getStatus()== MyGame.INIT) {
            if(count==10){
                game.setGhostsStatus(1,1);
            }
            if(count==20){
                game.setGhostsStatus(2,1);
            }
            if(count==50){
                game.setGhostsStatus(3,1);
            }
            if(count==80){
                game.setGhostsStatus(4,1);
            }

            int nextPos= handleInput(game);
            game.move(nextPos);
            drawGame(game);

            if (game.getStatus()== MyGame.INIT) {
                drawStart(game);
            }
            if (game.getStatus()== MyGame.PLAY) {
                count++;
            }

            StdDraw.show();
            StdDraw.pause(game.get_dt());

        }
        if (game.getStatus()== MyGame.DOWN) {
            drawWin(game);

        }
        if (game.getStatus()== MyGame.LOSS) {
            drawLoss(game);
        }
        StdDraw.show();
        StdDraw.pause(game.get_dt());
    }

    private static void drawStart(MyGame game) {
        int[][] board = game.getGame(0);

        Font font = new Font("Calibri", Font.BOLD, 20);
        StdDraw.setFont(font);

        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle((double) board.length /2,(double) (board[0].length /4)*3,(double) board[0].length/4,(double) board.length/12);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text((double) board.length /2, (double)( board[0].length /4)*3+0.5,"Press space to start");
        StdDraw.text((double) board.length /2, (double)( board[0].length /4)*3-0.5,"Use the arrow keys to move");
    }

    private static void drawWin(MyGame game) {
        int[][] board = game.getGame(0);

        Font font = new Font("Calibri", Font.BOLD, 40);
        StdDraw.setFont(font);

        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.text((double) board.length /2, (double) board[0].length /2,"you win!!");
    }

    private static void drawLoss(MyGame game) {
        int[][] board = game.getGame(0);

        Font font = new Font("Calibri", Font.BOLD, 40);
        StdDraw.setFont(font);

        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text((double) board.length /2, (double) board[0].length /2,"GAME OVER! :(");

    }

    private static void drawGame(MyGame game) {
        StdDraw.clear(StdDraw.BLACK);
        int[][] board = game.getGame(0);

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                int cell = board[x][y];

                if (cell == MyGame.BLUE) {
                    StdDraw.picture(x, y, "wall.png", 0.8, 0.8);

                }
                else if (cell == MyGame.PINK) {
                    StdDraw.setPenColor(StdDraw.PINK);
                    StdDraw.filledCircle(x, y, 0.2);
                }
                else if (cell == MyGame.GREEN) {
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledCircle(x, y, 0.3);
                }
            }
        }

        Index2D pPos = game.getPos2D(0);
        int dir = game.getDir();
        StdDraw.picture(pPos.getX(), pPos.getY(), "p1.png", 0.8, 0.8,dir);

        MyGhost[] ghosts = game.getGhosts(0);
        for (MyGhost g : ghosts) {
            Index2D gPos = g.getPos2D();
            int type = g.getType();
            int status = g.getStatus();

            String ghostImage = "g" + type + ".png";

            if(status !=2){
                StdDraw.picture(gPos.getX(), gPos.getY(), ghostImage, 0.8, 0.8);
            }else{
                StdDraw.picture(gPos.getX(), gPos.getY(), ghostImage, 0.5, 0.5);
            }
        }
    }

    private static int handleInput(MyGame game) {
        int ans=0;
        if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
            game.setStatus(MyGame.PLAY);
        }
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_UP)) {
            ans=MyGame.UP;
        };
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_LEFT)) {
            ans=MyGame.LEFT;
        };
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_DOWN)) {
            ans=MyGame.DOWN;
        };
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_RIGHT)) {
            ans=MyGame.RIGHT;
        };
        return ans;
    }
}
