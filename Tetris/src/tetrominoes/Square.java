package tetrominoes;

import javafx.scene.paint.Color;

public class Square extends Block {

	private static Color color = Color.RED;

	
	public Square() {
		super(getSquareShape());
		if (super.debug){
			System.out.println("made square");
		}
	}
	
	public static Tile[][] getSquareShape(){
		Tile shape[][]={
			    {new Tile(true, true, color), new Tile(true, true, color)},
			    {new Tile(true, true, color), new Tile(true, true, color)},
			};
		return shape;
	}
}
