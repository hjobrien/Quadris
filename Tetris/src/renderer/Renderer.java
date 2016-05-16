package renderer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import blocks.Tile;
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
import javafx.scene.shape.Rectangle;

/**
 * class that handles graphical output of all kinds from the game when run in graphical mode this
 * includes drawing the board, updating the score, and keeping track of all-time top scores 
 * 
 * @author Hank O'Brien
 *
 */
public class Renderer {


  private boolean doDebug;

  // private boolean doLog;

  public static final int VERTICAL_TILES = 20;
  public static final int HORIZONTAL_TILES = 10;

  public static final int SQUARE_SIZE = 30;

  // height should be double width
  public static final int GAME_WIDTH = HORIZONTAL_TILES * SQUARE_SIZE;
  public static final int GAME_HEIGHT = GAME_WIDTH * 2;

  public static final int SCREEN_WIDTH = GAME_WIDTH + 175;
  public static final int SCREEN_HEIGHT = GAME_HEIGHT;

  public static final int HELP_HEIGHT = SCREEN_HEIGHT - 210;
  public static final int HELP_WIDTH = SCREEN_WIDTH;


  public static final Rectangle[][] MAIN_BOARD_RECTS =
      new Rectangle[VERTICAL_TILES][HORIZONTAL_TILES];

  public static final Rectangle[][] NEXT_BOARD_RECTS = new Rectangle[4][4];

  // private static final String NEXT_PIECE_RECTS = null;

  private PrintStream scorePrinter;
  private Scanner scoreReader;
  private TextArea scoreList = null;
  private StackPane pauseView;
  private StringProperty valueProperty;
  // private boolean autoplay = false;

  // private Engine engine;

  private ArrayList<Integer> highScores = null; // need to put this here because i can't make
                                                // a
                                                // local variable passed to a method in the
                                                // listener

  /**
   * initialize the run values of the object
   * 
   * @param doDebug whether the object should print out its debug information
   * @param doLog whether the object should log its behavior
   */
  public Renderer(boolean doDebug) {
    this.doDebug = doDebug;
    setUpRects(MAIN_BOARD_RECTS);
    setUpRects(NEXT_BOARD_RECTS);
  }

  /**
   * updates the status of all graphical things, including drawing the game boards
   * @param score the score to display
   * @param numFullRows the number of full rows the engine has cleared
   * @param hasFullBoard if the engine has a full board
   * @param gameBoard the board to draw as the main game board
   * @param nextBoard the board to draw as the next piece board
   */
  public void update(int score, int numFullRows, boolean hasFullBoard, Tile[][] gameBoard,
      Tile[][] nextBoard) {
    updateScore(score, numFullRows);
    drawBoards(gameBoard, nextBoard);
    if (hasFullBoard) {
      updateHighScores(score);
    }

  }

  /**
   * initialize the Rectangle arrays to the square size and to the color white
   * @param rects the Rectangle array to initialize
   */
  public void setUpRects(Rectangle[][] rects) {
    for (int i = 0; i < rects.length; i++) {
      for (int j = 0; j < rects[i].length; j++) {
        rects[i][j] = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, Color.WHITE);
      }
    }
  }

  /**
   * called once to do the basic GUI setup things
   * 
   * @return A Scene containing the relevant StackPanes and GridPanes, its the main Scene
   * @throws IOException if file generation can't find a file or otherwise
   */
  public Scene makeGame() throws IOException {
    initializeScorePrinter();
    scoreList = new TextArea();
    scoreList.setText(getScoresForDisplay(highScores));
    StackPane main = new StackPane();

    GridPane mainGame = new GridPane();
    mainGame.getColumnConstraints().add(new ColumnConstraints(GAME_WIDTH));
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
    GridPane grid = new GridPane();
    GridPane nextBlockGrid = new GridPane();
    // Board gameBoard = new Board(VERTICAL_TILES, HORIZONTAL_TILES, SQUARE_SIZE, grid);
    // Board nextPieceBoard = new Board(4, 4, SQUARE_SIZE, nextBlock);
    makeBoardGrid(nextBlockGrid, 4, 4, NEXT_BOARD_RECTS);
    mainGame.add(nextBlockGrid, 2, 0);
    mainGame.add(grid, 0, 0, 1, 4);
    main.getChildren().add(mainGame);
    pauseView = constructPauseView(highScores);
    pauseView.setVisible(false);
    main.getChildren().add(pauseView);
    makeBoardGrid(grid, VERTICAL_TILES, HORIZONTAL_TILES, MAIN_BOARD_RECTS);
    return new Scene(main, SCREEN_WIDTH, SCREEN_HEIGHT);

  }

  /**
   * formats the GridPane in a way that is appropriate
   * 
   * @param grid a formatted GridPane
   */
  private void makeBoardGrid(GridPane grid, int height, int width, Rectangle[][] associatedRects) {
    for (int i = 0; i < width; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints(SQUARE_SIZE));
    }
    for (int i = 0; i < height; i++) {
      grid.getRowConstraints().add(new RowConstraints(SQUARE_SIZE));
    }
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        grid.add(associatedRects[i][j], j, i);
      }
    }
    grid.setGridLinesVisible(true);
  }

  /**
   * builds the view displayed when the game is paused
   * 
   * @param highScores a list of the historical high scores
   * @return a StackPane containing the necessary components
   */
  private StackPane constructPauseView(ArrayList<Integer> highScores) {
    final Label nameLabel = new Label("Quadris");
    nameLabel.setStyle(
        "-fx-font: 90 Arial; -fx-text-fill: rgb(255,255,255); -fx-font-weight: bold; -fx-font-style: italic; -fx-padding: -300 0 0 0;");
    GridPane pauseGrid = new GridPane();

    pauseGrid.getColumnConstraints().add(new ColumnConstraints(GAME_WIDTH));
    pauseGrid.getColumnConstraints().add(new ColumnConstraints(SCREEN_WIDTH - GAME_WIDTH));
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

    scoreList = new TextArea(getScoresForDisplay(highScores));
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

  /**
   * formats a string for the instructions view
   * 
   * @param s1 a key to be pressed (e.g. DOWN)
   * @param s2 the result of the key press (e.g. block down)
   * @return a formatted string based on the parameters
   */
  public String getInstructions(String s1, String s2) {
    return "\n" + String.format("%20s\t%s", s1, s2);
  }

  /**
   * formats the high scores into a display-able version
   * 
   * @param highScores the list of historical high scores
   * @return a string containing a formatted version of the parameter
   */
  private String getScoresForDisplay(ArrayList<Integer> highScores) {
    String scores = "\n\tHigh Scores:\n\n";
    String a = "";
    for (int i = 0; i < highScores.size(); i++) {
      a = i + 1 + ".";
      a += String.format("%" + (36 - (2 * Math.log10(highScores.get(i)))) + "s", "");
      scores = scores + a + highScores.get(i) + "\n";
      // scores = scores + (i+1) + ".\t\t\t\t\t\t" + highScores.get(i) + "\n";
    }
    return scores;
  }

  // /**
  // * called for each engine tick, draws the board
  // *
  // * @param board the board to be drawn
  // */
  // @Deprecated
  // public void draw(Board board) {
  // for (int i = 3; i < board.getBoardState().length; i++) {
  // for (int j = 0; j < board.getBoardState()[i].length; j++) {
  //
  // Tile current = board.getBoardState()[i][j];
  // if (current.isFilled()) {
  // board.getBoardRects()[i - 3][j].setFill(current.getColor());
  // } else {
  // board.getBoardRects()[i - 3][j].setFill(Color.WHITE);
  // }
  // }
  // }
  // }

  /**
   * called for each engine tick, draws the board
   * 
   * @param board the board to be drawn
   */
  private void drawToGameBoard(Tile[][] board) {
	  //the 3 should account for height accommodation
    for (int i = 3; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {

        Tile current = board[i][j];
        if (current.isFilled()) {
          MAIN_BOARD_RECTS[i - 3][j].setFill(current.getColor());
        } else {
          MAIN_BOARD_RECTS[i - 3][j].setFill(Color.WHITE);
        }
      }
    }
  }

  /**
   * called for each engine tick, draws the nextBoard
   * 
   * @param board the nextBoard to be drawn
   */
  private void drawToNextPieceBoard(Tile[][] board) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {

        Tile current = board[i][j];
        if (current.isFilled()) {
          NEXT_BOARD_RECTS[i][j].setFill(current.getColor());
        } else {
          NEXT_BOARD_RECTS[i][j].setFill(Color.WHITE);
        }
      }
    }
  }

  /**
   * toggles the pause state of the renderer
   */
  public void pause() {
    pauseView.setVisible(true);
  }

  /**
   * ends pause view by hiding parts of the pane
   */
  public void unpause() {
    pauseView.setVisible(false);
    for (Node child : pauseView.getChildren()) {
      if ((child instanceof StackPane)) {
        child.setVisible(false);
      }
    }

  }

  /**
   * updates the score shown on the right of the window
   * 
   * @param score the users current score
   * @param numOfFullRows the number of complete rows the user has created (and have then been
   *        cleared)
   */
  public void updateScore(int score, int numOfFullRows) {
    valueProperty.set("\tScore: " + score + "\nLines cleared: " + numOfFullRows);
  }

  /**
   * builds the FileIO objects for reading and writing the high scores file
   * 
   * @throws IOException for various file issues including FileNotFound
   */
  public void initializeScorePrinter() throws IOException {
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

  /**
   * reads in scores from persistent text-file storage
   * 
   * @param fileReader a Scanner on the high scores file
   * @return a List of integers representing the scores contained in the file
   */
  private ArrayList<Integer> readScores(Scanner fileReader) {
    ArrayList<Integer> scores = new ArrayList<Integer>(10);
    while (fileReader.hasNextInt()) {
      scores.add(fileReader.nextInt());
    }
    return scores;
  }

  /**
   * writes the scores to the file
   */
  public void writeScores() {
    for (int i = 0; i < highScores.size() - 1; i++) {
      scorePrinter.println(highScores.get(i));
    }
    if (!highScores.isEmpty()) {
      scorePrinter.print(highScores.get(highScores.size() - 1)); // so we don't indent after the
                                                                 // last score was printed
    }
  }

  /**
   * updates the scores with the new score from the just-ended game and trims the list to keep it
   * within 10 highscores
   * 
   * @param score the new score to add
   */
  public void updateHighScores(int score) {
    if (doDebug) {
      System.out.println(score);
    }
    highScores.add(score);
    Collections.sort(highScores, Collections.reverseOrder());
    if (!highScores.isEmpty() && highScores.size() > 10) {
      highScores.remove(highScores.size() - 1); // removes 11th high score
    }
  }

  /**
   * cleans up the file objects when the program is ended
   */
  public void close() {
    scorePrinter.close();
    scoreReader.close();
  }

  /**
   * draws the two boards to the screen by updating the color of the rectangles
   * @param gameBoard the mainGame to draw
   * @param nextPieceBoard the nextBoard to draw
   */
  public void drawBoards(Tile[][] gameBoard, Tile[][] nextPieceBoard) {
    drawToGameBoard(gameBoard);
    drawToNextPieceBoard(nextPieceBoard);
  }



}
