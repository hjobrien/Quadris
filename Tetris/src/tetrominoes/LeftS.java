package tetrominoes;

import javafx.scene.paint.Color;

public class LeftS extends Block{
	
	private static Color color = Color.GREEN;

	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			    {new Tile(color), new Tile()},
			    {new Tile(color), new Tile(color)},
			    {new Tile(), new Tile(color)},
			},
		new Tile [][]{
			{new Tile(),new Tile(color),new Tile(color)},
			{new Tile(color),new Tile(color),new Tile()},
		}
	};
	
	private static int rotationIndex = 0;
		
	
	

	public LeftS() {
		super(getLeftSShape());
		if (super.debug){
			System.out.println("made left S");
		}
	}
	
	public static Tile[][] getLeftSShape(){
		return configurations[rotationIndex];
	}
}
