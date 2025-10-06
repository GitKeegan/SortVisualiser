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
    private final JTextField rectangleField;
    public JLabel elapsedTimeField = new JLabel("");

    public void setSortingStatus(boolean state){
        isSorting = state;
    }

    int position = 10;
    public Visualiser(){
        //Creates a frame for the application to work in.
        JFrame frame = new JFrame("Visualiser");

        //Creates 3 buttons to start, stop and reset the sort.
        JButton sortButton = new JButton("Sort");
        JButton addButton = new JButton("Reset");
        JButton stopButton = new JButton("Stop");
        sortButton.addActionListener(this);
        addButton.addActionListener(this);
        stopButton.addActionListener(this);

        //Creates a separate panel for the buttons to be based in .
        rectangleField = new JTextField("70", 5); //A text box determining the number of rectangles to be used (Default = 70).
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.add(sortButton);
        buttonPanel.add(addButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(rectangleField);
        buttonPanel.setBackground(Color.BLACK);

        //A selector box to pick which sort you want to use (Default = BubbleSort).
        sortSelector = new JComboBox();
        sortSelector.addItem("BubbleSort");
        sortSelector.addItem("SelectionSort");
        sortSelector.addItem("InsertionSort");
        sortSelector.addItem("BogoSort");

        //Another panel containing the label showing elapsed time (Invisible upon first boot and reset).
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        timePanel.setBackground(Color.BLACK);
        timePanel.add(elapsedTimeField);
        elapsedTimeField.setForeground(Color.WHITE);

        //A panel to contain all the rectangles within.
        drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(800, 600));
        drawPanel.setBackground(Color.BLACK);

        //Adds all the created panels to the main frame.
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(drawPanel, BorderLayout.CENTER);
        frame.add(sortSelector, BorderLayout.SOUTH);
        frame.add(timePanel, BorderLayout.NORTH);
        frame.getContentPane().setBackground(Color.BLACK); //Sets the panel to dark mode
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //Adds all the rectangles to the screen (70 on startup).
        position = 10;
        for (int x = 0; x < 70; x++){
            drawPanel.addRectangle(new MyRectangle(position, 575, (int)(Math.random()*500)+1));
            position += 10;
        }

        isSorting = false; //Keeps track of whether the app is currently sorting.
    }

    public static void main(String[] args) {
        new Visualiser();
    }


    //The action listener for the buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        List<MyRectangle> rectangles = drawPanel.getRectangles(); //Gets the rectangles to be passed to the sorter.

        //If the sort button is pressed, and the program is not currently sorting, execute this.
        if (command.equals("Sort") && !isSorting) {
            Sorter sorter = new Sorter(rectangles, drawPanel, this);
            String sortSelected = sortSelector.getSelectedItem().toString(); //Gets the sort selected in the drop-down box.

            if (sortSelected.equals("BubbleSort")) {
                sorter.bubbleSort();
            }

            else if (sortSelected.equals("SelectionSort")) {
                sorter.selectionSort();
            }

            else if (sortSelected.equals("InsertionSort")) {
                sorter.insertionSort();
            }

            else if (sortSelected.equals("BogoSort")) {
                sorter.bogoSort();
            }
        }

        //If the reset button is pressed, and the program isn't sorting, execute this.
        else if (command.equals("Reset") && !isSorting) {
            elapsedTimeField.setText(""); //Make the time elapsed invisible again
            drawPanel.clear(); //Clear all rectangles
            position = 10; //Start placing rectangles from x = 10.
            int inputtedRectangleNumber;

            try{
                inputtedRectangleNumber = Integer.parseInt(rectangleField.getText().trim());
                if (inputtedRectangleNumber < 1 || inputtedRectangleNumber > 75) {
                    inputtedRectangleNumber = 70;
                    JOptionPane.showMessageDialog(null, "Please enter a number between 1 and 75");
                }
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Invalid number");
                inputtedRectangleNumber = 70;
            }

            for (int x = 0; x < inputtedRectangleNumber; x++){
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
        private final List<MyRectangle> myRectangles = new ArrayList<>(); //A list of rectangle objects.

        //Adds a rectangle to the list and to the screen.
        public void addRectangle(MyRectangle myRectangle){
            myRectangles.add(myRectangle);
            repaint();
        }

        //Deletes all rectangles.
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

            //Draws the rectangle to the screen.
            for (MyRectangle myRectangle : myRectangles) {
                g2d.setColor(myRectangle.colour);
                g2d.fillRect(myRectangle.x, myRectangle.y - myRectangle.height, myRectangle.width, myRectangle.height); //Draws a white rectangle.

                g2d.setColor(borderColor);
                g2d.drawRect(myRectangle.x, myRectangle.y - myRectangle.height, myRectangle.width, myRectangle.height); //Draws a grey border around each rectangle.

            }
        }

    }
}
