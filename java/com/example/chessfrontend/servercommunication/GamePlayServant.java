package com.example.chessfrontend.servercommunication;

import com.example.chessfrontend.Utilities;
import com.example.chessfrontend.modulus.ChessBoard;
import com.example.chessfrontend.modulus.ChessMove;
import javafx.application.Platform;
import javafx.event.Event;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class represents the implementation of the GamePlayService interface.
 * It handles the communication between the client and the server for chess game moves.
 */
public class GamePlayServant extends UnicastRemoteObject implements GamePlayService {

    private static final Utilities utilities = new Utilities();
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
    public void sendMove(String move) throws RemoteException {
        // Parse the move string into a ChessMove object
        ChessMove chessMove = parseMove(move);

        // Execute the move on the game board
        board.getGame().executeMove(chessMove.getCurrentPieceSquare(),
                chessMove.getTargetSquare(),
                chessMove.getTypeOfPieceToPromoteTo());

        // Update the graphical representation of the board
        board.updateBoard();
        board.setSquareYellow(chessMove.getTargetSquare());
        board.setSquareYellow(chessMove.getCurrentPieceSquare());
    }

    @Override
    public void enemyResigned() throws RemoteException {
        //TODO: finish later
        System.out.println("you won!");
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
     * Parses a move string into a ChessMove object.
     *
     * @param moveString The string representation of the chess move.
     *                   Format: sourceSquare + targetSquare + typeOfPieceToPromoteTo
     * @return The ChessMove object representing the parsed move.
     */
    private ChessMove parseMove(String moveString) {
        // Extract source square, target square, and type of piece to promote
        String sourceSquare = moveString.substring(0, 2);
        String targetSquare = moveString.substring(2, 4);
        char typeOfPieceToPromoteTo = moveString.charAt(4);

        // Convert square notation to byte representation
        byte currentPieceSquare = squareToByte(sourceSquare);
        byte targetSquareByte = squareToByte(targetSquare);

        // Create and return ChessMove object
        return new ChessMove(currentPieceSquare, targetSquareByte, typeOfPieceToPromoteTo);
    }

    /**
     * Converts a square notation (e.g., "a1") to its byte representation.
     *
     * @param square The square notation to be converted.
     * @return The byte representation of the square.
     */
    private byte squareToByte(String square) {
        // Extract file and rank from the square notation
        char fileChar = square.charAt(0);
        char rankChar = square.charAt(1);

        // Convert file and rank characters to zero-based indices
        int fileIndex = fileChar - 'a';
        int rankIndex = rankChar - '1';

        // Calculate the byte representation of the square
        return (byte) ((7 - rankIndex) * 8 + fileIndex);
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
