package mainGame;

import java.util.ArrayList;

import tetrominoes.Block;
import tetrominoes.Tile;

public class Board {
	private ArrayList<ArrayList<Tile>> boardState;
	private Block fallingBlock;
	public boolean blockAdded = false;
	
	//for debugging
	private boolean debug = true;
	
	public ArrayList<ArrayList<Tile>> getBoardState(){
		return this.boardState;
	}
	
	public Board(int height, int width){
		ArrayList<ArrayList<Tile>> tempBoard = new ArrayList<ArrayList<Tile>>();
		for(int i = 0; i < height; i++){
			ArrayList<Tile> temp = new ArrayList<Tile>();
			for(int j = 0; j < width; j++){
				temp.add(new Tile(false, false));			//not active, not filled
			}
			tempBoard.add(temp);
		}
		this.boardState = tempBoard;
	}
	
	
	//adds a block near the top of the screen
	//might need to be altered for when the stack gets very high
	public void updateBoardWithNewBlock(Block b){
		ArrayList<ArrayList<Tile>> blockShape = b.getShape();
		for (int i = 0; i < blockShape.size(); i++){
			for (int j = 0; j < blockShape.get(i).size(); j++){
				if (!tileAt(i, j+3).isFilled()){
					update(i, j+3, blockShape.get(i).get(j));
				}
			}
		}
	}
	
	//changes the fields of the tiles in Board based on the tile passed in
	public void update(int i, int j, Tile t){
		this.boardState.get(i).get(j).setActive(t.isActive());
		this.boardState.get(i).get(j).setFilled(t.isFilled());
	}
	
	public Tile tileAt(int i, int j){
		return this.boardState.get(i).get(j);
	}
	
	public void display(){
		for (int i = 0; i < this.boardState.size(); i++){
			for (int j = 0; j < this.boardState.get(i).size(); j++){
				System.out.print(this.boardState.get(i).get(j).isFilled());
			}
			System.out.println();
		}
	}
	
	public Block getFallingBlock(){
		return fallingBlock;
	}

	public void setFallingBlock(Block fallingBlock) {
		this.fallingBlock = fallingBlock;
	}

	public boolean checkBlockSpace() {
		//checks if the block has traversed the whole screen
		int lastIndex = boardState.size() - 1;
		for (int i = 0; i < boardState.get(lastIndex).size(); i++){
			if (tileAt(lastIndex, i).isActive()){
				if (debug){
					System.out.println("block has space beneath = false");
				}
				return false;
			}
		}
		
		//checks if there is an inactive tile (block that already fell) below
		for (int i = 0; i < boardState.size(); i++){
			for (int j = 0; j < boardState.get(i).size(); j++){
				Tile thisT = boardState.get(i).get(j);
				if (thisT.isActive()){
					Tile nextT = boardState.get(i + 1).get(j);
					if (nextT.isFilled() && !nextT.isActive()){
						if (debug){
							System.out.println("block has space beneath = false");
						}
						return false;
					}
				}
			}
		}
		if (debug){
			System.out.println("block has space beneath = true");
		}
		return true;
	}

	//moves all the blocks down 1 row
	public void blockDown() {
		for (int i = boardState.size() - 1; i >= 0; i--){
			for (int j = boardState.get(i).size() - 1; j >= 0; j--){
				if (tileAt(i, j).isActive()){
					//updates new tile
					update(i + 1, j, tileAt(i ,j));
					//clears old tile
					update(i, j, new Tile(false, false));
				}
			}
		}
	}

	//goes over the whole grid and sets each tile to not falling
	public void setNotFalling() {
		for (ArrayList<Tile> list : boardState){
			for (Tile t : list){
				t.setActive(false);
			}
		}
	}	
}
