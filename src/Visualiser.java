import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Visualiser extends JPanel implements ActionListener{
    final private DrawPanel drawPanel;

    int position = 10;
    public Visualiser(){
        JFrame frame = new JFrame("Visualiser");

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);

        JButton addButton = new JButton("Reset");
        addButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.add(sortButton);
        buttonPanel.add(addButton);

        drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(800, 600));

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(drawPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        position = 10;
        for (int x = 0; x < 70; x++){
            drawPanel.addRectangle(new MyRectangle(position, 575, (int)(Math.random()*500)+1));
            position += 10;
        }
    }
    public static void main(String[] args) {
        new Visualiser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        List<MyRectangle> rectangles = drawPanel.getRectangles();

        if (command.equals("Sort")){
            boolean swapped = true;
            while (swapped) {
                swapped = false;
                for (int i = 0; i < rectangles.size() - 1; i++) {
                    for (int j = 0; j < rectangles.size() - i - 1; j++) {
                        if (rectangles.get(j).value > rectangles.get(j + 1).value) {
                            // swap x positions
                            int tempX = rectangles.get(j).x;
                            rectangles.get(j).x = rectangles.get(j + 1).x;
                            rectangles.get(j + 1).x = tempX;

                            // swap rectangles in list for next iteration
                            MyRectangle temp = rectangles.get(j);
                            rectangles.set(j, rectangles.get(j + 1));
                            rectangles.set(j + 1, temp);

                            drawPanel.repaint();

                        }
                    }
                }

            }
        }
        else if (command.equals("Reset")){
            drawPanel.clear();
            position = 10;
            for (int x = 0; x < 70; x++){
                drawPanel.addRectangle(new MyRectangle(position, 575, (int)(Math.random()*500)+1));
                position += 10;
            }
        }

    }

    static class DrawPanel extends JPanel{
        private final List<MyRectangle> myRectangles = new ArrayList<>();

        public void addRectangle(MyRectangle myRectangle){
            myRectangles.add(myRectangle);
            repaint();
        }

        public void clear(){
            myRectangles.clear();
            repaint();
        }

        public List<MyRectangle> getRectangles(){
            return myRectangles;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(MyRectangle myRectangle : myRectangles){
                g.setColor(Color.black);
                g.fillRect(myRectangle.x, myRectangle.y- myRectangle.height, myRectangle.width, myRectangle.height);
            }
        }
    }
}
