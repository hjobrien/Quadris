package mainGame;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import blocks.Tile;
import blocks.blockGeneration.BlockGenerator;
import blocks.blockGeneration.RandomizeBlocks;
import engine.Engine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import renderer.Renderer;
import util.Util;

public class Game extends Application {


  // change these
  private GameMode gameMode = GameMode.AUTOPLAY;
  public static final int MAX_GAMES = 30;
  public static final int MAX_GENERATIONS = 15;
  public static final double MUTATION_FACTOR = 0.5; // value between 0 and 1 where 0 is no mutations
                                                    // ever and 1 is a mutation every time

  // time it takes for the timeUpdate to activate (should stop exponential time growth)
  private static int timeIncrease;

  // don't change these
  private boolean doDebug;
  // private static boolean doLog;
  private boolean autoplay;
  // private boolean randomizeBlocks;
  // private boolean playMultiple; // play multiple games in a row

  // public static final double[] WEIGHTS = new double[]{-294.75, -34.44, 101.72, 5};
//  public static final double[] WEIGHTS = new double[] {-200, -50, 100, 1.68};


  private int maxTimePerTurn = 1000000000; // nanoseconds
  private int minTimePerTurn = 200000000; // nanoseconds

  public static final int DEFAULT_VERTICAL_TILES = 20;
  public static final int DEFAULT_HORIZONTAL_TILES = 10;

  // can be Hank_Liam, Nintendo, or Simple
  public ScoreMode scoring;

  private static PrintStream printer;
  private int timeScore = 0;
  private long timePerTurn = maxTimePerTurn;

  private AnimationTimer timer;
  private Tile[][] gameBoard/* = new Tile[DEFAULT_VERTICAL_TILES][DEFAULT_HORIZONTAL_TILES] */;
  private Engine engine;
  private Renderer renderer;
//  private Cerulean cerulean;
  private boolean useGraphics;

  private int maxGamesPerGeneration = 0;

//  private static ArrayList<Integer> scoreHistory = new ArrayList<Integer>();

  // can be changed if not desired
  private boolean dropDownTerminatesBlock = true;

  // seeded possible solutions
//  public static double[][] species =
//      new double[][] {WEIGHTS, {-70, -70, 500, 5}, {-100, -50, 100, 2}, {-200, -70, 300, 7},
//          {-40, -100, 400, 1}, {-400, -300, 100, 1}, {-200, -100, 100, 3}, {-150, -70, 400, 0},
//          {-70, -150, 500, -5}, {-200, -35.4, 100, 8}, {-294.75, -34.44, 101.72, 5}};
  // public static double[][] species = new double[][]{{-70,-70,500, 5}, {-100, -50, 100, 8}, {-10,
  // -10, 100, 5}};

//  private static int currentSpecies = 0;
//  private static int generationNum = 0;

//  private static double[] speciesAvgScore = new double[species.length];


  private boolean paused = false;

  private volatile boolean gameIsActive = true;

  /**
   * convenience constructor that initializes the game to some suggested settings
   */
  public Game() {
    this(DEFAULT_VERTICAL_TILES, DEFAULT_HORIZONTAL_TILES, (int) 1e8, 5, GameMode.DISTRO, true,
        false, new RandomizeBlocks(), ScoreMode.SIMPLE);
  }

  /**
   * comprehensive constructor that sets all parts of the games configuration
   * 
   * @param boardHeight the height of the board
   * @param boardWidth the width of the board
   * @param minTimePerTurn the minimum time between game updates, in milliseconds
   * @param mode the gameMode to be used
   * @param useGraphics whether the game should expose its working through a GUI
   * @param doDebug whether the game should display debug information
   * @param randomizeBlocks whether blocks should be randomly generated or read from a file
   * @param playMultiple whether multiple games should be played consecutively
   */
  public Game(int boardHeight, int boardWidth, int minTimePerTurn, int maxGamesPerGen,
      GameMode mode, boolean useGraphics, boolean doDebug, BlockGenerator generator,
      ScoreMode scoring) {
    this.minTimePerTurn = minTimePerTurn;
    this.maxGamesPerGeneration = maxGamesPerGen;
    this.gameMode = mode;
    this.useGraphics = useGraphics;
    this.doDebug = doDebug;
    // this.playMultiple = playMultiple;
    this.autoplay = false;
    this.gameBoard = new Tile[boardHeight + 3][boardWidth]; // so that the board can accommodate
                                                            // blocks at the top
    this.engine = new Engine(gameBoard, autoplay, generator, scoring, null);
    this.scoring = scoring;
  }

  /**
   * comprehensive constructor that sets all parts of the games configuration
   * 
   * @param boardHeight the height of the board
   * @param boardWidth the width of the board
   * @param minTimePerTurn the minimum time between game updates, in milliseconds
   * @param mode the gameMode to be used
   * @param useGraphics whether the game should expose its working through a GUI
   * @param doDebug whether the game should display debug information
   * @param randomizeBlocks whether blocks should be randomly generated or read from a file
   * @param playMultiple whether multiple games should be played consecutively
   */
  public Game(Tile[][] board, int minTimePerTurn, int maxGamesPerGen, GameMode mode,
      boolean useGraphics, boolean doDebug, BlockGenerator generator, ScoreMode scoring) {
    this.minTimePerTurn = minTimePerTurn;
    this.maxGamesPerGeneration = maxGamesPerGen;
    this.gameMode = mode;
    this.useGraphics = useGraphics;
    this.doDebug = doDebug;
    // this.playMultiple = playMultiple;
    this.autoplay = false;
    this.gameBoard = board;
    this.scoring = scoring;
    this.engine = new Engine(gameBoard, autoplay, generator, scoring, null);
  }

  /**
   * comprehensive constructor to be used when an autoplaying game is requested, above the existing
   * constructor the caller additionally specifies weights to be used by the AI other values are
   * inferred based on autoplay
   * 
   * @param boardHeight the height of the board
   * @param boardWidth the width of the board
   * @param minTimePerTurn the minimum time between game updates, in milliseconds
   * @param mode the gameMode to be used
   * @param useGraphics whether the game should expose its working through a GUI
   * @param doDebug whether the game should display debug information
   * @param randomizeBlocks whether blocks should be randomly generated or read from a file
   * @param playMultiple whether multiple games should be played consecutively
   * @param weights the weights to be passed to the AI for its evaluation function
   */
  public Game(int boardHeight, int boardWidth, int minTimePerTurn, int maxGamesPerGen,
      GameMode mode, boolean useGraphics, boolean doDebug, BlockGenerator generator,
      double[] weights, ScoreMode scoring) {
    this(boardHeight, boardWidth, minTimePerTurn, maxGamesPerGen, mode, useGraphics, doDebug,
        generator, scoring);
    this.autoplay = true; // inferred because weights were passed
    this.dropDownTerminatesBlock = false;
//    this.cerulean = new Cerulean();
//    cerulean.setWeights(weights);
    this.engine = new Engine(gameBoard, autoplay, generator, scoring, weights);
  }


  @Override
  /**
   * the key method for interacting with the JavaFX UI
   */
  public void start(Stage stage) throws Exception {
    run();
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
    stage.show();
  }

  /**
   * sets up key parts of the object including files for logging
   * 
   * @param useGraphics whether the object is being run in graphical output mode
   */
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
  }

  /**
   * simple method to start the game from external clients while running in graphical mode this
   * method signature reflects that of the JavaFX start method
   * 
   * @param arg0 the base JavaFX stage
   * @throws Exception for a myriad of GUI things
   */
  public void run(Stage arg0) throws Exception {
    start(arg0);
  }

  /**
   * simple method to run the game when not in graphics mode
   */
  public void run() {
    setup(useGraphics);
    engine.addBlock();
    timeIncrease = (int) System.currentTimeMillis();

    // engine updates on separate thread every timePerTurn nanoseconds
    Util.exec.submit(() -> {
      while (engine.getGameNum() < maxGamesPerGeneration) { // controls number of max games,
                                                            // change from infinite games
        while (!engine.hasFullBoard()) {
          Util.sleep(timePerTurn);
          engine.update();
          
          //not needed?
          if (engine.hasFullBoard()) {
//            if (useGraphics) {
//              timer.stop();
//            }
          }
          if (!paused) {
            if ((int) System.currentTimeMillis() - timeIncrease > 500) {
              timeIncrease = (int) System.currentTimeMillis();
              timePerTurn = updateTime(timePerTurn);
            }
          }
        }
        
        gameIsActive = false;
        if(!useGraphics){
          resetGame(useGraphics);
        }
        
      }
      System.exit(0);
    });

  }

  public static int runGame(Game game, int gameNum) {
    // System.out.println(game.engine.getGameNum() + " " + Game.generationNum);
     game.setup(game.useGraphics);
    game.getEngine().addBlock();
    
    while (!game.getEngine().hasFullBoard()) {
      game.getEngine().update();
    }
    return game.getScore();
  }

  private Engine getEngine() {
    return this.engine;
  }

  /**
   * Aims to change the number of real-world seconds between each game tick without acceleration
   * 
   * @param turnTime the current time it takes for one tick
   * @return a new number of milliseconds for the next tick
   */
  private long updateTime(long turnTime) {

    // could also be a function of score (getScore())

    if (autoplay) {
      return minTimePerTurn;
    } else if (turnTime - 2500000 > minTimePerTurn) {
      return turnTime - 2500000;
    } else {
      return minTimePerTurn;
    }

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
        resetGame(useGraphics);

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
            engine.executeMove(Move.UP);
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
    
	  System.out.println("Game " + (engine.getGameNum() + 1) + ": " + getScore());

	engine.reset();
	
    this.gameIsActive = true;
    this.timeScore = 0;
    
    if (useGraphics){ // TODO: remove, switch to Logger class
      renderer.writeScores();
    }
    
    timePerTurn = maxTimePerTurn;
    
    engine.addBlock();
  }


  /**
   *
   * @return the score based on the scoring system
   */
  public int getScore() {
    if (scoring == ScoreMode.HANK_LIAM) {
      return (timeScore + engine.getScore());
    } else if (scoring == ScoreMode.NINTENDO) {
      return engine.getScore();
    } else if (scoring == ScoreMode.SIMPLE) {
      return engine.getScore();
    }
    throw new RuntimeException("Invalid Score Mode");
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

      private int counter = 0;

      @Override
      public void start() {
        super.start();
      }

      // UI updates on UI thread every frame refresh
      @Override
      public void handle(long time) {
        renderer.drawBoards(engine.getGameBoard(), engine.getNextPieceBoard());
        renderer.updateScore(getScore(), engine.getNumFullRows());
        
        if (engine.hasQuadris()){
        	//resets in case two quadris' are achieved in quick succession
        	counter = 1;
        	engine.setQuadris(false);
        }
        
        if (counter > 0){
        	counter++;
        	renderer.displayQuadrisGraphic();
        	
        	if (counter % 7 <= 3){
        	  renderer.removeQuadrisGraphic();
        	} else {
        	  renderer.displayQuadrisGraphic();
        	}
        	
        	if (counter == 50){
        	  renderer.removeQuadrisGraphic();
        	  counter = 0;
        	}
        }
        
        if (!gameIsActive) {
          renderer.displayEndGameGraphic();
          renderer.updateHighScores(getScore());
//          timer.stop();
        } else {
          renderer.removeEndGameGraphic();
        }

      }
    };
  }

  /**
   * toggles the pause state of the object
   */
  public void togglePause() {
    paused = !paused;
  }

  public ScoreMode getScoringMode() {
    return this.scoring;
  }
}
