package tetrominoes;

import javafx.scene.paint.Color;

public class TBlock extends Block {
		
	private static Color color = Color.AQUAMARINE;


	public TBlock() {
		super(getTShape());
		if (super.debug){
			System.out.println("made T");
		}
	}
	
	public static Tile[][] getTShape(){
		Tile shape[][]={
			    {new Tile(true, true, color),new Tile(true, true, color),new Tile(true, true, color)},
			    {new Tile(false, false), new Tile(true, true, color), new Tile(false, false)},
			};
		return shape;
	}

}
