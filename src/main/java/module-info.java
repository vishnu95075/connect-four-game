module com.game.connect4game {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.game.connect4game to javafx.fxml;
    exports com.game.connect4game;
}