package game;

import java.util.Stack;

class GameState {
    private static final Player INITIAL_LAST_FIRST_PLAYER = Player.CIRCLE;

    private Player lastFirstPlayer;
    private Player currentPlayer;
    private Player[][] occupied;

    private Stack<GameMove> prevMoves;
    private Stack<GameMove> nextMoves;

    public GameState() {
        lastFirstPlayer = INITIAL_LAST_FIRST_PLAYER;
        occupied = new Player[3][3];
        prevMoves = new Stack<GameMove>();
        nextMoves = new Stack<GameMove>();
        initGame();
    }

    private void chooseNewFirstPlayer() {
        if (lastFirstPlayer == Player.CROSS) {
            currentPlayer = Player.CIRCLE;
            lastFirstPlayer = Player.CIRCLE;
        } else {
            currentPlayer = Player.CROSS;
            lastFirstPlayer = Player.CROSS;
        }
    }

    private void chooseCurrentPlayer() {
        if (currentPlayer == Player.CIRCLE) {
            currentPlayer = Player.CROSS;
        } else {
            currentPlayer = Player.CIRCLE;
        }
    }

    private void clearOccupiedState() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                occupied[i][j] = Player.UNSET;
            }
        }
    }

    private void clearUndoRedoState() {
        prevMoves.clear();
        nextMoves.clear();
    }

    /**
     * Initialize the game to clean state;
     */
    public void initGame() {
        chooseNewFirstPlayer();
        clearOccupiedState();
        clearUndoRedoState();
    }

    public void setOccupied(GridNumber gridNum, Player player) {
        occupied[gridNum.row][gridNum.column] = player;
    }

    public Player getOccupiedBy(GridNumber gridNum) {
        return occupied[gridNum.row][gridNum.column];
    }

    public void setMove(GridNumber gridNum) {
        prevMoves.push(new GameMove(gridNum, currentPlayer));
        nextMoves.clear();
        setOccupied(gridNum, currentPlayer);
        chooseCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPreviousPlayer() {
        if (currentPlayer == Player.CIRCLE) {
            return Player.CROSS;
        } else {
            return Player.CIRCLE;
        }
    }

    public boolean canUndo() {
        return !prevMoves.empty();
    }

    public GameMove getUndoMove() {
        GameMove move = prevMoves.peek();
        return move;
    }

    public void undo() {
        if (prevMoves.empty()) {
            return;
        }

        GameMove move = prevMoves.peek();
        currentPlayer = move.player;
        occupied[move.location.row][move.location.column] = Player.UNSET;
        nextMoves.push(move);
        prevMoves.pop();
    }

    public boolean canRedo() {
        return !nextMoves.empty();
    }

    public GameMove getRedoMove() {
        return nextMoves.peek();
    }

    public void redo() {
        if (nextMoves.empty()) {
            return;
        }
        GameMove move = nextMoves.peek();
        if (move.player == Player.CIRCLE) {
            currentPlayer = Player.CROSS;
        } else {
            currentPlayer = Player.CIRCLE;
        }
        occupied[move.location.row][move.location.column] = move.player;

        prevMoves.push(move);
        nextMoves.pop();
    }
}
