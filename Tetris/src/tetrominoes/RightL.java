package tetrominoes;

import java.util.ArrayList;

public class RightL extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, true, 	false,	false},
		{false,	true,	false,	false},
		{false,	true,	true,	false},
		{false,	false,	false,	false}
	};

	public RightL() {
		super(getRightLShape());
		if (super.debug){
			System.out.println("made right L");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getRightLShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> firstLine = new ArrayList<Tile>();
		firstLine.add(new Tile(true, true));
		firstLine.add(new Tile(false, false));
		
		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(true, true));
		secondLine.add(new Tile(false, false));
		
		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(true, true));
		thirdLine.add(new Tile(true, true));

		shape.add(firstLine);
		shape.add(secondLine);
		shape.add(thirdLine);

		return shape;
	}

}
