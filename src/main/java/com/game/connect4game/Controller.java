package com.game.connect4game;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static final float CIRCLE_DIAMETER = 80;

    private static final String DIS_COLOR1 = "#24303E";
    private static final String DIS_COLOR2 = "#4CAABB";

    private static String PLAYER_ONE = "Player One";
    private static String PLAYER_TWO = "Player Two";


    private boolean isPlayerOneTurn = true;

    private final Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS];   // For the Structural Changes

    private boolean isAllowToInsert = true;
    @FXML
    public GridPane rootGridPane;

    @FXML
    public Pane insertedDiscPane;

    @FXML
    public Label playerNameLabel;

    @FXML
    public Button setPayerNamesButton;

    @FXML
    public TextField playerOneInput;

    @FXML
    public TextField playerTwoInput;


    public void createPlayground() {
        Shape rectangleWithHoles = createGameStructuralGrid();
        rootGridPane.add(rectangleWithHoles, 0, 1);
        List<Rectangle> rectangleList = createClickColumns();

        for (Rectangle rectangle : rectangleList) {
            rootGridPane.add(rectangle, 0, 1);
        }
    }

    public Shape createGameStructuralGrid() {
        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER / 2);
                circle.setCenterX(CIRCLE_DIAMETER / 2);
                circle.setCenterY(CIRCLE_DIAMETER / 2);
                circle.setSmooth(true);
                circle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
                circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);


            }
        }

        rectangleWithHoles.setFill(Color.WHITE);

        return rectangleWithHoles;
    }

    private List<Rectangle> createClickColumns() {
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int col = 0; col < COLUMNS; col++) {

            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
            rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));
            final int column = col;
            rectangle.setOnMouseClicked(mouseEvent -> {
                if (isAllowToInsert) {
                    isAllowToInsert = false;
                    insertDis(new Disc(isPlayerOneTurn), column);
                }
            });

            rectangleList.add(rectangle);
        }
        return rectangleList;
    }

    private void insertDis(Disc disc, int column) {
        int row = ROWS - 1;
        while (row >= 0) {
            if (getDiscIfPresent(row, column) == null)
                break;
            row--;
        }
        if (row < 0)
            return;

        insertedDiscArray[row][column] = disc; // Structural Changes
        insertedDiscPane.getChildren().add(disc);

        int currentRow = row;

        disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
        translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
        translateTransition.setOnFinished(actionEvent -> {
            isAllowToInsert = true;
            if (gameEnded(currentRow, column)) {
                gameOver();
                return;
            }
            isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO);
        });
        translateTransition.play();

    }


    private boolean gameEnded(int row, int column) {

//        Vertical Point :
        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(r, column))
                .collect(Collectors.toList());
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(col -> new Point2D(row, col))
                .collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint1.add(i, -i))
                .collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint2.add(i, i))
                .collect(Collectors.toList());

        return checkCombinationsList(verticalPoints) || checkCombinationsList(horizontalPoints)
                || checkCombinationsList(diagonal1Points) || checkCombinationsList(diagonal2Points);

    }

    private boolean checkCombinationsList(List<Point2D> Points) {
        int chain = 0;
        for (Point2D point : Points) {

            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowIndexForArray, columnIndexForArray);

            if (disc != null && disc.isPlayerOnMove == isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }
        return false;
    }

    private Disc getDiscIfPresent(int row, int column) {
        if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0)
            return null;
        return insertedDiscArray[row][column];
    }

    private void gameOver() {
        String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four Game");
        alert.setHeaderText("The Winner is : " + winner);
        alert.setContentText("Want to Play again?");
        ButtonType yesButton = new ButtonType("YES");
        ButtonType noButton = new ButtonType("No, Exit");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Platform.runLater(() -> {
            Optional<ButtonType> btnClicked = alert.showAndWait();

            if (btnClicked.isPresent() && btnClicked.get() == yesButton) {
                resetGame();
            } else {
                Platform.exit();
                System.exit(0);
            }
        });


    }

    public void resetGame() {
        insertedDiscPane.getChildren().clear();
        for (Disc[] discs : insertedDiscArray) {
            Arrays.fill(discs, null);
        }
        isPlayerOneTurn = true;
        playerNameLabel.setText(PLAYER_ONE);
        createPlayground();
        isAllowToInsert = true;
    }

    private static class Disc extends Circle {
        private final boolean isPlayerOnMove;

        public Disc(boolean isPlayerOnMove) {
            this.isPlayerOnMove = isPlayerOnMove;
            setRadius(CIRCLE_DIAMETER / 2);
            setFill(isPlayerOnMove ? Color.valueOf(DIS_COLOR1) : Color.valueOf(DIS_COLOR2));
            setCenterX(CIRCLE_DIAMETER / 2);
            setCenterY(CIRCLE_DIAMETER / 2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPayerNamesButton.setOnAction(actionEvent -> setNames());
    }

    private void setNames() {
        PLAYER_ONE = playerOneInput.getText().length() > 3 ? playerOneInput.getText() : PLAYER_ONE;
        PLAYER_TWO = playerTwoInput.getText().length() > 3 ? playerTwoInput.getText() : PLAYER_TWO;
        playerNameLabel.setText(isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO);
    }
}