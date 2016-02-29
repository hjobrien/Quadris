package tetrominoes;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class LeftL extends Block {
	
	private static Color color = Color.ORANGE;
	
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
		firstLine.add(new Tile(false, false));
		firstLine.add(new Tile(true, true, color));
		firstLine.add(new Tile(false, false));
		
		ArrayList<Tile> secondLine = new ArrayList<Tile>();
		secondLine.add(new Tile(false, false));
		secondLine.add(new Tile(false, false));
		secondLine.add(new Tile(true, true, color));
		secondLine.add(new Tile(false, false));
		
		ArrayList<Tile> thirdLine = new ArrayList<Tile>();
		thirdLine.add(new Tile(false, false));
		thirdLine.add(new Tile(true, true, color));
		thirdLine.add(new Tile(true, true, color));
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
