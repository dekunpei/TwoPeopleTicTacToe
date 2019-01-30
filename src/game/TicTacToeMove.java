package game;

/**
 * The TicTacToeMove class is used to represent a game move.
 */
class TicTacToeMove {
    GridNumber location;
    Player player;

    TicTacToeMove(GridNumber aLoc, Player aPlayer) {
        location = aLoc;
        player = aPlayer;
    }
}
