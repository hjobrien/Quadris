package mainGame;

public enum Move {
	RIGHT(0),
	LEFT(1),
	DOWN(2),
	DROP(3),
	ROT_RIGHT(4),
	ROT_LEFT(5),
	UP(6);
	
	private final int value;

    private Move(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
