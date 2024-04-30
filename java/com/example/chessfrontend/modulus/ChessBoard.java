package com.example.chessfrontend.modulus;

import com.example.chessfrontend.PiecesImage;
import com.example.chessfrontend.ServerResponse;
import com.example.chessfrontend.Utilities;
import com.example.chessfrontend.modulus.gamelogic.pieces.Piece;
import com.example.chessfrontend.servercommunication.ServerRequestHandler;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import static com.example.chessfrontend.servercommunication.ServerRequestHandler.HTTP_OK;

public class ChessBoard extends GridPane {

    // color for squares
    private static final String WHITE_SQUARE_COLOR = "#3C4F76";
    private static final String BLACK_SQUARE_COLOR = "#D1BEB0";

    // constants and massages
    private static final int BOARD_SIZE = 8;
    private static double SQUARE_SIZE = 52;
    public static final char DEFAULT_PROMOTION = 'q';
    private static final String YOU_WON = "Yoy won the game! checkmate!";
    private static final String GAME_IS_DRAW = "It's a tie! the game ended in a draw";

    private StackPane selectedSquare = null;

    // constant for handling the game
    private final ChessGame game;
    private final StackPane[][] boardStackPane = new StackPane[8][8];
    private final ServerRequestHandler serverRequestHandler;
    private final Utilities utilities;

    private final PiecesImage piecesImage;

    /**
     * Constructs a new ChessBoard instance with the specified server request handler,
     * utilities, and VBox size.
     *
     * @param serverRequestHandler The server request handler for communicating with the server.
     * @param utilities            Utility methods for the chess game.
     * @param VboxSize             The size of the VBox containing the chess board.
     */
    public ChessBoard(ServerRequestHandler serverRequestHandler, Utilities utilities, double VboxSize) {
        SQUARE_SIZE = VboxSize / 8;
        this.serverRequestHandler = serverRequestHandler;
        this.utilities = utilities;
        game = new ChessGame();

        // get images for the pieces
        piecesImage = new PiecesImage((int) SQUARE_SIZE);

        // Create the chess board grid
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane square = new StackPane();
                // set size and colors of the squares
                square.setMinSize(SQUARE_SIZE, SQUARE_SIZE);
                square.setMaxSize(SQUARE_SIZE, SQUARE_SIZE);
                square.setStyle((row + col) % 2 == 0 ? "-fx-background-color: " + WHITE_SQUARE_COLOR + ";"
                        : "-fx-background-color: " + BLACK_SQUARE_COLOR + ";");

                // Add event handler to mark squares when pressed
                square.setOnMouseClicked(new SquareClickHandler(row, col));

                boardStackPane[row][col] = square;
                this.add(square, col, row);
            }
        }
        updateBoard();
    }

    /**
     * Handles mouse click events on squares of the chess board.
     */
    private class SquareClickHandler implements EventHandler<MouseEvent> {
        /**
         * The row index of the clicked square.
         */
        private final int row;

        /**
         * The column index of the clicked square.
         */
        private final int col;

        /**
         * Constructs a new SquareClickHandler with the specified row and column indices.
         *
         * @param row The row index of the clicked square.
         * @param col The column index of the clicked square.
         */
        public SquareClickHandler(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * Handles the mouse click event on a square.
         *
         * @param event The mouse click event.
         */
        @Override
        public void handle(MouseEvent event) {
            StackPane clickedSquare = (StackPane) event.getSource();

            if (selectedSquare == null) {
                // First square clicked
                selectedSquare = clickedSquare;
                selectedSquare.setStyle("-fx-background-color: yellow;");
            } else {
                // Second square clicked
                int initialSquareNumber = selectedSquareIndex();
                int finalSquareNumber = ((BOARD_SIZE - 1 - row) * BOARD_SIZE) + col; // Calculate square number

                tryToPlayMove(initialSquareNumber, finalSquareNumber, event);

                // Reset selected square styles
                selectedSquare.setStyle((GridPane.getRowIndex(selectedSquare)
                        + GridPane.getColumnIndex(selectedSquare)) % 2 == 0
                        ? "-fx-background-color: " + WHITE_SQUARE_COLOR + ";"
                        : "-fx-background-color: " + BLACK_SQUARE_COLOR + ";");
                selectedSquare = null;
            }
        }
    }


    /**
     * Calculates the square number of the selected square on the chess board.
     *
     * @return The square number of the selected square.
     */
    private int selectedSquareIndex() {
        int rowIdx = GridPane.getRowIndex(selectedSquare);
        int colIdx = GridPane.getColumnIndex(selectedSquare);
        return ((BOARD_SIZE - 1 - rowIdx) * BOARD_SIZE) + colIdx; // Calculate square number
    }

    /**
     * Attempts to play a move on the chess board.
     *
     * @param initialSquareNumber The square number of the initial position of the piece to move.
     * @param finalSquareNumber   The square number of the final position to move the piece to.
     * @param event               The event that triggered the move.
     */
    public void tryToPlayMove(int initialSquareNumber, int finalSquareNumber, Event event) {
        // check that the piece exist
        if (game.getPiece((byte) initialSquareNumber) == null)
            return;

        ServerResponse response = serverRequestHandler.submitMove(utilities.getGameUser(), initialSquareNumber,
                finalSquareNumber,
                utilities.getCurrentGameID());

        if (response == null) {
            utilities.waitPopupAlert(Utilities.UNEXPECTED_ERROR);
            return;
        }

        // if server approved the move play it, and update the board
        if (response.getHttpStatus() != HTTP_OK) {
            utilities.waitPopupAlert(response.getResponse());
        } else {
            int gameResult = game.executeMove((byte) initialSquareNumber, (byte) finalSquareNumber, DEFAULT_PROMOTION);
            handleGameResult(gameResult, event);
            updateBoard();
        }
    }


    /**
     * Handles the result of the chess game.
     *
     * @param gameResult The result of the chess game.
     * @param event      The event that triggered the game result.
     */
    private void handleGameResult(int gameResult, Event event) {
        if (gameResult == ChessGame.CHECKMATE) {
            utilities.waitPopupAlert(YOU_WON);
            utilities.goToPage(Utilities.GAME_LOBBY_PATH, event);
        }

        if (gameResult == ChessGame.DRAW) {
            utilities.waitPopupAlert(GAME_IS_DRAW);
            utilities.goToPage(Utilities.GAME_LOBBY_PATH, event);
        }
    }

    /**
     * Updates the chessboard with the current state of the game.
     */
    public void updateBoard() {
        this.getChildren().clear(); // Clear previous content

        // update the board
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane square = boardStackPane[row][col];
                // set color
                square.setStyle((row + col) % 2 == 0 ? "-fx-background-color: " + WHITE_SQUARE_COLOR + ";"
                        : "-fx-background-color: " + BLACK_SQUARE_COLOR + ";");

                int squareNumber = (BOARD_SIZE - 1 - row) * BOARD_SIZE + col;

                // clear the image if it was
                square.getChildren().clear();
                Piece piece = game.getPiece((byte) squareNumber);

                // if there is a piece at that square , get the piece image and add it to that square
                if (piece != null) {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(SQUARE_SIZE);
                    imageView.setFitHeight(SQUARE_SIZE);
                    Image pieceImage = piecesImage.getImageOfPiece(piece);
                    imageView.setImage(pieceImage);
                    square.getChildren().add(imageView);
                }
                this.add(square, col, row);
            }
        }
    }

    /**
     * Sets the background color of a specified square to yellow.
     *
     * @param square The byte value representing the square.
     */
    public void setSquareYellow(byte square) {
        // Calculate row and column indices from the byte value
        int row = 7 - (square / 8);
        int col = square % 8;

        boardStackPane[row][col].setStyle("-fx-background-color: yellow;");
    }

    /**
     * Retrieves the current state of the chess game.
     *
     * @return The chess game object.
     */
    public ChessGame getGame() {
        return game;
    }
}