package engine;

import javafx.event.Event;
import javafx.event.EventType;
import tetrominoes.Block;
import tetrominoes.Tile;

public class BlockAddedEvent extends Event{

  private static final long serialVersionUID = -4147233310013968273L;   //idk why this is necessary
  
  
  public static final EventType<BlockAddedEvent> BLOCK_ADDED = new EventType<>(Event.ANY, "Block Added");
   
  private Block block;
  private Tile[][] boardState;
  



  public BlockAddedEvent(Block block, Tile[][] boardState){
    super(BLOCK_ADDED);
    this.block = block;
    this.boardState = boardState;
  }



  public Tile[][] getBoardState() {
    return this.boardState;
  }

  
  public Block getBlock() {
    return this.block;
  }


}
