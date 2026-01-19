package Ex3.myGame;

import Ex3.Index2D;
import Ex3.Map;
import exe.ex3.game.PacmanGame;

public class MyGame implements PacmanGame {
    private Index2D _pos;
    private int _dir;
    private Map _map;
    private MyGhost[]  _ghosts;
    private int _status;
    private int _dt;

    static int INIT = 0;
    static int PLAY = 1;
    static int PAUSE = 2;
    static int DONE = 3;
    static int LOSS = 4;

    static int LEFT = 2;
    static int RIGHT = 4;
    static int UP = 1;
    static int DOWN = 3;
    static int BLUE = 0;
    static int BLACK = 1;
    static int GREEN = 2;
    static int PINK = 3;

    public MyGame(Index2D pos, Map map, MyGhost[] ghosts) {
        _pos = pos;
        _map = map;
        _ghosts = ghosts;
        _status = INIT;
        _dt = 80;
        _dir = 0;
    }

    @Override
    public Character getKeyChar() {
        return null;
    }

    @Override
    public String getPos(int i) {
        return _pos.toString();
    }

    public Index2D getPos2D(int i) {
        return _pos;
    }

    public int getDir() {
        return this._dir;
    }

    @Override
    public MyGhost[] getGhosts(int i) {
        return _ghosts;
    }

    @Override
    public int[][] getGame(int i) {
        return _map.getMap();
    }

    @Override
    public String move(int i) {
        String ans="";
        if (_status==PLAY) {
            _pos = nextPos(i,_pos);
            if(i==UP){
                _dir=90;
            }
            if(i==DOWN){
                _dir=270;
            }
            if(i==LEFT){
                _dir=180;
            }
            if(i==RIGHT){
                _dir=0;
            }

            if (_map.getPixel(_pos) == PINK) {
                _map.setPixel(_pos, BLACK);
            }
            if(_map.getPixel(_pos)==GREEN){
                _map.setPixel(_pos, BLACK);
                for (int j =0;j<_ghosts.length;j++){
                    _ghosts[j].setStatus(2);
                    _ghosts[j].setGreenTime(40*_dt);
                }
            }

            for (int j =0;j<_ghosts.length;j++){
                Index2D ghostPos=posInt(_ghosts[j].getPos(i));
                if(ghostPos.equals(_pos)){
                    if(_ghosts[j].getStatus() != 2){
                        _status=LOSS;
                    }else{
                        _ghosts[j].setStatus(3);
                        _ghosts[j].setGreenTime(20*_dt);
                        _ghosts[j].setPos(new Index2D(10,7));
                    }
                }
                if(_ghosts[j].getStatus() == 1||_ghosts[j].getStatus() == 2){
                    int ghostDir = (int)(Math.random()*(5-1)) + 1;
                    Index2D nextGhostPos = nextPos(ghostDir,_ghosts[j].getPos2D());
                    _ghosts[j].setPos(nextGhostPos);
                }
                if (_ghosts[j].getStatus() == 2){
                    double time = _ghosts[j].remainTimeAsEatable(0)-_dt;
                    _ghosts[j].setGreenTime(time);
                    if(time<=0){
                        _ghosts[j].setStatus(1);
                    }
                }
                if (_ghosts[j].getStatus() == 3){
                    double time = _ghosts[j].remainTimeAsEatable(0)-_dt;
                    _ghosts[j].setGreenTime(time);
                    if(time<=0){
                        _ghosts[j].setStatus(1);
                    }
                }
            }
        }

        boolean remainPink=false;
        int[][] board=_map.getMap();
        for(int x=0;x<board.length;x++){
            for(int y=0;y<board[x].length;y++){
                if(board[x][y]==PINK){
                    remainPink=true;
                }
            }
        }

        if(!remainPink){
            _status=DONE;
        }

        return ans;
    }

    @Override
    public void play() {

    }

    @Override
    public String end(int i) {
        return "";
    }

    @Override
    public String getData(int i) {
        return "";
    }

    @Override
    public int getStatus() {
        return _status;
    }

    public void setStatus(int status) {
        _status = status;
    }

    public int get_dt(){
        return _dt;
    }

    @Override
    public boolean isCyclic() {
        return _map.isCyclic();
    }

    @Override
    public String init(int i, String s, boolean b, long l, double v, int i1, int i2) {
        return "";
    }

    private static Index2D posInt(String pos) {
        String[] parts = pos.replace("(", "")
                .replace(")", "")
                .split(",");
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        return new Index2D (x,y);
    }

    private Index2D nextPos(int i,Index2D pos) {
        Index2D nextPos = new Index2D(pos);
        if (i == UP) {
            if (pos.getY() < _map.getHeight() - 1) {
                nextPos.setY(pos.getY() + 1);
            } else {
                nextPos.setY(0);
            }
        }
        if (i == DOWN) {
            if (pos.getY() > 0) {
                nextPos.setY(pos.getY() - 1);
            } else {
                nextPos.setY(_map.getHeight() - 1);
            }
        }

        if (i == RIGHT) {
            if (pos.getX() < _map.getWidth() - 1) {
                nextPos.setX(pos.getX() + 1);
            } else {
                nextPos.setX(0);
            }
        }
        if (i == LEFT) {
            if (pos.getX() > 0) {
                nextPos.setX(pos.getX() - 1);
            } else {
                nextPos.setX(_map.getWidth() - 1);
            }
        }

        if(_map.getPixel(nextPos) == BLUE) {
            nextPos=pos;
        }

        return nextPos;
    }

    public void setGhostsStatus(int i, int status) {
        _ghosts[i].setStatus(status);
    }
}
