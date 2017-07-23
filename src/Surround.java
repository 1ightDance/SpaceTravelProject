import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static java.lang.Thread.sleep;

/**
 * Created by DELL on 2017/5/25.
 */
public class Surround extends JFrame{
    static JPanel panel=new JPanel();
    Ship ship=new Ship();

    Surround(){
        super("绘制轨道模拟");
        this.setSize(800,800);
        this.setTitle("绘制轨道模拟");
        this.setLayout(null);
        panel.setSize(800,800);
        panel.setOpaque(true);
        panel.setBackground(new Color(40,30,105));
        panel.setVisible(true);
        this.add(panel);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2;
        g2= (Graphics2D) panel.getGraphics();
        g2.setColor(Color.gray);
        g2.fillOval(395,395,10,10);
        AffineTransform tranShip=new AffineTransform();
        tranShip.rotate(ship.getShipDirection(),ship.getShipX(),ship.getShipY());
        g2.setTransform(tranShip);
        Rectangle2D rec=ship.drawShipPosition();
        g2.fill(rec);
        AffineTransform tran=new AffineTransform();
        tran.rotate(ship.getShipAngleθ(),400,400);
        Ellipse2D ellipse2=ship.drawKeplerTrace();
        g2.setTransform(tran);
        g2.draw(ellipse2);
    }
    public static void main(String[]args) throws InterruptedException {
        Surround srnd=new Surround();
        while (true){
            srnd.ship.shipMoving();
            srnd.repaint();
            sleep(400);
        }
    }
}
