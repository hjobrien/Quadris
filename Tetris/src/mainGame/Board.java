package mainGame;

import javafx.scene.canvas.GraphicsContext;

public class Board {

	private boolean[][] board;
	
	public boolean[][] getBoard(){
		return this.board;
	}
	
	public Board(int height, int width){
		this.board = new boolean[height][width];
	}
	
	public void update(int i, int j, boolean b){
		this.board[i][j] = b;
	}
	
	public boolean valueOf(int i, int j){
		return this.board[i][j];
	}
	
	public void display(){
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
}
