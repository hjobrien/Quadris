package blocks;

import javafx.scene.paint.Color;

public class StraightLine extends Block {
	
	private static BlockType type = BlockType.LINE;
	private static Color color = Color.PURPLE;
	
	//[change this index for rotations][row][column]
	private static Tile[][][] configurations = new Tile[][][]{
		
		new Tile [][]{
			{new Tile(color)},
			{new Tile(color)},
			{new Tile(color)},
			{new Tile(color)},
		},
		
		new Tile [][]{
          {new Tile(color), new Tile(color), new Tile(color), new Tile(color)}
        }
	};		
	
	public StraightLine() {
		super(configurations, type);
		if (super.debug){
			System.out.println("made straight line");
		}
	}
	
	public Tile[][] getStraightLineShape(){
		return getShape();
	}

}
