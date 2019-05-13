package Dx_Ball

import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Font;

import java.util.concurrent.TimeUnit;
import java.util.Random;

import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener,ActionListener{
    private boolean play = false;
    private int score = 0;
    
    private int totalBricks = 21;
    
    private Timer timer;
    private int delay = 8;
    
    private int playerX = 310;
    
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -2;
    private int ballYdir = -2;
    
    private MapGenerator map;
    
    public Gameplay(){
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start(); 
    }
    public void paint(Graphics g){ 
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        
        //drawing map
        map.draw((Graphics2D)g);
        
        // border
        g.setColor(Color.black);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        
        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100,8);
        
        //Ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20,20);
        
        // Scores
        g.setColor(Color.white);
        g.setFont(new Font("seirf", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);
        
        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("seirf", Font.BOLD, 30));
            g.drawString("You Won!! CONGRATS :)", 170, 180);
            g.drawString("Score: "+score, 260,230);
            g.setFont(new Font("seirf", Font.BOLD, 20));
            g.drawString("Press Enter To Restart!!", 220,290); 
        }
        
        // If Died
        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            if(score < 80){
                g.setColor(Color.red);
                g.setFont(new Font("seirf", Font.BOLD, 30));
                g.drawString("You Died so Fast Noob, You SUCK ", 100, 300);
                g.drawString("Score: "+score, 270,350);
                g.setFont(new Font("seirf", Font.BOLD, 20));
                g.drawString("Press Enter Play again NOOB!!", 210,400);
            }else{
                g.setColor(Color.red);
                g.setFont(new Font("seirf", Font.BOLD, 30));
                g.drawString("You Died!! :(", 250, 300);
                g.drawString("Score: "+score, 270,350);
                g.setFont(new Font("seirf", Font.BOLD, 20));
                g.drawString("Press Enter To Restart!!", 230,400);
            }
            g.dispose();
        }
    }
    
    public void actionPerformed(ActionEvent e){
        timer.start();
        if(play){          //ball movement
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYdir = -ballYdir;
            }
            
            A: for(int i = 0;i < map.map.length;i++){
                for(int j = 0;j < map.map[0].length;j++){
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;
                        
                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score += 5;
                            
                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }else{
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            
            ballposX += ballXdir;
            ballposY += ballYdir;
            if(ballposX < 0){
                ballXdir = -ballXdir;
            }
            if(ballposY < 0){
                ballYdir = -ballYdir;
            }
            if(ballposX > 670){
                ballXdir = -ballXdir;
            }
        }
        
        repaint();
    }
    
    
    
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX = 600;
            }else{
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                Random rand = new Random();
                if(score < 65){
                    play = true;
                    ballposX = rand.nextInt((350+100)+1)+100;
                    ballposY = 350;
                    ballXdir = -1;
                    ballYdir = -2;
                    playerX = 310;
                    score = 0;
                    totalBricks = 21;
                    map = new MapGenerator(3,7);
                    
                    repaint();
                }else{
                    play = true;
                    ballposX = rand.nextInt((350+100)+1)+100;
                    ballposY = 350;
                    ballXdir = -3;
                    ballYdir = -2;
                    playerX = 310;
                    score = 0;
                    totalBricks = 21;
                    map = new MapGenerator(3,7);
                    
                    repaint(); 
                }
            }
        }
    }   
    
//    movement
    public void moveRight(){
        play = true;
        playerX += 20;
    }
    public void moveLeft(){
        play = true;
        playerX -= 20;
    }
    
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}
