package blocks;

import javafx.scene.paint.Color;

public class Square extends Block {
	
	private static BlockType type = BlockType.SQUARE;
	public static final Color COLOR = Color.RED;
	
	//[change this index for rotations][row][column]
	public static final Tile[][][] CONFIGURATIONS = new Tile[][][]{
		new Tile [][]{
			{new Tile(COLOR), new Tile(COLOR)},
		    {new Tile(COLOR), new Tile(COLOR)}
		}
	};
	
	
	public Square() {
		super(CONFIGURATIONS, type, COLOR);
		if (super.debug){
			System.out.println("made square");
		}
	}
	
	public Tile[][] getSquareShape(){
		return getShape();
	}
}
