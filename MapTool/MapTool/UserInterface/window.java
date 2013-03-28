package UserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;


public class window extends JFrame{


	private JPanel map;
	private JPanel chatWindow;
	private JPanel videoChat;
	
	public window(){	
		initComponents();
	}

	public void resize(JPanel map, JPanel chatWindow, JPanel videoChat){
		chatWindow.setBounds(this.getWidth() - 220, 5, 198, (this.getHeight() - 49));
		videoChat.setBounds(5, this.getHeight() - 150, this.getWidth() - chatWindow.getWidth() - 32, 106);
		map.setBounds(5, 5, this.getWidth() - chatWindow.getWidth() - 32, this.getHeight() - videoChat.getHeight() - 54);
		System.out.println("" + this.getWidth() + " " + this.getHeight());
	}
	
	public void initComponents(){
		JPanel container = new JPanel();
		container.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				resize(map, chatWindow, videoChat);
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
			}
			
		});
		
		//initial window size
		container.setMinimumSize(new Dimension(800, 600));

		//chat panel
		chatWindow = new JPanel();
		chatWindow.setBackground(Color.RED);
		chatWindow.setBounds(container.getWidth() - 220, 5, 198, (container.getHeight() - 49));
		//video chat panel
		videoChat = new JPanel();
		videoChat.setBackground(Color.GREEN);
		//map panel
		map = new JPanel();
		map.setBackground(Color.BLUE);

		
		this.add(container);
		
		container.add(map);
		container.add(chatWindow);
		container.add(videoChat);

		resize(map, chatWindow, videoChat);
		container.validate();
		container.repaint();
		pack();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				new window().setVisible(true);			
			}
			
		});
	}
}