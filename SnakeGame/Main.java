import java.io.File;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {

        String Name = JOptionPane.showInputDialog(null,"Please enter your name : ",
				"Name",JOptionPane.QUESTION_MESSAGE);
		Vector<String> levels = new Vector<>();
		File curDir = new File(".");
		File[] list = curDir.listFiles();
		if(list!=null) {
			for (File fil : list) {

				if (fil.getName().matches("level\\d.txt")) {
					levels.add("Level "+fil.getName().substring(5,fil.getName().length()- 4));
				}
			}
		}
		ImageIcon img = new ImageIcon("icon.png");
		String level = (String) JOptionPane.showInputDialog(null,"Which level do you want to enter : " ,
				"Level", JOptionPane.QUESTION_MESSAGE, img, levels.toArray(),levels.toArray()[0]);

		new Snake(Name, Integer.parseInt(level.substring(6)));

	}

}
