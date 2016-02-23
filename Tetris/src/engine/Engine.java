package engine;

import javafx.animation.AnimationTimer;
import mainGame.Board;

public class Engine extends AnimationTimer {
	private Board board;

	public Engine(Board board){
		this.board = board;
	}
	
	
	@Override
	public void handle(long arg0) {
		//makes the update only happen every second
		//the update rate should increase as a function of score, but thats a (much) later feature
		if((arg0 % 1e9) == 0){
			update();
		}
	}

	//do collision detection here I think
	//when I tried to debug this, it was never reached by the program
	private void update() {
		if (board.blockAdded){
			board.blockAdded = false;
			board.updateBoard(board.getFallingBlock());
		}
	}


	public boolean[][] getBoardState(){
		return board.getBoardState();
	}

}
