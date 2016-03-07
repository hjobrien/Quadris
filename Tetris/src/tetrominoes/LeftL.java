package tetrominoes;

import javafx.scene.paint.Color;

public class LeftL extends Block {
	
	private static Color color = Color.ORANGE;
	//[change this index for rotations][row][column]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(),new Tile(color)},
			{new Tile(),new Tile(color)},
			{new Tile(color),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(color),new Tile(color),new Tile(color)},
			{new Tile(),new Tile(),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(color),new Tile(color)},
			{new Tile(color),new Tile()},
			{new Tile(color),new Tile()},
		},
		new Tile [][]{
			{new Tile(color),new Tile(),new Tile()},
			{new Tile(color),new Tile(color),new Tile(color)},
		}
	};
	
	
	public LeftL() {
		super(configurations);
		
		if (super.debug){
			System.out.println("made left L");
		}
	}

	//i feel like there should be a better way to do this
	public Tile[][] getLeftLShape(){
		return getShape();
	}
}
