package game;

import java.util.Stack;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;



/**
 * The Main class creates the JavaFX implementation.
 */
public class Main extends Application {
    private static final int PLAY_BUTTON_SIZE = 220;
    private static final String APP_TITLE = "Simple Tic-Tac-Toe";

    private ButtonClickHandler buttonClickHandler;
    private GameState gameState;

    private Button[][] playBs;
    private Button resetB;
    private Button undoB;
    private Button redoB;

    private HBox[] playRows;
    private VBox playGrid;

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
                playBs[i][j].setOnAction(buttonClickHandler);
                playBs[i][j].setStyle("-fx-font-size: 12em;");
            }
        }

        for (int k = 0; k < 3; k++) {
            playRows[k] = new HBox(playBs[k][0], playBs[k][1], playBs[k][2]);

        }
        resetB = new Button("Reset");
        resetB.setStyle("-fx-font-size: 2em;");
        resetB.setOnAction(buttonClickHandler);

        undoB = new Button("Undo");
        undoB.setStyle("-fx-font-size: 2em;");
        undoB.setOnAction(buttonClickHandler);

        redoB = new Button("Redo");
        redoB.setStyle("-fx-font-size: 2em;");
        redoB.setOnAction(buttonClickHandler);

        HBox buttonRow = new HBox(resetB, undoB, redoB);

        playGrid = new VBox(buttonRow, playRows[0], playRows[1], playRows[2]);

        Pane layout = new StackPane(playGrid);
        return layout;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle(APP_TITLE );
        gameState = new GameState();
        buttonClickHandler = new ButtonClickHandler(this);
        Pane layout = createButtonGroup();
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    boolean isPlayButton(Button b) {
        GridNumber gNum = getPlayButtonCoordinate(b);
        return gNum.isValid();
    }

    boolean isResetButton(Button b) {
        if (resetB == b) {
            return true;
        }
        return false;
    }

    boolean isUndoButton(Button b) {
        if (undoB == b) {
            return true;
        }
        return false;
    }

    boolean isRedoButton(Button b) {
        if (redoB == b) {
            return true;
        }
        return false;
    }

    private boolean isOccupied(Button b) {
        GridNumber gNum = getPlayButtonCoordinate(b);
        return gameState.getOccupiedBy(gNum) != Player.UNSET;
    }

    void play(Button b) {
        if (isOccupied(b)) {
            return;
        }
        Player currentPlayer = gameState.getCurrentPlayer();
        gameState.setMove(getPlayButtonCoordinate(b));
        if (currentPlayer == Player.CIRCLE) {
            b.setText("O");
        } else {
            b.setText("X");
        }
    }

    private GridNumber getPlayButtonCoordinate (Button b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (playBs[i][j] == b) {
                    return new GridNumber(i, j);
                }
            }
        }
        return new GridNumber(-1, -1);
    }

    void reset() {
        gameState.initGame();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playBs[i][j].setText(null);
            }
        }
    }

    void undo() {
        if (!gameState.canUndo()) {
            return;
        }
        GameMove move = gameState.getUndoMove();
        playBs[move.location.row][move.location.column].setText(null);
        gameState.undo();
    }

    void redo() {
        if (!gameState.canRedo()) {
            return;
        }
        GameMove move = gameState.getRedoMove();
        if (move.player == Player.CIRCLE) {
            playBs[move.location.row][move.location.column].setText("O");
        } else {
            playBs[move.location.row][move.location.column].setText("X");
        }
        gameState.redo();

    }
}
