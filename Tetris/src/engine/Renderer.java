package engine;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mainGame.Board;
import tetrominoes.Tile;

public class Renderer {

	private static final ArrayList<Rectangle[][]> STORED_BOARDS= new ArrayList<Rectangle[][]>();
	
	
	public static void draw(Board board){
		Rectangle[][] storedBoard = null;
		if(board.hasBeenDrawn()){
			storedBoard = getStoredBoard(board.getBoardState().length, board.getBoardState()[0].length);
		}
		else{
			storedBoard = initNewStoredBoard(board.getBoardState().length, board.getBoardState()[0].length);
		}

		
		for(int i = 0; i < board.getBoardState()[i].length; i++){
			for(int j = 0; j < board.getBoardState()[i].length; j++){
				Tile current = board.getBoardState()[i][j];
				Rectangle r = storedBoard[i][j];
				if (current.isFilled()){
					r.setFill(current.getColor());
				} else {
					r.setFill(Color.WHITE);
				}
				if(!board.hasBeenDrawn());
				board.getGrid().add(r, i, j);
			}
		}
//		
//		//just for debugging now, although a similar system could be used to display the actual blocks
//		for (int i = 0; i < board.getBoardState().length; i++){
//			for (int j = 0; j < board.getBoardState()[i].length; j++){
//				
//				Tile current = board.getBoardState()[i][j];
//				if (current.isFilled()){
//					board.getBoardRects()[i][j].setFill(current.getColor());
//				} else {
//					board.getBoardRects()[i][j].setFill(Color.WHITE);
//				}
//			}
//		}
		
	}


	private static Rectangle[][] getStoredBoard(int height, int width) {
		for(Rectangle[][] board : STORED_BOARDS){
			if(board.length == height && board[0].length == width){
				return board;
			}
		}
		//sholdn't ever happen
		throw new RuntimeException("No stored board matched");
	}


	private static Rectangle[][] initNewStoredBoard(int height, int width) {
		Rectangle[][] newBoard  = new Rectangle[height][width];
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				newBoard[i][j] = new Rectangle(29,29);
			}
		}
		STORED_BOARDS.add(newBoard);
		return newBoard;
	}
}
