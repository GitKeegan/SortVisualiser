import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Visualiser extends JPanel implements ActionListener{
    final private DrawPanel drawPanel;

    final private JComboBox sortSelector;

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

        sortSelector = new JComboBox();
        sortSelector.addItem("BubbleSort");
        sortSelector.addItem("SelectionSort");
        sortSelector.addItem("InsertionSort");

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(drawPanel, BorderLayout.CENTER);

        frame.add(sortSelector, BorderLayout.SOUTH);

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
            Sorter sorter = new Sorter(rectangles, drawPanel);
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
        else if (command.equals("Reset")){
            drawPanel.clear();
            position = 10;
            for (int x = 0; x < 70; x++){
                drawPanel.addRectangle(new MyRectangle(position, 575, (int)(Math.random()*500)+1));
                position += 10;
            }
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
            for (MyRectangle myRectangle : myRectangles) {
                g.setColor(myRectangle.colour);
                g.fillRect(myRectangle.x, myRectangle.y - myRectangle.height, myRectangle.width, myRectangle.height);
            }
        }

    }
}
