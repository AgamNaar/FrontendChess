package com.example.chessfrontend.servercommunication;

import com.example.chessfrontend.ServerResponse;
import com.example.chessfrontend.modulus.GameUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.chessfrontend.modulus.ChessBoard.DEFAULT_PROMOTION;

/**
 * Handles communication with the server, including sending requests and receiving responses.
 */
public class ServerRequestHandler {
    // URL constants for server endpoints
    public static final String SERVER_URL = "http://localhost:8080";
    public static final String USER_URL = SERVER_URL + "/user";
    public static final String LOGIN_URL = USER_URL + "/login";
    public static final String CREATE_ACCOUNT_URL = USER_URL + "/create";
    private static final String GET_TOP_4_URL = USER_URL + "/get-top4";
    private static final String FIND_PLAYER_URL = USER_URL + "/get-user";
    private static final String MATCHMAKING_URL = SERVER_URL + "/matchmaking";
    private static final String FIND_MATCH_URL = MATCHMAKING_URL + "/find-match";
    private static final String GAMEPLAY_URL = SERVER_URL + "game/online/submit-move/";

    // HTTP request methods
    public static final String POST = "POST";
    public static final String GET = "GET";

    // HTTP status codes
    public static final int HTTP_OK = 200;
    private static final int TIME_OUT_MILLISECOND = 20 * 1000;

    /**
     * Submits a move to the server for the specified game.
     *
     * @param gameUser            the user making the move
     * @param initialSquareNumber the square from which the piece is being moved
     * @param targetSquareNumber  the target square to which the piece is being moved
     * @param currentGameID       the ID of the current game
     * @return the server response to the move submission
     */
    public ServerResponse submitMove(GameUser gameUser, int initialSquareNumber,
                                     int targetSquareNumber, int currentGameID) {
        String jsonPayLoad = parseIntoJson(gameUser, initialSquareNumber, targetSquareNumber);
        return sendNewRequestToServer(POST, jsonPayLoad, GAMEPLAY_URL + currentGameID);
    }

    /**
     * Finds a player with the specified username.
     *
     * @param playerFindUsername the username of the player to find
     * @return the server response containing information about the player
     */
    public ServerResponse findPlayer(String playerFindUsername) {
        return sendNewRequestToServer(POST, playerFindUsername, FIND_PLAYER_URL);
    }

    /**
     * Logs into the user account using the provided GameUser object.
     *
     * @param gameUser the GameUser object representing the user's credentials
     * @return the server response containing the login status and user information
     */
    public ServerResponse logIntoAccount(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), LOGIN_URL);
    }

    /**
     * Creates a new user account using the provided GameUser object.
     *
     * @param gameUser the GameUser object representing the new user's information
     * @return the server response containing the account creation status
     */
    public ServerResponse createNewAccount(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), CREATE_ACCOUNT_URL);
    }

    /**
     * Retrieves the top four players from the server.
     *
     * @return the server response containing information about the top four players
     */
    public ServerResponse getTopFourPlayers() {
        return sendNewRequestToServer(GET, "", GET_TOP_4_URL);
    }

    /**
     * Initiates a matchmaking request to find a match against a random player.
     *
     * @param gameUser the GameUser object representing the current user
     * @return the server response containing information about the match
     */
    public ServerResponse findMatchVsRandom(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), FIND_MATCH_URL);
    }


    /**
     * Sends a new HTTP request to the server with the specified method, payload, and URL address.
     * Handles both successful and error responses from the server.
     *
     * @param method     the HTTP method (e.g., "POST", "GET") for the request
     * @param payload    the payload or data to be sent with the request
     * @param urlAddress the URL address of the server endpoint to send the request to
     * @return a ServerResponse object containing the server's response to the request
     */
    private ServerResponse sendNewRequestToServer(String method, String payload, String urlAddress) {
        HttpURLConnection connection = null;
        try {
            // Contract a new http request to the server
            URL url = new URL(urlAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(TIME_OUT_MILLISECOND);
            connection.setDoOutput(true);

            // Only write to output stream if the request method is POST
            if ("POST".equals(method)) {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();

            BufferedReader reader;
            if (responseCode >= 400) {
                // For error responses (status code 400 and above), read from error stream
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                // For successful responses (status code 200-399), read from input stream
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

            // Read the response from the server
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new ServerResponse(response.toString(), responseCode);
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * Converts the specified game move information into a JSON string representation.
     *
     * @param gameUser            the GameUser object representing the user's information
     * @param initialSquareNumber the square number from which the piece is being moved
     * @param targetSquareNumber  the square number to which the piece is being moved
     * @return a JSON string representing the game move information
     */
    public String parseIntoJson(GameUser gameUser, int initialSquareNumber, int targetSquareNumber) {
        // Construct JSON string manually
        return "{" +
                "\"gameUser\": {" +
                "\"userName\": \"" + gameUser.getUserName() + "\"," +
                "\"token\": \"" + gameUser.getToken() + "\"" +
                "}," +
                "\"chessMove\": {" +
                "\"currentPieceSquare\": " + initialSquareNumber + "," +
                "\"targetSquare\": " + targetSquareNumber + "," +
                "\"typeOfPieceToPromoteTo\": \"" + DEFAULT_PROMOTION + "\"" +
                "}" +
                "}";
    }


}
