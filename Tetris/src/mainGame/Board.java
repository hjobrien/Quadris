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
	
	
	//TO-DO
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
	
	//we might not want to change tiles, just the tile's fields
	public void update(int i, int j, Tile t){
		this.boardState.get(i).get(j).setActive(t.isActive());
		this.boardState.get(i).get(j).setFilled(t.isFilled());
	}
	
	//might not need these
//	public void updateActive(int i, int j, boolean b){
//		this.boardState.get(i).get(j).setActive(b);;
//	}
//	
//	public void updateFilled(int i, int j, boolean b){
//		this.boardState.get(i).get(j).setFilled(b);;
//	}
	
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
	
//	public void add(Block b){
//		blockAdded = true;
//		setFallingBlock(b);
//	}
	
	//could have some checker that makes sure the falling block is still falling
	//and if it isn't, a new one can be generated
	public Block getFallingBlock(){
		return fallingBlock;
	}

	public void setFallingBlock(Block fallingBlock) {
		this.fallingBlock = fallingBlock;
	}

	
//	//TO-DO
	public boolean checkBlockSpace() {
		int lastIndex = boardState.size() - 1;
		for (int i = 0; i < boardState.get(lastIndex).size(); i++){
			if (tileAt(lastIndex, i).isActive()){
				if (debug){
					System.out.println("block has space beneath = true");
				}
				return false;
			}
		}
		
		for (int i = 0; i < boardState.size(); i++){
			for (int j = 0; j < boardState.get(i).size(); j++){
				Tile thisT = boardState.get(i).get(j);
				if (thisT.isActive()){
					Tile nextT = boardState.get(i + 1).get(j);
					if (nextT.isFilled() && !nextT.isActive()){
						if (debug){
							System.out.println("block has space beneath = true");
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

	
	//still in development
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

	public void setNotFalling() {
		for (ArrayList<Tile> list : boardState){
			for (Tile t : list){
				t.setActive(false);
			}
		}
	}	
}
