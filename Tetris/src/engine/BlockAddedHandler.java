package engine;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import cerulean.Cerulean;
import javafx.event.EventHandler;
import mainGame.Move;

public class BlockAddedHandler implements EventHandler<BlockAddedEvent> {

  @Override
  public void handle(BlockAddedEvent event) {
    Cerulean.submitBlock(event.getBlock(), event.getBoardState());
    // Cerulean.setBlockToPlace(event.getBlockType());
    Move[] solution = Cerulean.getSolution();
    try {
      Robot r = new Robot();
      r.setAutoDelay(10);
      for (Move m : solution) {
        switch (m) {
          case RIGHT:
            r.keyPress(KeyEvent.VK_RIGHT);
            r.keyRelease(KeyEvent.VK_RIGHT);
            break;
          case LEFT:
            r.keyPress(KeyEvent.VK_LEFT);
            r.keyRelease(KeyEvent.VK_LEFT);
            break;
          case ROT_RIGHT:
            System.out.println("flagxx  right");
            r.keyPress(KeyEvent.VK_X);
            r.keyRelease(KeyEvent.VK_X);
            break;
            //no need for ROT_LEFT, its never used in autoplay mode
          case DROP:
            r.keyPress(KeyEvent.VK_SPACE);
            r.keyRelease(KeyEvent.VK_SPACE);
            break;
          default:
            r.keyPress(KeyEvent.VK_DOWN);
            r.keyRelease(KeyEvent.VK_DOWN);
            break;
        }
      }
    } catch (AWTException e) {

    }

    event.consume(); // handler for parent will not be called
  }

}
