package flappybird;
import java.awt.Graphics;
import javax.swing.JPanel;
public class Renderer extends JPanel
{
    /**
     *  Added serialVersionUID to comply with Java Serializability
     */
    private static final long serialVersionUID = -6973093079234550944L;

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        FlappyBird.flappyBird.repaint(g);
    }
}
