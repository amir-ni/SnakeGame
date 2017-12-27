import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class Snake extends JFrame implements KeyListener {
	private GamePixel[][] pixel;
    int[][] obstacles = new int[100][2];
	private final Color worm = new Color(0,180,0);
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

	public Snake(String name,int level) {
        this.level = level;
        try {
			URL gameicon = new URL("https://raw.githubusercontent.com/amir-ni/SnakeGame/master/icon.png");
			Image imgc = ImageIO.read(gameicon);
			ImageIcon img = new ImageIcon(imgc);
            this.setIconImage(img.getImage());
			URL levelURL = new URL("https://raw.githubusercontent.com/amir-ni/SnakeGame/master/levels/level"+ level +".txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(levelURL.openStream()));
			String inputLine;
			this.size = Integer.parseInt(in.readLine());
			int i = 0;
			while ((inputLine = in.readLine()) != null) {
				obstacles[i][0] = Integer.parseInt(inputLine.split(" ")[0]);
				obstacles[i][1] = Integer.parseInt(inputLine.split(" ")[1]);
				i++;
			}
			in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    this.name = name;
		pixel = new GamePixel[size][size];
		this.setSize((size*10),(size*10));
		this.setResizable(false);
		this.setLayout(new GridLayout(size,size));
		this.setTitle("Snake game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		length = 2;
		xsnake = 20;
		ysnake = 5;

		for(int j = 0;j < size;j++) {
			for (int i = 0;i < size;i++) {
				pixel[i][j] = new GamePixel();
				if(i == 0 || i == (size-1) || j == 0 || j == (size-1)) {
					pixel[i][j].setBackground(Color.gray);
				}else {
					pixel[i][j].setBackground(Color.black);
				}
				this.add(pixel[i][j]);
			}
		}
        for(int j = 0;j < obstacles.length;j++) {
            pixel[obstacles[j][0]][obstacles[j][1]].setBackground(Color.gray);
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
    private void moveSnake() {
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
		}
        for(int j = 0;j < obstacles.length;j++) {
            if(obstacles[j][0]==xsnake && obstacles[j][1]==ysnake){
                collision();
            }
        }

		pixel[xsnake][ysnake].setLength(length + 1);
	}
    private void collision() {
	    lost = true;
        for (int j = 0;j < (size);j++) {
            for (int i = 0;i < (size);i++) {
                if(((i+j)%2)==1) {
                    pixel[i][j].setBackground(Color.black);
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
        JOptionPane.showMessageDialog(null, name + " : " + (length-2), "Your score" , JOptionPane.INFORMATION_MESSAGE);
        try{
            File tmpDir = new File("scores.txt");
            FileWriter fw = new FileWriter(tmpDir,true);
            fw.write("name : " + this.name + " , score : " + (this.length-2) + " , level : " + this.level +"\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
					pixel[i][j].setBackground(Color.red);
				}else {
					pixel[i][j].setBackground(Color.black);
				}
			}
		}
        for(int j = 0;j < obstacles.length;j++) {
            pixel[obstacles[j][0]][obstacles[j][1]].setBackground(Color.gray);
        }
	}
    private void makeApple() {
		int x = (int) Math.round( ( Math.random() ) * (size-2)) + 1;
		int y = (int) Math.round( ( Math.random() ) * (size-2)) + 1;
		if (pixel[x][y].getLength() > 0 || pixel[x][y].getBackground() == Color.gray) {
			makeApple();
		}else {
			pixel[x][y].setApple(true);
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
