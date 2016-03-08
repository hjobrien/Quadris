package engine;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mainGame.Board;
import tetrominoes.Tile;

public class Renderer {

	
public static void draw(Board board, int height, int width){
		
		//just for debugging now, although a similar system could be used to display the actual blocks
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				
				Tile current = board.getBoardState()[i][j];
				if (current.isFilled()){
					Rectangle r = new Rectangle(29, 29);
					r.setFill(current.getColor());
					board.getGrid().add(r, j,i);
				} else {
					Rectangle r = new Rectangle(29, 29);
					r.setFill(Color.WHITE);
					board.getGrid().add(r, j, i);
				}

			}
		}
		
	}
}
