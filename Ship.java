
/* Authors: John Meyers and Sean Berndlmaier
 * Date: 10/19/2023
 * 	Ship class for battleship
 */

public class Ship {
	private int size;
	boolean isVert = false;
	//int shipToken = 1;
	public Ship(int x) {
		size = x;
	}
	public int getSize(){
		return size;
	}
}
