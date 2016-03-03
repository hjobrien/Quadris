package tetrominoes;

import javafx.scene.paint.Color;

public class StraightLine extends Block {

	private static Color color = Color.PURPLE;

	
	public StraightLine() {
		super(getStraightLineShape());
		if (super.debug){
			System.out.println("made straight line");
		}
	}
	
	public static Tile[][] getStraightLineShape(){
		Tile shape[][]={
			    {new Tile(true, true, color), new Tile(true, true, color), new Tile(true, true, color), new Tile(true, true, color)},
			};
		return shape;
	}

}
