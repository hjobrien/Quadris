package blocks;

import javafx.scene.paint.Color;

public class LeftS extends Block{
	
	private static BlockType type = BlockType.LEFT_S;
	public static final Color COLOR = Color.GREEN;

	//[change this index for rotations][row][column]
	public static final Tile[][][] CONFIGURATIONS = new Tile[][][]{
		new Tile [][]{
			    {new Tile(COLOR), new Tile()},
			    {new Tile(COLOR), new Tile(COLOR)},
			    {new Tile(), new Tile(COLOR)},
			},
		new Tile [][]{
			{new Tile(),new Tile(COLOR),new Tile(COLOR)},
			{new Tile(COLOR),new Tile(COLOR),new Tile()},
		}
	};		

	
	public LeftS() {
		super(CONFIGURATIONS, type, COLOR);
		if (super.debug){
			System.out.println("made left S");
		}
	}
	
	public Tile[][] getLeftSShape(){
		return getShape();
	}
}
