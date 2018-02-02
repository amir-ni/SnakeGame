import java.io.*;
import java.net.Socket;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {

	public static String getName(){
		return JOptionPane.showInputDialog(null,"Please enter your name : ",
				"Name",JOptionPane.QUESTION_MESSAGE);
	}
	public static void main(String[] args) {
		Socket mSocket;
		int port = 9090;
		String serverAddress = "127.0.0.1";
		InputStream fromServerStream;
		OutputStream toServerStream;
		DataInputStream reader;
		PrintWriter writer;

		try {
			ImageIcon img = new ImageIcon("resources/images/icon.png");
			String Name = getName();
			while (Name.isEmpty()){
				JOptionPane.showMessageDialog(null, "Name field cannot be empty", "Error" , JOptionPane.ERROR_MESSAGE);
				Name = getName();
			}
			Vector<String> levels = new Vector<>();
			mSocket = new Socket(serverAddress, port);
			fromServerStream = mSocket.getInputStream();
			toServerStream = mSocket.getOutputStream();
			reader = new DataInputStream(fromServerStream);
			writer = new PrintWriter(toServerStream, true);
			writer.println("getLevels");
			for (String line = reader.readLine(); !line.equals("Done"); line = reader.readLine()) {
				levels.add("Level " + line);
			}
			if(levels.isEmpty()){
				JOptionPane.showMessageDialog(null, "Sorry no levels added yet", "Error" , JOptionPane.ERROR_MESSAGE);
			}
			String level = (String) JOptionPane.showInputDialog(null,"Which level do you want to enter : " ,
					"Level", JOptionPane.QUESTION_MESSAGE, img, levels.toArray(),levels.toArray()[0]);
			new GameFrame(Name, Integer.parseInt(level.substring(6)));
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
