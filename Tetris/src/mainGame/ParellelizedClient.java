package mainGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import util.Util;

public class ParellelizedClient {
  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000000;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
  public static final boolean RANDOMIZE = false;
  public static final boolean PLAY_MULTIPLE = false;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;

  private static int maxGames = 10;

  public static void main(String args[]) throws IOException {
    List<Integer> gameScores;
    List<ListenableFuture<Integer>> results = new ArrayList<>();
    for(int i = 0; i <  maxGames; i++){
      final int j = i;
      results.add(Util.compute.submit(() -> Game.runGame(j)));
    }
    try{
      gameScores = Futures.allAsList(results).get();
    }catch(InterruptedException | ExecutionException e){
      throw new RuntimeException(e);
    }
    System.out.println(gameScores);
    int total = 0;
    for(int i = 0; i < gameScores.size(); i++){
      total += gameScores.get(i);
    }
    System.out.println(((double)total) / gameScores.size());
    
    System.exit(0);
//    long now = System.currentTimeMillis();
//    ArrayList<ListenableFuture<Integer>> scores = new ArrayList<ListenableFuture<Integer>>();
//    for (int i = 0; i < 4; i++) {
//      ListenableFuture<Integer> gameScore = Util.exec.submit(() -> {
//        Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.DISTRO, USE_GRAPHICS,
//        DO_DEBUG, RANDOMIZE, PLAY_MULTIPLE, SCORE_MODE);
//        return game.run();
//      });
//      gameScore.addListener(() -> {
//        try {
//          System.out.println(gameScore.get());
//        } catch (Exception e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
//      }, Util.exec);
//      scores.add(gameScore);
//    }
//    System.out.println("\nRuntime: " + (System.currentTimeMillis() - now));
  }
}
