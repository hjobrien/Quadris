package clients;

import java.io.PrintStream;
import java.util.Arrays;

import cerulean.Cerulean;
import clients.interfaces.Autoplayable;
import mainGame.ScoreMode;

public class WeightTrainingClient implements Autoplayable {

  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000000;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
  // should be 1, if a game finishes the thread should be allowed to take a new thread from the pool
  public static final int MAX_CONSEC_GAMES_PER_THREAD = 1;
  public static final int MAX_GAMES_PER_GEN = 50;
  public static final int MAX_GENERATIONS = 30;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;
  public static final double MUTATION_FACTOR = 0.5;


  private static double[][] species =
      new double[][] {{-200, -50, 1.68, 100, 100 * 3, 100 * 7, 100 * 20},
        {-70, -70, 5, 500, 500 * 3, 500 * 7, 500 * 20},
        {-100, -50, 2, 100, 100 * 3, 100 * 7, 100 * 20},
        {-200, -70, 7, 300, 300 * 3, 300 * 7, 300 * 20},
        {-40, -100, 1, 400, 400 * 3, 400 * 7, 400 * 20},
        {-400, -300, 1, 100, 100 * 3, 100 * 7, 100 * 20},
        {-200, -100, 3, 100, 100 * 3, 100 * 7, 100 * 20},
        {-150, -70, 0, 400, 400 * 3, 400 * 7, 400 * 20},
        {-70, -150, -5, 500, 500 * 3, 500 * 7, 500 * 20},
        {-200, -35.4, 8, 100, 100 * 3, 100 * 7, 100 * 20},
        {-294.75, -34.44, 5, 101.72, 101.72 * 3, 101.72 * 7, 101.72 * 20},
        {-72.15, -37.37, 5.0, 818.84, 1683.85, 2963.27, 11916.65}
      };

  public static void main(String[] args) {
//    File file = new File()
//    PrintStream gamePrinter = new PrintStream(file);
    ParellelizedCore runner = new ParellelizedCore(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN,
        MAX_CONSEC_GAMES_PER_THREAD, MAX_GAMES_PER_GEN, USE_GRAPHICS, DO_DEBUG, SCORE_MODE);

    double[] speciesAvgScore = new double[species.length];
    for (int genNumber = 0; genNumber < MAX_GENERATIONS; genNumber++) {
      for (int i = 0; i < species.length; i++) {
        double[] currentSpecies = species[i];
        double avgScore = runner.run(currentSpecies);
        System.out.println("Average Score of " + Arrays.toString(currentSpecies) + "\t over "
            + MAX_GAMES_PER_GEN + " Games using " + SCORE_MODE + " scoring is " + avgScore);
        speciesAvgScore[i] = avgScore;
      }
      System.out.println();
      species = Cerulean.breed(species, speciesAvgScore, MUTATION_FACTOR);
    }

    System.exit(0);
  }

}
