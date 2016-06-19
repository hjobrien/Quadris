package blocks;

import javafx.scene.paint.Color;

public class RightS extends Block {
	
	private static BlockType type = BlockType.RIGHT_S;
	public static final Color COLOR = Color.BLUE;
	
	//[change this index for rotations][row][column]
	public static final Tile[][][] CONFIGURATIONS = new Tile[][][]{
		new Tile [][]{
		    {new Tile(), new Tile(COLOR)},
		    {new Tile(COLOR), new Tile(COLOR)},
			{new Tile(COLOR), new Tile()},
		},
		new Tile [][]{
			{new Tile(COLOR),new Tile(COLOR),new Tile()},
			{new Tile(),new Tile(COLOR),new Tile(COLOR)},
		}
	};		
	
	public RightS() {
		super(CONFIGURATIONS, type, COLOR);
		if (super.debug){
			System.out.println("made Right S");
		}
	}
	
	public Tile[][] getRightSShape(){
		return getShape();
	}

}
