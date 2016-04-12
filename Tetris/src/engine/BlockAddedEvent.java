package engine;

import blocks.Block;
import blocks.Tile;
import javafx.event.Event;
import javafx.event.EventType;

public class BlockAddedEvent extends Event{

  private static final long serialVersionUID = -4147233310013968273L;   //idk why this is necessary
  
  
  public static final EventType<BlockAddedEvent> BLOCK_ADDED = new EventType<>(Event.ANY, "Block Added");
   
  private Block block;
  private Tile[][] boardState;
  


  /**
   * makes a new event based on a block that is to be added to the board state
   * @param block   the block to be added
   * @param boardState  the board state it should be added to
   */
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
