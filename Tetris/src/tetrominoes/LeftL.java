package tetrominoes;

import javafx.scene.paint.Color;

public class LeftL extends Block {
	
	private static Color color = Color.ORANGE;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(),new Tile(color)},
			{new Tile(),new Tile(color)},
			{new Tile(color),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(color),new Tile(color),new Tile(color)},
			{new Tile(),new Tile(),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(color),new Tile(color)},
			{new Tile(color),new Tile()},
			{new Tile(color),new Tile()},
		},
		new Tile [][]{
			{new Tile(color),new Tile(),new Tile()},
			{new Tile(color),new Tile(color),new Tile(color)},
		}
	};
	
	private static int rotationIndex = 0;
	
	public LeftL() {
		super(getLeftLShape());
		
		if (super.debug){
			System.out.println("made left L");
		}
	}

	//i feel like there should be a better way to do this
	public static Tile[][] getLeftLShape(){
		return configurations[rotationIndex];
	}
}
