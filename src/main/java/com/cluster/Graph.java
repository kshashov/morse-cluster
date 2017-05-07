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
import org.jzy3d.plot3d.rendering.lights.Light;
import org.jzy3d.plot3d.rendering.lights.LightSet;

import java.util.ArrayList;
import java.util.List;

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
            // sphere.setWireframeColor(new Color(0, 0, 0));
            chart.getScene().getGraph().add(sphere);
            bounds.add(new BoundingBox3d(sphere.getBounds().getXmin(), sphere.getBounds().getXmax(), sphere.getBounds().getYmin(), sphere.getBounds().getYmax(), sphere.getBounds().getZmin(), sphere.getBounds().getZmax()));
        }
        //chart.setScale(new Scale(bounds.getZmin(), bounds.getZmax()));
        // Create a chart
        float dist1 = 5;
        float dist = 5;
        bounds.setXmin(bounds.getXmin() - dist1);
        bounds.setYmin(bounds.getYmin() - dist1);
        bounds.setZmin(bounds.getZmin() - dist1);
        bounds.setXmax(bounds.getXmax() + dist);
        bounds.setYmax(bounds.getYmax() + dist);
        bounds.setZmax(bounds.getZmax() + dist);
        Color color = new Color(20, 20, 20);

        List<Light> lights = new ArrayList<>(4);
        Light l1 = new Light(0, true, false);
        l1.setPosition(new Coord3d(bounds.getXmin(), bounds.getYmin(), bounds.getZmin()));
        l1.setAmbiantColor(color);
        Light l2 = new Light(1, true, false);
        l2.setPosition(new Coord3d(bounds.getXmin(), bounds.getYmax(), bounds.getZmax()));
        l2.setAmbiantColor(color);
        Light l3 = new Light(2, true, false);
        l3.setPosition(new Coord3d(bounds.getXmax(), bounds.getYmax(), bounds.getZmin()));
        l3.setAmbiantColor(color);
        Light l4 = new Light(3, true, false);
        l4.setPosition(new Coord3d(bounds.getXmax(), bounds.getYmin(), bounds.getZmax()));
        l4.setAmbiantColor(color);

        lights.add(l1);
        lights.add(l2);
        lights.add(l3);
        lights.add(l4);
        chart.setAxeDisplayed(false);
        chart.getScene().setLightSet(new LightSet(lights));

    }

    @Override
    public String getName() {
        return "График конформации";
    }


}
