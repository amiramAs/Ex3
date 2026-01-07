package Ex3;

import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;
import exe.ex3.game.StdDraw;

import java.awt.event.KeyEvent;

public class ManualAlgo implements PacManAlgo{
    public ManualAlgo() {;}
    @Override
    public String getInfo() {
        return "This is a manual algorithm for manual controlling the PacMan using w,a,x,d (up,left,down,right).";
    }

    @Override
    public int move(PacmanGame game) {
        int ans = PacmanGame.ERR;
        Character cmd = Ex3Main.getCMD();
        if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {ans = PacmanGame.UP;}
        if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {ans = PacmanGame.DOWN;}
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {ans = PacmanGame.LEFT;}
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {ans = PacmanGame.RIGHT;}

        if (cmd != null) {
            if (cmd == 'w') {ans = PacmanGame.UP;}
            if (cmd == 's') {ans = PacmanGame.DOWN;}
            if (cmd == 'a') {ans = PacmanGame.LEFT;}
            if (cmd == 'd') {ans  = PacmanGame.RIGHT;}
        }

        GhostCL[] ghosts = game.getGhosts(0);
        printGhosts(ghosts);



        return  ans;
    }
    private static void printGhosts(GhostCL[] gs) {
        for(int i=0;i<gs.length;i++){
            GhostCL g = gs[i];
            System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
        }
    }
}
