import javax.swing.JPanel;


public class GamePixel extends JPanel {
	private int length;
	private boolean apple;
	
	public GamePixel() {
		length = 0;
		apple = false;
	}
	public void setLength(int i) {
		length = i;
	}
	public void iterate() {
		length--;
	}
	public int getLength() {
		return length;
	}
	public void setApple(boolean b) {
		apple = b;
	}
	public boolean isApple() {
		return apple;
	}
}
