import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class GameFrame extends JFrame {


    private javax.swing.JButton endButton;
    private javax.swing.JButton scoreButton;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel levelLabel;
    private GameLayout jPanel1;
    private javax.swing.JLabel levelValueLabel;
    private javax.swing.JLabel nameValueLabel;
    private String HighestScore;
    public void CloseFrame(){
        super.dispose();
    }

    public String HighestScore(){
        return HighestScore;
    }

    public GameFrame(String name, int level)
    {
        Socket mSocket;
        int port = 9090;
        String serverAddress = "127.0.0.1";
        InputStream fromServerStream;
        OutputStream toServerStream;
        DataInputStream reader;
        PrintWriter writer;
        int size = 0;
        try {
            mSocket = new Socket(serverAddress, port);
            fromServerStream = mSocket.getInputStream();
            toServerStream = mSocket.getOutputStream();
            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);
            writer.println("getLevel");
            writer.println(level);
            String inputLine;
            size = Integer.parseInt(reader.readLine());
            mSocket.close();
        }catch (Exception e){

        }
        try {
            mSocket = new Socket(serverAddress, port);
            fromServerStream = mSocket.getInputStream();
            toServerStream = mSocket.getOutputStream();
            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);
            writer.println("highscore");
            writer.println(level);
            HighestScore = reader.readLine();
            mSocket.close();
        }catch (Exception e){

        }

        ImageIcon img = new ImageIcon("resources/images/icon.png");
        this.setIconImage(img.getImage());
        this.setTitle("SnakeGame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameLayout jPanel1 = new GameLayout(name,level);
        endButton = new javax.swing.JButton();
        scoreButton = new javax.swing.JButton();
        levelValueLabel = new javax.swing.JLabel();
        nameValueLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        levelLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBackground(new java.awt.Color(204, 153, 0));
        jPanel1.setForeground(new java.awt.Color(204, 255, 102));

        endButton.setBackground(new Color(59, 89, 182));
        endButton.setForeground(Color.WHITE);
        endButton.setFocusPainted(false);
        endButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        endButton.setText("End");
        endButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jPanel1.collision();
            }
        });


        scoreButton.setText("High scores");
        scoreButton.setBackground(new Color(59, 89, 182));
        scoreButton.setForeground(Color.WHITE);
        scoreButton.setFocusPainted(false);
        scoreButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        scoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //call another method in the same class which will close this Jframe
                JOptionPane.showMessageDialog(null, HighestScore(), "Your score" , JOptionPane.INFORMATION_MESSAGE);
                jPanel1.setFocusable(true);
                jPanel1.requestFocusInWindow();
                addKeyListener(jPanel1);
            }
        });

        nameLabel.setText("Name : ");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        nameLabel.setForeground(new Color(59, 89, 182));

        nameValueLabel.setText(name);
        nameValueLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        nameValueLabel.setForeground(new Color(59, 89, 182));

        levelLabel.setText("Level : ");
        levelLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        levelLabel.setForeground(new Color(59, 89, 182));

        levelValueLabel.setText(Integer.toString(level));
        levelValueLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        levelValueLabel.setForeground(new Color(59, 89, 182));


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(endButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(scoreButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                                                .addComponent(levelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(levelValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(36, 36, 36))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(36, 36, 36))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(scoreButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(endButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(levelValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nameLabel)
                                        .addComponent(levelLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        pack();
        this.setResizable(false);
        jPanel1.setFocusable(true);
        jPanel1.requestFocusInWindow();
        addKeyListener(jPanel1);
        setVisible(true);
    }

}