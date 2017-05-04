package com.cluster;

import com.cluster.math.model.Bits;
import com.cluster.math.model.Vertex;
import com.cluster.math.utils.ClusterMath;
import com.cluster.math.utils.Config;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by envoy on 03.05.2017.
 */
public class Configuration {
    private static Config config;

    public static Config get() {
        return config;
    }

    public static void setupConfig(BufferedReader fileReader) throws IOException {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = fileReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        fileReader.close();
        config = gson.fromJson(sb.toString().replace("\\", "\\\\"), Config.class);

        ArrayList<Vertex> vertices = readVertices(new File(config.getINPUT_FILENAME()));

        if ((config.getSTART_CONF() == null) || (config.getSTART_CONF().isEmpty())) {
            config.setSTART_CONF(new Bits(vertices.size()).getBites().toString());
        }

        int n2 = 0;
        String startConf = config.getSTART_CONF();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < startConf.length(); i++) {
            if (startConf.charAt(i) == '0') {
                indexes.add(i);
            } else {
                n2++;
            }
        }

        ClusterMath.init(startConf, vertices, indexes);

        //strongin config
        config.setSTRONGIN_N(config.getN() - n2);
        config.setSTRONGIN_M(indexes.size());
        config.init();
    }

    private static ArrayList<Vertex> readVertices(File file) throws FileNotFoundException {
        ArrayList<Vertex> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextDouble()) {
                list.add(new Vertex(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
            }
        }
        return list;
    }
}
