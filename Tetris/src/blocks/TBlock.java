package blocks;

import javafx.scene.paint.Color;

public class TBlock extends Block {
		
	private static BlockType type = BlockType.T_BLOCK;
	public static final Color COLOR = Color.AQUAMARINE;
	
	//[change this index for rotations][row][column]
	public static final Tile[][][] CONFIGURATIONS = new Tile[][][]{

		new Tile [][]{
			{new Tile(COLOR),new Tile()},
		    {new Tile(COLOR), new Tile(COLOR)},
		    {new Tile(COLOR), new Tile()},
		},
		new Tile [][]{
			{new Tile(),new Tile(COLOR),new Tile()},
			{new Tile(COLOR),new Tile(COLOR),new Tile(COLOR)},
		},
		new Tile [][]{
			{new Tile(),new Tile(COLOR)},
		    {new Tile(COLOR), new Tile(COLOR)},
		    {new Tile(), new Tile(COLOR)},
		},
	    new Tile [][]{
          {new Tile(COLOR),new Tile(COLOR),new Tile(COLOR)},
          {new Tile(), new Tile(COLOR), new Tile()},
        },
	};
	
	public TBlock() {
		super(CONFIGURATIONS, type, COLOR);
		if (super.debug){
			System.out.println("made T");
		}
	}
	
	public Tile[][] getTShape(){
		return getShape();
	}

}
