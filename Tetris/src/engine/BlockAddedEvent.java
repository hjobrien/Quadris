package engine;

import javafx.event.Event;
import javafx.event.EventType;
import tetrominoes.BlockType;

public class BlockAddedEvent extends Event{

  private static final long serialVersionUID = -4147233310013968273L;   //idk why this is necessary
  
  
  public static final EventType<BlockAddedEvent> BLOCK_ADDED = new EventType<>(Event.ANY, "Block Added");
   
  private BlockType type;
  

  
  public BlockType getBlockType() {
    return this.type;
  }



  public BlockAddedEvent(BlockType type){
    super(BLOCK_ADDED);
    this.type = type;
  }
  

}
