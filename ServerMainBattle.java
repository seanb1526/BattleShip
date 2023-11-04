import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/* Authors: Sean Berndlmaier and John Meyers
 * Date: 10/19/2023
 * 
 * Run this to create server for Battleship. Also acts as a player 
 */
public class ServerMainBattle {

	public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException {
      GUITest v = new GUITest();
      BattleShipModel m = new BattleShipModel();
      BattleController2 b = new BattleController2(m, v);
      
      

}
}