package engine;

import java.util.Random;

import javafx.animation.AnimationTimer;
import mainGame.Board;
import tetrominoes.Block;
import tetrominoes.LeftL;
import tetrominoes.LeftS;
import tetrominoes.RightL;
import tetrominoes.RightS;
import tetrominoes.Square;
import tetrominoes.StraightLine;
import tetrominoes.T;

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


	public void addBlock() {
		Block blockToAdd = generateRandomBlock();
		board.setFallingBlock(blockToAdd);
		System.out.println("added");
		
	}
	
	private Block generateRandomBlock() {
		Random r = new Random();
		int i = r.nextInt(7);
		switch (i){
		case 0:
			return new LeftL();
		case 1:
			return new RightL();
		case 2:
			return new LeftS();
		case 3: 
			return new RightS();
		case 4:
			return new StraightLine();
		case 5:
			return new T();
		case 6: 
			return new Square();
		}
		
		//shouldnt ever happen
		return new StraightLine();
	}


}
