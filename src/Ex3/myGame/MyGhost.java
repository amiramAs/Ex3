package Ex3.myGame;

import Ex3.Index2D;
import exe.ex3.game.GhostCL;

public class MyGhost implements GhostCL {
    private int _type;
    private long _lastGreen;
    private long _greenTimeOut;
    private Index2D _pos;
    private int _status;
    private int _dir;

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String getPos(int i) {
        return _pos.getX() + "," + _pos.getY();
    }

    public Index2D getPos2D() {
        return _pos;
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public double remainTimeAsEatable(int i) {
        return 0;
    }

    @Override
    public int getStatus() {
        return this._status;
    }

    public void  setStatus(int status) {
        this._status = status;
    }
}
