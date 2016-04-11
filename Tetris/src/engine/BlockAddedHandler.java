package engine;

import javafx.event.EventHandler;

public class BlockAddedHandler implements EventHandler<BlockAddedEvent>{

  @Override
  public void handle(BlockAddedEvent event) {
    System.out.println(event.getBlockType());
//    event.consume() //if used, handler for parent will not be called
    
  }

}
