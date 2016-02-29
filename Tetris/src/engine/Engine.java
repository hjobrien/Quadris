package engine;

import java.util.ArrayList;
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
import tetrominoes.TBlock;
import tetrominoes.Tile;

public class Engine {
	private Board board;

	public Engine(Board board){
		this.board = board;
	}

	//do collision detection here I think
	//when I tried to debug this, it was never reached by the program
	public void update() {
		if (board.getFallingBlock().isFalling()){
			
			if (board.checkBlockSpace()){
				board.blockDown();
			} else {
				board.setNotFalling();
				addBlock();
			}
		}
		//TODO
		//checkForCompleteRows();
	}

	public ArrayList<ArrayList<Tile>> getBoardState(){
		return board.getBoardState();
	}

	//Adds a random block to the board
	public void addBlock() {
		Block blockToAdd = generateRandomBlock();
		board.setFallingBlock(blockToAdd);
		board.updateBoardWithNewBlock(blockToAdd);		
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
			return new TBlock();
		case 6: 
			return new Square();
		}
		
		//shouldnt ever happen
		return new StraightLine();
	}

	public Board getBoard(){
		return this.board;
	}

}
