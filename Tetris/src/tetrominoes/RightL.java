package tetrominoes;

import javafx.scene.paint.Color;

public class RightL extends Block {
	
	private static Color color = Color.YELLOW;
	//[change this index for rotations][x coordinate][y coordinate]
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
		super(configurations);
		if (super.debug){
			System.out.println("made right L");
		}
	}
	
	public Tile[][] getRightLShape(){
		return getShape();
	}

}
