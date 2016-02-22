package tetrominoes;

public class Square extends Block {

	public static final boolean[][] SHAPE = new boolean[][]{
		{false, false, false ,false},
		{false,true,true,false},
		{false,true,true,false},
		{false,false,false,false}
	};
	
	public Square() {
		super(SHAPE);
		// TODO Auto-generated constructor stub
	}
	
}
