import java.awt.Color;

import javax.swing.JButton;

/*
 * Battleship Model for MVC design
 * Authors: John Meyers and Sean Berndlmaier
 * Date: 10/19/2023
 * 
 */

public class BattleShipModel {
	
	private int[][] playerBoard = new int[10][10];	// model int board for playerBoard from view
	private int[][] oppBoard = new int[10][10];		// model int board for oppBoard from view
	Ship[] ship = new Ship[5];	// array of ships 
	private int shipcoordsX;	
	private int shipcoordsY;
	private int myShips = 17;
	private int hitNum = 0;
	private String turn = "client";
	private int count = 0;
	
	// Constructor
	public BattleShipModel(){
		for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				oppBoard[row][col] = 0;
				playerBoard[row][col] = 0;
			}
		}
		// create and add ships to array 
		displayBoard(playerBoard);
		ship[0] = new Ship(5);
		ship[1] = new Ship(4);
		ship[2] = new Ship(3);
		ship[3] = new Ship(3);
		ship[4] = new Ship(2);
		
	}

	//check board for hit 
	public boolean checkHit(int x, int y){
		
		boolean hit = false;
		if(playerBoard[x][y] == 1){
			hit = true;
			myShips--;
		}
		return hit;
	}

	// check if a user has won the game
	public boolean isWin(){
		if(myShips == 0){
			return true;
		}
		return false;
	}

	// see if a turn was a hit 
	public boolean recieveHit(String check){
		int hit = Integer.parseInt(check);
		if(hit == 0){
			return false;
		}
		else if(hit == 1){
			return true;
		}
		else{
			return false;
		}
		
	}

	// call random function that places ships randomly on board
	public void callRandom() {
		for(int i = 0; i < ship.length; i++) {
			placeRandom(ship[i]);
		}
		displayBoard(playerBoard);
	}

	// place ships randomly on board, avoiding collision and staying in bounds
	public void placeRandom(Ship ship)
    {
        
            boolean collides = true;
            int horiz = (int)(Math.random() * 2);
            int shipRow, shipCol;
            boolean horizontal = (horiz == 1) ? true : false;
            do{
                if(horizontal){
                    shipCol = (int)(Math.random() * (9 - ship.getSize()+1 + 1));
                    shipRow = (int)(Math.random() * (9 + 1));
                }
                else{
                    shipCol = (int)(Math.random() * (9 + 1));
                    shipRow = (int)(Math.random() * (9-ship.getSize()+1 + 1));
                }
              collides = placeShip(shipRow, shipCol, horizontal, ship);
            }
            while(!collides);
        
    }

	// place a ship on the board
    public boolean placeShip( int row, int col, boolean horizontal, Ship ship)
    {
        int length = ship.getSize();
        int iter = horizontal ? col : row;

        System.out.println(horizontal);

        for (int i = iter; i < iter+length; i++) {
            if(horizontal) {
                if(playerBoard[row][i] == 1) return false;}
            else {
                if(playerBoard[i][col] == 1) return false; }
        }

        for(int i = iter; i < iter + length; i++)
        {
            if(horizontal)
            {
                playerBoard[row][i] = 1;
            }
            else
            {
                playerBoard[i][col] = 1;
            }
        }
        displayBoard(playerBoard);
		return true;
		
    }




	// display the gameBoard
		public void displayBoard(int gameBoard[][]) {
			for(int row = 0; row < 10; row++) {
				for(int col = 0; col < 10; col++) {
					System.out.print(gameBoard[row][col]);
				}
				System.out.println();
			}
		}

		// set color of buttons on board
		public void setColor( JButton[][] Buttonarr){
			for(int row = 0; row < 10; row++){
				for(int col = 0; col < 10; col++){
					if(playerBoard[row][col] == 1){
						Buttonarr[row][col].setBackground(Color.GREEN);
					}
				}
			}
		}

		// display the playerBoard
		public void displayPlayerBoard() {
			for(int row = 0; row < 10; row++) {
				for(int col = 0; col < 10; col++) {
					System.out.print(playerBoard[row][col]);
				}
				System.out.println();
			}
		}

		 // clear the board of ships (clear it of int markers)
		public void clearBoard(){
			for(int row = 0; row < 10; row++) {
				for(int col = 0; col < 10; col++) {
					playerBoard[row][col] = 0;
				}
				
			}
		}

		//getter for playerBoard
		public int[][] getBoard(){
			return playerBoard;
		}

		// getter for ship
		public Ship[] getShips(){
			return ship;
		}

		// setter for grid posistion 
		public void setGridPos(int x, int y, int val)
		{ 
			playerBoard[x][y] = val;
		}

		// getter for position of ship on playerBoard
		public int getPos(int x, int y){
			return playerBoard[x][y];
		}

		//get the turn
		public String getTurn()
		{
			return turn;
		}

		// set the turn
		public void setTurn(String t)
		{
			turn = t;
		}

		// increase count
		public void incrementCount(){
			count++;
		}

		//decrease count
		public void decrementCount(){
			count--;
		}
		
}

