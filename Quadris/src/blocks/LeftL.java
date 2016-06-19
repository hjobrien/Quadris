package blocks;

import javafx.scene.paint.Color;

public class LeftL extends Block {
	
	private static BlockType type = BlockType.LEFT_L;
	public static final Color COLOR = Color.ORANGE;
	
	//[change this index for rotations][row][column]
	public static final Tile[][][] CONFIGURATIONS = new Tile[][][]{
		new Tile [][]{
			{new Tile(COLOR),new Tile(COLOR),new Tile(COLOR)},
			{new Tile(),new Tile(),new Tile(COLOR)},
		},
		new Tile [][]{
			{new Tile(COLOR),new Tile(COLOR)},
			{new Tile(COLOR),new Tile()},
			{new Tile(COLOR),new Tile()},
		},
		new Tile [][]{
			{new Tile(COLOR),new Tile(),new Tile()},
			{new Tile(COLOR),new Tile(COLOR),new Tile(COLOR)},
		}, 
		new Tile [][]{
			{new Tile(),new Tile(COLOR)},
			{new Tile(),new Tile(COLOR)},
			{new Tile(COLOR),new Tile(COLOR)},
		}
	};
	
	
	public LeftL() {
		super(CONFIGURATIONS, type, COLOR);
		
		if (super.debug){
			System.out.println("made left L");
		}
	}

	//i feel like there should be a better way to do this
	public Tile[][] getLeftLShape(){
		return getShape();
	}
}
