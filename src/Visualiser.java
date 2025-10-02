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

        if (command.equals("Sort")) {

            final int[] i = {0};
            final int[] j = {0};
            final boolean[] swapped = {false};

            Timer timer = new Timer(5, null); // runs every 50ms
            timer.addActionListener(e1 -> {
                for (MyRectangle r : rectangles) {
                    r.colour = Color.BLACK;
                }


                if (i[0] < rectangles.size() - 1) {
                    if (j[0] < rectangles.size() - i[0] - 1) {
                        if (rectangles.get(j[0]).value > rectangles.get(j[0] + 1).value) {
                            rectangles.get(j[0]).colour = Color.RED;
                            rectangles.get(j[0] + 1).colour = Color.RED;

                            // swap x positions
                            int tempX = rectangles.get(j[0]).x;
                            rectangles.get(j[0]).x = rectangles.get(j[0] + 1).x;
                            rectangles.get(j[0] + 1).x = tempX;

                            // swap rectangles in list
                            MyRectangle temp = rectangles.get(j[0]);
                            rectangles.set(j[0], rectangles.get(j[0] + 1));
                            rectangles.set(j[0] + 1, temp);

                            swapped[0] = true;


                        }
                        j[0]++;
                    } else {
                        if (!swapped[0]) {
                            timer.stop(); // stop if no swaps in this pass
                        }
                        j[0] = 0;
                        i[0]++;
                        swapped[0] = false;
                    }
                    drawPanel.repaint();
                } else {
                    timer.stop(); // fully sorted
                }
            });

            timer.start();
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
            for (MyRectangle myRectangle : myRectangles) {
                g.setColor(myRectangle.colour);
                g.fillRect(myRectangle.x, myRectangle.y - myRectangle.height, myRectangle.width, myRectangle.height);
            }
        }

    }
}
