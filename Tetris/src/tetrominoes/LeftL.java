package tetrominoes;

public class LeftL extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, false, 	true,	false},
		{false,	false,	true,	false},
		{false,	true,	true,	false},
		{false,	false,	false,	false}
	};

	public LeftL() {
		super(SHAPE);
		// TODO Auto-generated constructor stub
	}

}
