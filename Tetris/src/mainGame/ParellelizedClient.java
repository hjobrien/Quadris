package mainGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import blocks.BlockGenerator;
import blocks.StandardizeBlocks;
import engine.GameMode;
import util.Util;

public class ParellelizedClient implements Autoplayable {
  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000000;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
  public static final int MAX_GAMES_PER_THREAD = 1; // should be 1, if a game finishes the
                                                    // thread should be allowed to take a new game
                                                    // from the pool
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;

  private static int numGamesToPlay = 10;

  public static void main(String args[]) throws IOException {
    List<Integer> gameScores;
    List<ListenableFuture<Integer>> results = new ArrayList<>();
    for (int i = 0; i < numGamesToPlay; i++) {
      final int j = i;
      BlockGenerator generator = new StandardizeBlocks(j);
      Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, MAX_GAMES_PER_THREAD,
          GameMode.AUTOPLAY, USE_GRAPHICS, DO_DEBUG, generator, WEIGHTS, SCORE_MODE);
      results.add(Util.compute.submit(() -> Game.runGame(game)));
    }
    try {
      gameScores = Futures.allAsList(results).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    System.out.println(gameScores);
    int total = 0;
    for (int i = 0; i < gameScores.size(); i++) {
      total += gameScores.get(i);
    }
    System.out.println(((double) total) / gameScores.size());

    System.exit(0);
    // long now = System.currentTimeMillis();
    // ArrayList<ListenableFuture<Integer>> scores = new ArrayList<ListenableFuture<Integer>>();
    // for (int i = 0; i < 4; i++) {
    // ListenableFuture<Integer> gameScore = Util.exec.submit(() -> {
    // Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.DISTRO,
    // USE_GRAPHICS,
    // DO_DEBUG, RANDOMIZE, PLAY_MULTIPLE, SCORE_MODE);
    // return game.run();
    // });
    // gameScore.addListener(() -> {
    // try {
    // System.out.println(gameScore.get());
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }, Util.exec);
    // scores.add(gameScore);
    // }
    // System.out.println("\nRuntime: " + (System.currentTimeMillis() - now));
  }
}
