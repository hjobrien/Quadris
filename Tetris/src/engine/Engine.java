package engine;

import java.util.ArrayList;
import java.util.Random;

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
	private Rectangle[][] boardRects = new Rectangle[20][10];

	public Engine(Board board, Board nextPieceBoard){
		this.board = board;
		this.nextPieceBoard = nextPieceBoard;
		for (int i = 0; i < boardRects.length; i++){
			for (int j = 0; j < boardRects[i].length; j++){
				Rectangle r = new Rectangle(29, 29);
				r.setFill(Color.WHITE);
				boardRects[i][j] = r;
				board.getGrid().add(r, j,i);
			}
		}
	}

	public void update() {
		if(!isPaused){ //little hacky, could be improved
			if (board.getFallingBlock().isFalling()){
				if (board.checkDown()){
					board.blockDown();
				} else {
					ArrayList<Integer> linesToClear = board.getFullRows();
					if (!linesToClear.isEmpty()){
						board.clearLines(linesToClear);
					} else {
						board.setNotFalling();
						addBlock();
					}
				}
			}
		}
	}

	public Tile[][] getBoardState(){
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

	public boolean togglePause() {
		isPaused = !isPaused;
		return isPaused;
	}
	
	public void draw(Board board, int height, int width){
		
		//just for debugging now, although a similar system could be used to display the actual blocks
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				
				Tile current = board.getBoardState()[i][j];
				if (current.isFilled()){
					boardRects[i][j].setFill(current.getColor());
				} else {
					boardRects[i][j].setFill(Color.WHITE);
				}
			}
		}
		
	}

}
