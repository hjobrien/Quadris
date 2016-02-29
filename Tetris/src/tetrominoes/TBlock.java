package tetrominoes;

import java.util.ArrayList;

public class TBlock extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, false, 	false,	false},
		{true,	true,	true,	false},
		{false,	true,	false,	false},
		{false,	false,	false,	false}
	};

	public TBlock() {
		super(getTShape());
		if (super.debug){
			System.out.println("made T");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getTShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> firstLine = new ArrayList<Tile>();
		firstLine.add(new Tile(false, false));
		firstLine.add(new Tile(false, false));
		firstLine.add(new Tile(false, false));
		firstLine.add(new Tile(false, false));

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
