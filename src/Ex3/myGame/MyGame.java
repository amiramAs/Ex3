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

    /**
     * Returns the character representation of the key associated with this action.
     * @return The character of the key, or null if no specific key is assigned.
     */
    @Override
    public Character getKeyChar() {
        return null;
    }

    /**
     * Returns a string representation of the current position.
     * @param i The index or time step (currently not used in implementation).
     * @return A string describing the position.
     */
    @Override
    public String getPos(int i) {
        return _pos.toString();
    }

    /**
     * Retrieves the current position as a 2D index object.
     * @param i The index or time step (currently not used in implementation).
     * @return An Index2D object representing the 2D coordinates.
     */
    public Index2D getPos2D(int i) {
        return _pos;
    }

    /**
     * Gets the current direction of the entity.
     * @return An integer representing the direction (e.g., 0: Up, 1: Right, etc.).
     */
    public int getDir() {
        return this._dir;
    }

    /**
     * Returns an array containing all the ghost entities in the game.
     * @param i The index or time step (currently not used in implementation).
     * @return An array of MyGhost objects.
     */
    @Override
    public MyGhost[] getGhosts(int i) {
        return _ghosts;
    }

    /**
     * Retrieves the game board/map as a 2D integer array.
     * Each value in the array typically represents a type of tile (wall, path, etc.).
     * @param i The index or time step (currently not used in implementation).
     * @return A 2D int array representing the map layout.
     */
    @Override
    public int[][] getGame(int i) {
        return _map.getMap();
    }

    /**
     * The function implements the game server at each step.
     * It receives a number representing a direction of movement,
     * moves the Pacman if possible, eats pink dots if there are any, and ends the game if the Pacman hits a ghost.
     * @param i dir from client
     * @return null string
     */
    @Override
    public String move(int i) {
        String ans = "";
        if (_status == PLAY) {
            updatePlayerState(i);

            handleMapPixels();

            updateGhostsLogic(i);

            checkVictoryCondition();
        }
        return ans;
    }

    private void updatePlayerState(int i) {
        _pos = nextPos(i, _pos);
        if (i == UP) {
            _dir = 90;
        }
        if (i == DOWN) {
            _dir = 270;
        }
        if (i == LEFT) {
            _dir = 180;
        }
        if (i == RIGHT) {
            _dir = 0;
        }
    }

    private void handleMapPixels() {
        if (_map.getPixel(_pos) == PINK) {
            _map.setPixel(_pos, BLACK);
        }
        if (_map.getPixel(_pos) == GREEN) {
            _map.setPixel(_pos, BLACK);
            for (int j = 0; j < _ghosts.length; j++) {
                _ghosts[j].setStatus(2);
                _ghosts[j].setGreenTime(40 * _dt);
            }
        }
    }

    private void updateGhostsLogic(int i) {
        for (int j = 0; j < _ghosts.length; j++) {
            // בדיקת התנגשות
            Index2D ghostPos = pos2D(_ghosts[j].getPos(i));
            if (ghostPos.equals(_pos)) {
                if (_ghosts[j].getStatus() != 2) {
                    _status = LOSS;
                } else {
                    _ghosts[j].setStatus(3);
                    _ghosts[j].setGreenTime(20 * _dt);
                    _ghosts[j].setPos(new Index2D(10, 7));
                }
            }

            // תנועה אקראית
            if (_ghosts[j].getStatus() == 1 || _ghosts[j].getStatus() == 2) {
                int ghostDir = (int) (Math.random() * (5 - 1)) + 1;
                Index2D nextGhostPos = nextPos(ghostDir, _ghosts[j].getPos2D());
                _ghosts[j].setPos(nextGhostPos);
            }

            // עדכון טיימרים
            if (_ghosts[j].getStatus() == 2 || _ghosts[j].getStatus() == 3) {
                double time = _ghosts[j].remainTimeAsEatable(0) - _dt;
                _ghosts[j].setGreenTime(time);
                if (time <= 0) {
                    _ghosts[j].setStatus(1);
                }
            }
        }
    }

    private void checkVictoryCondition() {
        boolean remainPink = false;
        int[][] board = _map.getMap();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == PINK) {
                    remainPink = true;
                }
            }
        }

        if (!remainPink) {
            _status = DONE;
        }
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

    /**
     * Retrieves the current status of the game or entity.
     * * @return An integer representing the current state (e.g., Running, Paused, or Game Over).
     */
    @Override
    public int getStatus() {
        return _status;
    }

    /**
     * Updates the current status of the game or entity.
     * * @param status The new status code to be set.
     */
    public void setStatus(int status) {
        _status = status;
    }

    /**
     * Gets the "Delta Time" (dt) value used for game logic and physics updates.
     * This usually represents the time interval between frames or steps.
     * * @return The current delta time value.
     */
    public int get_dt(){
        return _dt;
    }

    /**
     * Checks whether the game map is cyclic (wraps around).
     * A cyclic map allows entities to move off one edge and reappear on the opposite side.
     * * @return true if the map boundaries are cyclic, false otherwise.
     */
    @Override
    public boolean isCyclic() {
        return _map.isCyclic();
    }

    @Override
    public String init(int i, String s, boolean b, long l, double v, int i1, int i2) {
        return "";
    }

    /**
     * Function converts position from string to object Index2D
     * @param pos String of position
     * @return Index2D of position
     */
    private static Index2D pos2D(String pos) {
        String[] parts = pos.replace("(", "")
                .replace(")", "")
                .split(",");
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        return new Index2D (x,y);
    }

    /**
     * The function receives the direction and current position of the Pacman,
     * checks based on the direction whether the next step is possible (walls, map symmetries) and returns a pixel of the next step.
     * @param i Step direction
     * @param pos Pacman's position
     * @return Index2D of the next step
     */
    private Index2D nextPos(int i, Index2D pos) {
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

    /**
     * Updates the status of a specific ghost in the game.
     * @param i The index of the ghost within the ghosts array.
     * @param status The new status value to be assigned to the specified ghost.
     * @throws ArrayIndexOutOfBoundsException if the index i is out of range.
     */
    public void setGhostsStatus(int i, int status) {
        _ghosts[i].setStatus(status);
    }
}
