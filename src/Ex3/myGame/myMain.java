package Ex3.myGame;

import Ex3.Index2D;
import Ex3.Map;
import exe.ex3.game.GhostCL;

import java.awt.Color;
import java.awt.event.KeyEvent;

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

        MyGhost[] ghosts = new MyGhost[1];
        ghosts[0] = new MyGhost();

        Index2D startPos = new Index2D(10, 9);
        MyGame game = new MyGame(startPos, map, ghosts);

        int scale = 30;
        StdDraw.setCanvasSize(cols * scale, rows * scale);
        StdDraw.setXscale(-0.5, cols - 0.5);
        StdDraw.setYscale(-0.5, rows - 0.5);
        StdDraw.enableDoubleBuffering();

        while (game.getStatus()!= MyGame.DONE){
            handleInput(game);
            drawGame(game);
            StdDraw.show();
            StdDraw.pause(game.get_dt());
        }

    }

    private static void drawGame(MyGame game) {
        StdDraw.clear(StdDraw.BLACK);
        int[][] board = game.getGame(0);

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                int cell = board[x][y];

                if (cell == MyGame.BLUE) {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.filledSquare(x, y, 0.5);
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

//        MyGhost[] ghosts = game.getGhosts(0);
//        for (MyGhost g : ghosts) {
//            Index2D gPos = g.getPos2D();
//            int type = g.getType();
//
//            String ghostImage = "g" + type + ".png";
//
//            StdDraw.picture(gPos.getX(), gPos.getY(), ghostImage, 0.8, 0.8);
//        }
    }

    private static void handleInput(MyGame game) {
        if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
            game.setStatus(MyGame.PLAY);
        }
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_UP)) {
            game.move(MyGame.UP);
        };
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_LEFT)) {
            game.move(MyGame.LEFT);
        };
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_DOWN)) {
            game.move(MyGame.DOWN);
        };
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_RIGHT)) {
            game.move(MyGame.RIGHT);
        };
    }
}
