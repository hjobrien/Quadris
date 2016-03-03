package tetrominoes;

import javafx.scene.paint.Color;

public class RightS extends Block {
	
	private static Color color = Color.BLUE;

	
	public RightS() {
		super(getRightSShape());
		if (super.debug){
			System.out.println("made Right S");
		}
	}
	
	public static Tile[][] getRightSShape(){
		Tile shape[][]={
			    {new Tile(false, false), new Tile(true, true, color)},
			    {new Tile(true, true, color), new Tile(true, true, color)},
			    {new Tile(true, true, color), new Tile(false, false)},
			};
		return shape;
	}

}
