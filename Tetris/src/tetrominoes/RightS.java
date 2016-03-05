package tetrominoes;

import javafx.scene.paint.Color;

public class RightS extends Block {
	
	private static Color color = Color.BLUE;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
		    {new Tile(), new Tile(color)},
		    {new Tile(color), new Tile(color)},
			{new Tile(color), new Tile()},
		},
		new Tile [][]{
			{new Tile(color),new Tile(color),new Tile()},
			{new Tile(),new Tile(color),new Tile(color)},
		}
	};
	
	private static int rotationIndex = 0;
		
	
	public RightS() {
		super(getRightSShape());
		if (super.debug){
			System.out.println("made Right S");
		}
	}
	
	public static Tile[][] getRightSShape(){
		return configurations[rotationIndex];
	}

}
