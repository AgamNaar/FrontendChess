package com.example.chessfrontend.servercommunication;

import com.example.chessfrontend.Utilities;
import com.example.chessfrontend.modulus.ChessBoard;
import com.example.chessfrontend.modulus.ChessMove;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static com.example.chessfrontend.Utilities.GAME_LOBBY_PATH;

/**
 * This class represents the implementation of the GamePlayService interface.
 * It handles the communication between the client and the server for chess game moves.
 */
public class GamePlayServant extends UnicastRemoteObject implements GamePlayService {

    private static final Utilities utilities = new Utilities();
    private static final String ENEMY_HAS_RESIGN_TEXT = "You have won, the enemy has resigned! ";
    private ChessBoard board;
    private Event event;

    /**
     * Constructs a GamePlayServant with the specified ChessBoard.
     *
     * @throws RemoteException if there is a communication-related exception.
     */
    public GamePlayServant() throws RemoteException {

    }

    /**
     * Sends a chess move to the other player to notify him.
     *
     * @param move The string representation of the chess move to be executed.
     *             Format: sourceSquare + targetSquare + typeOfPieceToPromoteTo
     * @throws RemoteException if there is a communication-related exception.
     */
    @Override
    public void sendMove(ChessMove move) throws RemoteException {
        // Parse the move string into a ChessMove object
        //ChessMove chessMove = parseMove(move);

        // Execute the move on the game board
        board.getGame().executeMove(move.getCurrentPieceSquare(),
                move.getTargetSquare(),
                move.getTypeOfPieceToPromoteTo());

        // Update the graphical representation of the board
        Platform.runLater(() -> {
            board.updateBoard();
            board.setSquareYellow(move.getTargetSquare());
            board.setSquareYellow(move.getCurrentPieceSquare());
        });
    }

    /**
     * Called when the enemy resigns from the game.
     * This method updates the UI to notify the player that the enemy has resigned and navigates
     * to the game lobby.
     *
     * @throws RemoteException If there is a communication-related exception during the method invocation.
     */
    @Override
    public void enemyResigned() throws RemoteException {
        try {
            // Run on the JavaFX Application Thread to update the UI
            Platform.runLater(() -> {
                // notify player that he has won
                utilities.waitPopupAlert(ENEMY_HAS_RESIGN_TEXT);
                // go back to the game lobby
                Stage stage = (Stage) board.getScene().getWindow();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(GAME_LOBBY_PATH));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    System.out.println("Error loading the new page: " + e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides the method to handle accepting a challenge in the game.
     *
     * @param gameID The ID of the game being accepted.
     * @throws RemoteException If there is a remote communication problem.
     */
    @Override
    public void challengeAccept(int gameID) throws RemoteException {
        // Set the current game ID
        utilities.setCurrentGameID(gameID);

        // Run on the JavaFX Application Thread to update the UI
        Platform.runLater(() -> utilities.goToPage(Utilities.CHESS_GAME_PATH, event));
    }


    /**
     * Gets the chessboard for the game.
     *
     * @return The chessboard.
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Sets the chessboard for the game.
     *
     * @param board The chessboard to set.
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Sets the event associated with the game.
     *
     * @param event The event to set.
     */
    public void setEvent(Event event) {
        this.event = event;
    }
}
