module com.example.chessfrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    opens com.example.chessfrontend.controllers to javafx.fxml;


    opens com.example.chessfrontend to javafx.fxml;
    exports com.example.chessfrontend.modulus;
    exports com.example.chessfrontend;
    exports com.example.chessfrontend.controllers;
    exports com.example.chessfrontend.modulus.gamelogic.pieces;
    exports com.example.chessfrontend.modulus.gamelogic.specialmoves;
    exports com.example.chessfrontend.modulus.gamelogic.pieces.logic;
    exports com.example.chessfrontend.servercommunication to java.rmi;

}

