package BrickBraker;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame obj = new JFrame("Brick Breaker");
		Gameplay gamePlay = new Gameplay();
		
		obj.setBounds(10, 10, 700, 600);
		obj.setResizable(false);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.setVisible(true);
		obj.add(gamePlay);

	}

}
