package com.example.chessfrontend.controllers;

import com.example.chessfrontend.ServerResponse;
import com.example.chessfrontend.Utilities;
import com.example.chessfrontend.modulus.ChessBoard;
import com.example.chessfrontend.modulus.GameUser;
import com.example.chessfrontend.servercommunication.ServerRequestHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.LinkedList;

import static com.example.chessfrontend.servercommunication.ServerRequestHandler.HTTP_OK;

/**
 * Controller class for the chess game interface.
 */
public class ChessGameController {

    private static final String YOU_LOST = "You lost the game";
    private static final String FAILED_TO_RESIGN = "Failed to resign, try again";

    private final ServerRequestHandler serverRequestHandler = new ServerRequestHandler();
    private final Utilities utilities = new Utilities();

    /**
     * Initializes the chess game interface.
     */
    public void initialize() {
        // Initialize and add the chess GUI to the VBox
        ChessBoard chessBoard = new ChessBoard(serverRequestHandler, utilities, ChessGameVBox.getPrefWidth());
        ChessGameVBox.getChildren().add(chessBoard);

        // set up the board for the servant
        utilities.getGamePlayServant().setBoard(chessBoard);
        setUpPlayersNamesAndRating();
    }

    /**
     * Sets up the names and ratings of the players.
     */
    private void setUpPlayersNamesAndRating() {
        ServerResponse response = serverRequestHandler.getPlayerOfMatch(utilities.getCurrentGameID());

        // if failed to get the player info
        if (response == null || response.getHttpStatus() != HTTP_OK)
            return;

        // parse the response from json into list of game  user
        LinkedList<GameUser> gameUserLinkedList = utilities.parseGameUserList(response.getResponse());

        // show players info
        WhiteUserNameText.setText(WhiteUserNameText.getText() + gameUserLinkedList.get(0).getUserName());
        WhiteRatingText.setText(WhiteRatingText.getText() + gameUserLinkedList.get(0).getRating());
        BlackUserNameText.setText(BlackUserNameText.getText() + gameUserLinkedList.get(1).getUserName());
        BlackRatingText.setText(BlackRatingText.getText() + gameUserLinkedList.get(1).getRating());
    }

    @FXML
    private Text BlackRatingText;

    @FXML
    private Text BlackUserNameText;

    @FXML
    private VBox ChessGameVBox;

    @FXML
    private Text WhiteRatingText;

    @FXML
    private Text WhiteUserNameText;

    /**
     * Handles the action when the resignation button is clicked.
     *
     * @param event The action event triggered by clicking the resignation button.
     */
    @FXML
    void ResignClicked(ActionEvent event) {
        ServerResponse response = serverRequestHandler.reignTheGame(utilities.getGameUser(),
                utilities.getCurrentGameID());
        if (response != null && response.getHttpStatus() == HTTP_OK) {
            utilities.waitPopupAlert(YOU_LOST);
            utilities.goToPage(Utilities.GAME_LOBBY_PATH, event);
        } else {
            utilities.waitPopupAlert(FAILED_TO_RESIGN);
        }
    }
}
