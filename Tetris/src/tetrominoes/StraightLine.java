package tetrominoes;

import javafx.scene.paint.Color;

public class StraightLine extends Block {

	private static Color color = Color.PURPLE;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(color), new Tile(color), new Tile(color), new Tile(color)}
		},
		new Tile [][]{
			{new Tile(color)},
			{new Tile(color)},
			{new Tile(color)},
			{new Tile(color)},
		}
	};
	
	private static int rotationIndex = 0;
		
	
	public StraightLine() {
		super(getStraightLineShape());
		if (super.debug){
			System.out.println("made straight line");
		}
	}
	
	public static Tile[][] getStraightLineShape(){
		return configurations[rotationIndex];
	}

}
