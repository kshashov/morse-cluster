package com.cluster.math.utils;

/**
 * Created by envoy on 21.04.2017.
 */
public class Config {
    private String INPUT_PATH;
    private String INPUT_FILENAME;
    private String START_CONF;
    private int N;
    private int M;
    private int STRONGIN_N;
    private int STRONGIN_M;
    private int STRONGIN_K = 6;
    private int STRONGIN_ITERATIONS = 2000;
    private int STRONGIN_REPOSITORY_SIZE = 20;
    private double STRONGIN_EPS = 20000;
    private double DISTANCE_MIN = 1.1;
    private double RO = 14;
    private double TOP_MAX_ENERGY_DELTA = 0.05;
    private int INF_SUP_ITERATIONS = 10000;
    private int INF_ITERATIONS = 10000;
    private int SUP_ITERATIONS = 10000;
    private double ROU_LO = 2;

    public Config() {
    }

    public Config(String INPUT_PATH, String INPUT_FILENAME, String START_CONF, int n) {
        this.INPUT_PATH = INPUT_PATH;
        this.INPUT_FILENAME = INPUT_FILENAME;
        this.N = n;
        this.START_CONF = START_CONF;
        this.M = START_CONF.length();
    }

    public String getINPUT_PATH() {
        return INPUT_PATH;
    }

    public String getINPUT_FILENAME() {
        return INPUT_FILENAME;
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

    public double getROU_LO() {
        return ROU_LO;
    }
}
