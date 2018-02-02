import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameLayout extends JPanel implements KeyListener {
	private GamePixel[][] pixel;
    ArrayList<Integer> obstacles = new ArrayList<>();

    private final Color worm = new Color(139, 61, 12);
	private char direction = 'r';
	private char bufdir = ' ';
	private int length;
	private int xsnake;
	private int ysnake;
    private int level;
	private String name;
	private int size;
    private int sleep = 100;
    private int loseDelay = 2000;
    private boolean lost = false;
	private Thread thread;
	private int startLengh;
    Socket mSocket;
    int port = 9090;
    String serverAddress = "127.0.0.1";
    InputStream fromServerStream;
    OutputStream toServerStream;
    DataInputStream reader;
    PrintWriter writer;
    JLabel apple = new JLabel();

    public class trap extends JLabel{
    	trap(){
			Dimension newDim = new Dimension(40, 40);
			this.setMaximumSize(newDim);
			this.setPreferredSize(newDim);
			this.setMaximumSize(newDim);
			this.setSize(newDim);

			try {
				BufferedImage image = ImageIO.read(new File("resources/images/trap.png"));
				Image dimg = image.getScaledInstance(40, 40,
						Image.SCALE_SMOOTH);
				this.setIcon(new ImageIcon(dimg));
			}catch (Exception e){
				System.out.println(e.getMessage());
			}

		}
	}

	public GameLayout(String name, int level) {
        this.level = level;
		Dimension newDim = new Dimension(40, 40);
        try {
			apple.setMaximumSize(newDim);
			apple.setPreferredSize(newDim);
			apple.setMaximumSize(newDim);
			apple.setSize(newDim);
			BufferedImage image = ImageIO.read(new File("resources/images/apple.png"));
			Image dimg = image.getScaledInstance(40, 40,
					Image.SCALE_SMOOTH);
			apple.setIcon(new ImageIcon(dimg));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            mSocket = new Socket(serverAddress, port);
            fromServerStream = mSocket.getInputStream();
            toServerStream = mSocket.getOutputStream();
            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);
            writer.println("getLevel");
            writer.println(level);
			this.size = Integer.parseInt(reader.readLine());
            this.startLengh = Integer.parseInt(reader.readLine());
            String inputLine = reader.readLine();
            xsnake = Integer.parseInt(inputLine.split(" ")[0]);
            ysnake = Integer.parseInt(inputLine.split(" ")[1]);
			for (String line = reader.readLine(); !line.equals("Done"); line = reader.readLine()) {
                obstacles.add(Integer.parseInt(line.split(" ")[0]));
                obstacles.add(Integer.parseInt(line.split(" ")[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.name = name;
		pixel = new GamePixel[size][size];
		this.setSize((size*10),(size*10));
		this.setLayout(new GridLayout(size,size));


		addKeyListener(this);

		length = startLengh;


		for(int j = 0;j < size;j++) {
			for (int i = 0;i < size;i++) {
				pixel[i][j] = new GamePixel();
				pixel[i][j].setMaximumSize(newDim);
				pixel[i][j].setPreferredSize(newDim);
				pixel[i][j].setMinimumSize(newDim);
				pixel[i][j].setSize(newDim);
				pixel[i][j].revalidate();
				if(i == 0 || i == (size-1) || j == 0 || j == (size-1)) {
					pixel[i][j].setBackground(Color.DARK_GRAY);
				}else {
					pixel[i][j].setBackground(Color.green);
				}
				this.add(pixel[i][j]);
			}
		}

		for(int i=0;i<obstacles.size();i+=2){
            pixel[obstacles.get(i)][obstacles.get(i+1)].add(new trap());
			pixel[obstacles.get(i)][obstacles.get(i+1)].isObstacle = true;
        }
        startWorm();
        this.setVisible(true);
		startMovement();
		makeApple();
	}
	private void startWorm() {
		pixel[((size)/2)-1][5].setBackground(worm);
		pixel[((size)/2)-1][5].setLength(1);
		pixel[((size)/2)][5].setBackground(worm);
		pixel[((size)/2)][5].setLength(2);
	}
    private void startMovement() {
		thread = new Thread(new Running());
		thread.setDaemon(true);
		thread.start();
	}
	@Override
	public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if(direction != 'u') { direction = 'd';	}
                break;
            case KeyEvent.VK_UP:
                if(direction != 'd') { direction = 'u';	}
                break;
            case KeyEvent.VK_LEFT:
                if(direction != 'r') { direction = 'l';	}
                break;
            case KeyEvent.VK_RIGHT:
                if(direction != 'l') { direction = 'r';	}
                break;
        }

	}
	@Override
	public void keyReleased(KeyEvent e) {

	}
	@Override
	public void keyTyped(KeyEvent e) {

	}
    private synchronized void moveSnake() {
		if (direction == 'u'){
			ysnake--;
		}else if (direction == 'd') {
			ysnake++;
		}else if (direction == 'r') {
			xsnake++;
		}else if (direction == 'l') {
			xsnake--;
		}
		if (pixel[xsnake][ysnake].getLength() > 1 || xsnake == 0 || xsnake == (size-1) || ysnake == 0 || ysnake == (size-1)) {
			collision();
		}else if (pixel[xsnake][ysnake].isApple()) {
			length++;
			pixel[xsnake][ysnake].setApple(false);
			makeApple();
		}else if(pixel[xsnake][ysnake].isObstacle){
            collision();
        }

		pixel[xsnake][ysnake].setLength(length + 1);
	}
    public void collision() {
	    lost = true;
        for (int j = 0;j < (size);j++) {
            for (int i = 0;i < (size);i++) {
                if(((i+j)%2)==1) {
                    pixel[i][j].setBackground(Color.BLACK);
                }else{
                    pixel[i][j].setBackground(Color.RED);
                }
            }
            try {
                Thread.sleep(loseDelay/size);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new ScoreSubmitter((this.length-startLengh),this.level,this.name);
        System.exit(0);
	}
    private void iterateSnake() {
	    if(lost)
	        return;
		for (int j = 1;j < (size-1);j++) {
			for (int i = 1;i < (size-1);i++) {
				pixel[i][j].iterate();
				if(pixel[i][j].getLength() > 0) {
					pixel[i][j].setBackground(worm);
				}else if (pixel[i][j].isApple()) {
					if(((i+j)%2)==1) {
						pixel[i][j].setBackground(new Color(76, 140, 19));
					}else{
						pixel[i][j].setBackground(new Color(7, 140, 59));
					}
				}else if(((i+j)%2)==1) {
                        pixel[i][j].setBackground(new Color(76, 139, 19));
                    }else{
                        pixel[i][j].setBackground(new Color(7, 139, 59));
                    }
			}
		}
		pixel[xsnake][ysnake].setBackground(Color.WHITE);
	}

    private void makeApple() {
		int x = (int) Math.round( ( Math.random() ) * (size-2)) + 1;
		int y = (int) Math.round( ( Math.random() ) * (size-2)) + 1;
		if (pixel[x][y].getLength() > 0 || pixel[x][y].getBackground() == Color.DARK_GRAY ||pixel[x][y].isObstacle) {
			makeApple();
		}else {
			pixel[x][y].setApple(true);
			pixel[x][y].add(apple);
			System.out.println(x+ " " + y);
		}
	}

	public class Running implements Runnable {

		@Override
		public void run() {
			while(!lost){
				try {
					Thread.sleep(sleep);
					moveSnake();
					if(bufdir != ' ') {
						direction = bufdir;
						bufdir = ' ';
					}
					iterateSnake();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
