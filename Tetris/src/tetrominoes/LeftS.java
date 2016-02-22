package tetrominoes;

public class LeftS extends Block{
	
	public static final boolean[][] SHAPE = new boolean[][]{
		{false, false, 	true,	false},
		{false,	true,	true,	false},
		{false,	true,	false,	false},
		{false,	false,	false,	false}
	};

	public LeftS() {
		super(SHAPE);
		// TODO Auto-generated constructor stub
	}

}
