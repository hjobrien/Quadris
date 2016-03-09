package engine;

import javafx.scene.paint.Color;
import mainGame.Board;
import tetrominoes.Tile;

public class Renderer {	
	
	public static void draw(Board board){
		for (int i = 0; i < board.getBoardState().length; i++){
			for (int j = 0; j < board.getBoardState()[i].length; j++){
				
				Tile current = board.getBoardState()[i][j];
				if (current.isFilled()){
					board.getBoardRects()[i][j].setFill(current.getColor());
				} else {
					board.getBoardRects()[i][j].setFill(Color.WHITE);
				}
			}
		}
		
	}
}
