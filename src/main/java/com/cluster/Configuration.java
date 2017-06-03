package com.cluster;

import com.google.gson.Gson;
import com.shashov.cluster.math.config.*;
import com.shashov.cluster.math.model.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by envoy on 03.05.2017.
 */
public class Configuration {
    private static String DEFAULT_INPUT = "input.txt";
    private static String DEFAULT_OUTPUT = "output";
    private static String inputFileName;
    private static String outputFolderName;
    private static Config config;

    public static Config get() {
        return config;
    }

    public static void setupConfig(BufferedReader configFileReader) throws IOException {
        String userDir = System.getProperty("user.dir") + File.separator;
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = configFileReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        configFileReader.close();
        Params params;
        try {
            params = gson.fromJson(sb.toString().replace("\\", "\\\\"), Params.class);
        } catch (Exception e) {
            throw new IOException("Config cannot be parsed");
        }

        if ((params.INPUT_FILENAME == null) || params.INPUT_FILENAME.isEmpty()) {
            inputFileName = userDir + DEFAULT_INPUT;
        } else {
            inputFileName = params.INPUT_FILENAME;
        }

        if ((params.OUTPUT_FOLDERNAME == null) || params.OUTPUT_FOLDERNAME.isEmpty()) {
            outputFolderName = userDir + DEFAULT_OUTPUT;
        } else {
            outputFolderName = params.OUTPUT_FOLDERNAME;
        }

        List<Vertex> vertices = readVertices(new File(inputFileName));

        setupConfig(params, vertices);
    }

    private static void setupConfig(Params params, List<Vertex> vertices) {
        LOParams loParams = new LOParams.Builder()
                .setEps(params.LO_EPS)
                .setIterations(params.LO_MAX_ITERATIONS)
                .build();

        GrowAlgParams growAlgParams = new GrowAlgParams.Builder()
                .setEnergyDelta(params.TOP_MAX_ENERGY_DELTA)
                .setIterations(params.INF_SUP_ITERATIONS)
                .setMinDistance(params.DISTANCE_MIN)
                .build();

        StronginParams stronginParams = new StronginParams.Builder()
                .setIterations(params.STRONGIN_ITERATIONS)
                .setK(params.STRONGIN_K)
                .setRepositorySize(params.STRONGIN_REPOSITORY_SIZE)
                .build();

        TaskParams taskParams = new TaskParams.Builder()
                .setN(params.N)
                .setMinsCount(params.MINS_COUNT)
                .setRo(params.RO)
                .setStartConf(params.START_CONF)
                .setThreadsCount(params.THREADS_COUNT)
                .setVertices(vertices)
                .build();

        config = new Config(taskParams, stronginParams, growAlgParams, loParams);
        config.setDecimalScale(params.BIG_DECIMAL_SCALE);
    }

    public static String getInputFileName() {
        return inputFileName;
    }

    public static String getOutputFolderName() {
        return outputFolderName;
    }

    private static List<Vertex> readVertices(File file) throws FileNotFoundException {
        List<Vertex> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextDouble()) {
                list.add(new Vertex(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
            }
        }
        return list;
    }
}
