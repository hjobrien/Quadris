package engine;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mainGame.Board;
import tetrominoes.Tile;

public class Renderer {

  /**
   * change these flags to change the run mode. any generalized distribution version should likely
   * have all off
   */

  // public static final boolean DEBUG_MODE = true;
  public static final boolean DEBUG_MODE = false;

  public static final boolean LOG_MODE = true;
  // public static final boolean LOG_MODE = false;

  public static final int VERTICAL_TILES = 20;
  public static final int HORIZONTAL_TILES = 10;

  // height should be double width
  private static final int SCREEN_WIDTH = 300;
  private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 2;

  private static final int GAME_WIDTH = SCREEN_WIDTH + 175;
  private static final int GAME_HEIGHT = SCREEN_HEIGHT;
  
  private static final int HELP_HEIGHT = GAME_HEIGHT - 210;
  private static final int HELP_WIDTH = GAME_WIDTH;
  
  private static PrintStream scorePrinter;
  private static Scanner scoreReader;
  private static TextArea scoreList = null;
  private static StackPane pauseView;
  private static StringProperty valueProperty;
  
  private static ArrayList<Integer> highScores = null; // need to put this here because i can't make a
                                                       // local variable passed to a method in the listener



  public static Scene makeGame() throws IOException {
    initializeScorePrinter();
    scoreList = new TextArea();
    scoreList.setText("\n\tHigh Scores:\n\n" + getScoresForDisplay(highScores));
    StackPane main = new StackPane();

    // if we only make 1 board and it's in engine, we can always just receive the boardState
    // from engine when we need it and we wont have to be translating boardStates
    GridPane grid = new GridPane();
    GridPane nextBlock = new GridPane();
    Engine.setMode(DEBUG_MODE, LOG_MODE);
    Board.setMode(DEBUG_MODE);
    Board gameBoard = new Board(VERTICAL_TILES, HORIZONTAL_TILES, grid);
    Board nextPieceBoard = new Board(4, 4, nextBlock);
    Engine.setValues(gameBoard, nextPieceBoard);
    GridPane mainGame = new GridPane();
    mainGame.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH));
    mainGame.getColumnConstraints().add(new ColumnConstraints(20));
    mainGame.getColumnConstraints().add(new ColumnConstraints(150));

    for (int i = 0; i < 3; i++) {
      mainGame.getRowConstraints().add(new RowConstraints(150));
    }
    Label scoreText = new Label("Score: " + 0 + "\nLines cleared: " + 0);
    valueProperty = new SimpleStringProperty();
    valueProperty.setValue("0");
    scoreText.textProperty().bind(valueProperty);
    mainGame.add(scoreText, 2, 2);

    for (int i = 0; i < 4; i++) {
      nextBlock.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / HORIZONTAL_TILES));
    }
    for (int i = 0; i < 4; i++) {
      nextBlock.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / VERTICAL_TILES));
    }
    nextBlock.setGridLinesVisible(true);
    mainGame.add(nextBlock, 2, 0);
    mainGame.add(grid, 0, 0, 1, 4);
    main.getChildren().add(mainGame);
    pauseView = constructPauseView(highScores);
    pauseView.setVisible(false);
    main.getChildren().add(pauseView);
    configureGrid(grid);
    return new Scene(main, GAME_WIDTH, GAME_HEIGHT);

  }

  private static void configureGrid(GridPane grid) {
    for (int i = 0; i < HORIZONTAL_TILES; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH / HORIZONTAL_TILES));
    }
    for (int i = 0; i < VERTICAL_TILES; i++) {
      grid.getRowConstraints().add(new RowConstraints(SCREEN_HEIGHT / VERTICAL_TILES));
    }
    grid.setGridLinesVisible(true);
  }

  private static StackPane constructPauseView(ArrayList<Integer> highScores) {
    final Label nameLabel = new Label("Quadris");
    nameLabel.setStyle(
        "-fx-font: 90 Arial; -fx-text-fill: rgb(255,255,255); -fx-font-weight: bold; -fx-font-style: italic; -fx-padding: -300 0 0 0;");
    GridPane pauseGrid = new GridPane();

    pauseGrid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH));
    pauseGrid.getColumnConstraints().add(new ColumnConstraints(GAME_WIDTH - SCREEN_WIDTH));
    pauseGrid.getRowConstraints().add(new RowConstraints(540));

    Button helpButton = new Button("Instructions");
    helpButton.setMinWidth(175);
    helpButton.setMinHeight(30);
    helpButton.setStyle("-fx-background-color: rgba(108, 116, 118, 1);");

    Button scoreButton = new Button("High Scores");
    scoreButton.setMinWidth(175);
    scoreButton.setMinHeight(30);
    scoreButton.setStyle("-fx-background-color: rgba(108, 116, 118, 1);");

    pauseGrid.add(helpButton, 1, 1);
    pauseGrid.add(scoreButton, 1, 2);

    StackPane helpPane = new StackPane();
    StackPane scorePane = new StackPane();

    helpPane.setMaxHeight(HELP_HEIGHT);
    helpPane.setMaxWidth(HELP_WIDTH);

    helpPane.setStyle("-fx-background-color: rgba(100, 0, 100, 0.5);");
    helpPane.setVisible(false);

    TextArea buttonMapping = new TextArea(

        "\n\n"

            + getInstructions("Left Arrow:", "Move left")

            + getInstructions("Right Arrow:", "Move right")

            + getInstructions("Down Arrow:", "Speed down")

            + getInstructions("Z:", "Rotate Left")

            + getInstructions("X:", "Rotate Right")

            + getInstructions("Spacebar:", "Drop Piece")

            + getInstructions("R:", "Restart")

            + getInstructions("P:", "Pause")

    );
    buttonMapping.setEditable(false); // keeps pesky users from typing in it
    buttonMapping.getStylesheets().add("stylesheets/TextAreaStyle.css");
    buttonMapping.setStyle("-fx-text-fill: white;");
    helpPane.getChildren().add(buttonMapping);

    helpButton.setOnAction(e -> {
      scorePane.setVisible(false);
      helpPane.setVisible(true);
    });


    scorePane.setMaxHeight(HELP_HEIGHT);
    scorePane.setMaxWidth(HELP_WIDTH);
    scorePane.setStyle("-fx-background-color: rgba(100, 0, 100, 0.5);");
    scorePane.setVisible(false);

    scoreList = new TextArea("\n\tHigh Scores:\n\n" + getScoresForDisplay(highScores));
    scoreList.getStylesheets().add("stylesheets/TextAreaStyle.css");
    scoreList.setEditable(false); // keeps pesky users from typing in it
    scoreList.setStyle("-fx-text-fill: white;");
    StackPane.setAlignment(scoreList, Pos.CENTER);
    scorePane.getChildren().add(scoreList);

    scoreButton.setOnAction(e -> {
      helpPane.setVisible(false);
      scorePane.setVisible(true);
    });

    StackPane glass = new StackPane();
    StackPane.setAlignment(nameLabel, Pos.CENTER);
    StackPane.setAlignment(helpPane, Pos.BOTTOM_CENTER);
    StackPane.setAlignment(scorePane, Pos.BOTTOM_CENTER);
    glass.getChildren().addAll(nameLabel, helpPane, scorePane, pauseGrid);
    glass.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
    return glass;
  }
  
  public static String getInstructions(String s1, String s2) {
    return "\n" + String.format("%20s\t%s", s1, s2);
  }
  
  private static String getScoresForDisplay(ArrayList<Integer> highScores) {
    String scores = "";
    String a = "";
    for (int i = 0; i < highScores.size(); i++) {
      a = i + 1 + ".";
      a += String.format("%" + (36 - (2 * Math.log10(highScores.get(i)))) + "s", "");
      scores = scores + a + highScores.get(i) + "\n";
      // scores = scores + (i+1) + ".\t\t\t\t\t\t" + highScores.get(i) + "\n";
    }
    return scores;
  }
  
  public static void draw(Board board) {
    for (int i = 3; i < board.getBoardState().length; i++) {
      for (int j = 0; j < board.getBoardState()[i].length; j++) {

        Tile current = board.getBoardState()[i][j];
        if (current.isFilled()) {
          board.getBoardRects()[i - 3][j].setFill(current.getColor());
        } else {
          board.getBoardRects()[i - 3][j].setFill(Color.WHITE);
        }
      }
    }
  }

  // maybe do more here, for right now it's its own method
  public static void pause() {
    pauseView.setVisible(true);

  }

  public static void unpause() {
    pauseView.setVisible(false);
    for (Node child : pauseView.getChildren()) {
      if ((child instanceof StackPane)) {
        child.setVisible(false);
      }
    }

  }

  public static void updateScore(int score, int numOfFullRows) {
    valueProperty.set("\tScore: " + score + "\nLines cleared: " + numOfFullRows);
  }

  public static void initializeScorePrinter() throws IOException {
    File scoreFile = new File("src/gameLogs/High Scores");
    if (!scoreFile.exists()) {
      scoreFile.createNewFile();
      highScores = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
      scoreReader = new Scanner(scoreFile);
    } else {
      scoreReader = new Scanner(scoreFile);
      highScores = readScores(scoreReader);
    }
    scorePrinter = new PrintStream(scoreFile);
  }

  private static ArrayList<Integer> readScores(Scanner fileReader) {
    ArrayList<Integer> scores = new ArrayList<Integer>(10);
    while (fileReader.hasNextInt()) {
      scores.add(fileReader.nextInt());
    }
    return scores;
  }

  public static void writeScores() {
    for (int i = 0; i < highScores.size() - 1; i++) {
      scorePrinter.println(highScores.get(i));
    }
    if (!highScores.isEmpty()) {
      scorePrinter.print(highScores.get(highScores.size() - 1)); // so we don't indent after the
                                                                 // last score was printed
    }
  }


  public static void updateHighScores(int score) {
    if (DEBUG_MODE) {
      System.out.println(score);
    }
    highScores.add(score);
    Collections.sort(highScores, Collections.reverseOrder());
    if (!highScores.isEmpty() && highScores.size() > 10) {
      highScores.remove(highScores.size() - 1); // removes 11th high score
    }
  }

  public static void close() {
    scorePrinter.close();
    scoreReader.close();
  }












}
