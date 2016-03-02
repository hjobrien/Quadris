package tetrominoes;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class RightS extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, true, 	false,	false},
		{false,	true,	true,	false},
		{false,	false,	true,	false},
		{false,	false,	false,	false}
	};

	private static Color color = Color.BLUE;

	
	public RightS() {
		super(getRightSShape());
		if (super.debug){
			System.out.println("made Right S");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getRightSShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> firstLine = new ArrayList<Tile>();
		firstLine.add(new Tile(true, true, color));
		firstLine.add(new Tile(false, false));
		
		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(true, true, color));
		secondLine.add(new Tile(true, true, color));
		
		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(true, true, color));
		thirdLine.add(new Tile(true, true));

		shape.add(firstLine);
		shape.add(secondLine);
		shape.add(thirdLine);

		return shape;
	}

}
