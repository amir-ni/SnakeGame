import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {

		try {
			URL gameicon = new URL("https://raw.githubusercontent.com/amir-ni/SnakeGame/master/icon.png");
			Image imgc = ImageIO.read(gameicon);
			ImageIcon img = new ImageIcon(imgc);
			String Name = JOptionPane.showInputDialog(null,"Please enter your name : ",
					"Name",JOptionPane.QUESTION_MESSAGE);
			Vector<String> levels = new Vector<>();

			URL levelURL;
			String levelsURL = "https://raw.githubusercontent.com/amir-ni/SnakeGame/master/levels/";
			for(int z=1;;z++) {
				levelURL = new URL(levelsURL + "level"+ z +".txt");
				HttpURLConnection huc = (HttpURLConnection) levelURL.openConnection();
				if(404 == huc.getResponseCode())
					break;
				else
					levels.add("Level " + z);
			}
			String level = (String) JOptionPane.showInputDialog(null,"Which level do you want to enter : " ,
					"Level", JOptionPane.QUESTION_MESSAGE, img, levels.toArray(),levels.toArray()[0]);

			new Snake(Name, Integer.parseInt(level.substring(6)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
