package tetrominoes;

import javafx.scene.paint.Color;

public class RightL extends Block {
	
	private static Color color = Color.YELLOW;

	public RightL() {
		super(getRightLShape());
		if (super.debug){
			System.out.println("made right L");
		}
	}
	
	public static Tile[][] getRightLShape(){
		Tile shape[][]={
			    {new Tile(true, true, color),new Tile(false, false)},
			    {new Tile(true, true, color),new Tile(false, false)},
			    {new Tile(true, true, color),new Tile(true, true, color)},
			};
		return shape;
	}

}
