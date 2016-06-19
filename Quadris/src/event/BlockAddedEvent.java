package event;

import blocks.Block;
import blocks.Tile;
import javafx.event.Event;
import javafx.event.EventType;

public class BlockAddedEvent extends Event {

	private static final long serialVersionUID = 1;

	// not currently used

	public static final EventType<BlockAddedEvent> BLOCK_ADDED = new EventType<>(Event.ANY, "Block Added");

	private Block block;
	private Tile[][] boardState;

	/**
	 * makes a new event based on a block that is to be added to the board state
	 * 
	 * @param block
	 *            the block to be added
	 * @param boardState
	 *            the board state it should be added to
	 */
	public BlockAddedEvent(Block block, Tile[][] boardState) {
		super(BLOCK_ADDED);
		this.block = block;
		this.boardState = boardState;
	}

	/**
	 * @return returns the board state of the event
	 */
	public Tile[][] getBoardState() {
		return this.boardState;
	}

	/**
	 * 
	 * @return the active block in the event
	 */
	public Block getBlock() {
		return this.block;
	}

}
