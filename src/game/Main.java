package game;

import java.util.Stack;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

/**
 * The Player enum is used to indicate the status of a playable location.
 */
enum Player {
    UNSET, CROSS, CIRCLE;
}

/**
 * The GridNum class is used to represent a location.
 */
class GridNum {
    int row;
    int column;

    public GridNum(int r, int c) {
        row = r;
        column = c;
    }

    public boolean isValid() {
        return row >=0 && column >=0;
    }
}

/**
 * The GameMove class is used to represent a game move.
 */
class GameMove {
    GridNum location;
    Player player;

    public GameMove(GridNum aLoc, Player aPlayer) {
        location = aLoc;
        player = aPlayer;
    }
}

/**
 * The Main class creates the JavaFX implementation.
 */
public class Main extends Application implements EventHandler<ActionEvent> {
    private static final int PLAY_BUTTON_SIZE = 220;
    private static final Player FIRST_PLAYER = Player.CROSS;

    private Button[][] playBs;
    private Button resetB;
    private Button undoB;
    private Button redoB;

    private HBox[] playRows;
    private VBox playGrid;


    private Player currentPlayer;
    private Player[][] occupied;

    private Stack<GameMove> moves;

    public static void main(String[] args) {
        launch(args);
    }


    // Create all the buttons for the application
    private Pane createButtonGroup() {
        playBs = new Button[3][3];
        playRows = new HBox[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playBs[i][j] = new Button();
                playBs[i][j].setPrefSize(PLAY_BUTTON_SIZE, PLAY_BUTTON_SIZE);
                playBs[i][j].setOnAction(this);
            }
        }

        for (int k = 0; k < 3; k++) {
            playRows[k] = new HBox(playBs[k][0], playBs[k][1], playBs[k][2]);

        }
        resetB = new Button("Reset");
        resetB.setStyle("-fx-font-size: 2em;");
        resetB.setOnAction(this);

        undoB = new Button("Undo");
        undoB.setStyle("-fx-font-size: 2em;");
        undoB.setOnAction(this);

        /*
        redoB = new Button("Redo");
        redoB.setStyle("-fx-font-size: 2em;");
        redoB.setOnAction(this);
        */

        HBox buttonRow = new HBox(resetB, undoB);

        playGrid = new VBox(buttonRow, playRows[0], playRows[1], playRows[2]);

        Pane layout = new StackPane(playGrid);
        return layout;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Simple Tic-Tac-Toe");
        Pane layout = createButtonGroup();
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();

        initGame();
    }

    // Initialize the game to unplayed state;
    private void initGame() {
        currentPlayer = FIRST_PLAYER;
        if (occupied == null) {
            occupied = new Player[3][3];
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                occupied[i][j] = Player.UNSET;
            }
        }
        moves = new Stack<GameMove>();
    }

    private boolean isPlayButton(Button b) {
        GridNum gNum = getPlayButtonCoordinate(b);
        return gNum.isValid();
    }

    private boolean isResetButton(Button b) {
        if (resetB == b) {
            return true;
        }
        return false;
    }

    private boolean isUndoButton(Button b) {
        if (undoB == b) {
            return true;
        }
        return false;
    }

    private void setOccupied(Button b, Player player) {
        GridNum gNum = getPlayButtonCoordinate(b);
        occupied[gNum.row][gNum.column] = player;
    }

    private boolean isOccupied(Button b) {
        GridNum gNum = getPlayButtonCoordinate(b);
        return occupied[gNum.row][gNum.column] != Player.UNSET;
    }

    private void clearMove(Button b) {

    }

    private void setMove(Button b) {
        if (isOccupied(b)) {
            return;
        }
        moves.push(new GameMove(getPlayButtonCoordinate(b), currentPlayer));
        if (currentPlayer == Player.CIRCLE) {
            b.setText("O");
            b.setStyle("-fx-font-size: 12em;");
            setOccupied(b, currentPlayer);
            currentPlayer = Player.CROSS;
        } else {
            b.setText("X");
            b.setStyle("-fx-font-size: 12em;");
            setOccupied(b, currentPlayer);
            currentPlayer = Player.CIRCLE;
        }
    }

    private GridNum getPlayButtonCoordinate (Button b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (playBs[i][j] == b) {
                    return new GridNum(i, j);
                }
            }
        }
        return new GridNum(-1, -1);
    }

    private void clearPlayButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playBs[i][j].setText(null);
            }
        }
        initGame();
    }


    private void undo() {
        if (moves.empty()) {
            return;
        }

        GameMove move = moves.peek();
        currentPlayer = move.player;
        occupied[move.location.row][move.location.column] = Player.UNSET;
        playBs[move.location.row][move.location.column].setText(null);
        moves.pop();

    }

    @Override
    public void handle(ActionEvent event) {
        if (!(event.getSource() instanceof Button)) {
            return;
        }
        Button clickedB = (Button) event.getSource();

        if (isPlayButton(clickedB)) {
            setMove(clickedB);
        } else if (isResetButton(clickedB)) {
            clearPlayButtons();
        } else if (isUndoButton(clickedB)) {
            undo();
        }
    }
}
