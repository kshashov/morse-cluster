package com.cluster.math.utils;

import com.cluster.math.TestExecutor;
import com.cluster.math.model.Conformation;
import com.cluster.math.model.Vertex;
import com.github.lbfgs4j.LbfgsMinimizer;
import com.github.lbfgs4j.liblbfgs.Function;
import com.github.lbfgs4j.liblbfgs.LbfgsConstant;

import java.util.ArrayList;

/**
 * Created by envoy on 28.04.2017.
 */
public class Lbfgs {

    public static Conformation minimize(Conformation conformation) {
        ArrayList<Vertex> vertices = conformation.getVertices();
        double[] input = new double[vertices.size() * 3];
        int j = 0;
        for (int i = 0; i < vertices.size(); i++) {
            input[j++] = vertices.get(i).getX();
            input[j++] = vertices.get(i).getY();
            input[j++] = vertices.get(i).getZ();
        }

        LbfgsConstant.LBFGS_Param params = new LbfgsConstant.LBFGS_Param(com.github.lbfgs4j.liblbfgs.Lbfgs.defaultParams());
        params.epsilon = TestExecutor.getConfig().getLO_EPS();
        params.max_iterations = TestExecutor.getConfig().getLO_MAX_ITERATIONS();

        MorseFunction morseFunction = new MorseFunction(input.length);
        LbfgsMinimizer minimizer = new LbfgsMinimizer(params, false);
        double[] output = minimizer.minimize(morseFunction, input);
        double min = morseFunction.valueAt(output);

        ArrayList<Vertex> verticesOpt = arrayToCollection(output);

        return new Conformation(conformation.getBits(), verticesOpt, min);
    }

    private static ArrayList<Vertex> arrayToCollection(double[] x) {
        ArrayList<Vertex> vertices = new ArrayList<>(x.length / 3);
        int j = 0;
        for (int i = 0; i < x.length / 3; i++) {
            vertices.add(new Vertex(x[j++], x[j++], x[j++]));
        }

        return vertices;
    }

    public static class MorseFunction implements Function {
        private int size;

        public MorseFunction(int size) {
            this.size = size;
        }

        @Override
        public int getDimension() {
            return size;
        }

        @Override
        public double valueAt(double[] x) {
            return ClusterMath.getEnergy(arrayToCollection(x));
        }

        @Override
        public double[] gradientAt(double[] x) {
            double[] grad = new double[x.length];
            ArrayList<Vertex> vertices = arrayToCollection(x);
            for (int k = 0; k < vertices.size(); k++) {
                for (int i = 0; i < vertices.size(); i++) {
                    if (k != i) {
                        Vertex xk = new Vertex(vertices.get(k).getX(), vertices.get(k).getY(), vertices.get(k).getZ());
                        Vertex xi = new Vertex(vertices.get(i).getX(), vertices.get(i).getY(), vertices.get(i).getZ());
                        double rki = xk.distanceTo(xi);

                        double[] aki = new double[3];
                        aki[0] = (xk.getX() - xi.getX()) / rki;
                        aki[1] = (xk.getY() - xi.getY()) / rki;
                        aki[2] = (xk.getZ() - xi.getZ()) / rki;

                        double fki = 2 * TestExecutor.getConfig().getRO() * (Math.exp(TestExecutor.getConfig().getRO() * (1 - rki)) - Math.exp(2 * TestExecutor.getConfig().getRO() * (1 - rki)));
                        grad[3 * k + 0] += fki * aki[0];
                        grad[3 * k + 1] += fki * aki[1];
                        grad[3 * k + 2] += fki * aki[2];
                    }
                }
            }

            return grad;
        }
    }
}
