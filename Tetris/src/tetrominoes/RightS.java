package tetrominoes;

import javafx.scene.paint.Color;

public class RightS extends Block {
	
	private static BlockType type = BlockType.RIGHT_S;
	private static Color color = Color.BLUE;
	
	//[change this index for rotations][row][column]
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
	
	public RightS() {
		super(configurations, type);
		if (super.debug){
			System.out.println("made Right S");
		}
	}
	
	public Tile[][] getRightSShape(){
		return getShape();
	}

}
