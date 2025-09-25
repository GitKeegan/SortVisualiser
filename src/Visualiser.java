import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visualiser implements ActionListener {
    public Visualiser(){
        JFrame frame = new JFrame("Visualiser");
        JPanel panel = new JPanel();

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 300, 300));
        panel.setLayout(new GridLayout(0,1));
        panel.add(sortButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Visualiser");
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new Visualiser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Hi");
    }
}
