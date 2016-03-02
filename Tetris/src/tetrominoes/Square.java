package tetrominoes;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class Square extends Block {

	private static Color color = Color.RED;

	
	public Square() {
		super(getSquareShape());
		if (super.debug){
			System.out.println("made square");
		}
	}
	
	public static ArrayList<ArrayList<Tile>> getSquareShape(){
		ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();

		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(false, false));
		secondLine.add(new Tile(true, true, color));
		secondLine.add(new Tile(true, true, color));
		secondLine.add(new Tile(false, false));
		
		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(true, true, color));
		thirdLine.add(new Tile(true, true, color));
		thirdLine.add(new Tile(false, false));

		shape.add(secondLine);
		shape.add(thirdLine);

		return shape;
	}
	
}
