import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.awt.event.MouseEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.TransferHandler;


/*  Controller for Battleship using MVC design
 * Authors: John Meyers and Sean Berndlmaier
 * Date: 10/19/2023
 */

public class BattleController{
    private BattleShipModel model;
    private GUITest view;
    private JButton[][] oppGridDis;
    Client application;
    String message = "";
    int myShips = 17;
    int points = 0;
    MouseListener listener = new MouseAdapter() {
				public void mousePressed(MouseEvent e)
				{
                    // when left click
                    JLabel c = (JLabel) e.getSource();
                    if(e.getButton() == MouseEvent.BUTTON1) {
                        
                    }
                    // when right click on ship, "rotate ship" -> change it to opposite direction png h<->v
                    if(e.getButton() == MouseEvent.BUTTON3) {
                        
                        if(c.getIcon().toString() == "shipImages/v_two.png")
                            c.setIcon(new ImageIcon("shipImages/h_two.png"));
                        else if(c.getIcon().toString() == "shipImages/v_three.png")
                            c.setIcon(new ImageIcon("shipImages/h_three.png"));
                        else if(c.getIcon().toString() == "shipImages/v_threetwo.png")
                            c.setIcon(new ImageIcon("shipImages/h_threetwo.png"));
                        else if(c.getIcon().toString() == "shipImages/v_four.png")
                            c.setIcon(new ImageIcon("shipImages/h_four.png"));
                        else if(c.getIcon().toString() == "shipImages/v_five.png")
                            c.setIcon(new ImageIcon("shipImages/h_five.png")); 
                        else if(c.getIcon().toString() == "shipImages/h_two.png")
                            c.setIcon(new ImageIcon("shipImages/v_two.png"));
                        else if(c.getIcon().toString() == "shipImages/h_three.png")
                            c.setIcon(new ImageIcon("shipImages/v_three.png"));
                        else if(c.getIcon().toString() == "shipImages/h_threetwo.png")
                            c.setIcon(new ImageIcon("shipImages/v_threetwo.png"));
                        else if(c.getIcon().toString() == "shipImages/h_four.png")
                            c.setIcon(new ImageIcon("shipImages/v_four.png"));
                        else if(c.getIcon().toString() == "shipImages/h_five.png")
                            c.setIcon(new ImageIcon("shipImages/v_five.png"));  
                    }
                    
					TransferHandler handler = c.getTransferHandler();
					handler.exportAsDrag(c, e, TransferHandler.COPY); // export copy of clicked component: Can we add a ship class object to the components?
				
                }
                
                public void mouseExited(MouseEvent x) {   
                   lookThrough();
                   
                   
                  
                }
                public void mouseDragged(MouseEvent d){
                
                }
			};
    public BattleController(BattleShipModel m , GUITest v) throws UnsupportedAudioFileException, LineUnavailableException
    {
        model = m;
        view = v;
        v.setTurn("My Turn!");
        v.setL(new ActionOnClick());
        v.setRandomListen(new RandomOnClick());
        v.setLock(new ClearOnClick());
        v.setMouseListener(listener);
        application = new Client("10.249.43.88");
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.runClient(); // run client application
        while(true){
            
            try {
               
                application.processConnection();
                view.randPlace.setEnabled(false);
                view.lockPlace.setEnabled(false);
                if(application.recieveMessage() == "Closing Connection Client Win!!"){
                    view.displayWin(application.recieveMessage());
                    application.closeConnection();
                    break;
                }
                
                waitForServer();
                

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        

            
          

        }
        //application.closeConnection();
        
    }
    
    public void waitForServer() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        // System.out.println("bruh1" + model.getTurn());
        if(model.getTurn() == "server"){
        String[] split2 = application.recieveMessage().split("");
        int x = Integer.parseInt(split2[0]);
        int y = Integer.parseInt(split2[1]);
        System.out.println(split2[0] + " " + split2[1]);
            System.out.println(x + " " + y);
        boolean hit = model.checkHit(x, y);
            System.out.println(hit);
        if(hit)
        {
            System.out.println("client sending 1");
            application.sendData("1");
            myShips--;
            view.changeHit(x, y);
            if(isWin()){
                application.sendData("Closing Connection Server Win!!");
                view.displayWin("Closing Connection Server Win!!");
                
            }
        }
        else
        {
            System.out.println("client sending 0");
            application.sendData("0");
            view.changeMiss(x, y);
        }
        unlockBoard();
        model.setTurn("client");
        view.setTurn("My turn!");
    }

    }
    public boolean isWin(){
		if(myShips == 0){
			return true;
		}
		return false;
	}

    public void lockBoard(){
        oppGridDis = view.getOppGrid();
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                oppGridDis[row][col].setEnabled(false);
            }
        }
        view.setOppGrid(oppGridDis);
    }
    public void unlockBoard(){
        oppGridDis = view.getOppGrid();
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                oppGridDis[row][col].setEnabled(true);
            }
        }
        view.setOppGrid(oppGridDis);
    }
    private class ClearOnClick implements ActionListener{
        public void actionPerformed( ActionEvent event )
	      {
            model.clearBoard();
            view.clearViewBoardAdd();
          }
        }
    private class ActionOnClick implements ActionListener{
        public void actionPerformed( ActionEvent event )
	      {
            lockBoard();
            JButton but = (JButton)event.getSource();
	    	if(model.getTurn() == "client"){
                application.sendData(but.getName());
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(model.recieveHit(application.recieveMessage())){
                    but.setBackground(Color.RED);
                    points++;
                    view.displayPlayerPoints(points);
                 }
                else{
                    but.setBackground(Color.white);
                } 
                
                model.setTurn("server");
                view.setTurn("Opponent's turn!");

            }
          }
    }
            


    private class RandomOnClick implements ActionListener{
        public void actionPerformed(ActionEvent e){
            model.clearBoard();
            view.clearViewBoard();
            //add code here to iterate through ships and add to map based on call random
            JLabel g[][] = view.getMyGrid();
            int play[][] = model.getBoard();
            Ship[] s = model.getShips();
            for(int i = 0; i <5; i++){
                placeRandomShip(s[i], g);
            }
           
            view.setMyGrid(g);
            
        }
    }
     public void placeRandomShip(Ship ship, JLabel[][] g) {
        boolean collides = true;
        int horiz = (int) (Math.random() * 2);
        int boardRow, boardCol;
        boolean horizontal = (horiz == 1) ? true : false;
        do {
            if (horizontal) {
                boardCol = (int) (Math.random() * (9 - ship.getSize() + 1));
                boardRow = (int) (Math.random() * (9 + 1));
            } else {
                boardCol = (int) (Math.random() * (9 + 1));
                boardRow = (int) (Math.random() * (9 - ship.getSize() + 1));
            }
            collides = placeShipRandom(boardRow, boardCol, horizontal, ship);
        }
        while (!collides);
        if (horizontal) {
            g[boardRow][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/h_left.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
            for (int i = 0; i < ship.getSize()-2; i++) {
                try {
                    g[boardRow][boardCol+i+1].setIcon(new ImageIcon(new ImageIcon("shipImages/h_middle.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    model.setGridPos(boardRow,boardCol, 1);

                } catch (Exception err) {
                    System.out.println("Couldn't set icon: " + err);
                }
            }
            g[boardRow][boardCol+ship.getSize()-1].setIcon(new ImageIcon(new ImageIcon("shipImages/h_right.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

        } else {
            g[boardRow][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/v_top.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

            for (int i = 0; i < ship.getSize()-2; i++) {
                try {
                        g[boardRow+i+1][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/v_middle.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
                        model.setGridPos(boardRow, boardCol, 1);

                } catch (Exception err) {
                    System.out.println("Couldn't set icon: " + err);
                }

            }
            g[boardRow+ship.getSize()-1][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/v_bottom.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

        }}

        boolean placeShipRandom(int row, int col, boolean horizontal, Ship ship)
    {
        int length = ship.getSize();
        int iter = horizontal ? col : row;

        // check if the ship will collide with any ships.
        for (int i = iter; i < iter+length; i++) {
            if(horizontal) {
                if(model.getPos(row, i) == 1) return false;}
            else {
                if(model.getPos(i, col) == 1) return false; }
        }

        //place the ship
        for (int i = iter; i < iter+length; i++) {
            if(horizontal) model.setGridPos(row, i, 1);
            else model.setGridPos(i, col, 1);
            model.incrementCount();
        }
        return true;
    
    }

    boolean placeShip(int row, int col, boolean horizontal, Ship ship)
    {
        int length = ship.getSize();
        int iter = horizontal ? col : row;

        // check if the ship will collide with any ships.
        System.out.println("Rwo: " + row + ", col: " + col);
        if(horizontal){
            if((col+ship.getSize()-1) < 10) {
                for (int i = iter; i < iter+length; i++) {
                    if(horizontal) {
                        if(model.getPos(row, i) == 1) return false;}
                }
            }
            else{
                return false;
            }
        }
        else{
            if(((row+ship.getSize()-1) < 10)){
                for (int i = iter; i < iter+length; i++) {
                    if(!horizontal) {
                        if(model.getPos(row, i) == 1) return false;}
                }
                
            }
            else{
                return false;
            }
        }
    

        //place the ship
        for (int i = iter; i < iter+length; i++) {
            if(horizontal) model.setGridPos(row, i, 1);
            else model.setGridPos(i, col, 1);
            model.incrementCount();
        }
        return true;
    }

    
    public void lookThrough(){
        JLabel g[][] = view.getMyGrid();
        Ship[] s = model.getShips();
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                
                if((g[row][col].getIcon()).toString() == "shipImages/v_five.png"){
                    
                    if(placeShip(row,col, false, s[0])){
                        draggedShip(row, col, s[0], g, false);
                        view.removeCarrier();
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/h_five.png"){
                    
                    if(placeShip(row,col, true, s[0])){         // Works but even if horizontal, it drops the ship vertically
                        draggedShip(row, col, s[0], g, true);
                        view.removeCarrier();
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/v_four.png"){
                    
                    if(placeShip(row,col, false, s[1])){
                        draggedShip(row, col, s[1], g, false);
                        view.removeBattleship();
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/h_four.png"){
                    
                    if(placeShip(row,col, true, s[1])){
                        draggedShip(row, col, s[1], g, true);
                        view.removeBattleship();
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/v_three.png"){
                    
                    if(placeShip(row,col, false, s[2])){
                        draggedShip(row, col, s[2], g, false);
                    
                            view.removeSubmarine();
                        
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/h_three.png"){
                    
                    if(placeShip(row,col, true, s[2])){
                        draggedShip(row, col, s[2], g, true);
                    
                            view.removeSubmarine();
                        
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/v_threetwo.png"){
                    
                    if(placeShip(row,col, false, s[2])){
                        draggedShip(row, col, s[2], g, false);
                    
                            view.removeCruiser();
                        
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/h_threetwo.png"){
                    
                    if(placeShip(row,col, true, s[2])){
                        draggedShip(row, col, s[2], g, true);
                    
                            view.removeCruiser();
                        
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/v_two.png"){
                    
                    if(placeShip(row,col, false, s[4])){
                        draggedShip(row, col, s[4], g, false);
                        view.removeDestroyer();
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                if((g[row][col].getIcon()).toString() == "shipImages/h_two.png"){
                    
                    if(placeShip(row,col, true, s[4])){
                        draggedShip(row, col, s[4], g, true);
                        view.removeDestroyer();
                    }
                    else{
                        g[row][col].setIcon(new ImageIcon(new ImageIcon("replaceImages/trans.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                    }
                }
                
            }
        }
       // model.displayPlayerBoard();
        view.setMyGrid(g);
    }
    public void draggedShip(int boardRow, int boardCol, Ship ship, JLabel[][] g, boolean hor){
        if(hor){
            if((boardCol+ship.getSize()-1) < 10){
            g[boardRow][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/h_left.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                for (int i = 0; i < ship.getSize()-2; i++) {
                    try {
                            g[boardRow][boardCol+i+1].setIcon(new ImageIcon(new ImageIcon("shipImages/h_middle.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
                            //model.setGridPos(boardRow, boardCol, 1);

                    } catch (Exception err) {
                        System.out.println("Couldn't set icon: " + err);
                    }

                }
                g[boardRow][boardCol+ship.getSize()-1].setIcon(new ImageIcon(new ImageIcon("shipImages/h_right.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

        }
    }
    else{
        if((boardRow+ship.getSize()-1) < 10){
            g[boardRow][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/v_top.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

                for (int i = 0; i < ship.getSize()-2; i++) {
                    try {
                            g[boardRow+i+1][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/v_middle.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
                            //model.setGridPos(boardRow, boardCol, 1);

                    } catch (Exception err) {
                        System.out.println("Couldn't set icon: " + err);
                    }

                }
                g[boardRow+ship.getSize()-1][boardCol].setIcon(new ImageIcon(new ImageIcon("shipImages/v_bottom.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

        }
    }
    }
} 