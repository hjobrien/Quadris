package engine;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import blocks.blockGeneration.BlockGenerator;
import blocks.blockGeneration.StandardizeBlocks;
import mainGame.Game;
import mainGame.GameMode;
import mainGame.ScoreMode;
import util.Util;

public class ParallelizedCore {

  private int gameHeight;
  private int gameWidth;
  private int minTimePerTurn;
  private int maxGamesPerThread;
  private int maxGamesPerGen = 16;
  private boolean useGraphcis = false;
  private boolean doDebug = false;
  private ScoreMode scoreMode = ScoreMode.SIMPLE;
  private int numBlocksToConsider;
  private static int numTimesRun = 0;
  


  public ParallelizedCore(int gameHeight, int gameWidth, int minTimePerTurn, int maxGamesPerThread,
      int maxGamesPerGen, boolean useGraphics, boolean doDebug, ScoreMode scoreMode, int numBlocksToConsider) {
    this.gameHeight = gameHeight;
    this.gameWidth = gameWidth;
    this.minTimePerTurn = minTimePerTurn;
    this.maxGamesPerThread = maxGamesPerThread;
    this.maxGamesPerGen = maxGamesPerGen;
    this.useGraphcis = useGraphics;
    this.doDebug = doDebug;
    this.scoreMode = scoreMode;
    this.numBlocksToConsider = numBlocksToConsider;
  }


  public double run(double[] weights) throws IOException {
  File gameRecords = new File("/Users/Hank/IdeaProjects/Quadris/Quadris/Scores/" + (numTimesRun < 10 ? "0" + numTimesRun : numTimesRun) + Arrays.toString(weights) + ".txt");
    numTimesRun++;
    gameRecords.createNewFile();
    PrintStream scorePrinter = new PrintStream(gameRecords);
    List<Integer> gameScores;
    List<ListenableFuture<Integer>> results = new ArrayList<>();
    for (int i = 0; i < maxGamesPerGen; i++) {
      final int gameNum = i;
      BlockGenerator generator = new StandardizeBlocks(gameNum);
      Game game = new Game(gameHeight, gameWidth, minTimePerTurn, maxGamesPerThread,
          GameMode.AUTOPLAY, useGraphcis, doDebug, generator, weights, scoreMode, numBlocksToConsider);   //1 for now, change to 2 when sped up
        results.add(Util.compute.submit(() -> Game.runGame(game, gameNum)));
    }
    try {
      gameScores = Futures.allAsList(results).get();
    } catch (InterruptedException | ExecutionException e) {
      scorePrinter.close();
      throw new RuntimeException(e);
    }
    int total = 0;
    for (int i = 0; i < gameScores.size(); i++) {
      int gameScore = gameScores.get(i);
      total += gameScore;
//      System.out.println(gameScore);
      scorePrinter.println(gameScore);
    }
    scorePrinter.close();
    return ((double) total) / gameScores.size();
  }
}
