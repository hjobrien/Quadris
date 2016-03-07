package tetrominoes;

import javafx.scene.paint.Color;

public class Square extends Block {

	private static Color color = Color.RED;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(color), new Tile(color)},
		    {new Tile(color), new Tile(color)}
		}
	};
	
	
	public Square() {
		super(configurations);
		if (super.debug){
			System.out.println("made square");
		}
	}
	
	public Tile[][] getSquareShape(){
		return getShape();
	}
}
