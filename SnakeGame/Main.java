import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

			String Name = JOptionPane.showInputDialog(null,"Please enter your name : ",
					"Name",JOptionPane.QUESTION_MESSAGE);
			Vector<String> levels = new Vector<>();
			URL level = new URL("https://raw.githubusercontent.com/amir-ni/SnakeGame/master/levels/");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(level.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();
			//File curDir = new File(".");
			//File[] list = curDir.listFiles();
			//if(list!=null) {
			//	for (File fil : list) {

			//		if (fil.getName().matches("level\\d.txt")) {
			//			levels.add("Level "+fil.getName().substring(5,fil.getName().length()- 4));
			//		}
			//	}
			//}


			ImageIcon img = new ImageIcon(imgc);
			String level = (String) JOptionPane.showInputDialog(null,"Which level do you want to enter : " ,
					"Level", JOptionPane.QUESTION_MESSAGE, img, levels.toArray(),levels.toArray()[0]);

			new Snake(Name, Integer.parseInt(level.substring(6)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
