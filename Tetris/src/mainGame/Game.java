package mainGame;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import blocks.Tile;
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
import javafx.stage.Stage;

public class Game extends Application {


  // change these
  private GameMode gameMode = GameMode.AUTOPLAY;
  public static final int MAX_GAMES = 30;
  public static final int MAX_GENERATIONS = 15;
  public static final double MUTATION_FACTOR = 0.5; // value between 0 and 1 where 0 is no mutations
                                                    // ever and 1 is a mutation every time


  // don't change these
  private boolean doDebug;
  // private static boolean doLog;
  private boolean autoplay;
  private boolean randomizeBlocks;
  private boolean playMultiple; // play multiple games in a row

  // public static final double[] WEIGHTS = new double[]{-294.75, -34.44, 101.72, 5};
  public static final double[] WEIGHTS = new double[] {-200, -50, 100, 1.68};


  private int maxTimePerTurn = 1000;
  private int minTimePerTurn = 100;

  public static final int VERTICAL_TILES = 20;
  public static final int HORIZONTAL_TILES = 10;
  // private static final int SQUARE_SIZE = 29;

  // if nintendo scoring = false, hank/liam scoring is used
  public static final boolean NINTENDO_SCORING = false;


  private static AnimationTimer timer;
  private static PrintStream printer;
  private int timeScore = 0;
  private double timePerTurn = maxTimePerTurn;

  private static Tile[][] gameBoard = new Tile[VERTICAL_TILES][HORIZONTAL_TILES];
  private Engine engine;
  private Renderer renderer;
  private Cerulean cerulean;
  private boolean useGraphics;

  private static ArrayList<Integer> scoreHistory = new ArrayList<Integer>();

  // can be changed if not desired
  private boolean dropDownTerminatesBlock = true;

  // seeded possible solutions
  public static double[][] species =
      new double[][] {WEIGHTS, {-70, -70, 500, 5}, {-100, -50, 100, 2}, {-200, -70, 300, 7},
          {-40, -100, 400, 1}, {-400, -300, 100, 1}, {-200, -100, 100, 3}, {-150, -70, 400, 0},
          {-70, -150, 500, -5}, {-200, -35.4, 100, 8}, {-294.75, -34.44, 101.72, 5}};
  // public static double[][] species = new double[][]{{-70,-70,500, 5}, {-100, -50, 100, 8}, {-10,
  // -10, 100, 5}};

  private static int currentSpecies = 0;
  private static int generationNum = 0;

  private static double[] speciesAvgScore = new double[species.length];


  private static boolean paused = false;

  private static boolean gameIsActive = true;
  
  
  public Game(){
    this(VERTICAL_TILES, HORIZONTAL_TILES, 100, GameMode.DISTRO, true, false, true, false);
  }

  public Game(int boardHeight, int boardWidth, int minTimePerTurn, GameMode mode,
      boolean useGraphics, boolean doDebug, boolean randomizeBlocks, boolean playMultiple) {
    this.minTimePerTurn = minTimePerTurn;
    this.gameMode = mode;
    this.useGraphics = useGraphics;
    this.doDebug = doDebug;
    this.randomizeBlocks = randomizeBlocks;
    this.playMultiple = playMultiple;
  }

  public Game(int gameHeight, int gameWidth, int minTimePerTurn, GameMode mode, double[] weights,
      boolean useGraphics, boolean doDebug, boolean randomizeBlocks, boolean playMultiple) {
    this.minTimePerTurn = minTimePerTurn;
    this.gameMode = mode;
    this.useGraphics = useGraphics;
    this.doDebug = doDebug;
    this.autoplay = true;   //inferred because weights were passed
    this.dropDownTerminatesBlock = false;
    cerulean = new Cerulean();
    cerulean.setWeights(weights);
    this.randomizeBlocks = randomizeBlocks;
    this.playMultiple = playMultiple;
  }


  @Override
  public void start(Stage stage) throws Exception {
    setup(useGraphics);
    if (useGraphics) {
      Scene boardScene = renderer.makeGame();
      if (!autoplay) {
        stage.addEventFilter(KeyEvent.KEY_PRESSED, new UserInputHandler());
      }

      stage.addEventFilter(KeyEvent.KEY_PRESSED, new BasicInputHandler());

      stage.setScene(boardScene);

    }
    timer = configureTimer(useGraphics);
    timer.start();


    engine.addBlock(); // needs to be towards the end of method so initial event fires correctly
    if(useGraphics){
      stage.show();
    }
  }

  private void setup(boolean useGraphics) {
    if (useGraphics) {
      renderer = new Renderer(doDebug);
    }
    if (gameMode == GameMode.AI_TRAINING) {
      File aiLogFile = new File("src/gameLogs/AI output" + System.currentTimeMillis());
      try {
        aiLogFile.createNewFile();
        printer = new PrintStream(aiLogFile);
      } catch (IOException e) {
        System.err.println("Error on file creation");
      }
    }
    engine = new Engine(gameBoard, autoplay, randomizeBlocks);
  }

  public void run(Stage arg0) throws Exception {
    start(arg0);
  }

  public void run() {
    setup(useGraphics);
    engine.addBlock(); // needs to be towards the end of method so initial event fires correctly
    timer = configureTimer(useGraphics);
    timer.start();

  }

  private void update(boolean useGraphics) {
    if (useGraphics) {
      renderer.updateScore(getScore(), engine.getNumFullRows());
    }
    engine.update();
    if (engine.hasFullBoard()) {
      int score = getScore();
      System.out.println("Game " + (engine.getGameNum() + 1) + ": " + score);
      if (useGraphics)
        renderer.updateHighScores(score);
      gameIsActive = false;
      timer.stop();

      if (gameMode == GameMode.AI_TRAINING) {

        printer.print("Species " + (currentSpecies + 1) + " ");
        printer.print("Game " + (engine.getGameNum() + 1) + " score: " + score + " ");
        printer.println("weights: " + cerulean.getWeights());
        scoreHistory.add(getScore());
        if (currentSpecies < species.length) {
          if (playMultiple && engine.getGameNum() == MAX_GAMES - 1) {
            speciesAvgScore[currentSpecies] = this.getAvgScore();
            currentSpecies++;
            if (currentSpecies < species.length) {
              cerulean.setWeights(species[currentSpecies]);
              scoreHistory.clear();
              resetGame(useGraphics);
              engine.resetGameNum();
            }

          } else if (playMultiple && engine.getGameNum() < MAX_GAMES - 1) {
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
            speciesAvgScore[speciesAvgScore.length - 1] = this.getAvgScore();
            species = cerulean.breed(species, speciesAvgScore, MUTATION_FACTOR);
            currentSpecies = 0;
            resetGame(useGraphics);
            engine.resetGameNum();
            cerulean.setWeights(species[currentSpecies]);
          }
        }
      } else {
        resetGame(useGraphics);
      }
    }
    if (useGraphics) {
      renderer.drawBoards(engine.getGameBoard(), engine.getNextPieceBoard());
    }

    if (!paused) {
      if (!NINTENDO_SCORING) {
        timeScore++;
      }
    }
    if (autoplay) {
      timePerTurn = minTimePerTurn;
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
      return maxTimePerTurn - (0.09 * getScore());
    } else {
      if (turnTime > minTimePerTurn) {
        return maxTimePerTurn - (0.09 * getScore());
        // return MAX_MILLIS_PER_TURN - (9 * Math.sqrt(getScore()));
      } else {
        return minTimePerTurn;
      }
    }

  }

  /**
   * gets the average score over the current candidates history
   * 
   * @return the candidates average score
   */
  private double getAvgScore() {
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
        renderer.writeScores();
        renderer.close();
        if (gameMode == GameMode.AI_TRAINING)
          printer.close();
        System.exit(0);
      } else if (key.getCode() == KeyCode.P) {
        paused = engine.togglePause();
        if (paused) {
          renderer.pause();
        } else {
          renderer.unpause();
        }
      } else if (key.getCode() == KeyCode.R) {
        resetGame(true);

      }
      if (!paused) {
        renderer.drawBoards(engine.getGameBoard(), engine.getNextPieceBoard());
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
      if (!paused && engine.rowsAreNotFalling() && !engine.hasFullBoard()) {
        switch (key.getCode()) {
          case RIGHT:
            engine.executeMove(Move.RIGHT);
            break;
          case LEFT:
            engine.executeMove(Move.LEFT);
            break;
          case X:
            engine.executeMove(Move.ROT_RIGHT);
            break;
          case Z:
            engine.executeMove(Move.ROT_LEFT);
            break;
          case DOWN:
            engine.executeMove(Move.DOWN);
            break;
          case SPACE:
            engine.executeMove(Move.DROP);
            if (dropDownTerminatesBlock)
              engine.update();
            break;
          case UP:
            if (doDebug) {
              engine.executeMove(Move.UP);
            }
            break;
          default:
            // key pressed wasn't an active key, do nothing
            break;
        }
        if (!paused) {
          renderer.drawBoards(engine.getGameBoard(), engine.getNextPieceBoard());
        }
      }
    }

  }


  /**
   * resets the game when called, typically after a loss
   */
  public void resetGame(boolean useGraphics) {
    if (gameMode == GameMode.AI_TRAINING) {
      printer.flush();
      speciesAvgScore[currentSpecies] = getAvgScore();
    }
    gameIsActive = true;
    if (useGraphics) // TODO: remove, switch to Logger class
      renderer.writeScores();
    engine.reset();
    engine.clearBoard(engine.getGameBoard());
    this.timeScore = 0;
    timePerTurn = maxTimePerTurn;
    timer.start();
    engine.addBlock();

  }


  /**
   *
   * @return the score as a sum of the time score and the lines cleared score
   */
  public int getScore() {
    return (timeScore + engine.getScore());
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

  public void togglePause() {
    paused = !paused;
  }
}
