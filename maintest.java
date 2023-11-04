import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/* Authors: John Meyers and Sean Berndlmaier
 * Date: 10/19/2023
 * 
 * This is our Client Class. When ServerMainBattle is run, the server is created
 * then this program should be run to connect to the server and begin the game.
 * 
 */
public class maintest {

	public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException {
      GUITest v = new GUITest();
      BattleShipModel m = new BattleShipModel();
      BattleController b = new BattleController(m, v);
		
      

}
}
