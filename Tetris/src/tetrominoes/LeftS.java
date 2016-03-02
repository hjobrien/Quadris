package tetrominoes;

import java.util.ArrayList;

public class LeftS extends Block{
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, false, 	true,	false},
		{false,	true,	true,	false},
		{false,	true,	false,	false},
		{false,	false,	false,	false}
	};

	public LeftS() {
		super(getLeftSShape());
		if (super.debug){
			System.out.println("made left S");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getLeftSShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> firstLine = new ArrayList<Tile>();
		firstLine.add(new Tile(false, false));
		firstLine.add(new Tile(true, true));
		
		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(true, true));
		secondLine.add(new Tile(true, true));
		
		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(true, true));
		thirdLine.add(new Tile(false, false));

		shape.add(firstLine);
		shape.add(secondLine);
		shape.add(thirdLine);

		return shape;
	}

}
