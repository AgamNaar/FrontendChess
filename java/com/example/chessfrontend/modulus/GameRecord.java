package com.example.chessfrontend.modulus;

/**
 * Represents a record of a chess game.
 */
public class GameRecord {
    private final String whitePlayerName;
    private final String blackPlayerName;
    private final int whitePlayerRating;
    private final int blackPlayerRating;
    private final String winnerUserName;
    private final int gameID;

    /**
     * Constructs a new GameRecord object.
     *
     * @param whitePlayerName   The name of the white player
     * @param blackPlayerName   The name of the black player
     * @param whitePlayerRating The rating of the white player
     * @param blackPlayerRating The rating of the black player
     * @param winnerUserName    The username of the winner player
     * @param gameID            The ID of the game
     */
    public GameRecord(String whitePlayerName, String blackPlayerName,
                      int whitePlayerRating, int blackPlayerRating, String winnerUserName, int gameID) {
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        this.whitePlayerRating = whitePlayerRating;
        this.blackPlayerRating = blackPlayerRating;
        this.winnerUserName = winnerUserName;
        this.gameID = gameID;
    }

    /**
     * Gets the name of the white player.
     *
     * @return The name of the white player
     */
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    /**
     * Gets the name of the black player.
     *
     * @return The name of the black player
     */
    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    /**
     * Gets the rating of the white player.
     *
     * @return The rating of the white player
     */
    public int getWhitePlayerRating() {
        return whitePlayerRating;
    }

    /**
     * Gets the rating of the black player.
     *
     * @return The rating of the black player
     */
    public int getBlackPlayerRating() {
        return blackPlayerRating;
    }

    /**
     * Gets the username of the winner player.
     *
     * @return The username of the winner player
     */
    public String getWinnerUserName() {
        return winnerUserName;
    }

    /**
     * Gets the ID of the game.
     *
     * @return The ID of the game
     */
    public int getGameID() {
        return gameID;
    }
}
