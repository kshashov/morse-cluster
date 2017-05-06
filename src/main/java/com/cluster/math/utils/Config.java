package com.cluster.math.utils;

/**
 * Created by envoy on 21.04.2017.
 */
public class Config {
    private String INPUT_FILENAME;  //required
    private String OUTPUT_FOLDERNAME;  //required
    private String START_CONF;  //or calculated
    private int N;  //required
    private int M;  //only calculated
    private int STRONGIN_N;  //only calculated
    private int STRONGIN_M;  //only calculated
    private int STRONGIN_K;  //or calculated
    private int STRONGIN_ITERATIONS; //or calculated
    private int STRONGIN_REPOSITORY_SIZE = 20;
    private double STRONGIN_EPS = 20000;
    private double DISTANCE_MIN = 1.1;
    private double RO = 14;
    private double TOP_MAX_ENERGY_DELTA = 0.05;
    private int INF_SUP_ITERATIONS = 10;
    private int INF_ITERATIONS; //or calculated
    private int SUP_ITERATIONS; //or calculated
    private long ROU_LO = 2;
    private int BIG_DECIMAL_SCALE = 30;
    private int THREADS_COUNT = 2;
    private double LO_EPS = 1e-8;
    private int LO_MAX_ITERATIONS = 10000;
    private int MINS_COUNT = 30;

    public Config() {
    }

    public void init() {
        if (M == 0) {
            M = START_CONF.length();
        }

        if (STRONGIN_K == 0) {
            STRONGIN_K = STRONGIN_N / 2;
        }

        if (STRONGIN_ITERATIONS == 0) {
            STRONGIN_ITERATIONS = 100 * STRONGIN_K + 10 * STRONGIN_M;
        }

        if (INF_ITERATIONS == 0) {
            INF_ITERATIONS = INF_SUP_ITERATIONS;
        }

        if (SUP_ITERATIONS == 0) {
            SUP_ITERATIONS = INF_SUP_ITERATIONS;
        }

    }

    public String getINPUT_FILENAME() {
        return INPUT_FILENAME;
    }

    public void setINPUT_FILENAME(String INPUT_FILENAME) {
        this.INPUT_FILENAME = INPUT_FILENAME;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getSTRONGIN_N() {
        return STRONGIN_N;
    }

    public int getSTRONGIN_M() {
        return STRONGIN_M;
    }

    public int getSTRONGIN_K() {
        return STRONGIN_K;
    }

    public String getSTART_CONF() {
        return START_CONF;
    }

    public double getDISTANCE_MIN() {
        return DISTANCE_MIN;
    }

    public double getRO() {
        return RO;
    }

    public double getTOP_MAX_ENERGY_DELTA() {
        return TOP_MAX_ENERGY_DELTA;
    }

    public int getINF_SUP_ITERATIONS() {
        return INF_SUP_ITERATIONS;
    }

    public int getINF_ITERATIONS() {
        return INF_ITERATIONS;
    }

    public int getSUP_ITERATIONS() {
        return SUP_ITERATIONS;
    }

    public void setSTRONGIN_N(int STRONGIN_N) {
        this.STRONGIN_N = STRONGIN_N;
    }

    public void setSTRONGIN_M(int STRONGIN_M) {
        this.STRONGIN_M = STRONGIN_M;
    }

    public void setSTRONGIN_K(int STRONGIN_K) {
        this.STRONGIN_K = STRONGIN_K;
    }

    public int getSTRONGIN_ITERATIONS() {
        return STRONGIN_ITERATIONS;
    }

    public void setSTRONGIN_ITERATIONS(int STRONGIN_ITERATIONS) {
        this.STRONGIN_ITERATIONS = STRONGIN_ITERATIONS;
    }

    public int getSTRONGIN_REPOSITORY_SIZE() {
        return STRONGIN_REPOSITORY_SIZE;
    }

    public void setSTRONGIN_REPOSITORY_SIZE(int STRONGIN_REPOSITORY_SIZE) {
        this.STRONGIN_REPOSITORY_SIZE = STRONGIN_REPOSITORY_SIZE;
    }

    public double getSTRONGIN_EPS() {
        return STRONGIN_EPS;
    }

    public void setSTRONGIN_EPS(double STRONGIN_EPS) {
        this.STRONGIN_EPS = STRONGIN_EPS;
    }

    public long getROU_LO() {
        return ROU_LO;
    }

    public int getBIG_DECIMAL_SCALE() {
        return BIG_DECIMAL_SCALE;
    }

    public void setBIG_DECIMAL_SCALE(int BIG_DECIMAL_SCALE) {
        this.BIG_DECIMAL_SCALE = BIG_DECIMAL_SCALE;
    }

    public String getOUTPUT_FOLDERNAME() {
        return OUTPUT_FOLDERNAME;
    }

    public void setOUTPUT_FOLDERNAME(String OUTPUT_FOLDERNAME) {
        this.OUTPUT_FOLDERNAME = OUTPUT_FOLDERNAME;
    }

    public int getTHREADS_COUNT() {
        return THREADS_COUNT;
    }

    public void setTHREADS_COUNT(int THREADS_COUNT) {
        this.THREADS_COUNT = THREADS_COUNT;
    }

    public double getLO_EPS() {
        return LO_EPS;
    }

    public void setLO_EPS(double LO_EPS) {
        this.LO_EPS = LO_EPS;
    }

    public int getLO_MAX_ITERATIONS() {
        return LO_MAX_ITERATIONS;
    }

    public void setLO_MAX_ITERATIONS(int LO_MAX_ITERATIONS) {
        this.LO_MAX_ITERATIONS = LO_MAX_ITERATIONS;
    }

    public int getMINS_COUNT() {
        return MINS_COUNT;
    }

    public void setMINS_COUNT(int MINS_COUNT) {
        this.MINS_COUNT = MINS_COUNT;
    }

    public void setM(int m) {
        this.M = m;
    }

    public void setSTART_CONF(String START_CONF) {
        this.START_CONF = START_CONF;
    }
}
