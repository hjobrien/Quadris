package tetrominoes;

import java.util.ArrayList;

public class StraightLine extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{true, 	true, 	true,	true},
		{false,	false,	false,	false},
		{false,	false,	false,	false},
		{false,	false,	false,	false}
	};

	public StraightLine() {
		super(getStraightLineShape());
		if (super.debug){
			System.out.println("made straight line");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getStraightLineShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> firstLine = new ArrayList<Tile>();
		firstLine.add(new Tile(true, true));
		firstLine.add(new Tile(true, true));
		firstLine.add(new Tile(true, true));
		firstLine.add(new Tile(true, true));
		
		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(false, false));
		secondLine.add(new Tile(false, false));
		secondLine.add(new Tile(false, false));
		secondLine.add(new Tile(false, false));

		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(false, false));
		
		ArrayList<Tile> fourthLine = new ArrayList<Tile>();
		fourthLine.add(new Tile(false, false));
		fourthLine.add(new Tile(false, false));
		fourthLine.add(new Tile(false, false));
		fourthLine.add(new Tile(false, false));

		shape.add(firstLine);
		shape.add(secondLine);
		shape.add(thirdLine);
		shape.add(fourthLine);

		return shape;
	}

}
