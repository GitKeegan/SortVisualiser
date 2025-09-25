import javax.swing.*;
import java.awt.*;

public class MyRectangle extends JPanel {
    public int x;
    public int y;
    public int value;
    public int width = 10;
    public int height;

    public MyRectangle(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.height = value;
    }

    public int getX() {
        return x;
    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.drawRect(x, y, width, height);
    }



}
