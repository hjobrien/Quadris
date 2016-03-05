package tetrominoes;

import javafx.scene.paint.Color;

public class RightL extends Block {
	
	private static Color color = Color.YELLOW;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(true, true, color),new Tile(false, false)},
		    {new Tile(true, true, color),new Tile(false, false)},
		    {new Tile(true, true, color),new Tile(true, true, color)},
		},
		new Tile [][]{
			{new Tile(false, false),new Tile(false, false),new Tile(true, true, color)},
			{new Tile(true, true, color),new Tile(true, true, color),new Tile(true, true, color)},
		},
		new Tile [][]{
			{new Tile(true, true, color),new Tile(true, true, color)},
		    {new Tile(false, false),new Tile(true, true, color)},
		    {new Tile(false, false),new Tile(true, true, color)},
		},
		new Tile [][]{
			{new Tile(true, true, color),new Tile(true, true, color),new Tile(true, true, color)},
			{new Tile(true, true, color),new Tile(false, false),new Tile(false, false)},
		}
	};
	
	private static int rotationIndex = 0;
		
	
	
	public RightL() {
		super(getRightLShape());
		if (super.debug){
			System.out.println("made right L");
		}
	}
	
	public static Tile[][] getRightLShape(){
		return configurations[rotationIndex];
	}

}
