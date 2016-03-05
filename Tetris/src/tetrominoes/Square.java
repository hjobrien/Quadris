package tetrominoes;

import javafx.scene.paint.Color;

public class Square extends Block {

	private static Color color = Color.RED;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(true, true, color), new Tile(true, true, color)},
		    {new Tile(true, true, color), new Tile(true, true, color)}
		}
	};
	
	private static int rotationIndex = 0;
		
	
	public Square() {
		super(getSquareShape());
		if (super.debug){
			System.out.println("made square");
		}
	}
	
	public static Tile[][] getSquareShape(){
		return configurations[rotationIndex];
	}
}
