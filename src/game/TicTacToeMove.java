package game;

/**
 * The TicTacToeMove class is used to represent a game move.
 */
class TicTacToeMove {
    private GridNumber location;
    private Player player;

    TicTacToeMove(GridNumber aLoc, Player aPlayer) {
        location = aLoc;
        player = aPlayer;
    }

    GridNumber getLocation() {
        return location;
    }

    Player getPlayer() {
        return player;
    }
}
