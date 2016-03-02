package tetrominoes;

import java.util.ArrayList;

public class LeftL extends Block {
	
	public LeftL() {
		super(getLeftLShape());
		if (super.debug){
			System.out.println("made left L");
		}
	}

	//i feel like there should be a better way to do this
	public static ArrayList<ArrayList<Tile>> getLeftLShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> firstLine = new ArrayList<Tile>();
		firstLine.add(new Tile(false, false));
		firstLine.add(new Tile(true, true));
		
		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(false, false));
		secondLine.add(new Tile(true, true));
		
		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(true, true));
		thirdLine.add(new Tile(true, true));

		shape.add(firstLine);
		shape.add(secondLine);
		shape.add(thirdLine);

		return shape;
	}
}
