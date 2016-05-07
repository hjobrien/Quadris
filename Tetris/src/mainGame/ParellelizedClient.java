package mainGame;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class ParellelizedClient extends Application{
  public static final int MIN_TIME_PER_TURN = 1;

  private static int gameNumber = 0;

  public static void main(String args[]) throws IOException {
//    long now = System.currentTimeMillis();
//    ArrayList<ListenableFuture<Integer>> scores = new ArrayList<ListenableFuture<Integer>>();
//    for (int i = 0; i < 4; i++) {
//      ListenableFuture<Integer> gameScore = Util.exec.submit(() -> {
//        Game game = new Game(); // only seems to use this info if not running in
//        // graphics mode
//        game.configureSettings();
//        boolean randomizeBlocks = false;
//        game.run(randomizeBlocks);
//        return game.getScore();
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

  @Override
  public void start(Stage arg0) throws Exception {
    // TODO Auto-generated method stub
    
  }
}
