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
    static int ERR = -1;
    static int STAY = 0;
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
        _dt = 100;
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
        Index2D nextPos = new Index2D(_pos);
        if (_status==PLAY) {
            if (i == UP) {
                if (_pos.getY() < _map.getHeight() - 1) {
                    nextPos.setY(_pos.getY() + 1);
                } else {
                    nextPos.setY(0);
                }
                _dir = 90;
            }
            if (i == DOWN) {
                if (_pos.getY() > 0) {
                    nextPos.setY(_pos.getY() - 1);
                } else {
                    nextPos.setY(_map.getHeight() - 1);
                }
                _dir = 270;
            }

            if (i == RIGHT) {
                if (_pos.getX() < _map.getWidth() - 1) {
                    nextPos.setX(_pos.getX() + 1);
                } else {
                    nextPos.setX(0);
                }
                _dir = 0;
            }

            if (i == LEFT) {
                if (_pos.getX() > 0) {
                    nextPos.setX(_pos.getX() - 1);
                } else {
                    nextPos.setX(_map.getWidth() - 1);
                }
                _dir = 180;
            }

            if(_map.getPixel(nextPos) != BLUE) {
                _pos=nextPos;
            }

            if (_map.getPixel(_pos) == PINK) {
                _map.setPixel(_pos, BLACK);
            }

//        for (int j =0;j<_ghosts.length;j++){
//            Index2D ghostPos=posInt(_ghosts[j].getPos(i));
//            if( ghostPos== _pos){
//                if(_ghosts[j].getStatus() == 1){
//                    _status=DONE;
//                }
//                if(_ghosts[j].getStatus() == 2){
//                    _ghosts[j].setStatus(0);
//                }
//            }
//        }
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
}
