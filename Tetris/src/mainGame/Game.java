package mainGame;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import cerulean.Cerulean;
import engine.Engine;
import engine.GameMode;
import engine.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Game extends Application {


  // change these
  public static final GameMode GAME_MODE = GameMode.AUTOPLAY;
  public static final int MAX_GAMES = 30;
  public static final int MAX_GENERATIONS = 15;
  public static final double MUTATION_FACTOR = 0.5; // value between 0 and 1 where 0 is no mutations
                                                    // ever and 1 is a mutation every time


  // don't change these
  private static boolean doDebug;
  private static boolean doLog;
  private static boolean autoplay;
  private static boolean randomizeBlocks;
  private static boolean playMultiple; // play multiple games in a row

  // public static final double[] WEIGHTS = new double[]{-294.75, -34.44, 101.72, 5};
  public static final double[] WEIGHTS = new double[] {-200, -50, 100, 1.68};


  public static final int MAX_MILLIS_PER_TURN = 1000;
  public static final int MIN_MILLIS_PER_TURN = 100;

  public static final int VERTICAL_TILES = 20;
  public static final int HORIZONTAL_TILES = 10;
  private static final int SQUARE_SIZE = 29;

  // if nintendo scoring = false, hank/liam scoring is used
  public static final boolean NINTENDO_SCORING = false;


  private static AnimationTimer timer;
  private static PrintStream printer;
  private static int timeScore = 0;
  private static double timePerTurn = MAX_MILLIS_PER_TURN;

  private static ArrayList<Integer> scoreHistory = new ArrayList<Integer>();

  // can be changed if not desired
  private static boolean dropDownTerminatesBlock = true;

  // seeded possible solutions
  public static double[][] species = new double[][] {{-70, -70, 500, 5}, {-100, -50, 100, 2},
      {-200, -70, 300, 7}, {-40, -100, 400, 1}, {-200, -50, 100, 0}, {-400, -300, 100, 1},
      {-200, -100, 100, 3}, {-150, -70, 400, 0}, {-70, -150, 500, -5}, {-200, -35.4, 100, 8},
      {-294.75, -34.44, 101.72, 5}};
  // public static double[][] species = new double[][]{{-70,-70,500, 5}, {-100, -50, 100, 8}, {-10,
  // -10, 100, 5}};

  private static int currentSpecies = 0;
  private static int generationNum = 0;

  private static double[] speciesAvgScore = new double[species.length];


  private static boolean paused = false;

  private static boolean gameIsActive = true;



  public static void main(String[] args) throws IOException {
    configureSettings();

    launch(args);
  }

  /**
   * configures the run settings of the game based on the user selected run configuration
   * 
   * @throws IOException if the file cannot be created or it cannot be found
   */
  public static void configureSettings() {
    if (GAME_MODE == GameMode.AI_TRAINING) {
      File aiLogFile = new File("src/gameLogs/AI output" + System.currentTimeMillis());
      try {
        aiLogFile.createNewFile();
        printer = new PrintStream(aiLogFile);
      } catch (IOException e) {
        System.err.println("Error on file creation");
      }
    }

    GridPane grid = new GridPane();
    GridPane nextBlock = new GridPane();
    Engine.setMode(doDebug, doLog, autoplay);
    Board.setMode(doDebug);
    Board gameBoard = new Board(VERTICAL_TILES, HORIZONTAL_TILES, SQUARE_SIZE, grid);
    Board nextPieceBoard = new Board(4, 4, SQUARE_SIZE, nextBlock);
    Engine.setBoards(gameBoard, nextPieceBoard);
    // setup program settings
    switch (GAME_MODE) {
      case DISTRO:
        doDebug = false;
        doLog = false;
        autoplay = false;
        randomizeBlocks = true;
        playMultiple = false;
        break;
      case DEBUG:
        doDebug = false;
        doLog = false;
        autoplay = false;
        randomizeBlocks = false;
        playMultiple = false;
        break;
      case LOGGER:
        doDebug = false;
        doLog = true;
        autoplay = false;
        randomizeBlocks = true;
        playMultiple = false;
        break;
      case AUTOPLAY:
        Cerulean.setWeights(WEIGHTS);
        dropDownTerminatesBlock = false;
        doDebug = false;
        doLog = false;
        autoplay = true;
        randomizeBlocks = true;
        playMultiple = false;
        break;
      case AI_TRAINING:
        dropDownTerminatesBlock = false;
        playMultiple = true;
        doDebug = false;
        doLog = false;
        autoplay = true;
        randomizeBlocks = false;
        break;
      default:
        System.err.println("Error: unsupported mode");
        doDebug = false;
        doLog = false;
        autoplay = false;
        randomizeBlocks = true;
        playMultiple = false;
        break;
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    Renderer.setMode(doDebug, doLog, autoplay);
    Scene boardScene = Renderer.makeGame();
    Renderer.draw(Engine.getBoard());
    Engine.setRandomizeBlocks(randomizeBlocks);
    if (!autoplay) {
      stage.addEventFilter(KeyEvent.KEY_PRESSED, new UserInputHandler());
    }

    stage.addEventFilter(KeyEvent.KEY_PRESSED, new BasicInputHandler());

    stage.setScene(boardScene);

    timer = configureTimer(true);
    timer.start();


    stage.show();
    Engine.addBlock(); // needs to be towards the end of method so initial event fires correctly
  }

  public void run(boolean randomizeBlocks, boolean useGraphics) throws IOException {

    long pastTime = 0;
    Engine.setRandomizeBlocks(randomizeBlocks);
    if (useGraphics) {
      launch();
    } else {
      timer = configureTimer(useGraphics);
      timer.start();

      Engine.addBlock(); // needs to be towards the end of method so initial event fires correctly
      while (!Engine.getBoard().isFull()) {
        long now = System.currentTimeMillis();
        if (!paused && now - pastTime >= timePerTurn) {
          update(useGraphics);
          pastTime = now;
        }
      }
    }
  }

  private void update(boolean useGraphics) {
    if (useGraphics) {
      Renderer.updateScore(timeScore + Engine.getBoard().getBoardScore(),
          Engine.getBoard().getNumOfFullRows());
    }
    Engine.update();
    if (Engine.getBoard().isFull()) {
      int score = getScore();
      System.out.println("Game " + (Engine.getGameNum() + 1) + ": " + score);
      if (useGraphics)
        Renderer.updateHighScores(score);
      gameIsActive = false;
      timer.stop();

      if (GAME_MODE == GameMode.AI_TRAINING) {

        printer.print("Species " + (currentSpecies + 1) + " ");
        printer.print("Game " + (Engine.getGameNum() + 1) + " score: " + score + " ");
        printer.println("weights: " + Cerulean.getWeights());
        scoreHistory.add(getScore());
        if (currentSpecies < species.length) {
          if (playMultiple && Engine.getGameNum() == MAX_GAMES - 1) {
            speciesAvgScore[currentSpecies] = Game.getAvgScore();
            currentSpecies++;
            if (currentSpecies < species.length) {
              Cerulean.setWeights(species[currentSpecies]);
              scoreHistory.clear();
              resetGame(useGraphics);
              Engine.resetGameNum();
            }

          } else if (playMultiple && Engine.getGameNum() < MAX_GAMES - 1) {
            resetGame(useGraphics);
          }
        }
        if (currentSpecies == species.length) {
          printer.println("--------------------");
          for (double speciesScore : speciesAvgScore) {
            printer.println("Average: " + speciesScore);
          }
          printer.println("--------------------");

          generationNum++;
          if (generationNum < MAX_GENERATIONS) {
            printer.println("Generation " + generationNum + " over, Breeding...");
            speciesAvgScore[speciesAvgScore.length - 1] = Game.getAvgScore();
            species = Cerulean.breed(species, speciesAvgScore, MUTATION_FACTOR);
            currentSpecies = 0;
            resetGame(useGraphics);
            Engine.resetGameNum();
            Cerulean.setWeights(species[currentSpecies]);
          }
        }
      } else {
        resetGame(useGraphics);
      }
    }
    if (useGraphics)
      Renderer.draw(Engine.getBoard());

    if (!paused) {
      if (!NINTENDO_SCORING) {
        timeScore++;
      }
    }
    if (autoplay) {
      timePerTurn = MIN_MILLIS_PER_TURN;
    } else {
      timePerTurn = updateTime(timePerTurn);
    }

  }

  /**
   * Aims to change the number of real-world seconds between each game tick without acceleration
   * 
   * @param turnTime the current time it takes for one tick
   * @return a new number of milliseconds for the next tick
   */
  private double updateTime(double turnTime) {
    if (NINTENDO_SCORING) {
      // probably not the best algorithm
      return MAX_MILLIS_PER_TURN - (0.09 * getScore());
    } else {
      if (turnTime > MIN_MILLIS_PER_TURN) {
        return MAX_MILLIS_PER_TURN - (0.09 * getScore());
        // return MAX_MILLIS_PER_TURN - (9 * Math.sqrt(getScore()));
      } else {
        return MIN_MILLIS_PER_TURN;
      }
    }

  }

  /**
   * gets the average score over the current candidates history
   * 
   * @return the candidates average score
   */
  private static double getAvgScore() {
    double total = 0;
    for (Integer i : scoreHistory) {
      total += i;
    }
    return total / (scoreHistory.size());
  }

  /**
   * handles basic key input that needs to be constant across all run configurations
   * 
   * @author Hank
   *
   */
  private class BasicInputHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent key) {
      if (key.getCode() == KeyCode.ESCAPE) {
        Renderer.writeScores();
        Renderer.close();
        if (GAME_MODE == GameMode.AI_TRAINING)
          printer.close();
        System.exit(0);
      } else if (key.getCode() == KeyCode.P) {
        paused = Engine.togglePause();
        if (paused) {
          Renderer.pause();
        } else {
          Renderer.unpause();
        }
      } else if (key.getCode() == KeyCode.R) {
        resetGame(true);

      }
      if (!paused) {
        Renderer.draw(Engine.getBoard());
      }
    }

  }


  /**
   * deals with advanced keyboard input from user related to control of blocks
   * 
   * @author Hank
   *
   */
  private class UserInputHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent key) {
      if (!paused && Engine.getBoard().rowsAreNotFalling() && !Engine.getBoard().full) {
        switch (key.getCode()) {
          case RIGHT:
            Engine.getBoard().pressed(Move.RIGHT);
            break;
          case LEFT:
            Engine.getBoard().pressed(Move.LEFT);
            break;
          case X:
            Engine.getBoard().pressed(Move.ROT_RIGHT);
            break;
          case Z:
            Engine.getBoard().pressed(Move.ROT_LEFT);
            break;
          case DOWN:
            Engine.getBoard().pressed(Move.DOWN);
            break;
          case SPACE:
            Engine.getBoard().pressed(Move.DROP);
            if (dropDownTerminatesBlock)
              Engine.update();
            break;
          case UP:
            if (doDebug) {
              Engine.getBoard().pressed(Move.UP);
            }
            break;
          default:
            // key pressed wasn't an active key, do nothing
            break;
        }
        if (!paused) {
          Renderer.draw(Engine.getBoard());
        }
      }
    }

  }


  /**
   * resets the game when called, typically after a loss
   */
  public static void resetGame(boolean useGraphics) {
    if (GAME_MODE == GameMode.AI_TRAINING) {
      printer.flush();
      speciesAvgScore[currentSpecies] = getAvgScore();
    }
    gameIsActive = true;
    if (useGraphics) // TODO: remove, switch to Logger class
      Renderer.writeScores();
    Engine.reset();
    Engine.getBoard().clearBoard();
    Game.timeScore = 0;
    timePerTurn = MAX_MILLIS_PER_TURN;
    timer.start();
    Engine.addBlock();

  }


  /**
   *
   * @return the score as a sum of the time score and the lines cleared score
   */
  private int getScore() {
    return (timeScore + Engine.getBoard().getBoardScore());
  }

  /**
   * 
   * @return if the game is active (not paused)
   */
  public boolean isActive() {
    return gameIsActive;
  }

  /**
   * sets up the required settings for the timer used to control the pace of the game
   * 
   * @return a fully configured AnimationTimer instance
   */
  private AnimationTimer configureTimer(boolean useGraphics) {
    return new AnimationTimer() {
      private long pastTime;

      @Override
      public void start() {
        pastTime = System.currentTimeMillis();
        super.start();
      }

      @Override
      public void handle(long time) {
        long now = System.currentTimeMillis();
        if (now - pastTime >= timePerTurn) {
          update(useGraphics);
          pastTime = now;
        }

      }
    };
  }

  public static void togglePause() {
    paused = !paused;
  }
}
