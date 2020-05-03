package flappybird;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
public class FlappyBird implements ActionListener,MouseListener,KeyListener
{
    public static FlappyBird flappyBird;
    public final int w=800, h=800;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public int ticks,ymotion,score;
    public boolean end,start,gameOver;
    public Random r;
    public FlappyBird()
    {
        JFrame frame=new JFrame();
        Timer t=new Timer(20,this); //Swing Timer for refreshing screen
        renderer=new Renderer();
        r=new Random();
        frame.add(renderer);
        frame.setTitle("FLAPPY BIRD");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(w, h);
        frame.addMouseListener(this);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setVisible(true);
        bird=new Rectangle(w/2-10,h/2-10,20,20);
        columns=new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        gameOver=false;
        t.start();
    }
    public void addColumn(boolean start) //add the columns(pipes) the player(bird) must avoid
    {
        int space=300;
        int w1=100;
        int h1=50+r.nextInt(300);
        if(start)
        {
            columns.add(new Rectangle(w+w1+columns.size()*300,h-h1-120,w1,h1));
            columns.add(new Rectangle(w+w1+(columns.size()-1)*300,0,w1,h-h1-space));
        }
        else
        {
            columns.add(new Rectangle(columns.get(columns.size()-1).x+600,h-h1-120,w1,h1));
            columns.add(new Rectangle(columns.get(columns.size()-1).x,0,w1,h-h1-space));
        }
    }
    public void Columnpaint(Graphics g,Rectangle column) //paint the columns with the required color
    {
        g.setColor(Color.GREEN.darker());
        g.fillRect(column.x,column.y,column.width,column.height);
    }
    public void jump()
    {
        if(end) // reset the score on mouse click or key release if game end
        {
            bird=new Rectangle(w/2-10,h/2-10,20,20);
            columns.clear();
            ymotion=0;
            score=0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            end=false;
        }
        if(!start) // start the game on key press(or mouse click) if not already started
        {
            start=true;
        }
        else if(!end)
        {
            if(ymotion>0)
                ymotion=0;
            ymotion-=10;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        int speed=10;
        ticks++;
        if(start)
        {
            for(int i=0;i<columns.size();i++) // cause cloumn motion in x axis
            {
                Rectangle column=columns.get(i);
                column.x-=speed;
            }
            if(ticks%2==0 && ymotion<15) // cause bird motion in y axis
                ymotion+=2;
            for(int i=0;i<columns.size();i++)
            {
                Rectangle column=columns.get(i);
                if(column.x+column.width<0) // remove the older columns if they're out of window
                {
                    columns.remove(column);
                    if(column.y==0) // adds new column after removing old one
                        addColumn(false);
                }
            }
            bird.y+=ymotion;
            for(Rectangle column:columns)
            {
                if(column.y==0 && bird.x+bird.width/2>column.x+column.width/2-10 && bird.x+bird.width/2<column.x+column.width/2+10) //increment score if the bird  has not collided
                    score++;
                if(column.intersects(bird)) // perform collision detection of bird to column
                {
                    end=true;
                    if(bird.x<=column.x)
                        bird.x=column.x-bird.width; // push the bird along with the moving columns on game over
                    else
                    {
                        if(column.y!=0)
                            bird.y=column.y-bird.height;
                        else if(bird.y<column.height)
                            bird.y=column.height;
                    }
                }
            }
            if(bird.y>h-120 || bird.y<0) // also check if bird hits ground
                end=true;
            if(bird.y+ymotion>=h-120) // also check if bird hits sky
            {
                bird.y=h-120-bird.height;
                end=true;
            }
        }
        if(gameOver) {
            gameOver = false;
            flappyBird = new FlappyBird();
        }
        if(!gameOver)
            renderer.repaint();
    }
    public void repaint(Graphics g) // paints the various components in frame
    {
        g.setColor(Color.cyan); // sky
        g.fillRect(0, 0, w, h);
        
        g.setColor(Color.orange); // earth
        g.fillRect(0,h-120,w,120);
        
        g.setColor(Color.green); // grass
        g.fillRect(0,h-120,w,20);
        
        g.setColor(Color.red); // bird
        g.fillRect(bird.x,bird.y,bird.width,bird.height);
        
        for(Rectangle column:columns) // for each column, draw each
            Columnpaint(g,column);
        
        g.setColor(Color.white); // draw the text
        g.setFont(new Font("Arial",1,100));
        if(!start) // helpful text at start of game
            g.drawString("Click to Start", 75, h/2-50);
        if(end) // helpful test at end of game
        {
            g.drawString("Game Over", 100, h/2-50);
            g.drawString("Score: ", 175, 100);
            g.drawString(String.valueOf(score), w/2+100, 100);
            gameOver=true;
        }
        
        if(!end && start) // score test printed during game
            g.drawString(String.valueOf(score), w/2-25, 100);
    }
    public static void main(String[] args)
    {
        flappyBird=new FlappyBird();
    }
    @Override
    public void mouseClicked(MouseEvent e) // use mouse to initiate jump
    {
        jump();
    }
    @Override
    public void keyReleased(KeyEvent e) // use spacebar key to initiate jump
    {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
            jump();
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
}
