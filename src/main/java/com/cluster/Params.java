package com.cluster;

/**
 * Created by envoy on 03.06.2017.
 */
public class Params {
    public String INPUT_FILENAME;  //required
    public String OUTPUT_FOLDERNAME;  //required
    public String START_CONF;  //or calculated
    public int N;  //required
    public int STRONGIN_K;  //or calculated
    public int STRONGIN_ITERATIONS; //or calculated
    public int STRONGIN_REPOSITORY_SIZE = 20;
    public double STRONGIN_EPS = 20000;
    public double DISTANCE_MIN = 1.1;
    public double RO = 14;
    public double TOP_MAX_ENERGY_DELTA = 0.05;
    public int INF_SUP_ITERATIONS = 10;
    public long ROU_LO = 2;
    public int BIG_DECIMAL_SCALE = 30;
    public int THREADS_COUNT = 2;
    public double LO_EPS = 1e-8;
    public int LO_MAX_ITERATIONS = 10000;
    public int MINS_COUNT = 30;
}
