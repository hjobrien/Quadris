package clients.interfaces;

public interface Autoplayable {
	
	// Board Weight constants
	// negative means its a bad thing being weighted (overall board height)
	// positive means its a good thing (full lines);

	static final double HEIGHT_WEIGHT = -70;
	static final double VOID_WEIGHT = -97.85;
	static final double EDGE_WEIGHT = 5;
	static final double ONE_LINE_WEIGHT = 50;
	static final double TWO_LINES_WEIGHT = 150;
	static final double THREE_LINES_WEIGHT = 350;
	static final double FOUR_LINES_WEIGHT = 1000;
	
  public static final double[] DEFAULT_WEIGHTS = new double[] {HEIGHT_WEIGHT, VOID_WEIGHT, EDGE_WEIGHT, 
		  ONE_LINE_WEIGHT, TWO_LINES_WEIGHT, THREE_LINES_WEIGHT, FOUR_LINES_WEIGHT};

}
