package practice.ui;

import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.pushingpixels.radiance.theming.api.renderer.RadianceDefaultListCellRenderer;
import org.pushingpixels.radiance.theming.internal.ui.RadianceButtonUI;
import practice.back.Function;
import practice.back.FunctionWrapper;
import practice.back.GoldenSection;
import practice.back.HintTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserFrame extends ApplicationFrame {
    Function function = (x) -> 3 * Math.pow(x, 4) - 4 * Math.pow(x, 3) - 12 * x * x + 2;
    Function function1 = (x) -> x - Math.pow(x, 3) / 6 + Math.pow(x, 5) / 120;
    Function function2 = (x) -> 3 * x * x - 8 * Math.sin(2 * x) - 2 * x;
    Function function3 = (x) -> -Math.pow(x, 3) + 12 * Math.sin(3 * x) - 5 * x;
    Function function4 = (x) -> x * x - 5 * x + 6.25;
    ChartPanel chartPanel;
    FunctionWrapper functionWrapper = new FunctionWrapper(function);

    private final HintTextField start = new HintTextField("-10", SwingConstants.LEFT);
    private final HintTextField finish = new HintTextField("10", SwingConstants.LEFT);
    private final HintTextField epsilon = new HintTextField("0.001", SwingConstants.LEFT);
    private JButton button;
    private static JLabel answer;

    public UserFrame() {

        super("Поиск экстремума функции методом золотого сечения");

        chartPanel = getGhartPanel();
        chartPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        JPanel left = getLeftPanel();
        button.addActionListener(e -> {
            try {
                functionWrapper.setCoordinates(Double.parseDouble(start.getText()),
                    Double.parseDouble(finish.getText()),
                    Double.parseDouble(epsilon.getText()));
                new GoldenSection(functionWrapper, answer).execute();
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Заполните пустые поля!");
                start.rebase();
                finish.rebase();
                epsilon.rebase();
            }
            catch (IllegalArgumentException ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
                start.rebase();
                finish.rebase();
                epsilon.rebase();
            }
        });
        left.setAlignmentY(Component.TOP_ALIGNMENT);
        left.setBorder(new EmptyBorder(0, 10, 10, 10));
        left.setMaximumSize(new Dimension(250, 360));
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.LINE_AXIS));
        main.add(chartPanel);
        main.add(left);
        main.setComponentOrientation(
                ComponentOrientation.LEFT_TO_RIGHT);
        this.setContentPane(main);
        this.pack();
        this.setVisible(true);
    }

    private JPanel getLeftPanel() {
        JPanel startInterval = textField("Начало интервала:", start);
        startInterval.setAlignmentX(SwingConstants.LEFT);
        JPanel finishInterval = textField("Конец интервала:", finish);
        finishInterval.setAlignmentX(SwingConstants.LEFT);
        JPanel eps = textField("Точность:", epsilon);
        eps.setAlignmentX(SwingConstants.LEFT);
        JLabel min = new JLabel("Точка экстремума:");
        min.setAlignmentX(SwingConstants.LEFT);
        answer = new JLabel("  ");
        answer.setAlignmentX(SwingConstants.LEFT);
        button = new JButton();
        button.setText("Найти минимум");
        button.setUI((ButtonUI) RadianceButtonUI.createUI(button));
        button.setAlignmentX(SwingConstants.LEFT);
        JPanel left = new JPanel();
        left.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));

        var comboBox = popupMenu();
        comboBox.addActionListener(event -> {
            functionWrapper.setDefaultCoordinates();
            functionWrapper.top.clear();
            start.rebase();
            finish.rebase();
            epsilon.rebase();
            answer.setText("");
            int geg = Integer.parseInt(Objects.requireNonNull(comboBox.getSelectedItem()).toString());
            switch (geg) {
                case 1 -> functionWrapper.changeBodyFunction(function);
                case 2 -> functionWrapper.changeBodyFunction(function1);
                case 3 -> functionWrapper.changeBodyFunction(function2);
                case 4 -> functionWrapper.changeBodyFunction(function3);
                case 5 -> functionWrapper.changeBodyFunction(function4);
            }
        });
        comboBox.setAlignmentX(SwingConstants.LEFT);
        left.add(Box.createRigidArea(new Dimension(0, 35)));
        left.add(comboBox);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(startInterval);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(finishInterval);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(eps);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(min);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(answer);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(button);
        left.add(Box.createRigidArea(new Dimension(0, 10)));

        return left;
    }

    public static class MyInputVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            try {
                Double.valueOf(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }


    private JPanel textField(String text, JTextField textField) {
        JLabel a = new JLabel(text, SwingConstants.LEFT);
        textField.setInputVerifier(new MyInputVerifier());
        textField.setBackground(Color.WHITE);
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(a);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textField);
        return panel;
    }

    private ChartPanel getGhartPanel() {
        JFreeChart chart = ChartFactory.createXYLineChart("График функции",
                "x", "y", functionWrapper.getXyDataset());

        var plot = chart.getXYPlot();
        plot.setDomainGridlinePaint(new Color(47, 79, 79));
        plot.setRangeGridlinePaint(new Color(47, 79, 79));
        plot.setBackgroundPaint(Color.WHITE);
        chart.setBorderVisible(false);
        chart.getLegend().setVisible(false);
        chart.setBackgroundPaint(new Color(228, 232, 240));
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2));
        renderer.setSeriesPaint(0, new Color(72, 180, 224));
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesLinesVisible(1,false);
        //renderer.setSeriesStroke(1, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        renderer.setSeriesPaint(1, new Color(225, 41, 0));
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesStroke(2, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        renderer.setSeriesPaint(2, new Color(225, 41, 0));
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesStroke(3, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        renderer.setSeriesPaint(3, new Color(225, 41, 0));
        renderer.setSeriesShapesVisible(3, true);
        renderer.setSeriesStroke(4, new BasicStroke(2));
        renderer.setSeriesPaint(4, new Color(225, 41, 0));
        renderer.setSeriesShapesVisible(4, true);
        plot.setRenderer(renderer);
        return new ChartPanel(chart);
    }

    private @NotNull
    JComboBox popupMenu() {
        Map<String, String> comboFunctions = new HashMap<>();

        comboFunctions.put("1", "3x^4 - 4x^3 - 12x^2 + 2");
        comboFunctions.put("2", "x - (x^3)/6 + (x^5)/120");
        comboFunctions.put("3", "3 * x^2 - 8sin(2x) - 2x");
        comboFunctions.put("4", "-x^3 + 12sin(3x) - 5x");
        comboFunctions.put("5", "x^2 - 5x + 6.25");

        var comboBox = new JComboBox();
        comboBox.setRenderer(new MapRenderer(comboFunctions));
        comboBox.addItem("1");
        comboBox.addItem("2");
        comboBox.addItem("3");
        comboBox.addItem("4");
        comboBox.addItem("5");
        comboBox.setFont(new Font("Times New Roman", Font.ITALIC, 20));

        return comboBox;
    }

    static class MapRenderer extends RadianceDefaultListCellRenderer {
        private final Map<String, String> functionMap;

        public MapRenderer(Map<String, String> functionMap) {
            this.functionMap = functionMap;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setMinimumSize(new Dimension(300, 60));

            String geg = value.toString();
            String text = switch (geg) {
                case "1" -> functionMap.get("1");
                case "2" -> functionMap.get("2");
                case "3" -> functionMap.get("3");
                case "4" -> functionMap.get("4");
                case "5" -> functionMap.get("5");
                default -> "none";
            };
            label.setText(text);
            return label;
        }
    }

}


