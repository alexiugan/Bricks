package BrickBraker;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
	
	public int map[][];
	public int brickWidth;
	public int brickHeight;
	public int color;
	
	public MapGenerator(int level, int row, int col) {
		map = new int[row][col];
		color=level;
		
		//notam initial cu 1 toate brick-urile. Adica vrem ca toate sa fie vizibile, cand una e sparta, ii schimbam valoarea la 0
		for(int i=0 ; i<map.length ; i++)
		{
			for(int j=0 ; j<map[0].length ; j++)
			{
				map[i][j]=1;
			}
		}
		
		brickWidth =  540/col;
		brickHeight = 150/row;
	}
	
	// drawing the bricks
	
	public void draw(Graphics2D g) {
		for(int i=0 ; i<map.length ; i++)
		{
			for(int j=0 ; j<map[0].length ; j++)
			{
				if(map[i][j] > 0)
				{
					//daca e level 5 si suntem la extremele ultimului rand, 4 brickuri sunt colorate diferit pt ca nu pot fi sparte
					if(color==5 && i==map.length-1 && (j==0 || j==1 || j==map[0].length-1 || j==map[0].length-2))
					{
						g.setColor(Color.LIGHT_GRAY);
					}
					
					else {
						switch(color) {
						case 1: {
								g.setColor(Color.white);
								break;
								}
						case 2: {
							g.setColor(Color.blue);
							break;
							}
						case 3: {
							g.setColor(Color.red);
							break;
							}
						case 4: {
							g.setColor(Color.pink);
							break;
							}
						default:{
							g.setColor(Color.DARK_GRAY);
							break;
							}
						}
					}
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
	}
	
	public void setBrickValue(int value, int row, int col)
	{
		map[row][col] = value;
	}
}
