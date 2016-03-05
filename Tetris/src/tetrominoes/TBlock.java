package tetrominoes;

import javafx.scene.paint.Color;

public class TBlock extends Block {
		
	private static Color color = Color.AQUAMARINE;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(true, true, color),new Tile(true, true, color),new Tile(true, true, color)},
		    {new Tile(false, false), new Tile(true, true, color), new Tile(false, false)},
		},
		new Tile [][]{
			{new Tile(true, true, color),new Tile(false, false)},
		    {new Tile(true, true, color), new Tile(true, true, color)},
		    {new Tile(true, true, color), new Tile(false, false)},
		},
		new Tile [][]{
			{new Tile(false, false),new Tile(true, true, color),new Tile(false, false)},
			{new Tile(true, true, color),new Tile(true, true, color),new Tile(true, true, color)},
		},
		new Tile [][]{
			{new Tile(false, false),new Tile(true, true, color)},
		    {new Tile(true, true, color), new Tile(true, true, color)},
		    {new Tile(false, false), new Tile(true, true, color)},
		}
	};
	
	private static int rotationIndex = 0;
		

	public TBlock() {
		super(getTShape());
		if (super.debug){
			System.out.println("made T");
		}
	}
	
	public static Tile[][] getTShape(){
		return configurations[rotationIndex];
	}

}
