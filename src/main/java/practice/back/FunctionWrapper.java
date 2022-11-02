package practice.back;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;
import java.util.List;

public class FunctionWrapper {
    Function bodyFunction;
    double a = -10;
    double b = 10;
    double eps = 0.01;

    XYSeries mainLine = new XYSeries(0);
    public XYSeries top = new XYSeries("2");
    XYSeries left = new XYSeries("3");
    XYSeries right = new XYSeries("4");
    XYSeries midl =  new XYSeries("5");

    XYSeriesCollection collection ;
    List<Double> coordinatesOnX = new ArrayList<>();
    XYDataset xyDataset;

    public FunctionWrapper(Function bodyFunction) {
        this.bodyFunction = bodyFunction;
        setCoordinates(a, b, eps);
        collection = new XYSeriesCollection(mainLine);
        collection.addSeries(top);
        collection.addSeries(left);
        collection.addSeries(right);
        collection.addSeries(midl);
        this.xyDataset = collection;
        setDataset();
    }

    public void changeBodyFunction(Function bodyFunction)
    {
        this.bodyFunction = bodyFunction;
        setCoordinates(a, b, eps);
        setDataset();
    }

    public void setCoordinates(double a, double b,
                                double eps)
    {
        if (a >= b)
            throw new IllegalArgumentException("Конец интервала не может быть меньше начала!");
        if (eps < 0 || eps > 1)
            throw new IllegalArgumentException("Значение точности содержится в пределе от 0 до 1!");
        coordinatesOnX.clear();
        this.a = a;
        this.b = b;
        this.eps = eps;
        double tmp = a;
        while( tmp <= b)
        {
            coordinatesOnX.add(tmp);
            tmp = tmp+eps;
        }
        setDataset();
    }

    public void setDefaultCoordinates()
    {
        coordinatesOnX.clear();
        this.a = -10;
        this.b = 10;
        this.eps = 0.01;
        double tmp = a;
        while( tmp <= b)
        {
            coordinatesOnX.add(tmp);
            tmp = tmp+eps;
        }
        setDataset();
    }

    public void setDataset() {
        mainLine.clear();
        for (Double x : coordinatesOnX) {
            mainLine.add(x, bodyFunction.f(x));
        }
    }

    public XYDataset getXyDataset()
    {
        return xyDataset;
    }

}