package tetrominoes;

public class Line extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{true, 	true, 	true,	true},
		{false,	false,	false,	false},
		{false,	false,	false,	false},
		{false,	false,	false,	false}
	};

	public Line() {
		super(SHAPE);
		// TODO Auto-generated constructor stub
	}

}
