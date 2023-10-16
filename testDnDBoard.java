package JLabel;

/*
 * Drag and Drop functionality between JLabels:
 * Two JPanels have a JLabel attached to each, which can drag and drop ImageIcons between each other
 *
 *
 */

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class testDnDBoard extends JFrame {

	JPanel mainPanel, gameBoard, shipPanel;
	// storypanel  imageselect

	// Constructor
	public testDnDBoard() {

			mainPanel = new JPanel(new BorderLayout()); // main panel to hold everything
			gameBoard = new JPanel(new GridLayout(10,10));	// this is going to be a GridLayout(10,10)
			shipPanel = new JPanel(new FlowLayout()); // this is going to be a FLowLayout
			
			
			

			MouseListener listener = new MouseAdapter() {
				public void mousePressed(MouseEvent e)
				{
					JComponent c = (JComponent) e.getSource();
					TransferHandler handler = c.getTransferHandler();
					handler.exportAsDrag(c, e, TransferHandler.COPY); // export copy of clicked component: Can we add a ship class object to the components?
				}
			};

			//---------------------------------------------------------
			// create 1 single jpanel with imageIcon of dog
			
			int j = 0; // what's this for
			BufferedImage myImages;
			JLabel shipSelect = new JLabel(); // why do we do this twice?
			try { myImages = ImageIO.read(new File("boat.jpg"));
			shipSelect = new JLabel(new ImageIcon(myImages)); // initialized twice??
			shipSelect.setPreferredSize(new Dimension(80,80));
			System.out.println(j); // why is this here? ERROR CHECKING in CONSOLE?
			shipPanel.add(shipSelect);
			shipSelect.addMouseListener(listener);
			shipSelect.setTransferHandler(new TransferHandler("icon"));

			} catch(Exception e) {};
			
			
			
			// -----------------------------------------------
			
			//---------------------------------------------------------
			// create 1 single jpanel with imageIcon of cat

			int i = 0; // what for
			BufferedImage boardImage;
			JLabel boardLabel = new JLabel();

			try { boardImage = ImageIO.read(new File("water.jpeg"));
			
			for(int row = 0; row < 10; row++) {
				for(int col = 0; col < 10; col++) {
					
					boardLabel = new JLabel(new ImageIcon(boardImage));
					//System.out.println(i); // error check in console
					boardLabel.setPreferredSize(new Dimension(50,50));
					boardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					boardLabel.addMouseListener(listener);
					boardLabel.setTransferHandler(new TransferHandler("icon"));
					gameBoard.add(boardLabel);
				}
			}
			
			
			} catch (Exception e) {};
			
			
			
			
			
			//---------------------------------------------------------



			mainPanel.add(gameBoard, BorderLayout.NORTH);
			mainPanel.add(shipPanel, BorderLayout.SOUTH);

			getContentPane().add(mainPanel);

	} // end constructor

	public static void main(String[] args) {

		System.out.println("Application Running");
		JFrame mainFrame = new testDnDBoard();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1000, 1000);
		mainFrame.setVisible(true);
	}

}
