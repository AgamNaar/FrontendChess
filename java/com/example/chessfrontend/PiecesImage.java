package com.example.chessfrontend;

import com.example.chessfrontend.modulus.gamelogic.pieces.*;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Objects;

/**
 * A class that represents an object responsible for retrieving images of the pieces from folders.
 */
public class PiecesImage {

    // Arrays to store images of white and black pieces
    private static final Image[] whitePiecesImages = new Image[6];
    private static final Image[] blackPiecesImages = new Image[6];

    // Constants representing piece types
    private static final String KING = "King";
    private static final String QUEEN = "Queen";
    private static final String ROOK = "Rook";
    private static final String BISHOP = "Bishop";
    private static final String KNIGHT = "Knight";
    private static final String PAWN = "Pawn";

    // Constants representing piece type indices in the arrays
    private static final int KING_PIECE_TYPE = 0;
    private static final int QUEEN_PIECE_TYPE = 1;
    private static final int ROOK_PIECE_TYPE = 2;
    private static final int BISHOP_PIECE_TYPE = 3;
    private static final int KNIGHT_PIECE_TYPE = 4;
    private static final int PAWN_PIECE_TYPE = 5;

    private static final String IMAGE_FILE_NAME = "imageFile";

    private static final File dir = new File(IMAGE_FILE_NAME);

    /**
     * Constructor to initialize the PiecesImage object and load images of pieces.
     *
     * @param size The size of the images to load.
     */
    public PiecesImage(int size) {
        // make sure it's a directory
        if (dir.isDirectory()) {
            for (final File f : Objects.requireNonNull(dir.listFiles(IMAGE_FILTER))) {
                try {
                    // insert the image to the right place on the collection
                    insertImageToCollection(f, size);
                } catch (final IOException e) {
                    System.out.println("error test");
                }
            }
        }
    }

    /**
     * Array containing supported image file extensions.
     */
    private static final String[] EXTENSIONS = new String[]{"gif", "png", "bmp"};

    /**
     * Filename filter used to identify image files based on their extensions.
     * This filter is used to filter files in a directory to include only image files.
     */
    private static final FilenameFilter IMAGE_FILTER = (dir, name) -> {
        // Iterate through each supported extension
        for (final String ext : EXTENSIONS) {
            // Check if the file name ends with the current extension
            if (name.endsWith("." + ext)) {
                // Return true if the file matches any of the supported extensions
                return true;
            }
        }
        // If the file does not match any supported extension, return false
        return false;
    };

    /**
     * Inserts the image represented by the file into the appropriate collection based on its name and color.
     *
     * @param f    The file representing the image of the chess piece.
     * @param size The size of the image.
     * @throws IOException If an error occurs while reading the image file.
     */
    private void insertImageToCollection(File f, int size) throws IOException {
        int type = -1;
        int indexOfPoint;

        // Extract the name of the piece from the file name
        String fileName = f.getName();
        indexOfPoint = fileName.indexOf(".");
        String pieceTypeString = fileName.substring(1, indexOfPoint);

        // Determine the type of chess piece based on its name
        switch (pieceTypeString) {
            case KING -> type = KING_PIECE_TYPE;
            case QUEEN -> type = QUEEN_PIECE_TYPE;
            case ROOK -> type = ROOK_PIECE_TYPE;
            case BISHOP -> type = BISHOP_PIECE_TYPE;
            case KNIGHT -> type = KNIGHT_PIECE_TYPE;
            case PAWN -> type = PAWN_PIECE_TYPE;
        }

        // Determine the color of the piece based on the first character of the file name
        if (fileName.charAt(0) == 'W') {
            // If the piece is white, store the image in the white pieces collection
            whitePiecesImages[type] = new Image(f.toURI().toString(), size, size, true, true);
        } else {
            // If the piece is black, store the image in the black pieces collection
            blackPiecesImages[type] = new Image(f.toURI().toString(), size, size, true, true);
        }
    }


    /**
     * Retrieves the image corresponding to the specified chess piece.
     *
     * @param piece The chess piece for which to retrieve the image.
     * @return The image representing the specified chess piece.
     */
    public Image getImageOfPiece(Piece piece) {
        // Default to the king piece type
        int pieceType = KING_PIECE_TYPE;

        // Determine the type of chess piece
        if (piece instanceof Queen) {
            pieceType = QUEEN_PIECE_TYPE;
        } else if (piece instanceof Rook) {
            pieceType = ROOK_PIECE_TYPE;
        } else if (piece instanceof Bishop) {
            pieceType = BISHOP_PIECE_TYPE;
        } else if (piece instanceof Knight) {
            pieceType = KNIGHT_PIECE_TYPE;
        } else if (piece instanceof Pawn) {
            pieceType = PAWN_PIECE_TYPE;
        }

        // Determine the color of the piece and retrieve the corresponding image
        if (piece.getColor()) {
            // If the piece is white, return the image from the white pieces collection
            return whitePiecesImages[pieceType];
        } else {
            // If the piece is black, return the image from the black pieces collection
            return blackPiecesImages[pieceType];
        }
    }


}


