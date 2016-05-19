package mainGame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import blocks.BlockGenerator;
import blocks.StandardizeBlocks;
import engine.GameMode;
import util.Util;

public class ParellelizedRunner {

  private int gameHeight;
  private int gameWidth;
  private int minTimePerTurn;
  private int maxGamesPerThread;
  private int maxGamesPerGen = 10;
  private boolean useGraphcis = false;
  private boolean doDebug = false;
  private ScoreMode scoreMode = ScoreMode.SIMPLE;


  public ParellelizedRunner(int gameHeight, int gameWidth, int minTimePerTurn, int maxGamesPerThread,
      int maxGamesPerGen, boolean useGraphics, boolean doDebug, ScoreMode scoreMode) {
    this.gameHeight = gameHeight;
    this.gameWidth = gameWidth;
    this.minTimePerTurn = minTimePerTurn;
    this.maxGamesPerThread = maxGamesPerThread;
    this.maxGamesPerGen = maxGamesPerGen;
    this.useGraphcis = useGraphics;
    this.doDebug = doDebug;
    this.scoreMode = scoreMode;
  }


  public double run(double[] weights) {
    List<Integer> gameScores;
    List<ListenableFuture<Integer>> results = new ArrayList<>();
    for (int i = 0; i < maxGamesPerGen; i++) {
      final int gameNum = i;
      BlockGenerator generator = new StandardizeBlocks(gameNum);
      Game game = new Game(gameHeight, gameWidth, minTimePerTurn, maxGamesPerThread,
          GameMode.AUTOPLAY, useGraphcis, doDebug, generator, weights, scoreMode);
      results.add(Util.compute.submit(() -> Game.runGame(game, gameNum)));
    }
    try {
      gameScores = Futures.allAsList(results).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    int total = 0;
    for (int i = 0; i < gameScores.size(); i++) {
      total += gameScores.get(i);
    }
    return ((double) total) / gameScores.size();
  }
}
