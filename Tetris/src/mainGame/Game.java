package mainGame;

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
import javafx.stage.Stage;

public class Game extends Application {


  // change this
  public static final GameMode GAME_MODE = GameMode.AI_TRAINING;
  public static final int MAX_GAMES = 20;


  // don't change these
  private static boolean doDebug;
  private static boolean doLog;
  private static boolean autoplay;
  private static boolean randomizeBlocks;
  private static boolean playMultiple; // play multiple games in a row



  public static final int MAX_MILLIS_PER_TURN = 1000;
  public static final int MIN_MILLIS_PER_TURN = 100;


  // if nintendo scoring = false, hank/liam scoring is used
  public static final boolean NINTENDO_SCORING = false;


  private static AnimationTimer timer;
  private static int timeScore = 0;
  private static double timePerTurn = MAX_MILLIS_PER_TURN;

  private static ArrayList<Integer> scoreHistory = new ArrayList<Integer>();

  // can be changed if not desired
  private boolean dropDownTerminatesBlock = false;

  public static double[][] SPECIES = new double[][] {{-70, -70, 500}, {-100, -50, 100}, {-200, -70, 300}, {-100, -50, 70}};

  private static int currentSpecies = 0;

  private static double[] speciesAvgScore = new double[SPECIES.length];


  private static boolean paused = false;

  private static boolean gameIsActive = true;



  public static void main(String[] args) {
    configureSettings();

    launch(args);
  }

  /**
   * configures the run settings of the game based on the user selected run configuration
   */
  private static void configureSettings() {
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
        doDebug = true;
        doLog = true;
        autoplay = false;
        randomizeBlocks = true;
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
        doDebug = false;
        doLog = false;
        autoplay = true;
        randomizeBlocks = true;
        playMultiple = false;
        break;
      case AI_TRAINING:
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

    timer = configureTimer();
    timer.start();


    stage.show();
    Engine.addBlock(); // needs to be towards the end of method so initial event fires correctly
  }

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
        for (double score : speciesAvgScore) {
          System.out.println("Average: " + score);
        }
        Renderer.writeScores();
        Renderer.close();
        System.exit(0);
      } else if (key.getCode() == KeyCode.P) {
        paused = Engine.togglePause();
        if (paused) {
          Renderer.pause();
        } else {
          Renderer.unpause();
        }
      } else if (key.getCode() == KeyCode.R) {
        resetGame();

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
  public static void resetGame() {
    speciesAvgScore[currentSpecies] = getAvgScore();
    gameIsActive = true;
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
  private AnimationTimer configureTimer() {
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
        if (!paused && now - pastTime >= timePerTurn) {

          Renderer.updateScore(timeScore + Engine.getBoard().getBoardScore(),
              Engine.getBoard().getNumOfFullRows());

          Engine.update();
          if (Engine.getBoard().isFull()) {
            int score = getScore();
            System.out.println("Game " + (Engine.getGameNum() + 1) + " score: " + score);
            System.out.println(Cerulean.getWeights());
            System.out.println("blocks: " + Engine.getBlockCount());
            Renderer.updateHighScores(score);
            timer.stop();
            gameIsActive = false;
            scoreHistory.add(getScore());
            if (currentSpecies < SPECIES.length) {
              if (playMultiple && Engine.getGameNum() == MAX_GAMES - 1) {
                speciesAvgScore[currentSpecies] = Game.this.getAvgScore();
                currentSpecies++;
                if (currentSpecies < SPECIES.length) {
                  Cerulean.updateWeights(SPECIES[currentSpecies]);
                  scoreHistory.clear();
                  resetGame();
                  Engine.resetGameNum();
                }
                
              } else if (playMultiple && Engine.getGameNum() < MAX_GAMES - 1) {
                resetGame();
              }
            }
            if(currentSpecies == SPECIES.length){
              System.out.println("Breeding...");
              speciesAvgScore[speciesAvgScore.length - 1] = Game.this.getAvgScore();
              // breed species
            }
          }
          Renderer.draw(Engine.getBoard());
          pastTime = now;
        }
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
    };
  }

  public static void togglePause() {
    paused = !paused;
  }
}
