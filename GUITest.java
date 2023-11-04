import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.net.http.WebSocket.Listener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import static javax.swing.SwingUtilities.getRootPane;

/* "View" for Battleship in MVC design 
 *  Authors: Sean Berndlmaier and John Meyers
 *  Date: 10/19/2023
 *  
 *  
 */

class GUITest{
    JFrame frame;   // main frame
    JPanel myPanel; // panel for user 
    JPanel oppPanel;    // panel to display opponents board
    JPanel myShips; // ships to be dragged and dropped 
    JPanel controls;    // controls such as random placement button, clear board button

    Font buttonFont;
    //Creation of JLabel ships to be added to myShips panel
    JLabel carrier = new JLabel();
    JLabel battleship = new JLabel();       
    JLabel cruiser = new JLabel();
    JLabel submarine = new JLabel();
    JLabel destroyer = new JLabel();
    JButton[][] oppBoardView = new JButton[10][10];     // JButton double array for Opponent board, used for attack turn 
    JLabel[][] playerBoardView = new JLabel [10][10];   // User board holds players ships (locations), and opponents fired shots
    JLabel turn;    // keep track of who's turn it is
    JButton randPlace;  // randomPlacement button for ships
    JButton lockPlace;  // lock ships onto board 
    JLabel pointsLabel;    // label for points/score of opponent
    JLabel playerPointsLabel;   // label for Player points
    int points = 0;

    // Constructor
    public GUITest(){
        turn = new JLabel();    
        turn.setForeground(Color.white);

        pointsLabel = new JLabel("Opponent Points: " + points);
        pointsLabel.setForeground(Color.white);

        playerPointsLabel = new JLabel("My Points: " + points);
        playerPointsLabel.setForeground(Color.white);

        randPlace = new JButton("Random Placement");
        randPlace.setBackground(Color.GREEN);
    
        lockPlace = new JButton("Clear Board!");
        lockPlace.setBackground(Color.RED);

        buttonFont = new Font("verdana", Font.PLAIN, 40);
        randPlace.setFont(buttonFont);
        lockPlace.setFont(buttonFont);
        turn.setFont(buttonFont);
        pointsLabel.setFont(buttonFont);
        playerPointsLabel.setFont(buttonFont);
        frame = new JFrame();
        myPanel = new JPanel();
        oppPanel = new JPanel();
        controls = new JPanel();
        myShips = new JPanel();

        frame.setLayout(new GridLayout(2,2));
        frame.setBackground(new Color(0,0,0));

        myPanel.setLayout(new GridLayout(10,10));   // set to grid layout for proper battleship design
        myPanel.setBackground(new Color(51,204,255));
        String title = "My Board";
        Border border = BorderFactory.createTitledBorder(title);
        Border border2 = BorderFactory.createLineBorder(new Color(102,102,102), 15);
        Border borderf = BorderFactory.createCompoundBorder(border2,border);
        myPanel.setBorder(borderf);

        oppPanel.setLayout(new GridLayout(10,10));  // set to grid layout for proper game board
        oppPanel.setBackground(new Color(51,204,255));  
        String title2 = "Opponents Board:";
        Border b = BorderFactory.createTitledBorder(title2);
        Border bf =  BorderFactory.createCompoundBorder(border2,b);
        oppPanel.setBorder(bf);

        controls.setLayout(new GridLayout(5,1));
        controls.setBackground(new Color(25,25,25));
       
        // add buttons and labels to controls panel
        controls.add(turn);
        controls.add(pointsLabel);
        controls.add(playerPointsLabel);
        controls.add(randPlace);
        controls.add(lockPlace);

        myShips.setLayout(new GridLayout(2,3));
        myShips.setBackground(new Color(25,25,25));

        /* Below we initialize all 5 ships for the game with imageIcons and add them to myShips panel
         *  carrier, battleship, cruiser, submarine, and destroyer
         */
        carrier.setIcon(new ImageIcon("shipImages/v_five.png"));
        myShips.add(carrier);
     
        battleship.setIcon(new ImageIcon("shipImages/v_four.png"));
        myShips.add(battleship);


        cruiser.setIcon(new ImageIcon("shipImages/v_threetwo.png"));
        myShips.add(cruiser);

        submarine.setIcon(new ImageIcon("shipImages/v_three.png"));
        myShips.add(submarine);


        destroyer.setIcon(new ImageIcon("shipImages/v_two.png"));
        myShips.add(destroyer);

        
        getRootPane(frame).setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        
        JLabel boardLabel = new JLabel();

        // Nest for loop to initialize new Board Label and assign it to the spots of playerBoardView
        // We do this to add transfer handler properly to labels for Drag and Drop functionality 
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                boardLabel = new JLabel();
		        boardLabel.setTransferHandler(new TransferHandler("icon"));
                playerBoardView[row][col] =boardLabel;
                oppBoardView[row][col] = new JButton();
                playerBoardView[row][col].setName(row + "" + col);
                oppBoardView[row][col].setName(row+""+col);
                myPanel.add(playerBoardView[row][col]);
                oppPanel.add(oppBoardView[row][col]);

                playerBoardView[row][col].setBorder(BorderFactory.createLineBorder(new Color(102,102,102), 1));
                playerBoardView[row][col].setIcon((new ImageIcon(new ImageIcon("replaceImages/trans.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH))));
                oppBoardView[row][col].setBorder(BorderFactory.createLineBorder(new Color(102,102,102), 1));
                oppBoardView[row][col].setBackground(new Color(51,204,255));

            }
        }

        
        // add all custom Panels to mainFrame
        frame.add(oppPanel);
        frame.add(controls);
        frame.add(myPanel);
        frame.add(myShips);

        // format frame to our game standards
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setMaximumSize(new Dimension(800, 800));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);

    }
    
    // getter return playerBoardView
    public JLabel[][] getMyGrid()
    {
        return playerBoardView;
    }

    // getter oppBoardView
    public JButton[][] getOppGrid(){
        return oppBoardView;
    }

    // setter OppBoardView 
    public void setOppGrid(JButton[][] g){
        oppBoardView = g;
    }

    // setter: update playerBoardView with passed argument board
    public void setMyGrid(JLabel[][] g)
    {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoardView[i][j] = g[i][j];
            }
        }
    }

    // Remove carrier from myShips
    public void removeCarrier(){
        myShips.remove(carrier);
        myShips.revalidate();
        myShips.repaint();
    }

    // Remove destroyer from myShips
    public void removeDestroyer(){
        myShips.remove(destroyer);
        myShips.revalidate();
        myShips.repaint();
    }

    // Remove submarine from myShips
    public void removeSubmarine(){
        myShips.remove(submarine);
        myShips.revalidate();
        myShips.repaint();
    }

    // Remove battleship from myShips
    public void removeBattleship(){
        myShips.remove(battleship);
        myShips.revalidate();
        myShips.repaint();
    }

    // Remove cruiser from myShips
    public void removeCruiser(){
        myShips.remove(cruiser);
        myShips.revalidate();
        myShips.repaint();
    }

    // Mouse Listener for ships on myShips panel : allows for drag and drop
    public void setMouseListener(MouseListener l){
        carrier.addMouseListener(l);
		carrier.setTransferHandler(new TransferHandler("icon"));

        battleship.addMouseListener(l);
		battleship.setTransferHandler(new TransferHandler("icon"));

        cruiser.addMouseListener(l);
		cruiser.setTransferHandler(new TransferHandler("icon"));

        submarine.addMouseListener(l);
		submarine.setTransferHandler(new TransferHandler("icon"));

        destroyer.addMouseListener(l);
		destroyer.setTransferHandler(new TransferHandler("icon"));
    }

    // setter for action listeners
    public void setL(ActionListener l)
    {
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                oppBoardView[row][col].addActionListener(l);

        }}
    }
    
    // add action listener to lock button
    public void setLock(ActionListener l){
        lockPlace.addActionListener(l);
    }
    
    // add action listener to random placement button
    public void setRandomListen(ActionListener l){

        randPlace.addActionListener(l);
    }

    // setter for turn 
    public void setTurn(String x)
    {
        turn.setText(x);
    }

    // display message when there is a winner
    public void displayWin(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    // display points for opponent
    public void displayPoints(){
        pointsLabel.setText("Opponent Points: " + points);
    }

    // display points for player
    public void displayPlayerPoints(int p){
        playerPointsLabel.setText("My Points: " + p);
    }

    // if a ship is hit, change appearance accordingly and play sound
    public void changeHit(int x, int y) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        points++;
        displayPoints();
        playerBoardView[x][y].setIcon(new ImageIcon(new ImageIcon("replaceImages/Explosion_0.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        AudioInputStream audioInputStream;
        Clip clip;
        audioInputStream =  AudioSystem.getAudioInputStream(new File("sounds/mixkit-arcade-game-explosion-1699.wav").getAbsoluteFile());
        clip = AudioSystem.getClip(); 
        clip.open(audioInputStream); 
        clip.start();
    }

    // if ship is missed, change appearance accordingly and play sound
    public void changeMiss(int x, int y) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        playerBoardView[x][y].setIcon(new ImageIcon(new ImageIcon("replaceImages/Solid_white.svg.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        AudioInputStream audioInputStream;
        Clip clip;
        audioInputStream =  AudioSystem.getAudioInputStream(new File("sounds/mixkit-water-splash-1311.wav").getAbsoluteFile());
        clip = AudioSystem.getClip(); 
        clip.open(audioInputStream); 
        clip.start();
    }

    // clear the board of all ships, hits, misses, etc
    public void clearViewBoard(){
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                playerBoardView[row][col].setIcon(new ImageIcon("/replaceImages/trans.png"));
            }
        }
        removeBattleship();
        removeCarrier();
        removeCruiser();
        removeDestroyer();
        removeSubmarine();
        myShips.revalidate();
        myShips.repaint();
        myPanel.revalidate();
        myPanel.repaint();
    
    }

    // clear player board
     public void clearViewBoardAdd(){
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                playerBoardView[row][col].setIcon(new ImageIcon("/replaceImages/trans.png"));
            }
        }
        
        
        myShips.add(submarine);
        myShips.add(battleship);
        myShips.add(carrier);
        myShips.add(cruiser);
        myShips.add(destroyer);
        myShips.revalidate();
        myShips.repaint();
    
    }

    // getter for myShips panel
    public JPanel getShipsPanel()
    {
        return myShips;
    }

    // setter for myShips panel
    public void setShipsPanel(JPanel p)
    {
        myShips = p;
    }

    
}