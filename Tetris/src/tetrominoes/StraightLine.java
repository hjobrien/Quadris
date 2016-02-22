package tetrominoes;

public class StraightLine extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{true, 	true, 	true,	true},
		{false,	false,	false,	false},
		{false,	false,	false,	false},
		{false,	false,	false,	false}
	};

	public StraightLine() {
		super(SHAPE);
		// TODO Auto-generated constructor stub
	}

}
