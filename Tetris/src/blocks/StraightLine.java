package blocks;

import javafx.scene.paint.Color;

public class StraightLine extends Block {
	
	private static BlockType type = BlockType.LINE;
	public static final Color COLOR = Color.PURPLE;
	
	//[change this index for rotations][row][column]
	public static final Tile[][][] CONFIGURATIONS = new Tile[][][]{
		
		new Tile [][]{
			{new Tile(COLOR)},
			{new Tile(COLOR)},
			{new Tile(COLOR)},
			{new Tile(COLOR)},
		},
		
		new Tile [][]{
          {new Tile(COLOR), new Tile(COLOR), new Tile(COLOR), new Tile(COLOR)}
        }
	};		
	
	public StraightLine() {
		super(CONFIGURATIONS, type, COLOR);
		if (super.debug){
			System.out.println("made straight line");
		}
	}
	
	public Tile[][] getStraightLineShape(){
		return getShape();
	}

}
