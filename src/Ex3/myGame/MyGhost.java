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

    /**
     * Constructs a new MyGhost with specified type, position, and status.
     * * @param _type   The type of the ghost (e.g., specific ID or color).
     * @param _pos    The initial 2D position of the ghost.
     * @param _status The initial status of the ghost.
     */
    public MyGhost(int _type, Index2D _pos, int _status) {
        this._type = _type;
        this._pos = _pos;
        this._status = _status;
        this._greenTimeOut = 0;
    }

    /**
     * Copy constructor. Creates a new MyGhost as a deep copy of an existing one.
     * * @param _myGhost The ghost object to copy.
     */
    public MyGhost(MyGhost _myGhost) {
        this._type = _myGhost._type;
        this._pos = _myGhost._pos;
        this._status = _myGhost._status;
        this._greenTimeOut = _myGhost._greenTimeOut;
        this._dir = _myGhost._dir;
        this._lastGreen = _myGhost._lastGreen;
    }

    /**
     * Retrieves the type identifier of this ghost.
     * * @return An integer representing the ghost's type.
     */
    @Override
    public int getType() {
        return _type;
    }

    /**
     * Returns the ghost's position as a formatted string: "x,y".
     * * @param i The index or time step (not used in this implementation).
     * @return A string representation of the coordinates.
     */
    @Override
    public String getPos(int i) {
        return _pos.getX() + "," + _pos.getY();
    }

    /**
     * Returns the current position as an Index2D object.
     * * @return The Index2D object representing current coordinates.
     */
    public Index2D getPos2D() {
        return _pos;
    }

    /**
     * Provides additional information about the ghost.
     * * @return An empty string (placeholder for future implementation).
     */
    @Override
    public String getInfo() {
        return "";
    }

    /**
     * Returns the remaining time the ghost stays in an "eatable" (vulnerable) state.
     * * @param i The index or time step (not used in this implementation).
     * @return The remaining timeout value as a double.
     */
    @Override
    public double remainTimeAsEatable(int i) {
        return _greenTimeOut;
    }

    /**
     * Sets the duration for which the ghost remains in its "green" (eatable) state.
     * * @param time The duration to set.
     */
    public void setGreenTime(double time) {
        _greenTimeOut = time;
    }

    /**
     * Gets the current status code of the ghost.
     * * @return An integer representing the status.
     */
    @Override
    public int getStatus() {
        return this._status;
    }

    /**
     * Updates the current status of the ghost.
     * * @param status The new status value.
     */
    public void setStatus(int status) {
        this._status = status;
    }

    /**
     * Updates the ghost's current position.
     * * @param pos The new Index2D position.
     */
    public void setPos(Index2D pos) {
        _pos = pos;
    }
}
