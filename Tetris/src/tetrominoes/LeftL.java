package tetrominoes;

import javafx.scene.paint.Color;

public class LeftL extends Block {
	
	private static Color color = Color.ORANGE;
	
	public LeftL() {
		super(getLeftLShape());
		if (super.debug){
			System.out.println("made left L");
		}
	}

	//i feel like there should be a better way to do this
	public static Tile[][] getLeftLShape(){
		Tile shape[][]={
			    {new Tile(false, false),new Tile(true, true, color)},
			    {new Tile(false, false),new Tile(true, true, color)},
			    {new Tile(true, true, color),new Tile(true, true, color)},
			};
		return shape;
	}
}
