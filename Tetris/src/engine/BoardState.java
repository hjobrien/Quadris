package engine;

import mainGame.Board;
import tetrominoes.Block;

public class BoardState {
	private Board board;
	private Block activeBlock;
	
	public Board getBoard(){
		return board;
	}
}
