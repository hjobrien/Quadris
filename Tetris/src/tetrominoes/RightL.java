package tetrominoes;

import engine.BlockType;
import javafx.scene.paint.Color;

public class RightL extends Block {
	
	
	private static BlockType type = BlockType.RIGHT_L;
	private static Color color = Color.YELLOW;
	
	//[change this index for rotations][row][column]
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
			
	public RightL() {
		super(configurations, type);
		if (super.debug){
			System.out.println("made right L");
		}
	}
	
	public Tile[][] getRightLShape(){
		return getShape();
	}

}
