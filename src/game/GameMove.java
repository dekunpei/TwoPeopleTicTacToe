package game;

/**
 * The GameMove class is used to represent a game move.
 */
class GameMove {
    GridNumber location;
    Player player;

    public GameMove(GridNumber aLoc, Player aPlayer) {
        location = aLoc;
        player = aPlayer;
    }
}
