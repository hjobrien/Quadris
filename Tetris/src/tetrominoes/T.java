package tetrominoes;

public class T extends Block {
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, false, 	false,	false},
		{true,	true,	true,	false},
		{false,	true,	false,	false},
		{false,	false,	false,	false}
	};

	public T() {
		super(SHAPE);
		// TODO Auto-generated constructor stub
	}

}
