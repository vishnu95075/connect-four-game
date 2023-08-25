package com.game.connect4game;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private Controller controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("application.fxml"));

        GridPane rootGridPane = fxmlLoader.load();

        controller = fxmlLoader.getController();
        controller.createPlayground();

        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        Scene scene = new Scene(rootGridPane);

        stage.setScene(scene);
        stage.setTitle("Connect 4 Game!!");
        stage.setResizable(false);
        stage.show();
    }

    public MenuBar createMenu() {
        // File Menu

        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(actionEvent -> controller.resetGame());

        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(actionEvent -> controller.resetGame());
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Exit Game");
        exitGame.setOnAction(actionEvent -> exitGame());

        fileMenu.getItems().addAll(newGame, resetGame, separatorMenuItem, exitGame);

        // Help Menu
        // File Menu

        Menu helpMenu = new Menu("Help");

        MenuItem aboutGame = new MenuItem("About Game!!");
        aboutGame.setOnAction(actionEvent -> aboutGame());
        SeparatorMenuItem separatorMenuItemHelp = new SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(actionEvent -> aboutMe());
        helpMenu.getItems().addAll(aboutGame, separatorMenuItemHelp, aboutMe);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;

    }

    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Developer");
        alert.setHeaderText("Vishnu Kumar Prajapati");
        alert.setContentText("My name is Vishnu Kumar Prajapati.I am a Full Stack Java Game Developer.I am pursuing B.Tech From Ramgarh Engineering College.");
        alert.show();
    }

    private void aboutGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four Game");
        alert.setHeaderText("How To Play Game?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves. A gameplay example (right), shows the first player starting Connect Four by dropping one of their yellow discs into the center column of an empty game board. The two players then alternate turns dropping one of their discs at a time into an unfilled column, until the second player, with red discs, achieves a diagonal four in a row, and wins the game. " + "If the board fills up before either player achieves four in a row, then the game is a draw.");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }


    public static void main(String[] args) {
        launch();
    }
}