import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class Server {
    ServerSocket mServer;
    int serverPort = 9090;

    InputStream fromClientStream;
    OutputStream toClientStream;

    DataInputStream reader;
    PrintWriter writer;

    public Server() {

        try {
            mServer = new ServerSocket(serverPort);
            while (true) {
                Socket client = mServer.accept();

                fromClientStream = client.getInputStream();

                toClientStream = client.getOutputStream();

                reader = new DataInputStream(fromClientStream);
                writer = new PrintWriter(toClientStream, true);

                String switcher = reader.readLine();
                if (switcher.equals("getLevel")) {
                    try {
                        int level = Integer.parseInt(reader.readLine());
                        File tmpDir = new File("levels/level" + level + ".txt");
                        if (tmpDir.exists()) {
                            Scanner scanner = new Scanner(tmpDir);
                            writer.println(scanner.nextInt());
                            writer.println(scanner.nextInt());
                            writer.println(scanner.next());
                            while (scanner.hasNextInt()) {
                                writer.println(scanner.nextInt() + " " + scanner.nextInt());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    writer.println("Done");
                } else if (switcher.equals("post")) {
                    String name = reader.readLine();
                    String Score = reader.readLine();
                    int level = Integer.parseInt(reader.readLine());
                    try {
                        File tmpDir = new File("scores/level" + level  + ".txt");
                        FileWriter fw = new FileWriter(tmpDir, true);
                        fw.write( Base64.getEncoder().encodeToString(name.getBytes("UTF-8")) + "," + Base64.getEncoder().encodeToString(Score.getBytes("UTF-8")) + "\n");
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    writer.println("Done");
                } else if (switcher.equals("getLevels")) {
                    File curDir = new File("./levels");
                    File[] list = curDir.listFiles();
                    if (list != null) {
                        for (File fil : list) {
                            if (fil.getName().matches("level\\d{0,}.txt")) {

                                writer.println(fil.getName().substring(5, fil.getName().length() - 4));
                            }
                        }
                    }
                    writer.println("Done");
                }else if(switcher.equals("highscore")){
                    try {
                        int level = Integer.parseInt(reader.readLine());
                        File tmpDir = new File("scores/level" + level + ".txt");
                        if (tmpDir.exists()) {
                            Scanner scanner = new Scanner(tmpDir);
                            int max = 0;
                            String maxed = null;
                            while (scanner.hasNext()) {
                                String t = scanner.nextLine();
                                String tt[] = t.split(",");
                                tt[0] =  new String(Base64.getDecoder().decode(tt[0]));
                                tt[1] =  new String(Base64.getDecoder().decode(tt[1]));
                                if(Integer.parseInt(tt[1]) > max){
                                    max = Integer.parseInt(tt[1]);
                                    maxed = tt[0];
                                }
                            }
                            writer.println(maxed + " has best score by scoring " + max + " points. ");
                        }else {
                            writer.println("Error");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    writer.println("Done");
                }
            }
        } catch (IOException e) {
        }

    }

    public static void main(String[] args) {
        new Server();
    }
}
