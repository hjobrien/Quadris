package tetrominoes;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class StraightLine extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{true, 	true, 	true,	true},
		{false,	false,	false,	false},
		{false,	false,	false,	false},
		{false,	false,	false,	false}
	};

	private static Color color = Color.PURPLE;

	
	public StraightLine() {
		super(getStraightLineShape());
		if (super.debug){
			System.out.println("made straight line");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getStraightLineShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(true, true, color));
		secondLine.add(new Tile(true, true, color));
		secondLine.add(new Tile(true, true, color));
		secondLine.add(new Tile(true, true, color));

		shape.add(secondLine);

		return shape;
	}

}
