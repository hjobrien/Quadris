package engine;

public enum BlockType {
	LEFT_L(0),				//0
	LEFT_S(1),				//1
	RIGHT_L(2),				//2
	RIGHT_S(3),				//3
	SQUARE(4),				//4
	LINE(5),				//5
	T_BLOCK(6);				//6
	
	private final int value;

    private BlockType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}