package mainGame;

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


  // don't change these
  private static boolean doDebug;
  private static boolean doLog;
  private static boolean autoplay;
  private static boolean randomizeBlocks;



  public static final int MAX_MILLIS_PER_TURN = 1000;
  public static final int MIN_MILLIS_PER_TURN = 100;

  // if nintendo scoring = false, hank/liam scoring is used
  public static final boolean NINTENDO_SCORING = false;


  private AnimationTimer timer;
  private int gameCounter = 1;
  private int timeScore = 0;
  private double timePerTurn = MAX_MILLIS_PER_TURN;

  // can be changed if not desired
  private boolean dropDownTerminatesBlock = true;



  boolean paused = false;

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
        break;
      case DEBUG:
        doDebug = true;
        doLog = true;
        autoplay = false;
        randomizeBlocks = true;
        break;
      case LOGGER:
        doDebug = false;
        doLog = true;
        autoplay = false;
        randomizeBlocks = true;
        break;
      case AUTOPLAY:
        doDebug = false;
        doLog = false;
        autoplay = true;
        randomizeBlocks = true;
        break;
      case AI_TRAINING:
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
        break;
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    Renderer.setMode(doDebug, doLog, autoplay);
    Scene boardScene = Renderer.makeGame();
    Renderer.draw(Engine.getBoard());
    Engine.setRandomizeBlocks(randomizeBlocks);

    // if (autoplay) { // do we care about these events in user mode?
    // stage.addEventFilter(BlockAddedEvent.BLOCK_ADDED, new BlockAddedHandler());
    // }
    // else{ //uncomment when AI works
    stage.addEventFilter(KeyEvent.KEY_PRESSED, new UserInputHandler());
    // }
    // added regardless of run configuration
    stage.addEventFilter(KeyEvent.KEY_PRESSED, new BasicInputHandler());

    stage.setScene(boardScene);

    timer = configureTimer();
    timer.start();


    stage.show();
    // if (autoplay) {
    // try {
    // Robot r = new Robot();
    // r.keyPress(java.awt.event.KeyEvent.VK_META);
    // r.keyPress(java.awt.event.KeyEvent.VK_TAB);
    // r.keyRelease(java.awt.event.KeyEvent.VK_TAB);
    // r.keyRelease(java.awt.event.KeyEvent.VK_META);
    //
    // } catch (AWTException e) {
    // e.printStackTrace();
    // }
    // }
    Engine.addBlock(); // needs to be towards the end of method so initial event fires correctly
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
  private void resetGame() {
    gameIsActive = true;
    int score = getScore();
    System.out.println("Game " + this.gameCounter + " score: " + score + "\n");
    System.out.println("blocks: " + Engine.getBlockCount());
    Engine.reset();
    Renderer.updateHighScores(score); // updates ArrayList by reference, can't do it well
                                      // because of 'final or effectively final' issue
    Renderer.writeScores();
    Engine.getBoard().clearBoard();
    this.timeScore = 0;
    gameCounter++;
    timePerTurn = MAX_MILLIS_PER_TURN;
    timer.start();
    Engine.addBlock();

//    try {
//      Renderer.initializeScorePrinter();
//    } catch (Exception e1) {
//      throw new RuntimeException("Error on file re-generation");
//    }
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
  // TODO: remove time acceleration
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
            timer.stop();
            gameIsActive = false;
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
}
