package engine;

import tetrominoes.Block;
import tetrominoes.Tile;

public class Logger {
	
	
	public void log(Tile[][] boardState, Block fallingBlock, Block nextBlock){
		boolean[][] isFilled = tileToBoolean(boardState);
		int falling = fallingBlock.getType().getValue();
		int next = nextBlock.getType().getValue();
	}

	private boolean[][] tileToBoolean(Tile[][] boardState) {
		boolean state[][] = new boolean[][]{};
		for(int i = 0; i < boardState.length; i++){
			for(int j = 0; j < boardState[i].length; j++){
				state[i][j] = boardState[i][j].isFilled();
			}
		}
		return state;
	}
}
