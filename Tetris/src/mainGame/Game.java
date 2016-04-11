package mainGame;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import engine.Engine;
import engine.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Game extends Application {



  /**
   * change these flags to change the run mode. any generalized distribution version should likely
   * have all off
   */

  // public static final boolean DEBUG_MODE = true;
  public static final boolean DEBUG_MODE = false;

  public static final boolean LOG_MODE = true;
  // public static final boolean LOG_MODE = false;


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

  private boolean gameIsActive = true;

  @Override
  public void start(Stage stage) throws Exception {
    Scene boardScene = Renderer.makeGame();
    Renderer.draw(Engine.getBoard());
    Engine.addBlock();
    
    
    stage.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        Renderer.writeScores();
        Renderer.close();
        System.exit(0);
      } else if (e.getCode() == KeyCode.P) {
        paused = Engine.togglePause();
        if (paused) {
          Renderer.pause();
        } else {
          Renderer.unpause();
        }
      } else if (e.getCode() == KeyCode.R) {
        resetGame();

      } else if (!paused && Engine.getBoard().rowsAreNotFalling() && !Engine.getBoard().full) {
        switch(e.getCode()){
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
            Engine.getBoard().pressed(Move.FULL_DOWN);
            if (dropDownTerminatesBlock)
              Engine.update();
            break;
          case UP:
            if(DEBUG_MODE){
              Engine.getBoard().pressed(Move.UP);
            }
            break;
          default:
              //key pressed wasn't an active key, do nothing
            break;
        }
        if (!paused) {
          Renderer.draw(Engine.getBoard());
        }
      }
    });
    
    stage.setScene(boardScene);


    timer = configureTimer();
    timer.start();


    stage.show();

  }

  

  private void resetGame() {
    gameIsActive = true;
    int score = getScore();
    System.out.println("Game " + this.gameCounter + " score: " + score + "\n");
    Renderer.updateHighScores(score); // updates ArrayList by reference, can't do it well
                                         // because of 'final or effectively final' issue
    Renderer.writeScores();
    Engine.getBoard().clearBoard();
    Engine.addBlock();
    this.timeScore = 0;
    gameCounter++;
    timePerTurn = MAX_MILLIS_PER_TURN;
    timer.start();
    try {
      Renderer.initializeScorePrinter();
    } catch (Exception e1) {
      throw new RuntimeException("Error on file re-generation");
    }
  }



  private int getScore() {
    return (timeScore + Engine.getBoard().getBoardScore());
  }
  
  public boolean isActive(){
    return gameIsActive;
  }
  
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

          Renderer.updateScore(timeScore + Engine.getBoard().getBoardScore(), Engine.getBoard().getNumOfFullRows());
          
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
        timePerTurn = updateTime(timePerTurn);


      }

      private double updateTime(double turnTime) {
        if (NINTENDO_SCORING) {
          // probably not the best algorithm
          return MAX_MILLIS_PER_TURN - (0.09 * getScore());
        } else {
          if (turnTime > MIN_MILLIS_PER_TURN) {
            return MAX_MILLIS_PER_TURN - (0.09 * getScore());
            // return MAX_MILLIS_PER_TURN - (9 * Math.sqrt(getScore()));
          }
        }
        return MIN_MILLIS_PER_TURN;
      }
    };
  }
}
