package tetrominoes;

import engine.BlockType;
import javafx.scene.paint.Color;

public class LeftS extends Block{
	
	private static BlockType type = BlockType.LEFT_S;
	private static Color color = Color.GREEN;

	//[change this index for rotations][row][column]
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

	
	public LeftS() {
		super(configurations, type);
		if (super.debug){
			System.out.println("made left S");
		}
	}
	
	public Tile[][] getLeftSShape(){
		return getShape();
	}
}
