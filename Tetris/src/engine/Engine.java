package engine;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	private Board nextPieceBoard;
	private Block nextBlock = generateRandomBlock();
	private boolean isPaused = false;

	public Engine(Board board, Board nextPieceBoard){
		this.board = board;
		this.nextPieceBoard = nextPieceBoard;
	}

	//do collision detection here I think
	//when I tried to debug this, it was never reached by the program
	public void update() {
		if(!isPaused){ //little hacky, could be improved
			if (board.getFallingBlock().isFalling()){
				if (board.checkDown()){
					board.blockDown();
				} else {
					if (board.checkFullRow()){
						board.clearLine();
					} else {
						board.setNotFalling();
						addBlock();
					}
				}
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
		board.setFallingBlock(nextBlock);
		board.updateBoardWithNewBlock(nextBlock);
		nextBlock = generateRandomBlock();	
		nextPieceBoard.clearBoard();
		draw(nextPieceBoard, 4,4);
		nextPieceBoard.setFallingBlock(nextBlock);
		nextPieceBoard.updateBoardWithNewBlock(nextBlock);
		nextPieceBoard.setNotFalling();
		draw(nextPieceBoard, 4,4);
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
		
//		shouldnt ever happen
		return new StraightLine();
	}

	public Board getBoard(){
		return this.board;
	}

	public void togglePause() {
		isPaused = !isPaused;
		
	}
	
	public void draw(Board board, int height, int width){
		
		//just for debugging now, although a similar system could be used to display the actual blocks
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				
				Tile current = board.getBoardState().get(i).get(j);
				if (current.isFilled()){
					Rectangle r = new Rectangle();
					r.setHeight(29);
					r.setWidth(29);
					r.setFill(current.getColor());
					board.getGrid().add(r, j,i);
				} else {
					Rectangle r = new Rectangle();
					r.setHeight(29);
					r.setWidth(29);
					r.setFill(Color.WHITE);
					board.getGrid().add(r, j, i);
				}

			}
		}
		
	}

}
