package blocks;

import javafx.scene.paint.Color;

public class RightL extends Block {
	
	
	private static BlockType type = BlockType.RIGHT_L;
	public static final Color COLOR = Color.YELLOW;
	
	//[change this index for rotations][row][column]
	public static final Tile[][][] CONFIGURATIONS = new Tile[][][]{
		new Tile [][]{
			{new Tile(),new Tile(),new Tile(COLOR)},
			{new Tile(COLOR),new Tile(COLOR),new Tile(COLOR)},
		},
		new Tile [][]{
			{new Tile(COLOR),new Tile(COLOR)},
		    {new Tile(),new Tile(COLOR)},
		    {new Tile(),new Tile(COLOR)},
		},
		new Tile [][]{
			{new Tile(COLOR),new Tile(COLOR),new Tile(COLOR)},
			{new Tile(COLOR),new Tile(),new Tile()},
		}, 
		new Tile [][]{
			{new Tile(COLOR),new Tile()},
		    {new Tile(COLOR),new Tile()},
		    {new Tile(COLOR),new Tile(COLOR)},
		}
	};
			
	public RightL() {
		super(CONFIGURATIONS, type, COLOR);
		if (super.debug){
			System.out.println("made right L");
		}
	}
	
	public Tile[][] getRightLShape(){
		return getShape();
	}

}
