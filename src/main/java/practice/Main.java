package practice;

import org.jfree.chart.ChartPanel;
import practice.ui.UserFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("org.pushingpixels.radiance.theming.api.skin.RadianceBusinessBlackSteelLookAndFeel");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                UserFrame ui = new UserFrame();
                ui.getForeground();
                ui.setForeground(Color.WHITE);
                ui.setBackground(Color.WHITE);
                ui.pack();
                ui.setVisible(true);
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            }
        });

    }
}
