package tetrominoes;

import javafx.scene.paint.Color;

public class TBlock extends Block {
		
	private static Color color = Color.AQUAMARINE;
	//[change this index for rotations][x coordinate][y coordinate]
	private static Tile[][][] configurations = new Tile[][][]{
		new Tile [][]{
			{new Tile(color),new Tile(color),new Tile(color)},
		    {new Tile(), new Tile(color), new Tile()},
		},
		new Tile [][]{
			{new Tile(color),new Tile()},
		    {new Tile(color), new Tile(color)},
		    {new Tile(color), new Tile()},
		},
		new Tile [][]{
			{new Tile(),new Tile(color),new Tile()},
			{new Tile(color),new Tile(color),new Tile(color)},
		},
		new Tile [][]{
			{new Tile(),new Tile(color)},
		    {new Tile(color), new Tile(color)},
		    {new Tile(), new Tile(color)},
		}
	};
	
	public TBlock() {
		super(configurations);
		if (super.debug){
			System.out.println("made T");
		}
	}
	
	public Tile[][] getTShape(){
		return getShape();
	}

}
