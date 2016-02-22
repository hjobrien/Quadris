package engine;

import javafx.animation.AnimationTimer;

public class Engine extends AnimationTimer {

	public Engine(boolean[][] boardState){
		
	}
	
	
	@Override
	public void handle(long arg0) {
		//makes the update only happen every second
		//the update rate should increase as a function of score, but thats a (much) later feature
		if((arg0 % 1e9) == 0){
			update();
		}
	}

	//do collision detection here I think
	private void update() {
		// TODO Auto-generated method stub
		
	}

}
