package clients;

import java.util.Arrays;

import mainGame.Autoplayable;
import mainGame.ParellelizedRunner;
import mainGame.ScoreMode;

public class WeightTrainingClient implements Autoplayable {

  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000000;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
  // should be 1, if a game finishes the thread should be allowed to take a new thread from the pool
  public static final int MAX_CONSEC_GAMES_PER_THREAD = 1;
  public static final int MAX_GAMES_PER_GEN = 10;
  public static final int MAX_GENERATIONS = 1;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;

  public static double[][] species = new double[][] {{-200, -50, 100, 1.68}, {-70, -70, 500, 5}};//,
//      {-100, -50, 100, 2}, {-200, -70, 300, 7}, {-40, -100, 400, 1}, {-400, -300, 100, 1},
//      {-200, -100, 100, 3}, {-150, -70, 400, 0}, {-70, -150, 500, -5}, {-200, -35.4, 100, 8},
//      {-294.75, -34.44, 101.72, 5}};

  public static void main(String[] args) {
    ParellelizedRunner runner = new ParellelizedRunner(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN,
        MAX_CONSEC_GAMES_PER_THREAD, MAX_GAMES_PER_GEN, USE_GRAPHICS, DO_DEBUG, SCORE_MODE);
    for (int genNumber = 0; genNumber < MAX_GENERATIONS; genNumber++) {
      for (double[] currentSpecies : species) {
        System.out.println("Average Score of " + Arrays.toString(currentSpecies) + "\t over " + MAX_GAMES_PER_GEN + " Games using " + SCORE_MODE
            + " scoring is " + runner.run(currentSpecies));
      }
    }
    
    System.exit(0);
  }

}
