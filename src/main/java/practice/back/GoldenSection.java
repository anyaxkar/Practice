package practice.back;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GoldenSection  extends SwingWorker<Double, Double> {

    final double PHI = (1 + Math.sqrt(5)) / 2;
    private FunctionWrapper functionWrapper;
    public JLabel answer;

    public GoldenSection(FunctionWrapper functionWrapper, JLabel answer) {
        super();
        this.functionWrapper = functionWrapper;
        this.answer = answer;
    }

    @Override
    protected Double doInBackground() throws Exception {

        double x1, x2;
        double a = functionWrapper.a;
        double b = functionWrapper.b;
        double e = functionWrapper.eps;
        int count = 0;
        do {
            x1 = b - (b - a) / PHI;
            x2 = a + (b - a) / PHI;
            publish(a, functionWrapper.bodyFunction.f(a),
                    b, functionWrapper.bodyFunction.f(b));
            if (count < 5)
                Thread.sleep(1000);
            if (functionWrapper.bodyFunction.f(x1) >= functionWrapper.bodyFunction.f(x2))
                a = x1;
            else
                b = x2;
            publish(a, functionWrapper.bodyFunction.f(a),
                    b, functionWrapper.bodyFunction.f(b));
            if (count < 5)
            Thread.sleep(1000);
            count++;
        } while (Math.abs(b - a) > e);
        return (a + b) / 2;
    }

    @Override
    protected void process(List<Double> chunks) {
        functionWrapper.top.clear();
        functionWrapper.top.add(chunks.get(0), chunks.get(1));
        functionWrapper.top.add(chunks.get(2), chunks.get(3));
        functionWrapper.left.clear();
        functionWrapper.left.add(chunks.get(0), chunks.get(1));
        functionWrapper.left.add(chunks.get(0), Double.valueOf(0));
        functionWrapper.right.clear();
        functionWrapper.right.add(chunks.get(2), Double.valueOf(0));
        functionWrapper.right.add(chunks.get(2), chunks.get(3));
        functionWrapper.midl.clear();
        functionWrapper.midl.add(chunks.get(0), Double.valueOf(0));
        functionWrapper.midl.add(chunks.get(2), Double.valueOf(0));
    }

    @Override
    protected void done() {
        double resultX=0, resultY;
        try {
            resultX = get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        resultY = functionWrapper.bodyFunction.f(resultX);
        functionWrapper.top.clear();
        functionWrapper.top.add(resultX, resultY);
        functionWrapper.left.clear();
        functionWrapper.right.clear();
        functionWrapper.midl.clear();
        answer.setText(String.format("(%.4f;%.4f)", resultX, resultY));
    }

}
