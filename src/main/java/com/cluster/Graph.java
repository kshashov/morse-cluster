package com.cluster; /**
 * Created by envoy on 07.03.2017.
 */

import com.cluster.math.model.Vertex;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Sphere;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;

public class Graph extends AbstractAnalysis {
    ArrayList<Vertex> vertices;

    public Graph(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void init() {
        if (vertices == null) {
            return;
        }

        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        BoundingBox3d bounds = new BoundingBox3d();
        for (Vertex vertex : vertices) {
            Sphere sphere = new Sphere(new Coord3d(vertex.getX(), vertex.getY(), vertex.getZ()), 0.5f, 30, new Color(170, 170, 170));
            sphere.setWireframeDisplayed(false);
            sphere.setWireframeColor(new Color(0, 0, 0));
            chart.getScene().getGraph().add(sphere);
            bounds.add(sphere.getBounds());
        }
        // Create a chart
        float dist1 = 3;
        float dist = 10;
        bounds.setXmin(bounds.getXmin() - dist1);
        bounds.setYmin(bounds.getYmin() - dist1);
        bounds.setZmin(bounds.getZmin() - dist1);
        bounds.setXmax(bounds.getXmax() + dist);
        bounds.setYmax(bounds.getYmax() + dist);
        bounds.setZmax(bounds.getZmax() + dist);

        chart.addLight(new Coord3d(bounds.getXmin(), bounds.getYmin(), bounds.getZmin())).setRepresentationDisplayed(false);
        chart.addLight(new Coord3d(bounds.getXmin(), bounds.getYmax(), bounds.getZmax())).setRepresentationDisplayed(false);
        chart.addLight(new Coord3d(bounds.getXmax(), bounds.getYmax(), bounds.getZmin())).setRepresentationDisplayed(false);
        chart.addLight(new Coord3d(bounds.getXmax(), bounds.getYmin(), bounds.getZmax())).setRepresentationDisplayed(false);

    }


}
