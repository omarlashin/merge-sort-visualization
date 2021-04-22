import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sorting {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored){}
        SortingFrame frame = new SortingFrame();
        frame.setVisible(true);
    }
}

class SortingFrame extends JFrame implements ActionListener {
    private final int size = 100;
    private final int[] A = new int[size];
    private final int side = 5;
    private final int maxValue = 100;
    private final Random random = new Random();
    private final JButton btn = new JButton("Start");
    private final SortingPanel pnl = new SortingPanel(this.A, this.side);

    public SortingFrame() {
        this.add(this.btn, BorderLayout.NORTH);
        this.btn.addActionListener(this);
        this.add(this.pnl);
        this.pnl.setPreferredSize(new Dimension(this.size * this.side, this.maxValue * this.side));
        this.pack();
        this.setTitle("Sorting Visualization");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(100, 100);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Thread sorter = new Thread() {
            @Override
            public void run() {
                btn.setEnabled(false);
                for (int i = 0; i < A.length; i++)
                    A[i] = random.nextInt(maxValue) + 1;
                repaint();
                try {
                    Thread.sleep(500);
                }
                catch (Exception ignored){}
                mergeSort(A, 0, size);
                btn.setEnabled(true);
            }
        };
        sorter.start();
    }

    private void mergeSort(int[] A, int start, int stop) {
        if (stop - start > 1) {
            int center = start + (stop - start) / 2;
            mergeSort(A, start, center);
            mergeSort(A, center, stop);
            merge(A, start, center, stop);
        }
    }
    private void merge(int[] A, int start, int center, int stop) {
        int[] L = new int[center - start];
        int[] R = new int[stop - center];
        System.arraycopy(A, start, L, 0, L.length);
        System.arraycopy(A, center, R, 0, R.length);
        for (int i = start, j = 0, k = 0; i < stop; i++) {
            if (j == L.length) {
                A[i] = R[k];
                k++;
            }
            else if (k == R.length) {
                A[i] = L[j];
                j++;
            }
            else if (L[j] < R[k]) {
                A[i] = L[j];
                j++;
            }
            else {
                A[i] = R[k];
                k++;
            }
            this.repaint();
            try {
                Thread.sleep(50);
            }
            catch (Exception ignored){}
        }
    }
}

class SortingPanel extends JPanel {
    private final int[] A;
    private final int side;

    public SortingPanel(int[] A, int side) {
        this.A = A;
        this.side = side;
    }

    @Override
    public void paint(Graphics gfx) {
        super.paint(gfx);
        for (int i = 0; i < A.length; i++)
            gfx.fillRect(i * side, this.getHeight() - A[i] * side, side, A[i] * side);
    }
}