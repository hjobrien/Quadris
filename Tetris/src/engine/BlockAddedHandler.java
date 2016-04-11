package engine;

import cerulean.Cerulean;
import javafx.event.EventHandler;

public class BlockAddedHandler implements EventHandler<BlockAddedEvent>{

  @Override
  public void handle(BlockAddedEvent event) {
    System.out.println(event.getBlockType());
//    Cerulean.setBlockToPlace(event.getBlockType());
    event.consume(); //handler for parent will not be called
    
  }

}
