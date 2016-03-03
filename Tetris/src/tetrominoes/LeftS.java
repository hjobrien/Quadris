package tetrominoes;

import javafx.scene.paint.Color;

public class LeftS extends Block{
	
	private static Color color = Color.GREEN;


	public LeftS() {
		super(getLeftSShape());
		if (super.debug){
			System.out.println("made left S");
		}
	}
	
	public static Tile[][] getLeftSShape(){
		Tile shape[][]={
			    {new Tile(true, true, color), new Tile(false, false)},
			    {new Tile(true, true, color), new Tile(true, true, color)},
			    {new Tile(false, false), new Tile(true, true, color)},
			};
		return shape;
	}
}
