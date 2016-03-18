package tetrominoes;

import javafx.scene.paint.Color;

public class Square extends Block {
	
	private static BlockType type = BlockType.SQUARE;
	private static Color color = Color.RED;
	
	//[change this index for rotations][row][column]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(color), new Tile(color)},
		    {new Tile(color), new Tile(color)}
		}
	};
	
	
	public Square() {
		super(configurations, type);
		if (super.debug){
			System.out.println("made square");
		}
	}
	
	public Tile[][] getSquareShape(){
		return getShape();
	}
}
