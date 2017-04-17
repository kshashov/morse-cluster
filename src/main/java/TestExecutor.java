import jogamp.opengl.openal.av.ALAudioSink;
import matlabcontrol.*;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.lights.Light;
import org.omg.CORBA.MARSHAL;

import java.util.ArrayList;

/**
 * Created by envoy on 05.03.2017.
 */
public class TestExecutor extends AbstractAnalysis {
    private static ArrayList<ArrayList<Coord3d>> shapes = new ArrayList<ArrayList<Coord3d>>();

    public static void main(String[] args) {
        // create proxy
        MatlabProxyFactoryOptions options =
                new MatlabProxyFactoryOptions.Builder()
                        .setUsePreviouslyControlledSession(true)
                        .build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        try {
            execute(factory);
        } catch (MatlabConnectionException e) {
            e.printStackTrace();
        } catch (MatlabInvocationException e) {
            e.printStackTrace();
        }
    }

    public static void execute(MatlabProxyFactory factory) throws MatlabConnectionException, MatlabInvocationException {
        MatlabProxy proxy = factory.getProxy();

        // call user-defined function (must be on the path)
        proxy.eval("addpath('D:\\Google Disk Files\\ДИССЕРТАЦИЯ\\27.02 версия 4\\')");
        Object[] out = proxy.returningFeval("MAIN_MONTE_KARLO_2017_GRAPH", 1, null);


        //MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
        //MatlabNumericArray array = processor.getNumericArray("array");

        //System.out.println("entry: " + array.getRealValue(2, 1, 0));
        //double[][][] javaArray = array.getRealArray3D();
        //System.out.println("entry: " + javaArray[2][1][0]);
        double[] arr = (double[]) out[0];
        ArrayList<Coord3d> shape = new ArrayList<Coord3d>();
        int size = arr.length / 3;
        for (int i = 0; i < size; i++) {
            Coord3d coord3d = new Coord3d(arr[i], arr[i + size], arr[i + 2 * size]);
            shape.add(coord3d);
        }

        shapes.add(shape);
        // close connection
        proxy.eval("rmpath('D:\\Google Disk Files\\ДИССЕРТАЦИЯ\\27.02 версия 4\\')");
        proxy.disconnect();


        if (shapes.isEmpty()) {
            return;
        }

        try {
            AnalysisLauncher.open(new TestExecutor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        ArrayList<Coord3d> shape = shapes.get(0);

        double dist = 0.0f;
        int j = 0;
        for (Coord3d coord : shape) {
            Polygon polygon = new Polygon();
        /*    for (Coord3d coord2 : shape) {
                dist = Math.sqrt((coord.x-coord2.x)*(coord.x-coord2.x) + (coord.y-coord2.y)*(coord.y-coord2.y) + (coord.z-coord2.z)*(coord.z-coord2.z));
                if (dist <= 1.1) {
                    polygon.add(new Point(coord2));
                }
            }

            polygon.setFaceDisplayed(false);
            polygon.setWireframeDisplayed(false);*/
            Sphere sphere = new Sphere(coord, 0.5f, 20, new Color(100, 100, 100, 255));
            sphere.setWireframeColor(new Color(150, 150, 150, 255));
            sphere.setWireframeDisplayed(false);
            sphere.setWireframeWidth(1);
            sphere.setFaceDisplayed(true);

            Shape sh = new Shape();
            sh.add(sphere);
            System.out.println("x:" + coord.x + " y:" + coord.y + " z:" + coord.z);
            //polygon.setColor(new Color(j*10, j*10, j*10));
            //chart.getScene().getGraph().add(polygon);
            //chart.getScene().getGraph().add(polygon);
            chart.getScene().getGraph().add(sphere);
            j++;
        }
        Light light = new Light();
        light.setPosition(new Coord3d(chart.getScene().getGraph().getBounds().getXmax() + 10,
                chart.getScene().getGraph().getBounds().getYmax() + 10,
                chart.getScene().getGraph().getBounds().getZmax() + 10));
        chart.getScene().getLightSet().add(light);
    }
}
