import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ScoreSubmitter {
    Socket mSocket;
    int port = 9090;
    String serverAddress = "127.0.0.1";

    InputStream fromServerStream;
    OutputStream toServerStream;

    DataInputStream reader;
    PrintWriter writer;

    public ScoreSubmitter(int Score,int level,String name) {
        try {
            mSocket = new Socket(serverAddress, port);
            fromServerStream = mSocket.getInputStream();
            toServerStream = mSocket.getOutputStream();
            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);
            writer.println("post");
            writer.println(name);
            writer.println(Score);
            writer.println(level);
            String msg = reader.readLine();
            if(msg.equals("Done") ){
                JOptionPane.showMessageDialog(null, name + " : " + Score, "Your score" , JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Exception happened while submitting score.", "Error" , JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error" , JOptionPane.ERROR_MESSAGE);
        }

    }
}
