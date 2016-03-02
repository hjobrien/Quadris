package tetrominoes;

import java.util.ArrayList;

public class T extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, false, 	false,	false},
		{true,	true,	true,	false},
		{false,	true,	false,	false},
		{false,	false,	false,	false}
	};

	public T() {
		super(getTShape());
		if (super.debug){
			System.out.println("made T");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getTShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();

		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(true, true));
		secondLine.add(new Tile(true, true));
		secondLine.add(new Tile(true, true));
		secondLine.add(new Tile(false, false));
		
		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(true, true));
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(false, false));

		shape.add(secondLine);
		shape.add(thirdLine);

		return shape;
	}

}
