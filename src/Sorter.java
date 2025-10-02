import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Sorter {
    private final List<MyRectangle> rectangles;
    private final Visualiser.DrawPanel drawPanel;
    private Timer timer;

    public Sorter(List<MyRectangle> rectangles, Visualiser.DrawPanel drawPanel) {
        this.rectangles = rectangles;
        this.drawPanel = drawPanel;
    }


    public void bubbleSort(){
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
                        //Set the colour of the currently viewed rectangles
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

    public void selectionSort() {
        final int[] i = {0};        // outer loop index
        final int[] j = {1};        // inner loop index
        final int[] min = {0};      // index of current minimum

        Timer timer = new Timer(5, null);
        timer.addActionListener(e -> {
            // Reset all colors each step
            for (MyRectangle r : rectangles) {
                r.colour = java.awt.Color.BLACK;
            }

            int n = rectangles.size();

            if (i[0] < n - 1) {
                // Highlight the current range
                rectangles.get(i[0]).colour = java.awt.Color.GREEN; // sorted part
                rectangles.get(min[0]).colour = java.awt.Color.RED; // current min
                if (j[0] < n) {
                    rectangles.get(j[0]).colour = java.awt.Color.BLUE; // currently checking

                    if (rectangles.get(j[0]).value < rectangles.get(min[0]).value) {
                        min[0] = j[0]; // found new min
                    }
                    j[0]++;
                } else {
                    // Swap once per outer loop
                    if (min[0] != i[0]) {
                        int tempX = rectangles.get(i[0]).x;
                        rectangles.get(i[0]).x = rectangles.get(min[0]).x;
                        rectangles.get(min[0]).x = tempX;

                        MyRectangle temp = rectangles.get(i[0]);
                        rectangles.set(i[0], rectangles.get(min[0]));
                        rectangles.set(min[0], temp);
                    }

                    // Move to next i
                    i[0]++;
                    min[0] = i[0];
                    j[0] = i[0] + 1;
                }

                drawPanel.repaint();
            } else {
                // Mark last element sorted and stop
                rectangles.get(n - 1).colour = java.awt.Color.GREEN;
                drawPanel.repaint();
                timer.stop();
            }
        });

        timer.start();
    }

    public void insertionSort() {
        final int[] i = {1};   // index of the "key" we are inserting
        final int[] j = {0};   // index for shifting
        final MyRectangle[] key = {null};

        timer = new Timer(5, null);
        timer.addActionListener(e -> {
            // Reset colours each step
            for (MyRectangle r : rectangles) {
                r.colour = Color.BLACK;
            }

            int n = rectangles.size();

            if (i[0] < n) {
                if (key[0] == null) {
                    // pick up the key
                    key[0] = rectangles.get(i[0]);
                    j[0] = i[0] - 1;
                    rectangles.get(i[0]).colour = Color.RED; // highlight the key
                } else if (j[0] >= 0 && rectangles.get(j[0]).value > key[0].value) {
                    // shift one element to the right
                    rectangles.set(j[0] + 1, rectangles.get(j[0]));
                    rectangles.get(j[0] + 1).x = (j[0] + 1) * 10 + 10; // update x
                    rectangles.get(j[0]).colour = Color.BLUE;
                    j[0]--;
                } else {
                    // insert the key
                    rectangles.set(j[0] + 1, key[0]);
                    rectangles.get(j[0] + 1).x = (j[0] + 1) * 10 + 10; // update x
                    key[0] = null; // reset for next round
                    i[0]++;
                }
                drawPanel.repaint();
            } else {
                timer.stop(); // fully sorted
            }
        });

        timer.start();
    }

}
