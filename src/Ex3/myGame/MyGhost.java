package Ex3.myGame;

import Ex3.Index2D;
import exe.ex3.game.GhostCL;

public class MyGhost implements GhostCL {
    private int _type;
    private long _lastGreen;
    private double _greenTimeOut;
    private Index2D _pos;
    private int _status;
    private int _dir;

    public MyGhost(int _type, Index2D _pos, int _status) {
        this._type = _type;
        this._pos = _pos;
        this._status = _status;
        this._greenTimeOut = 0;
    }

    public MyGhost(MyGhost _myGhost) {
        this._type = _myGhost._type;
        this._pos = _myGhost._pos;
        this._status = _myGhost._status;
        this._greenTimeOut = _myGhost._greenTimeOut;
        this._dir = _myGhost._dir;
        this._lastGreen = _myGhost._lastGreen;
    }

    @Override
    public int getType() {
        return _type;
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
        return _greenTimeOut;
    }

    public void setGreenTime(double time) {
        _greenTimeOut = time;
    }

    @Override
    public int getStatus() {
        return this._status;
    }

    public void  setStatus(int status) {
        this._status = status;
    }

    public void setPos(Index2D pos) {
        _pos=pos;
    }
}
