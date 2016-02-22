package tetrominoes;

public class RightS extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, true, 	false,	false},
		{false,	true,	true,	false},
		{false,	false,	true,	false},
		{false,	false,	false,	false}
	};

	public RightS() {
		super(SHAPE);
		// TODO Auto-generated constructor stub
	}

}
