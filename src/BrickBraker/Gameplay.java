package BrickBraker;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

	private int level = 1;
	private boolean play = false;
	private int score = 0;
	
	private boolean win = false;
	
	private int totalBricks = 21;
	
	//Timer-ul poate fi folosit pentru evenimente repetitive. E folosit pentru animatii sau updateul componentelor
	private Timer timer;
	private int delay = 8; // delayul cu care Timer-ul declanseaza action events
	
	private int playerX = 310; //starting position for slider
	
	private int ballPosX = 120;
	private int ballPosY = 350;
	private int ballXdir = -3;
	private int ballYdir = -4;
	
	private MapGenerator map;
	
	public Gameplay() {
		
		map = new MapGenerator(level, 3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//drawing map
		map.draw((Graphics2D)g);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(682, 0, 3, 592);
		
		
		//score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);
		
		//for pause
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 14));
		g.drawString("Press 'p' to pause", 20, 20);
		
		//level
	
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 14));
		g.drawString("Level " + level, 320, 20);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//ball
		g.setColor(Color.yellow);
		g.fillOval(ballPosX, ballPosY, 20, 20);
		
		//in caz ca terminam jocul
		if(totalBricks <= 0 && level == 5)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You finished the game! More level to come!" , 70, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Congratulations! Press Enter to exit.", 180, 350);
			win = false;
			
		}
		
		if(totalBricks <= 0 && level != 5)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won! Congratulations!" , 150, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter for next level", 230, 350);
			win = true;
		}
		
		if(ballPosY > 570){
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
			win = false;
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(play) {
			
			/*
			 * incadram bila intr-un patrat ca sa vedem daca patratul ei se intersecteaza cu cel al paletei
			 * daca da, schimbam directia bilei pe axa Y, din pozitiv in negativ
			 */
			
			if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8)))
				ballYdir = -ballYdir;
			
			
			//punem un label primului for ca sa putem iesi mai usor din el
			A: for(int i=0; i<map.map.length; i++)
			{
				for(int j=0; j<map.map[0].length; j++)
				{
					/*
					 * Pentru fiecare brick, facem un patrat in jurul ei, si al bilei. 
					 * Daca ele se intersecteaza, adica daca bila le loveste, 
					 * brick-ul primeste valoarea 0 si nu mai e desenat.
					 * 
					 */
					
					if(map.map[i][j] > 0)
					{
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20); 
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect))
						{
							//brickul e spart doar daca nu e unul din brickurile care nu pot fi sparte din level 5
							if(!(level==5 && i==map.map.length-1 && (j==0 || j==1 || j==map.map[0].length-1 || j==map.map[0].length-2)))
							{
								map.setBrickValue(0, i, j);
								totalBricks--;
								score += 5;
							}
							
							//if-elseul asta e pentru coliziuni. Cand bila loveste un brick, directia ei e schimbata
							if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width)
								ballXdir = -ballXdir;
							else
								ballYdir = -ballYdir;
							//dupa ce am distrus un brick, se iese din for-ul principal pentru a relua functia de la inceput
							break A;
						}
					}
				}
			}
			
			ballPosX += ballXdir;
			ballPosY += ballYdir;

			/*
			 * Daca bila loveste marginea stanga, directia pe axa X se schimba din -1 in 1 pentru ca trebuie sa creasca
			 * Daca bila loveste marginea de sus, la fel, directia pe axa Y se schimba din negativ in pozitiv (cu cat e mai sus cu atat Y e mai mic)
			 * Daca bila loveste marginea din dreapta, a ajuns la maximul axei X, asa ca se schimba din pozitiv in negativ
			 */
			
			//for left border
			if(ballPosX < 0) 
				ballXdir = -ballXdir;
			//for top border
			if(ballPosY < 0) 
				ballYdir = -ballYdir;
			//for right border
			if(ballPosX > 670) 
				ballXdir = -ballXdir;
			
		}
		
		repaint(); //recalls the paint method, making the the movements visible
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}

	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if(playerX >= 600)
				playerX = 600;
			else
				moveRight();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if(playerX < 10)
				playerX = 10;
			else
				moveLeft();
		}
		
		//cheat code for easy testing
		if(e.getKeyCode() == KeyEvent.VK_W)
			nextLevel();
		
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(!play)
			{
				play = true;
				ballPosX = 120;
				ballPosY = 350;
				
				if(!win)
				{
					ballXdir = -3-level+1;
					ballYdir = -4-level+1;
					playerX = 310;
					score = 0;
					// totalBricks = (level+2) * 7;
				}
				
				if(win)
					nextLevel();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_P)
			play = (play==true)?false:true;
		
	}


	public void moveRight() {
		play = true;
		playerX += (((level+3)*5) > 30)?30:(level+3)*5;
	}
	
	public void moveLeft() {
		play = true;
		playerX -= (((level+3)*5) > 30)?30:(level+3)*5;
	}
	
	public void nextLevel()
	{
		{
			level++;
			ballXdir = -3-level+1;
			ballYdir = -4-level+1;
			
			//daca e level 5, bricksurile sunt cu 4 mai putine, pentru ca 4 nu pot fi sparte
			totalBricks = (level==5)?((level+2) * 7) - 4 : (level+2) * 7;
			}
		map = new MapGenerator(level, level+2, 7);
		
		repaint();
	}
}
