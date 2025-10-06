import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Sorter {
    private final List<MyRectangle> rectangles;
    private final Visualiser.DrawPanel drawPanel;
    private Timer timer;
    private final SoundHelper soundHelper = new SoundHelper();
    private final Visualiser visualiser;
    long time;

    ExecutorService soundExecutor = Executors.newFixedThreadPool(5);

    public Sorter(List<MyRectangle> rectangles, Visualiser.DrawPanel drawPanel, Visualiser visualiser) {
        this.rectangles = rectangles;
        this.drawPanel = drawPanel;
        this.visualiser = visualiser;
    }

    public void bubbleSort(){
        time = System.currentTimeMillis();
        visualiser.setSortingStatus(true);

        final int[] i = {0};
        final int[] j = {0};
        final boolean[] swapped = {false};

        timer = new Timer(5, null); // runs every 50ms
        timer.addActionListener(e1 -> {
            for (MyRectangle r : rectangles) {
                r.colour = Color.WHITE;
            }


            if (i[0] < rectangles.size() - 1) {
                if (visualiser.shouldStop){
                    timer.stop();
                    visualiser.setSortingStatus(false);
                    visualiser.shouldStop = false;
                }

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
                    soundExecutor.submit(() -> SoundHelper.playTone(rectangles.get(j[0]).value, 5));
                    j[0]++;
                }
                else {
                    if (!swapped[0]) {
                        timer.stop(); // stop if no swaps in this pass
                        playCompletionSweep();
                    }
                    j[0] = 0;
                    i[0]++;
                    swapped[0] = false;
                }
                drawPanel.repaint();
            } else {
                timer.stop(); // fully sorted
                playCompletionSweep();

            }
        });
        timer.start();
    }

    public void selectionSort() {
        time = System.currentTimeMillis();
        visualiser.setSortingStatus(true);

        final int[] i = {0};        // outer loop index
        final int[] j = {1};        // inner loop index
        final int[] min = {0};      // index of current minimum

        timer = new Timer(5, null);
        timer.addActionListener(e -> {
            // Reset all colors each step
            for (MyRectangle r : rectangles) {
                r.colour = java.awt.Color.WHITE;
            }

            int n = rectangles.size();

            if (i[0] < n - 1) {
                if (visualiser.shouldStop){
                    timer.stop();
                    visualiser.setSortingStatus(false);
                    visualiser.shouldStop = false;
                }

                // Highlight the current range
                rectangles.get(i[0]).colour = java.awt.Color.GREEN; // sorted part
                rectangles.get(min[0]).colour = java.awt.Color.RED;// current min
                soundExecutor.submit(() -> SoundHelper.playTone(rectangles.get(min[0]).value, 5));
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
                playCompletionSweep();
            }
        });

        timer.start();
    }

    public void insertionSort() {
        time = System.currentTimeMillis();
        visualiser.setSortingStatus(true);

        final int[] i = {1};   // index of the "key" we are inserting
        final int[] j = {0};   // index for shifting
        final MyRectangle[] key = {null};

        timer = new Timer(5, null);
        timer.addActionListener(e -> {
            // Reset colours each step
            for (MyRectangle r : rectangles) {
                r.colour = Color.WHITE;
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
                    soundExecutor.submit(() -> SoundHelper.playTone(rectangles.get(j[0]).value, 5));
                    j[0]--;
                } else {
                    // insert the key
                    rectangles.set(j[0] + 1, key[0]);
                    rectangles.get(j[0] + 1).x = (j[0] + 1) * 10 + 10; // update x
                    key[0] = null; // reset for next round
                    i[0]++;
                }
                drawPanel.repaint();

                if (visualiser.shouldStop){
                    timer.stop();
                    visualiser.setSortingStatus(false);
                    visualiser.shouldStop = false;
                }
            }

            else {
                playCompletionSweep();
                timer.stop(); // fully sorted

            }
        });

        timer.start();


    }

    public void bogoSort() {
        time = System.currentTimeMillis();
        visualiser.setSortingStatus(true);

        timer = new Timer(5, null);
        timer.addActionListener(e -> {
           Collections.shuffle(rectangles);
           drawPanel.repaint();

           for (int i = 0; i < rectangles.size(); i++) {
               rectangles.get(i).x = 10 + 10 * i;
           }
           if (isSorted()){
               drawPanel.repaint();
               playCompletionSweep();
               timer.stop();
           }
           if (visualiser.shouldStop){
               timer.stop();
               visualiser.setSortingStatus(false);
               visualiser.shouldStop = false;
           }
        });
        timer.start();

    }

    private boolean isSorted() {
        for (int i = 0; i < rectangles.size() - 1; i++) {
            if (rectangles.get(i).value > rectangles.get(i + 1).value) {
                int finalI = i;
                soundExecutor.submit(() -> SoundHelper.playTone(rectangles.get(finalI).value, 5));
                soundExecutor.submit(() -> SoundHelper.playTone(rectangles.get(finalI + 1).value, 5));

                return false;
            }
        }
        return true;
    }

    private void playCompletionSweep() {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - time;
        visualiser.elapsedTimeField.setText(elapsedTime + "ms");


        final int[] sweepIndex = {0};

        // Create a new, slower timer just for the sweep effect (e.g., 25ms per rectangle)
        Timer sweepTimer = new Timer(10, null);

        sweepTimer.addActionListener(e -> {
            int index = sweepIndex[0];
            int n = rectangles.size();

            if (index < n) {
                MyRectangle r = rectangles.get(index);

                // 1. Set the color to GREEN
                r.colour = Color.GREEN;

                // 2. Play the tone corresponding to the rectangle's value
                // Use a longer duration than the rapid comparison beeps (e.g., 50ms)
                soundExecutor.submit(() -> SoundHelper.playTone(r.value, 50));

                // 3. Redraw the panel
                drawPanel.repaint();

                sweepIndex[0]++;
            } else {
                sweepTimer.stop(); // The sweep is complete
                visualiser.setSortingStatus(false);
            }
        });

        sweepTimer.start();
    }

}
