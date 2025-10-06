import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Visualiser extends JPanel implements ActionListener{
    final private DrawPanel drawPanel;

    final private JComboBox sortSelector;

    private boolean isSorting;
    public volatile boolean shouldStop = false;

    public void setSortingStatus(boolean state){
        isSorting = state;
    }

    int position = 10;
    public Visualiser(){
        JFrame frame = new JFrame("Visualiser");

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);

        JButton addButton = new JButton("Reset");
        addButton.addActionListener(this);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.add(sortButton);
        buttonPanel.add(addButton);
        buttonPanel.add(stopButton);
        buttonPanel.setBackground(Color.BLACK);

        drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(800, 600));
        drawPanel.setBackground(Color.BLACK);

        sortSelector = new JComboBox();
        sortSelector.addItem("BubbleSort");
        sortSelector.addItem("SelectionSort");
        sortSelector.addItem("InsertionSort");

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(drawPanel, BorderLayout.CENTER);

        frame.add(sortSelector, BorderLayout.SOUTH);

        frame.getContentPane().setBackground(Color.BLACK);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        position = 10;
        for (int x = 0; x < 70; x++){
            drawPanel.addRectangle(new MyRectangle(position, 575, (int)(Math.random()*500)+1));
            position += 10;
        }

        isSorting = false;
    }
    public static void main(String[] args) {
        new Visualiser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        List<MyRectangle> rectangles = drawPanel.getRectangles();

        if (command.equals("Sort") && !isSorting) {
            Sorter sorter = new Sorter(rectangles, drawPanel, this);
            String sortSelected = sortSelector.getSelectedItem().toString();
            if (sortSelected.equals("BubbleSort")) {
                sorter.bubbleSort();
            }
            else if (sortSelected.equals("SelectionSort")) {
                sorter.selectionSort();
            }
            else if (sortSelected.equals("InsertionSort")) {
                sorter.insertionSort();
            }

        }
        else if (command.equals("Reset") && !isSorting) {
            drawPanel.clear();
            position = 10;
            for (int x = 0; x < 70; x++){
                drawPanel.addRectangle(new MyRectangle(position, 575, (int)(Math.random()*500)+1));
                position += 10;
            }
        }
        else if (command.equals("Stop") && isSorting) {
            isSorting = false;
            this.shouldStop = true;
        }

    }

    public static class DrawPanel extends JPanel{
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
            Graphics2D g2d = (Graphics2D) g;

            Color borderColor = Color.GRAY;
            int borderWidth = 1;

            g2d.setStroke(new BasicStroke(borderWidth));


            for (MyRectangle myRectangle : myRectangles) {
                g2d.setColor(myRectangle.colour);
                g2d.fillRect(myRectangle.x, myRectangle.y - myRectangle.height, myRectangle.width, myRectangle.height);

                g2d.setColor(borderColor);
                g2d.drawRect(myRectangle.x, myRectangle.y - myRectangle.height, myRectangle.width, myRectangle.height);

            }
        }

    }
}
