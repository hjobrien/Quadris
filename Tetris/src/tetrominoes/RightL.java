package tetrominoes;

import javafx.scene.paint.Color;

public class RightL extends Block {
	
	private static Color color = Color.YELLOW;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(color),new Tile()},
		    {new Tile(color),new Tile()},
		    {new Tile(color),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(),new Tile(),new Tile(color)},
			{new Tile(color),new Tile(color),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(color),new Tile(color)},
		    {new Tile(),new Tile(color)},
		    {new Tile(),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(color),new Tile(color),new Tile(color)},
			{new Tile(color),new Tile(),new Tile()},
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
